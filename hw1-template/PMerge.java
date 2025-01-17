//UT-EID=

import java.util.*;
import java.util.concurrent.*;

public class PMerge {
    /* Notes:
     * Arrays A and B are sorted in the ascending order
     * These arrays may have different sizes.
     * Array C is the merged array sorted in the descending order
     */
    public static void parallelMerge(int[] A, int[] B, int[] C, int numThreads){
        // TODO: Implement your parallel merge function
        // use binary search
        // fixed thread pool
        // edge case: upper bound and lower bound binary search for A B

        // creates an executor with max number of threads numThreads
        // if send more tasks than numThreads, then they will be blocked until some are free
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // create the main task, sort_arrays
        // this is the initial call
//        sort_arrays toSort = new sort_arrays(A, B, C, numThreads, );
//        executor.submit(toSort); // submit() can return result of computation, execute() has return type void


//        for(int i = 0; i < A.length; i++){
//            System.out.println(Arrays.toString(C));
//            sort_arrays toSort = new sort_arrays(A, B, C, true, i);
//            toSort.run();
//        }
//        for(int i =0; i < B.length; i++){
//            sort_arrays toSort1 = new sort_arrays(A, B, C, false, i);
//            toSort1.run();
//            System.out.println(Arrays.toString(C));
//        }

        for(int i = 0; i < A.length; i++){
            Future<?> cIndex = executor.submit(new sort_arrays(A, B, C, true, i));
        }

        for(int j = 0; j < B.length; j++){
            executor.submit(new sort_arrays(A, B, C, false, j));
        }
        // manual shutdown
        executor.shutdown();
        try {
            executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   }

    public static class sort_arrays implements Runnable{
        private int[] A;
        private int[] B;
        private int[] C;
        private boolean compA; //which number thread
        private int currIndex;

        public sort_arrays(int[] A, int[]B, int[] C, boolean compA, int currIndex){
            // need to create an object for each side so that we can call
            // fork() on each part of the array
            this.A = A;
            this.B = B;
            this.C = C;
            this.compA = compA;
            this.currIndex = currIndex;
        }
        @Override
        public void run(){
            int indexC;
            if(compA){
                indexC = currIndex + LowerBoundBinarySearch(B, A[currIndex]);
                indexC = C.length - indexC - 1;
                C[indexC] = A[currIndex];

            }
            if(!compA){
                indexC = currIndex + UpperBoundBinarySearch(A, B[currIndex]);
                indexC = C.length - indexC - 1;
                C[indexC] = B[currIndex];
            }
        }

        public int LowerBoundBinarySearch(int[] arr, int elem) {
//            int low = 0;
//            int high = arr.length - 1;
//            int mid = -1;
//            while (low < high) {
//                mid = low + (high - low + 1)/2;
//                if (arr[mid] >= elem) {
//                    return mid;
//                }
//                else if (elem > arr[mid]){
//                    low = mid + 1 ;
//                }else{
//                    high = mid;
//                }
//            }
//            return mid;

            int low = 0;
            int high = arr.length - 1;
            int mid;
            while(low < high){
                mid = low + (high - low) / 2;
                if (elem > arr[mid]){
                    low = mid + 1;
                }
                else{
                    high = mid;
                }
            }

            if((low <= (arr.length - 1)) && arr[low] < elem){
                low++;
            }

            return low;
        }
        public int UpperBoundBinarySearch(int[] arr, int elem){
//            int low = 0;
//            int high = arr.length - 1;
//            int mid = -1 ;
//            while (low < high) {
//                mid = low + (high-low)/2;
//                if (arr[mid] >= elem) {
//                    return mid;
//                }
//                else if (elem > arr[mid]){
//                    low = mid;
//                }else{
//                    high = mid - 1;
//                }
//            }
//            return mid;
            int low = 0;
            int high = arr.length - 1;
            int mid;

            while(low < high){
                mid = low + (high - low) / 2;
                if(elem < arr[mid]){
                    high = mid;
                }
                else{
                    low = mid + 1;
                }
            }
            if(low <= (arr.length - 1) && arr[low] <= elem){
                low++;
            }
            return low;
        }

    }
}


