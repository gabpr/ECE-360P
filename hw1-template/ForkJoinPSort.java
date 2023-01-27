//UT-EID= rsv423,

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
        else{
            ForkJoinPool threadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            Sorted toSort = new Sorted(A, begin, end, increasing); // initializing first object
            threadPool.invoke(toSort);
        }
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
            this.begin = begin; //begin inclusive
            this.end = end - 1; //end exclusive
            this.increasing = increasing;
        }

        @Override
        protected Integer compute() {
            if(this.begin >= this.end){
                return null;
            }
            if(this.A.length <= 16){
                InsertionSort(A, increasing);
            }
            else{
                int index = partition(A, begin, end, increasing);
                Sorted left = new Sorted(A, begin, index, increasing);
                Sorted right = new Sorted(A, index+1, end, increasing);
                left.fork();
                right.compute();
                left.join();
            }
            return null;
        }


        public int partition(int[] array, int begin, int end, boolean increasing){
            int pivot = array[end];
            int i = 0 ;
            if(increasing) {
                i = begin - 1;
                for(int j = begin; j < end; j++){
                    if (array[j] < pivot) {  //move smaller elem to left of pivot
                        i++;
                        swap(array, i, j);
                    }
                }
            }  if(!increasing) {
                i = begin;
                for(int j = begin; j < (end - 1); j++){
                    if (array[j] < pivot) {  //move smaller elem to left of pivot
                        i++;
                        swap(array, i, j);
                    }
                }
            }
            swap(array, i + 1, end);
            return i ;
        }

     /*   public void quickSort(){
           if(begin < end){
                int partitionIndex = partition();
                quickSort();
            }
        } */

        public void swap(int[] array, int i, int j){
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
        }

        public void InsertionSort(int[] arr, boolean increasing){
            int n = arr.length;
            for (int i = 1; i < n; ++i) {
                int key = arr[i];
                int j = i - 1;
                while ((j >= 0) && (arr[j] > key)) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
        }
    }
}
