/*
The MIT License(MIT)
Copyright(c) 2016 Fan Wen Jie

Permission is hereby granted, free of charge, to any person obtaining a copy
of this softwareand associated documentation files(the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and /or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions :

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
// Source: https://github.com/fan-wenjie/LeNet-5
// v2: GPU-Accelerated Pooling, Dot Product, and Asynchronous Batch Processing (Refactored)

#include "lenet.h"
#include <cuda_runtime.h>
#include <device_launch_parameters.h>
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>

#define GETLENGTH(array) (sizeof(array)/sizeof(*(array)))
#define GETCOUNT(array)  (sizeof(array)/sizeof(double))
#define FOREACH(i,count) for (int i = 0; i < count; ++i)

#define TILE_WIDTH 16
#define FILTER_SIZE LENGTH_KERNEL
#define MAX_BATCH_SIZE 256

// Helper macro for checking CUDA errors
#define CHECK_CUDA(call) { \
    cudaError_t err = call; \
    if (err != cudaSuccess) { \
        fprintf(stderr, "CUDA Error: %s at line %d\n", cudaGetErrorString(err), __LINE__); \
        exit(1); \
    } \
}

// Constant Memory for C1 and C3 Layers
__constant__ double c_weight0_1[INPUT * LAYER1 * LENGTH_KERNEL * LENGTH_KERNEL];
__constant__ double c_bias0_1[LAYER1];
__constant__ double c_weight2_3[LAYER2 * LAYER3 * LENGTH_KERNEL * LENGTH_KERNEL];
__constant__ double c_bias2_3[LAYER3];

// Persistent GPU Memory and Streams for Batch Processing
typedef struct {
    // Device memory for features (allocated for MAX_BATCH_SIZE to ensure private buffers per stream)
    double *d_input;    // [MAX_BATCH_SIZE][INPUT][LENGTH_FEATURE0][LENGTH_FEATURE0]
    double *d_layer1;   // [MAX_BATCH_SIZE][LAYER1][LENGTH_FEATURE1][LENGTH_FEATURE1]
    double *d_layer2;   // [MAX_BATCH_SIZE][LAYER2][LENGTH_FEATURE2][LENGTH_FEATURE2]
    double *d_layer3;   // [MAX_BATCH_SIZE][LAYER3][LENGTH_FEATURE3][LENGTH_FEATURE3]
    double *d_layer4;   // [MAX_BATCH_SIZE][LAYER4][LENGTH_FEATURE4][LENGTH_FEATURE4]
    double *d_layer5;   // [MAX_BATCH_SIZE][LAYER5][LENGTH_FEATURE5][LENGTH_FEATURE5]
    double *d_output;   // [MAX_BATCH_SIZE][OUTPUT]
    
    // Device memory for weights
    double *d_weight4_5;
    double *d_bias4_5;
    double *d_weight5_6;
    double *d_bias5_6;
    
    // CUDA streams (one per potential image in batch)
    cudaStream_t streams[MAX_BATCH_SIZE];
    
    bool initialized;
} GPUContext;

static GPUContext g_gpu_ctx = {0};

// --- GPU Kernels ---

// Tiled Convolution Kernel using __ldg()
__global__ void TiledConvolutionKernel(
    const double* __restrict__ input, 
    double* __restrict__ output,
    const double* __restrict__ weight,
    const double* __restrict__ bias,
    int input_channels, int output_channels,
    int input_height, int input_width,
    int output_height, int output_width
) {
    __shared__ double s_input[TILE_WIDTH + FILTER_SIZE - 1][TILE_WIDTH + FILTER_SIZE - 1];

    int tx = threadIdx.x;
    int ty = threadIdx.y;
    int col_o = blockIdx.x * TILE_WIDTH + tx;
    int row_o = blockIdx.y * TILE_WIDTH + ty;
    int oc = blockIdx.z;

    double acc = 0.0;
    
    for (int ic = 0; ic < input_channels; ++ic) {
        int t_in_size = TILE_WIDTH + FILTER_SIZE - 1;
        int row_i_base = blockIdx.y * TILE_WIDTH; 
        int col_i_base = blockIdx.x * TILE_WIDTH;

        for (int i = ty * TILE_WIDTH + tx; i < t_in_size * t_in_size; i += TILE_WIDTH * TILE_WIDTH) {
            int r = i / t_in_size;
            int c = i % t_in_size;
            int global_r = row_i_base + r;
            int global_c = col_i_base + c;
            
            if (global_r < input_height && global_c < input_width) {
                int input_idx = ic * (input_height * input_width) + global_r * input_width + global_c;
                s_input[r][c] = __ldg(&input[input_idx]);
            } else {
                s_input[r][c] = 0.0;
            }
        }
        
        __syncthreads();

        if (col_o < output_width && row_o < output_height && tx < TILE_WIDTH && ty < TILE_WIDTH) {
            for (int wy = 0; wy < FILTER_SIZE; ++wy) {
                for (int wx = 0; wx < FILTER_SIZE; ++wx) {
                    double val = s_input[ty + wy][tx + wx];
                    int w_idx = ic * (output_channels * FILTER_SIZE * FILTER_SIZE) + 
                                oc * (FILTER_SIZE * FILTER_SIZE) + 
                                wy * FILTER_SIZE + wx;
                    acc += val * __ldg(&weight[w_idx]);
                }
            }
        }
        
        __syncthreads();
    }

    if (col_o < output_width && row_o < output_height && oc < output_channels) {
        acc += __ldg(&bias[oc]);
        if (acc < 0) acc = 0; // ReLU
        int out_idx = oc * (output_height * output_width) + row_o * output_width + col_o;
        output[out_idx] = acc;
    }
}

// Tiled Convolution Kernel using Constant Memory
__global__ void TiledConvolutionKernel_ConstMem(
    const double* __restrict__ input, 
    double* __restrict__ output,
    int layer_id,  // 0 for C1, 1 for C3
    int input_channels, int output_channels,
    int input_height, int input_width,
    int output_height, int output_width
) {
    __shared__ double s_input[TILE_WIDTH + FILTER_SIZE - 1][TILE_WIDTH + FILTER_SIZE - 1];

    int tx = threadIdx.x;
    int ty = threadIdx.y;
    int col_o = blockIdx.x * TILE_WIDTH + tx;
    int row_o = blockIdx.y * TILE_WIDTH + ty;
    int oc = blockIdx.z;

    double acc = 0.0;
    
    for (int ic = 0; ic < input_channels; ++ic) {
        int t_in_size = TILE_WIDTH + FILTER_SIZE - 1;
        int row_i_base = blockIdx.y * TILE_WIDTH; 
        int col_i_base = blockIdx.x * TILE_WIDTH;

        for (int i = ty * TILE_WIDTH + tx; i < t_in_size * t_in_size; i += TILE_WIDTH * TILE_WIDTH) {
            int r = i / t_in_size;
            int c = i % t_in_size;
            int global_r = row_i_base + r;
            int global_c = col_i_base + c;
            
            if (global_r < input_height && global_c < input_width) {
                int input_idx = ic * (input_height * input_width) + global_r * input_width + global_c;
                s_input[r][c] = __ldg(&input[input_idx]);
            } else {
                s_input[r][c] = 0.0;
            }
        }
        
        __syncthreads();

        if (col_o < output_width && row_o < output_height && tx < TILE_WIDTH && ty < TILE_WIDTH) {
            for (int wy = 0; wy < FILTER_SIZE; ++wy) {
                for (int wx = 0; wx < FILTER_SIZE; ++wx) {
                    double val = s_input[ty + wy][tx + wx];
                    int w_idx = ic * (output_channels * FILTER_SIZE * FILTER_SIZE) + 
                                oc * (FILTER_SIZE * FILTER_SIZE) + 
                                wy * FILTER_SIZE + wx;
                    
                    if (layer_id == 0) {
                        acc += val * c_weight0_1[w_idx];
                    } else {
                        acc += val * c_weight2_3[w_idx];
                    }
                }
            }
        }
        
        __syncthreads();
    }

    if (col_o < output_width && row_o < output_height && oc < output_channels) {
        if (layer_id == 0) {
            acc += c_bias0_1[oc];
        } else {
            acc += c_bias2_3[oc];
        }
        if (acc < 0) acc = 0; // ReLU
        int out_idx = oc * (output_height * output_width) + row_o * output_width + col_o;
        output[out_idx] = acc;
    }
}

// Max Pooling Kernel
__global__ void MaxPoolKernel(
    const double* __restrict__ input,
    double* __restrict__ output,
    int channels, int in_h, int in_w, int out_h, int out_w
) {
    int col = blockIdx.x * blockDim.x + threadIdx.x;
    int row = blockIdx.y * blockDim.y + threadIdx.y;
    int ch = blockIdx.z;

    if (col < out_w && row < out_h && ch < channels) {
        int in_row_base = row * 2;
        int in_col_base = col * 2;
        
        double max_val = input[ch * (in_h * in_w) + in_row_base * in_w + in_col_base];
        
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                double val = input[ch * (in_h * in_w) + (in_row_base + i) * in_w + (in_col_base + j)];
                if (val > max_val) max_val = val;
            }
        }
        output[ch * (out_h * out_w) + row * out_w + col] = max_val;
    }
}

// Dot Product Kernel (Small fully connected layer)
__global__ void DotProductKernel(
    const double* __restrict__ input,
    double* __restrict__ output,
    const double* __restrict__ weight, // [in_features][out_features]
    const double* __restrict__ bias,
    int in_features, int out_features,
    int use_relu
) {
    int oc = blockIdx.x * blockDim.x + threadIdx.x;

    if (oc < out_features) {
        double acc = 0.0;
        for (int ic = 0; ic < in_features; ++ic) {
            acc += input[ic] * weight[ic * out_features + oc];
        }
        acc += bias[oc];
        if (use_relu && acc < 0) acc = 0;
        output[oc] = acc;
    }
}

// --- Original C macros kept for CPU-side training ---
#define CONVOLUTE_VALID(input,output,weight)											\
{																						\
	FOREACH(o0,GETLENGTH(output))														\
		FOREACH(o1,GETLENGTH(*(output)))												\
			FOREACH(w0,GETLENGTH(weight))												\
				FOREACH(w1,GETLENGTH(*(weight)))										\
					(output)[o0][o1] += (input)[o0 + w0][o1 + w1] * (weight)[w0][w1];	\
}

#define CONVOLUTE_FULL(input,output,weight)												\
{																						\
	FOREACH(i0,GETLENGTH(input))														\
		FOREACH(i1,GETLENGTH(*(input)))													\
			FOREACH(w0,GETLENGTH(weight))												\
				FOREACH(w1,GETLENGTH(*(weight)))										\
					(output)[i0 + w0][i1 + w1] += (input)[i0][i1] * (weight)[w0][w1];	\
}

#define CONVOLUTION_BACKWARD(input,inerror,outerror,weight,wd,bd,actiongrad)\
{																			\
	for (int x = 0; x < GETLENGTH(weight); ++x)								\
		for (int y = 0; y < GETLENGTH(*weight); ++y)						\
			CONVOLUTE_FULL(outerror[y], inerror[x], weight[x][y]);			\
	FOREACH(i, GETCOUNT(inerror))											\
		((double *)inerror)[i] *= actiongrad(((double *)input)[i]);			\
	FOREACH(j, GETLENGTH(outerror))											\
		FOREACH(i, GETCOUNT(outerror[j]))									\
		bd[j] += ((double *)outerror[j])[i];								\
	for (int x = 0; x < GETLENGTH(weight); ++x)								\
		for (int y = 0; y < GETLENGTH(*weight); ++y)						\
			CONVOLUTE_VALID(input[x], wd[x][y], outerror[y]);				\
}

#define SUBSAMP_MAX_BACKWARD(input,inerror,outerror)											\
{																								\
	const int len0 = GETLENGTH(*(inerror)) / GETLENGTH(*(outerror));							\
	const int len1 = GETLENGTH(**(inerror)) / GETLENGTH(**(outerror));							\
	FOREACH(i, GETLENGTH(outerror))																\
	FOREACH(o0, GETLENGTH(*(outerror)))															\
	FOREACH(o1, GETLENGTH(**(outerror)))														\
	{																							\
		int x0 = 0, x1 = 0, ismax;																\
		FOREACH(l0, len0)																		\
			FOREACH(l1, len1)																	\
		{																						\
			ismax = input[i][o0*len0 + l0][o1*len1 + l1] > input[i][o0*len0 + x0][o1*len1 + x1];\
			x0 += ismax * (l0 - x0);															\
			x1 += ismax * (l1 - x1);															\
			}																						\
		inerror[i][o0*len0 + x0][o1*len1 + x1] = outerror[i][o0][o1];							\
	}																							\
}

#define DOT_PRODUCT_BACKWARD(input,inerror,outerror,weight,wd,bd,actiongrad)	\
{																				\
	for (int x = 0; x < GETLENGTH(weight); ++x)									\
		for (int y = 0; y < GETLENGTH(*weight); ++y)							\
			((double *)inerror)[x] += ((double *)outerror)[y] * weight[x][y];	\
	FOREACH(i, GETCOUNT(inerror))												\
		((double *)inerror)[i] *= actiongrad(((double *)input)[i]);				\
	FOREACH(j, GETLENGTH(outerror))												\
		bd[j] += ((double *)outerror)[j];										\
	for (int x = 0; x < GETLENGTH(weight); ++x)									\
		for (int y = 0; y < GETLENGTH(*weight); ++y)							\
			wd[x][y] += ((double *)input)[x] * ((double *)outerror)[y];			\
}

double relu(double x) { return x*(x > 0); }
double relugrad(double y) { return y > 0; }
double identity(double x) { return x; }

// --- GPU Context Management ---

static void SyncGPUWeights(LeNet5 *lenet) {
    if (!g_gpu_ctx.initialized) return;
    
    // Constant memory initialization
    CHECK_CUDA(cudaMemcpyToSymbol(c_weight0_1, lenet->weight0_1, sizeof(lenet->weight0_1)));
    CHECK_CUDA(cudaMemcpyToSymbol(c_bias0_1, lenet->bias0_1, sizeof(lenet->bias0_1)));
    CHECK_CUDA(cudaMemcpyToSymbol(c_weight2_3, lenet->weight2_3, sizeof(lenet->weight2_3)));
    CHECK_CUDA(cudaMemcpyToSymbol(c_bias2_3, lenet->bias2_3, sizeof(lenet->bias2_3)));
    
    // Global weights copy
    CHECK_CUDA(cudaMemcpy(g_gpu_ctx.d_weight4_5, lenet->weight4_5, sizeof(lenet->weight4_5), cudaMemcpyHostToDevice));
    CHECK_CUDA(cudaMemcpy(g_gpu_ctx.d_bias4_5, lenet->bias4_5, sizeof(lenet->bias4_5), cudaMemcpyHostToDevice));
    CHECK_CUDA(cudaMemcpy(g_gpu_ctx.d_weight5_6, lenet->weight5_6, sizeof(lenet->weight5_6), cudaMemcpyHostToDevice));
    CHECK_CUDA(cudaMemcpy(g_gpu_ctx.d_bias5_6, lenet->bias5_6, sizeof(lenet->bias5_6), cudaMemcpyHostToDevice));
}

static void InitGPU(LeNet5 *lenet) {
    if (g_gpu_ctx.initialized) return;
    
    // Memory per image
    size_t sz_in = INPUT * LENGTH_FEATURE0 * LENGTH_FEATURE0 * sizeof(double);
    size_t sz_l1 = LAYER1 * LENGTH_FEATURE1 * LENGTH_FEATURE1 * sizeof(double);
    size_t sz_l2 = LAYER2 * LENGTH_FEATURE2 * LENGTH_FEATURE2 * sizeof(double);
    size_t sz_l3 = LAYER3 * LENGTH_FEATURE3 * LENGTH_FEATURE3 * sizeof(double);
    size_t sz_l4 = LAYER4 * LENGTH_FEATURE4 * LENGTH_FEATURE4 * sizeof(double);
    size_t sz_l5 = LAYER5 * LENGTH_FEATURE5 * LENGTH_FEATURE5 * sizeof(double);
    size_t sz_out = OUTPUT * sizeof(double);
    
    // Allocate for batch
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_input, MAX_BATCH_SIZE * sz_in));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_layer1, MAX_BATCH_SIZE * sz_l1));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_layer2, MAX_BATCH_SIZE * sz_l2));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_layer3, MAX_BATCH_SIZE * sz_l3));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_layer4, MAX_BATCH_SIZE * sz_l4));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_layer5, MAX_BATCH_SIZE * sz_l5));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_output, MAX_BATCH_SIZE * sz_out));
    
    // Weights
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_weight4_5, LAYER4 * LAYER5 * LENGTH_KERNEL * LENGTH_KERNEL * sizeof(double)));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_bias4_5, LAYER5 * sizeof(double)));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_weight5_6, (LAYER5 * LENGTH_FEATURE5 * LENGTH_FEATURE5) * OUTPUT * sizeof(double)));
    CHECK_CUDA(cudaMalloc(&g_gpu_ctx.d_bias5_6, OUTPUT * sizeof(double)));
    
    for (int i = 0; i < MAX_BATCH_SIZE; ++i) {
        CHECK_CUDA(cudaStreamCreate(&g_gpu_ctx.streams[i]));
    }
    
    g_gpu_ctx.initialized = true;
    SyncGPUWeights(lenet);
    printf("GPU Context initialized (Batch size: %d)\n", MAX_BATCH_SIZE);
}

static void CleanupGPU() {
    if (!g_gpu_ctx.initialized) return;
    cudaFree(g_gpu_ctx.d_input); cudaFree(g_gpu_ctx.d_layer1); cudaFree(g_gpu_ctx.d_layer2);
    cudaFree(g_gpu_ctx.d_layer3); cudaFree(g_gpu_ctx.d_layer4); cudaFree(g_gpu_ctx.d_layer5);
    cudaFree(g_gpu_ctx.d_output); cudaFree(g_gpu_ctx.d_weight4_5); cudaFree(g_gpu_ctx.d_bias4_5);
    cudaFree(g_gpu_ctx.d_weight5_6); cudaFree(g_gpu_ctx.d_bias5_6);
    for (int i = 0; i < MAX_BATCH_SIZE; ++i) cudaStreamDestroy(g_gpu_ctx.streams[i]);
    g_gpu_ctx.initialized = false;
}

// Forward Propagation on GPU (Entire flow)
static void forward_gpu_async(int img_idx, int use_relu_final) {
    cudaStream_t s = g_gpu_ctx.streams[img_idx];
    
    // Offsets for private buffers
    double *d_in = g_gpu_ctx.d_input + img_idx * (INPUT * LENGTH_FEATURE0 * LENGTH_FEATURE0);
    double *d_l1 = g_gpu_ctx.d_layer1 + img_idx * (LAYER1 * LENGTH_FEATURE1 * LENGTH_FEATURE1);
    double *d_l2 = g_gpu_ctx.d_layer2 + img_idx * (LAYER2 * LENGTH_FEATURE2 * LENGTH_FEATURE2);
    double *d_l3 = g_gpu_ctx.d_layer3 + img_idx * (LAYER3 * LENGTH_FEATURE3 * LENGTH_FEATURE3);
    double *d_l4 = g_gpu_ctx.d_layer4 + img_idx * (LAYER4 * LENGTH_FEATURE4 * LENGTH_FEATURE4);
    double *d_l5 = g_gpu_ctx.d_layer5 + img_idx * (LAYER5 * LENGTH_FEATURE5 * LENGTH_FEATURE5);
    double *d_out = g_gpu_ctx.d_output + img_idx * OUTPUT;

    dim3 block(16, 16);

    // C1: Conv
    dim3 grid1((LENGTH_FEATURE1 + 15) / 16, (LENGTH_FEATURE1 + 15) / 16, LAYER1);
    TiledConvolutionKernel_ConstMem<<<grid1, block, 0, s>>>(d_in, d_l1, 0, INPUT, LAYER1, LENGTH_FEATURE0, LENGTH_FEATURE0, LENGTH_FEATURE1, LENGTH_FEATURE1);

    // S2: Pool
    dim3 grid2((LENGTH_FEATURE2 + 15) / 16, (LENGTH_FEATURE2 + 15) / 16, LAYER2);
    MaxPoolKernel<<<grid2, block, 0, s>>>(d_l1, d_l2, LAYER1, LENGTH_FEATURE1, LENGTH_FEATURE1, LENGTH_FEATURE2, LENGTH_FEATURE2);

    // C3: Conv
    dim3 grid3((LENGTH_FEATURE3 + 15) / 16, (LENGTH_FEATURE3 + 15) / 16, LAYER3);
    TiledConvolutionKernel_ConstMem<<<grid3, block, 0, s>>>(d_l2, d_l3, 1, LAYER2, LAYER3, LENGTH_FEATURE2, LENGTH_FEATURE2, LENGTH_FEATURE3, LENGTH_FEATURE3);

    // S4: Pool
    dim3 grid4((LENGTH_FEATURE4 + 15) / 16, (LENGTH_FEATURE4 + 15) / 16, LAYER4);
    MaxPoolKernel<<<grid4, block, 0, s>>>(d_l3, d_l4, LAYER3, LENGTH_FEATURE3, LENGTH_FEATURE3, LENGTH_FEATURE4, LENGTH_FEATURE4);

    // C5: Conv
    dim3 grid5((LENGTH_FEATURE5 + 15) / 16, (LENGTH_FEATURE5 + 15) / 16, LAYER5);
    TiledConvolutionKernel<<<grid5, block, 0, s>>>(d_l4, d_l5, g_gpu_ctx.d_weight4_5, g_gpu_ctx.d_bias4_5, LAYER4, LAYER5, LENGTH_FEATURE4, LENGTH_FEATURE4, LENGTH_FEATURE5, LENGTH_FEATURE5);

    // FC Output: Dot Product
    DotProductKernel<<<1, OUTPUT, 0, s>>>(d_l5, d_out, g_gpu_ctx.d_weight5_6, g_gpu_ctx.d_bias5_6, LAYER5 * LENGTH_FEATURE5 * LENGTH_FEATURE5, OUTPUT, use_relu_final);
}

// --- Helper Functions ---

static inline void load_input(Feature *features, image input)
{
	double (*layer0)[LENGTH_FEATURE0][LENGTH_FEATURE0] = features->input;
	const long sz = sizeof(image) / sizeof(**input);
	double mean = 0, std = 0;
	FOREACH(j, sizeof(image) / sizeof(*input))
		FOREACH(k, sizeof(*input) / sizeof(**input))
	{
		mean += input[j][k];
		std += input[j][k] * input[j][k];
	}
	mean /= sz;
	std = sqrt(std / sz - mean*mean);
	FOREACH(j, sizeof(image) / sizeof(*input))
		FOREACH(k, sizeof(*input) / sizeof(**input))
	{
		layer0[0][j + PADDING][k + PADDING] = (input[j][k] - mean) / std;
	}
}

static inline void softmax(double input[OUTPUT], double loss[OUTPUT], int label, int count)
{
	double inner = 0;
	for (int i = 0; i < count; ++i)
	{
		double res = 0;
		for (int j = 0; j < count; ++j)
		{
			res += exp(input[j] - input[i]);
		}
		loss[i] = 1. / res;
		inner -= loss[i] * loss[i];
	}
	inner += loss[label];
	for (int i = 0; i < count; ++i)
	{
		loss[i] *= (i == label) - loss[i] - inner;
	}
}

static void backward(LeNet5 *lenet, LeNet5 *deltas, Feature *errors, Feature *features, double(*actiongrad)(double))
{
    // CPU Backward
	DOT_PRODUCT_BACKWARD(features->layer5, errors->layer5, errors->output, lenet->weight5_6, deltas->weight5_6, deltas->bias5_6, actiongrad);
	CONVOLUTION_BACKWARD(features->layer4, errors->layer4, errors->layer5, lenet->weight4_5, deltas->weight4_5, deltas->bias4_5, actiongrad);
	SUBSAMP_MAX_BACKWARD(features->layer3, errors->layer3, errors->layer4);
	CONVOLUTION_BACKWARD(features->layer2, errors->layer2, errors->layer3, lenet->weight2_3, deltas->weight2_3, deltas->bias2_3, actiongrad);
	SUBSAMP_MAX_BACKWARD(features->layer1, errors->layer1, errors->layer2);
	CONVOLUTION_BACKWARD(features->input, errors->input, errors->layer1, lenet->weight0_1, deltas->weight0_1, deltas->bias0_1, actiongrad);
}

// --- API Implementation ---

extern "C" void TrainBatch(LeNet5 *lenet, image *inputs, uint8 *labels, int batchSize)
{
    if (batchSize > MAX_BATCH_SIZE) batchSize = MAX_BATCH_SIZE;

   
    if (!g_gpu_ctx.initialized) InitGPU(lenet);
    else SyncGPUWeights(lenet); // Crucial for accuracy: update GPU weights each batch

    double buffer[GETCOUNT(LeNet5)] = { 0 };
    Feature *all_features = (Feature*)calloc(batchSize, sizeof(Feature));
    Feature *all_errors = (Feature*)calloc(batchSize, sizeof(Feature));

    // Pipeline: Asynchronous Launch per image
    for (int i = 0; i < batchSize; ++i) {
        cudaStream_t s = g_gpu_ctx.streams[i];
        load_input(&all_features[i], inputs[i]);
        
        // Host to Device
        CHECK_CUDA(cudaMemcpyAsync(g_gpu_ctx.d_input + i * (INPUT * LENGTH_FEATURE0 * LENGTH_FEATURE0), 
                                   all_features[i].input, 
                                   INPUT * LENGTH_FEATURE0 * LENGTH_FEATURE0 * sizeof(double), 
                                   cudaMemcpyHostToDevice, s));
        
        // Forward Propagation
        forward_gpu_async(i, 1); // use ReLU
        
        // Device to Host
        CHECK_CUDA(cudaMemcpyAsync(all_features[i].layer1, g_gpu_ctx.d_layer1 + i * (LAYER1 * LENGTH_FEATURE1 * LENGTH_FEATURE1), 
                                   LAYER1 * LENGTH_FEATURE1 * LENGTH_FEATURE1 * sizeof(double), cudaMemcpyDeviceToHost, s));
        CHECK_CUDA(cudaMemcpyAsync(all_features[i].layer2, g_gpu_ctx.d_layer2 + i * (LAYER2 * LENGTH_FEATURE2 * LENGTH_FEATURE2), 
                                   LAYER2 * LENGTH_FEATURE2 * LENGTH_FEATURE2 * sizeof(double), cudaMemcpyDeviceToHost, s));
        CHECK_CUDA(cudaMemcpyAsync(all_features[i].layer3, g_gpu_ctx.d_layer3 + i * (LAYER3 * LENGTH_FEATURE3 * LENGTH_FEATURE3), 
                                   LAYER3 * LENGTH_FEATURE3 * LENGTH_FEATURE3 * sizeof(double), cudaMemcpyDeviceToHost, s));
        CHECK_CUDA(cudaMemcpyAsync(all_features[i].layer4, g_gpu_ctx.d_layer4 + i * (LAYER4 * LENGTH_FEATURE4 * LENGTH_FEATURE4), 
                                   LAYER4 * LENGTH_FEATURE4 * LENGTH_FEATURE4 * sizeof(double), cudaMemcpyDeviceToHost, s));
        CHECK_CUDA(cudaMemcpyAsync(all_features[i].layer5, g_gpu_ctx.d_layer5 + i * (LAYER5 * LENGTH_FEATURE5 * LENGTH_FEATURE5), 
                                   LAYER5 * LENGTH_FEATURE5 * LENGTH_FEATURE5 * sizeof(double), cudaMemcpyDeviceToHost, s));
        CHECK_CUDA(cudaMemcpyAsync(all_features[i].output, g_gpu_ctx.d_output + i * OUTPUT, 
                                   OUTPUT * sizeof(double), cudaMemcpyDeviceToHost, s));
    }

    // Granular Synchronization: CPU starts backward as soon as a stream finishes
    for (int i = 0; i < batchSize; ++i) {
        CHECK_CUDA(cudaStreamSynchronize(g_gpu_ctx.streams[i]));
        
        LeNet5 deltas = { 0 };
        softmax((double*)all_features[i].output, (double*)all_errors[i].output, labels[i], OUTPUT);
        backward(lenet, &deltas, &all_errors[i], &all_features[i], relugrad);
        
        FOREACH(j, GETCOUNT(LeNet5)) buffer[j] += ((double *)&deltas)[j];
    }

    double k = ALPHA / batchSize;
    FOREACH(i, GETCOUNT(LeNet5)) ((double *)lenet)[i] += k * buffer[i];

    free(all_features); free(all_errors);
}

extern "C" uint8 Predict(LeNet5 *lenet, image input, uint8 count)
{
    if (!g_gpu_ctx.initialized) InitGPU(lenet);
    else SyncGPUWeights(lenet);
    
    Feature f = {0};
    load_input(&f, input);
    
    cudaStream_t s = g_gpu_ctx.streams[0];
    CHECK_CUDA(cudaMemcpyAsync(g_gpu_ctx.d_input, f.input, INPUT * LENGTH_FEATURE0 * LENGTH_FEATURE0 * sizeof(double), cudaMemcpyHostToDevice, s));
    
    forward_gpu_async(0, 0); // index 0, identity for final layer
    
    CHECK_CUDA(cudaMemcpyAsync(f.output, g_gpu_ctx.d_output, OUTPUT * sizeof(double), cudaMemcpyDeviceToHost, s));
    
    CHECK_CUDA(cudaStreamSynchronize(s)); // Sync before using results on CPU
    
    double *output = (double*)f.output;
    uint8 result = 0;
    double maxvalue = output[0];
    for (uint8 i = 1; i < count; ++i) {
        if (output[i] > maxvalue) {
            maxvalue = output[i];
            result = i;
        }
    }
    return result;
}

extern "C" void Initial(LeNet5 *lenet)
{
    static int randbit = 0;
	if (!randbit) {
		srand((unsigned)time(0));
		for (int i = RAND_MAX; i; i >>= 1, ++randbit);
	}
    auto f64rand = [&]() {
        unsigned long long lvalue = 0x4000000000000000L;
        int i = 52 - randbit;
        for (; i > 0; i -= randbit) lvalue |= (unsigned long long)rand() << i;
        lvalue |= (unsigned long long)rand() >> -i;
        return *(double *)&lvalue - 3;
    };

	CleanupGPU();
	for (double *pos = (double *)lenet->weight0_1; pos < (double *)lenet->bias0_1; *pos++ = f64rand());
	for (double *pos = (double *)lenet->weight0_1; pos < (double *)lenet->weight2_3; *pos++ *= sqrt(6.0 / (LENGTH_KERNEL * LENGTH_KERNEL * (INPUT + LAYER1))));
	for (double *pos = (double *)lenet->weight2_3; pos < (double *)lenet->weight4_5; *pos++ *= sqrt(6.0 / (LENGTH_KERNEL * LENGTH_KERNEL * (LAYER2 + LAYER3))));
	for (double *pos = (double *)lenet->weight4_5; pos < (double *)lenet->weight5_6; *pos++ *= sqrt(6.0 / (LENGTH_KERNEL * LENGTH_KERNEL * (LAYER4 + LAYER5))));
	for (double *pos = (double *)lenet->weight5_6; pos < (double *)lenet->bias0_1; *pos++ *= sqrt(6.0 / (LAYER5 + OUTPUT)));
	for (int *pos = (int *)lenet->bias0_1; pos < (int *)(lenet + 1); *pos++ = 0);
}

extern "C" void Train(LeNet5 *lenet, image input, uint8 label)
{
    TrainBatch(lenet, (image *)input, &label, 1);
}

extern "C" void Cleanup() { CleanupGPU(); }

void PrintResult(int confusion_matrix[OUTPUT][OUTPUT])
{
	printf("%15sPredicted label\n%10s", " ", " ");
	for (int col = 0; col < 10; col++) printf("%6d", col);
	printf("%10s\n", "Total");
	for (int n = 0; n < 70; n++) printf("%s", "-");
	printf("\nTrue label\n");
	int row_labels = 0, total = 0;
	for (int row = 0; row < 10; row++) {
		row_labels = 0;
		printf("%10d", row);
		for (int col = 0; col < 10; col++) {
			printf("%6d", confusion_matrix[row][col]);
			row_labels += confusion_matrix[row][col];
		}
		printf("%10d\n", row_labels);
		total += row_labels;
	}
	for (int n = 0; n < 70; n++) printf("%s", "-");
	printf("\n%67s = %10d\n", "Total number of input images tested", total);
	for (int n = 0; n < 70; n++) printf("%s", "-");
	printf("\n");
}
