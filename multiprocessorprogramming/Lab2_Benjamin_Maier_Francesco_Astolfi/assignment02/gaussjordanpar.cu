/***************************************************************************
 *
 * Parallel version of Gauss-Jordan row reduction
 *
 ***************************************************************************/

#include <stdio.h>

#include <cuda_runtime.h>

#define MAX_SIZE 4096
#define NUM_PER_BLOCK 1024
const dim3 NUM_PER_BLOCK_2D(16, 16);

#define CUDA_CHECK(call) do { \
    cudaError_t err = call; \
    if (err != cudaSuccess) { \
        fprintf(stderr, "CUDA error %s:%d: %s\n", __FILE__, __LINE__, cudaGetErrorString(err)); \
        exit(EXIT_FAILURE); \
    } \
} while(0)

#define CEILING(a,b) (((a)+(b)-1)/(b))

typedef double matrix[MAX_SIZE][MAX_SIZE];

int	N;		/* matrix size		*/
int	maxnum;		/* max number of element*/
char* Init;		/* matrix init type	*/
int	PRINT;		/* print switch		*/
matrix	A;		/* matrix A		*/
double	b[MAX_SIZE];	/* vector b             */
double	y[MAX_SIZE];	/* vector y             */

/* forward declarations */
void work(void);
void Init_Matrix(void);
void Print_Matrix(void);
void Init_Default(void);
void Read_Options(int, char**);

int
main(int argc, char** argv)
{
    printf("Gauss Jordan\n");

    Init_Default();		/* Init default values	*/
    Read_Options(argc, argv);	/* Read arguments	*/
    Init_Matrix();		/* Init the matrix	*/
    work();
    if (PRINT == 1)
        Print_Matrix();
}

__global__
void scale_pivot_row(double* d_A, double* d_b, double* d_y, int k, int N) {
    int tid = blockIdx.x * blockDim.x + threadIdx.x;

    int j = k + 1 + tid;
    if (j < N) {
        d_A[k * N + j] = d_A[k * N + j] / d_A[k * N + k];
    }
    
    if (tid == 0) {
        d_y[k] = d_b[k] / d_A[k * N + k];
        d_A[k * N + k] = 1.0;
    }
}

__global__
void eliminate_rows(double* d_A, double* d_b, double* d_y, int k, int N) {
    // map a 2D grid of (row, col) to threads. Each thread handles one A[i][j]
    int i = blockIdx.y * blockDim.y + threadIdx.y; // absolute row index
    int j_off = blockIdx.x * blockDim.x + threadIdx.x; // offset for column dimension (0..(N-(k+1)-1))
    int row = k + 1 + i; // rows > k
    int col = k + 1 + j_off; // cols > k

    // eliminate for rows > k
    if (row < N && col < N) {
        d_A[row * N + col] = d_A[row * N + col] - d_A[row * N + k] * d_A[k * N + col];
    }
}

__global__
void update_rows(double* d_A, double* d_b, double* d_y, int k, int N) {
    int i = blockIdx.x * blockDim.x + threadIdx.x; // absolute row index
    int row = k + 1 + i; // rows > k
    
    if (row < N) {
        d_b[row] = d_b[row] - d_A[row * N + k] * d_y[k];
        d_A[row * N + k] = 0.0;
    }
}

__global__
void eliminate_rows_above(double* d_A, double* d_b, double* d_y, int k, int N) {
    int i = blockIdx.y * blockDim.y + threadIdx.y; // index within [0..k-1]
    int j_off = blockIdx.x * blockDim.x + threadIdx.x;
    int row = i;              // row < k
    int col = k + 1 + j_off;  // columns > k

    // eliminate for rows < k
    if (row < k && col < N) {
        d_A[row * N + col] = d_A[row * N + col] - d_A[row * N + k] * d_A[k * N + col];
    }
}

__global__
void update_rows_above(double* d_A, double* d_b, double* d_y, int k, int N) {
    int i = blockIdx.x * blockDim.x + threadIdx.x; // absolute row index
    int row = i; // rows < k
    
    if (row < k) {
        d_y[row] = d_y[row] - d_A[row * N + k] * d_y[k];
        d_A[row * N + k] = 0.0;
    }
}

