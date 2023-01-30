//UT-EID=

import java.util.*;
import java.util.concurrent.*;

public class PMerge {
    /* Notes:
     * Arrays A and B are sorted in the ascending order
     * These arrays may have different sizes.
     * Array C is the merged array sorted in the descending order
     */
    public static void parallelMerge(int[] A, int[] B, int[] C, int numThreads) {
        // TODO: Implement your parallel merge function
        // use binary search
        // fixed thread pool
        // edge case: upper bound and lower bound binary search for A B
    }

    public class sorted_arrays implements Runnable{
        private int[] A;
        private int[] B;
        private int[] C;
        private int tNum; //which number thread

        public sorted_arrays(int[] A, int[]B, int[] C, int tNum){
            // need to create an object for each side so that we can call
            // fork() on each part of the array
            this.A = A;
            this.B = B;
            this.C = C;
            this.tNum = tNum;

        }
        @Override
        public void run(){
            if(tNum < A.length){ //compare thread number to

            }

        }

        public int binarySearch(int[] arr, int elem) { //modify so that it returns the index of where
            int low = 0;
            int high = arr.length - 1;
            int mid = -1 ;
            while (low != high) {
                mid = (low + high) / 2;
                if (arr[mid] == elem) {
                    return mid;
                }
                if (elem > arr[mid]){
                    low = mid +1 ;
                }else if(elem < arr[mid]){
                    high = mid - 1;
                }
            }
            return mid;
        }
    }
}


