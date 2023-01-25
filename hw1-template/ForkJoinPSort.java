//UT-EID=

import java.util.Arrays;
import java.util.concurrent.*;


public class ForkJoinPSort {
    /* Notes:
     * The input array (A) is also the output array,
     * The range to be sorted extends from index begin, inclusive, to index end, exclusive,
     * Sort in increasing order when increasing=true, and decreasing order when increasing=false,
     */
    public static void parallelSort(int[] A, int begin, int end, boolean increasing) {
        // TODO: Implement your parallel sort function using ForkJoinPool
        // fork() overrides

        if(begin == end){
            return;
        }
        if(A.length <= 16){
            A = Arrays.copyOf(sort(A, increasing), A.length);
        }

        ForkJoinPool threadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        Sorted toSort = new Sorted(A, begin, end, increasing); // initializing first object
        threadPool.invoke(toSort);
    }
    public static class Sorted extends RecursiveTask<Integer> {
        private int[] A;
        private int begin;
        private int end;
        private final boolean increasing;

        public Sorted(int[] A, int begin, int end, boolean increasing){
            // need to create an object for each side so that we can call
            // fork() on each part of the array
            this.A = A;
            this.begin = begin;
            this.end = end;
            this.increasing = increasing;
        }

        @Override
        protected Integer compute() {
            if(this.begin >= this.end){
                return 1;
            }
            if(this.A.length <= 16){
                this.A = sort(this.A, increasing);
            }
            else{
//                Sorted left = new Sorted(A, begin, )
            }
            return null;
        }

        public int partition(){
            int pivot = this.A[this.end];
            int i = this.begin - 1;
            for(int j = this.begin; j <= this.end; j++){
                if(this.A[j] < pivot){
                    i++;
                    swap(i, j);
                }
            }
            swap(i + 1, end);
            return i + 1;

        }

        public void quickSort(int begin, int end){
            if(begin < end){
                int partitionIndex = partition();
            }
        }
        public void swap(int i, int j){
                int temp = this.A[i];
                this.A[i] = this.A[j];
                this.A[j] = temp;
        }

    }
    // make class here
    static int[] sort(int[] arr, boolean increasing)
    {
        int n = arr.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            int min_idx = i;
            if(increasing){
                for (int j = i+1; j < n; j++)
                    if (arr[j] < arr[min_idx]){
                        min_idx = j;
                    }
            }
            // Find the minimum element in unsorted array
            if(!increasing){
                for (int j = i+1; j < n; j++)
                    if (arr[j] > arr[min_idx]){
                        min_idx = j;
                    }
            }
            // Swap the found minimum element with the first
            // element
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }
}
