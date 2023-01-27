//UT-EID=

import java.util.*;
import java.util.concurrent.*;

public class RunnablePSort {
    /* Notes:
     * The input array (A) is also the output array,
     * The range to be sorted extends from index begin, inclusive, to index end, exclusive,
     * Sort in increasing order when increasing=true, and decreasing order when increasing=false,
     */
    public static void parallelSort(int[] A, int begin, int end, boolean increasing){
        // TODO: Implement your parallel sort function using Runnables

        Sorted toSort = new Sorted(A, begin,end, increasing);
        Thread thread = new Thread(toSort);
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){ System.out.println("main");}

    }
    public static class Sorted implements Runnable {
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
        public void run() {
            if(begin >= end){
                return;
            }
            if((end - begin) <= 16){
                InsertionSort(A, increasing);
            }
            else{
                int index = partition(A, begin, end, increasing);
                Sorted left = new Sorted(A, begin, index, increasing);
                Sorted right = new Sorted(A, index + 1, end, increasing);
                Thread leftThread = new Thread(left);
                Thread rightThread = new Thread(right);
                leftThread.start();
                rightThread.start();
                try{
                    leftThread.join();
                    rightThread.join();
                }catch (InterruptedException e){
                    System.out.println("System exception");
                }
            }
        }

        public int partition(int[] array, int begin, int end, boolean increasing){
            int pivot = array[end];
            int i = 0 ;
            if(increasing) {
                i = begin - 1;
                for(int j = begin; j <= end; j++){
                    if (array[j] < pivot) {  //move smaller elem to left of pivot
                        i++;
                        swap(array, i, j);
                    }
                }
            }  if(!increasing) {
                i = begin - 1;
                for(int j = begin; j < end; j++){
                    if (array[j] < pivot) {  //move smaller elem to left of pivot
                        i++;
                        swap(array, i, j);
                    }
                }
            }
            swap(array, i + 1, end);
            return i + 1;
        }

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
                if(increasing){
                    while ((j >= 0) && (arr[j] > key)) {
                        arr[j + 1] = arr[j];
                        j = j - 1;
                    }
                }
                if(!increasing){
                    while ((j >= 0) && (arr[j] < key)) {
                        arr[j + 1] = arr[j];
                        j = j - 1;
                    }
                }
                arr[j + 1] = key;
            }
        }
    }
}