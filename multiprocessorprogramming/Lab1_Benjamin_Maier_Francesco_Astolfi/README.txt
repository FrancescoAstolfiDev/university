1. Parallel Gaussian elimination

Compile: gcc -O2 -o gaussianseq gaussianseq.c
Run: time ./gaussianseq

Compile: gcc -O2 -o gaussianpar gaussianpar.c -lpthread
RUN: time ./gaussianpar

1. Parallel Quicksort

Compile: gcc -O2 -o qsortseq qsortseq.c
Run: time ./qsortseq

Compile: gcc -O2 -o qsortpar qsortpar.c -lpthread
RUN: time ./qsortpar