// gmp843
// rsv423

import java.util.concurrent.Semaphore;

/* Use only semaphores to accomplish the required synchronization */
public class SemaphoreCyclicBarrier implements CyclicBarrier {
    // CyclicBarrier is a synchronization aid that allows a set of threads
    // to wait for all other threads to reach a common point

    private int parties; // threads that need to synchronize their execution
    // TODO Add other useful variables

    private Semaphore flag;
    private Semaphore mutex;
    private boolean barrierState = true;
    private int index = 0;

    public SemaphoreCyclicBarrier(int parties) {
        this.parties = parties;
        this.flag = new Semaphore(0); // initialize with 0 permits so that the threads have to wait
        this.mutex = new Semaphore(1);
        // TODO Add any other initialization statements
    }

    /*
     * An active CyclicBarrier waits until all parties have invoked
     * await on this CyclicBarrier. If the current thread is not
     * the last to arrive then it is disabled for thread scheduling
     * purposes and lies dormant until the last thread arrives.
     * An inactive CyclicBarrier does not block the calling thread. It
     * instead allows the thread to proceed by immediately returning.
     * Returns: the arrival index of the current thread, where index 0
     * indicates the first to arrive and (parties-1) indicates
     * the last to arrive.
     */
    public int await() throws InterruptedException {
        // TODO Implement this function

        mutex.acquire();
        int currIndex = index;
        index++;
        if(barrierState){
            // when index == parties the last thread has arrived, all threads have reached barrier and are waiting
            if(index == parties){
                // semaphore is released with parties - 1 permits to unblock all the waiting parties
                // allows them to continue executing

                // parties - 1 because all prev. threads acquired() so they are waiting for permit. last thread
                // did not enter the else block so it is not waiting. therefore we need parties - 1 permits
                flag.release(parties - 1);

                // reset everything so that CyclicBarrier can be used again
                // barrierState = false;
                index = 0;
                // create a new semaphore so that rounds of threads don't take permits from other rounds
                flag = new Semaphore(0);
                mutex.release();
            }
            else{
                Semaphore f = flag;
                mutex.release();
                f.acquire();
            }
        }
        else{
            mutex.release();
        }
        return currIndex;
    }

    /*
     * This method activates the cyclic barrier. If it is already in
     * the active state, no change is made.
     * If the barrier is in the inactive state, it is activated and
     * the state of the barrier is reset to its initial value.
     */
    public void activate() throws InterruptedException {
        // TODO Implement this function

        mutex.acquire();
        if(!barrierState){
            barrierState = true;
            flag = new Semaphore(0);
            index = 0;
        }
        mutex.release();
    }

    /*
     * This method deactivates the cyclic barrier.
     * It also releases any waiting threads
     */
    public void deactivate() throws InterruptedException {
        // TODO Implement this function
        mutex.acquire();
        if(barrierState){
            barrierState = false;
            // release any permits being used
            flag.release(parties - flag.availablePermits());
        }
        mutex.release();
    }
}
