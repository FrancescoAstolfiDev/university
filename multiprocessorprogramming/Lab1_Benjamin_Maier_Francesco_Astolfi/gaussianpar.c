/***************************************************************************
 *
 * Sequential version of Gaussian elimination
 *
 ***************************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX_SIZE 4096
unsigned NUM_THREADS = 16; /* higher values do not increase performance anymore */

typedef double matrix[MAX_SIZE][MAX_SIZE];

int	N;		/* matrix size		*/
int	maxnum;		/* max number of element*/
char	*Init;		/* matrix init type	*/
int	PRINT;		/* print switch		*/
matrix	A;		/* matrix A		*/
double	b[MAX_SIZE];	/* vector b             */
double	y[MAX_SIZE];	/* vector y             */

pthread_barrier_t barrier; /* barrier for synchronization */

/* forward declarations */
void work(void);
void Init_Matrix(void);
void Print_Matrix(void);
void Init_Default(void);
int Read_Options(int, char **);

int
main(int argc, char **argv)
{
    int i, timestart, timeend, iter;

    Init_Default();		/* Init default values	*/
    Read_Options(argc,argv);	/* Read arguments	*/
    Init_Matrix();		/* Init the matrix	*/
    work();
    if (PRINT == 1)
	   Print_Matrix();
}

typedef struct {
    int tid;
} elimination_arg_t;

void*
elimination_func(void *arg)
{
    elimination_arg_t *data = (elimination_arg_t *)arg;
    int tid = data->tid;

    for (int k = 0; k < N; k++) {
        /* Division step only done by thread with ID 0 */
        if (tid == 0) {
            for (int j = k + 1; j < N; j++)
                A[k][j] /= A[k][k];  /* Division step */
            y[k] = b[k] / A[k][k];
            A[k][k] = 1.0;
        }

        /* Wait for division step to complete */
        pthread_barrier_wait(&barrier);

        /* Parallel elimination step in a cyclic manner */
        for (int i = k + 1 + tid; i < N; i += NUM_THREADS) {
            for (int j = k + 1; j < N; j++)
                A[i][j] = A[i][j] - A[i][k]*A[k][j]; /* Elimination step */
            b[i] = b[i] - A[i][k]*y[k];
	        A[i][k] = 0.0;
        }

        /* Wait for all threads to complete elimination before next k */
        pthread_barrier_wait(&barrier);
    }
    pthread_exit(NULL);
}

void
work(void)
{
    pthread_t threads[NUM_THREADS];
    elimination_arg_t elimintation_args[NUM_THREADS];

    /* Initialize barrier */
    pthread_barrier_init(&barrier, NULL, NUM_THREADS);

    /* Spawn worker threads */
    for (int t = 0; t < NUM_THREADS; t++) {
        elimintation_args[t].tid = t;
        pthread_create(&threads[t], NULL, elimination_func, &elimintation_args[t]);
    }

    /* Join worker threads */
    for (int t = 0; t < NUM_THREADS; t++)
        pthread_join(threads[t], NULL);

    /* Destroy barrier */
    pthread_barrier_destroy(&barrier);
}

void
Init_Matrix()
{
    int i, j;

    printf("\nsize      = %dx%d ", N, N);
    printf("\nmaxnum    = %d \n", maxnum);
    printf("Init	  = %s \n", Init);
    printf("Initializing matrix...");

    if (strcmp(Init,"rand") == 0) {
        for (i = 0; i < N; i++){
            for (j = 0; j < N; j++) {
                if (i == j) /* diagonal dominance */
                    A[i][j] = (double)(rand() % maxnum) + 5.0;
                else
                    A[i][j] = (double)(rand() % maxnum) + 1.0;
            }
        }
    }
    if (strcmp(Init,"fast") == 0) {
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
    printf("Vector b:\n[");
    for (j = 0; j < N; j++)
        printf(" %5.2f,", b[j]);
    printf("]\n");
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
    Init = "rand";
    maxnum = 15.0;
    PRINT = 0;
}

int
Read_Options(int argc, char **argv)
{
    char    *prog;

    prog = *argv;
    while (++argv, --argc > 0)
        if (**argv == '-')
            switch ( *++*argv ) {
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
                    printf("           [-N ] number of workers \n");
                    exit(0);
                    break;
                case 'D':
                    printf("\nDefault:  n         = %d ", N);
                    printf("\n          Init      = rand" );
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
                case 'N':
                    --argc;
                    NUM_THREADS = atoi(*++argv);
                    break;
                default:
                    printf("%s: ignored option: -%s\n", prog, *argv);
                    printf("HELP: try %s -u \n\n", prog);
                    break;
            }
}
