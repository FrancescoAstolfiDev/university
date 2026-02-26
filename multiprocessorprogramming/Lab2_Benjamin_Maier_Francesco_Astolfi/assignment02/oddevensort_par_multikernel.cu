#include <vector>
#include <algorithm>
#include <iostream>
#include <chrono>

#include <cuda_runtime.h>

// The odd-even sort algorithm
// Total number of odd phases + even phases = the number of elements to sort
void oddeven_sort(std::vector<int>& numbers)
{
    auto s = numbers.size();
    for (int i = 1; i <= s; i++) {
        for (int j = i % 2; j < s-1; j = j + 2) { // This can be parallelized
            if (numbers[j] > numbers[j + 1]) {
                std::swap(numbers[j], numbers[j + 1]);
            }
        }
    }
}

void print_sort_status(std::vector<int> numbers)
{
    std::cout << "The input is sorted?: " << (std::is_sorted(numbers.begin(), numbers.end()) == 0 ? "False" : "True") << std::endl;
}

__global__
void oddeven_phase_kernel(int* d_data, int n, int phase)
{
    int tid      = blockIdx.x * blockDim.x + threadIdx.x;
    int nthreads = blockDim.x * gridDim.x;

    int start = phase % 2;                 // 0 even, 1 odd

    int pairs = (n - start) / 2;          // integer division

    for (int pair_id = tid; pair_id < pairs; pair_id += nthreads) {
        int j = start + 2 * pair_id;
        if (j < n - 1) {
            if (d_data[j] > d_data[j + 1]) {
                int tmp       = d_data[j];
                d_data[j]     = d_data[j + 1];
                d_data[j + 1] = tmp;
            }
        }
    }
}

void oddeven_sort_gpu_multikernel(std::vector<int>& numbers, int threadsPerBlock, int numBlocks)
{
    size_t n = numbers.size();
    size_t bytes = n * sizeof(int);

    int* cuda_data = nullptr;
    cudaMalloc(&cuda_data, bytes);
    cudaMemcpy(cuda_data, numbers.data(), bytes, cudaMemcpyHostToDevice);

    // One kernel launch per phase
    for (int phase = 1; phase <= n; ++phase) {
        oddeven_phase_kernel<<<numBlocks, threadsPerBlock>>>(cuda_data, n, phase);
        // Ensure completion before next phase:
        // cudaDeviceSynchronize(); // not needed
    }

    cudaMemcpy(numbers.data(), cuda_data, bytes, cudaMemcpyDeviceToHost);
    cudaFree(cuda_data);
}

int main()
{
    constexpr unsigned int size = 100000; // Number of elements in the input
    // constexpr unsigned int size = 524288; // Number of elements in the input

    // Initialize a vector with integers of value 0
    std::vector<int> numbers(size);
    // Populate our vector with (pseudo)random numbers
    srand(time(0));
    std::generate(numbers.begin(), numbers.end(), rand);

    const int threadsPerBlock = 1024;
    const int numBlocks      = 16;

    print_sort_status(numbers);
    auto start = std::chrono::steady_clock::now();
    oddeven_sort_gpu_multikernel(numbers, threadsPerBlock, numBlocks);
    auto end = std::chrono::steady_clock::now();
    print_sort_status(numbers);
    std::cout << "Elapsed time =  " << std::chrono::duration<double>(end - start).count() << " sec\n";
}