void
work(void)
{
    size_t A_bytes = sizeof(double) * N * N;
    double* d_A = nullptr;
    CUDA_CHECK(cudaMalloc(&d_A, A_bytes));

    for (int i = 0; i < N; i++)
        CUDA_CHECK(cudaMemcpy(&d_A[i * N], A[i], sizeof(double) * N, cudaMemcpyHostToDevice));

    size_t b_bytes = sizeof(double) * N;
    double* d_b = nullptr;
    CUDA_CHECK(cudaMalloc(&d_b, b_bytes));
    CUDA_CHECK(cudaMemcpy(d_b, b, b_bytes, cudaMemcpyHostToDevice));

    size_t y_bytes = sizeof(double) * N;
    double* d_y = nullptr;
    CUDA_CHECK(cudaMalloc(&d_y, y_bytes));
    CUDA_CHECK(cudaMemcpy(d_y, y, y_bytes, cudaMemcpyHostToDevice));

    /* Gaussian elimination algorithm, Algo 8.4 from Grama */
    for (int k = 0; k < N; k++) { /* Outer loop */
        int num_cols_to_scale = N - (k + 1);
        int blocks = CEILING(num_cols_to_scale, NUM_PER_BLOCK);
        if (blocks < 1) blocks = 1;
        scale_pivot_row<<<blocks, NUM_PER_BLOCK>>>(d_A, d_b, d_y, k, N);
        CUDA_CHECK(cudaGetLastError());
        CUDA_CHECK(cudaDeviceSynchronize());
        
        // elimination for rows > k
        int rows_gt = N - (k+1);
        int cols_gt = N - (k+1);
        if (rows_gt > 0 && cols_gt > 0) {
            dim3 gridElim( CEILING(cols_gt, NUM_PER_BLOCK_2D.x),
                           CEILING(rows_gt, NUM_PER_BLOCK_2D.y) );
            eliminate_rows<<<gridElim, NUM_PER_BLOCK_2D>>>(d_A, d_b, d_y, k, N);
            CUDA_CHECK(cudaGetLastError());
            CUDA_CHECK(cudaDeviceSynchronize());
        }

        // update b vector for rows > k
        if (rows_gt > 0) {
            blocks = CEILING(rows_gt, NUM_PER_BLOCK);
            update_rows<<<blocks, NUM_PER_BLOCK>>>(d_A, d_b, d_y, k, N);
            CUDA_CHECK(cudaGetLastError());
            CUDA_CHECK(cudaDeviceSynchronize());
        }

        // elimination for rows < k (Gauss-Jordan back elimination)
        int rows_lt = k;
        if (cols_gt < 1) cols_gt = 1;
        if (rows_lt > 0 && cols_gt > 0) {
            dim3 gridElim2( CEILING(cols_gt, NUM_PER_BLOCK_2D.x),
                            CEILING(rows_lt, NUM_PER_BLOCK_2D.y) );
            eliminate_rows_above<<<gridElim2, NUM_PER_BLOCK_2D>>>(d_A, d_b, d_y, k, N);
            CUDA_CHECK(cudaGetLastError());
            CUDA_CHECK(cudaDeviceSynchronize());
        }

        // update y vector for rows < k
        if (rows_lt > 0) {
            blocks = CEILING(rows_lt, NUM_PER_BLOCK);
            update_rows_above<<<blocks, NUM_PER_BLOCK>>>(d_A, d_b, d_y, k, N);
            CUDA_CHECK(cudaGetLastError());
            CUDA_CHECK(cudaDeviceSynchronize());
        }
    }

    // copy results back to host
    for (int i = 0; i < N; i++)
        CUDA_CHECK(cudaMemcpy(A[i], &d_A[i * N], sizeof(double) * N, cudaMemcpyDeviceToHost));

    CUDA_CHECK(cudaMemcpy(b, d_b, b_bytes, cudaMemcpyDeviceToHost));
    CUDA_CHECK(cudaMemcpy(y, d_y, y_bytes, cudaMemcpyDeviceToHost));

    cudaFree(d_A);
    cudaFree(d_b);
    cudaFree(d_y);
}

void
Init_Matrix()
{
    int i, j;

    printf("\nsize      = %dx%d ", N, N);
    printf("\nmaxnum    = %d \n", maxnum);
    printf("Init	  = %s \n", Init);
    printf("Initializing matrix...");

    if (strcmp(Init, "rand") == 0) {
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                if (i == j) /* diagonal dominance */
                    A[i][j] = (double)(rand() % maxnum) + 5.0;
                else
                    A[i][j] = (double)(rand() % maxnum) + 1.0;
            }
        }
    }
    if (strcmp(Init, "fast") == 0) {
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                if (i == j) /* diagonal dominance */
                    A[i][j] = 5.0;
                else
                    A[i][j] = 2.0;
            }
        }
    }

    /* Initialize vectors b and y */
    for (i = 0; i < N; i++) {
        b[i] = 2.0;
        y[i] = 1.0;
    }

    printf("done \n\n");
    if (PRINT == 1)
        Print_Matrix();
}

void
Print_Matrix()
{
    int i, j;

    printf("Matrix A:\n");
    for (i = 0; i < N; i++) {
        printf("[");
        for (j = 0; j < N; j++)
            printf(" %5.2f,", A[i][j]);
        printf("]\n");
    }
    printf("Vector y:\n[");
    for (j = 0; j < N; j++)
        printf(" %5.2f,", y[j]);
    printf("]\n");
    printf("\n\n");
}

void
Init_Default()
{
    N = 2048;
    Init = (char*)"fast";
    maxnum = 15.0;
    PRINT = 0;
}

void
Read_Options(int argc, char** argv)
{
    char* prog;

    prog = *argv;
    while (++argv, --argc > 0)
        if (**argv == '-')
            switch (*++ * argv) {
            case 'n':
                --argc;
                N = atoi(*++argv);
                break;
            case 'h':
                printf("\nHELP: try sor -u \n\n");
                exit(0);
                break;
            case 'u':
                printf("\nUsage: gaussian [-n problemsize]\n");
                printf("           [-D] show default values \n");
                printf("           [-h] help \n");
                printf("           [-I init_type] fast/rand \n");
                printf("           [-m maxnum] max random no \n");
                printf("           [-P print_switch] 0/1 \n");
                exit(0);
                break;
            case 'D':
                printf("\nDefault:  n         = %d ", N);
                printf("\n          Init      = rand");
                printf("\n          maxnum    = 5 ");
                printf("\n          P         = 0 \n\n");
                exit(0);
                break;
            case 'I':
                --argc;
                Init = *++argv;
                break;
            case 'm':
                --argc;
                maxnum = atoi(*++argv);
                break;
            case 'P':
                --argc;
                PRINT = atoi(*++argv);
                break;
            default:
                printf("%s: ignored option: -%s\n", prog, *argv);
                printf("HELP: try %s -u \n\n", prog);
                break;
            }
}