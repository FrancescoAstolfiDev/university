/***************************************************************************
 *
 * Parallel version of Quick sort using pthreads
 *
 ***************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

#define KILO (1024)
#define MEGA (1024*1024)
#define MAX_ITEMS (64*MEGA)
#define swap(v, a, b) {unsigned tmp; tmp=v[a]; v[a]=v[b]; v[b]=tmp;}

unsigned NUM_THREADS = 32;
sem_t thread_semaphore;

unsigned MIN_SIZE = 131072; // MAX_ITEMS / 512
int CHECK = 0;

static int *v;

typedef struct {
    int *v;
    unsigned low;
    unsigned high;
} sort_args_t;

static unsigned
partition(int *v, unsigned low, unsigned high, unsigned pivot_index)
{
    if (pivot_index != low)
        swap(v, low, pivot_index);

    pivot_index = low;
    low++;

    while (low <= high) {
        if (v[low] <= v[pivot_index])
            low++;
        else if (v[high] > v[pivot_index])
            high--;
        else
            swap(v, low, high);
    }

    if (high != pivot_index)
        swap(v, pivot_index, high);
    return high;
}

static void* quick_sort_thread(void *arg);

static void
quick_sort(int *v, unsigned low, unsigned high)
{
    unsigned pivot_index = (low + high) / 2;
    pivot_index = partition(v, low, high, pivot_index);

    unsigned len1 = pivot_index - low;
    unsigned len2 = high - pivot_index;

    sort_args_t* arg;
    pthread_t t;

    int spawn_left = 0;
    int spawn_right = 0;
    int created_thread = 0;

    // Decide which side to spawn a thread for, the larger one if threshold is not met
    if (len1 >= MIN_SIZE && len1 >= len2) spawn_left = 1;
    else if (len2 >= MIN_SIZE && len2 > len1) spawn_right = 1;

    // Spawn thread for the larger side
    if (spawn_left) {
        // Try to acquire semaphore before creating a new thread, if all threads are busy, sort in current thread
        if (sem_trywait(&thread_semaphore) == 0) {
            arg = (sort_args_t*)malloc(sizeof(sort_args_t));
            arg->v = v;
            arg->low = low;
            arg->high = pivot_index - 1;
            pthread_create(&t, NULL, quick_sort_thread, arg);
            created_thread = 1;
        } else {
            quick_sort(v, low, pivot_index - 1);
        }

        if (len2 > 1) {
            quick_sort(v, pivot_index + 1, high);
        }
    } else if (spawn_right) {
        // Try to acquire semaphore before creating a new thread, if all threads are busy, sort in current thread
        if (sem_trywait(&thread_semaphore) == 0) {
            arg = (sort_args_t*)malloc(sizeof(sort_args_t));
            arg->v = v;
            arg->low = pivot_index + 1;
            arg->high = high;
            pthread_create(&t, NULL, quick_sort_thread, arg);
            created_thread = 1;
        } else {
            quick_sort(v, pivot_index + 1, high);
        }
        
        if (len1 > 1) {
            quick_sort(v, low, pivot_index - 1);
        }
        
    } else {
        if (len1 > 1) {
            quick_sort(v, low, pivot_index - 1);
        }
        if (len2 > 1) {
            quick_sort(v, pivot_index + 1, high);
        }
    }
    if (created_thread) pthread_join(t, NULL);
}

static void* quick_sort_thread(void *arg)
{
    sort_args_t* a = (sort_args_t*)arg;
    quick_sort(a->v, a->low, a->high); // NOTE: function call does not add much overhead!
    sem_post(&thread_semaphore); // Release semaphore
    free(arg);
    return NULL;
}

static void
init_array(void)
{
    v = (int *) malloc(MAX_ITEMS * sizeof(int));
    if (!v) {
        fprintf(stderr, "malloc failed\n");
        exit(EXIT_FAILURE);
    }
    for (unsigned i = 0; i < MAX_ITEMS; i++)
        v[i] = rand();
}

static void
check_sorted(void)
{
    for (unsigned i = 1; i < MAX_ITEMS; i++) {
        if (v[i - 1] > v[i]) {
            printf("Array is not sorted at index %u: %d > %d\n", i - 1, v[i - 1], v[i]);
            return;
        }
    }
    printf("Array is sorted.\n");
}

void
Read_Options(int argc, char **argv)
{
    char    *prog;

    prog = *argv;
    while (++argv, --argc > 0)
        if (**argv == '-')
            switch ( *++*argv ) {
                case 's':
                    --argc;
                    MIN_SIZE = atoi(*++argv);
                    break;
                case 'C':
                    --argc;
                    CHECK = atoi(*++argv);
                    break;
                case 'N':
                    --argc;
                    NUM_THREADS = atoi(*++argv);
                    break;
                default:
                    printf("%s: ignored option: -%s\n", prog, *argv);
                    break;
            }
}

int
main(int argc, char **argv)
{
    Read_Options(argc,argv);
    init_array();
    //print_array();

    sem_init(&thread_semaphore, 0, NUM_THREADS); // Initialize semaphore

    quick_sort(v, 0, MAX_ITEMS - 1);

    if (CHECK) check_sorted();
    //print_array();

    sem_destroy(&thread_semaphore); // Destroy semaphore
    free(v);
    return 0;
}
