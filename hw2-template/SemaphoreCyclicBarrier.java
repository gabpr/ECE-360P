// EID 1
// EID 2


import java.util.concurrent.Semaphore;

/* Use only semaphores to accomplish the required synchronization */
public class SemaphoreCyclicBarrier implements CyclicBarrier {
    // CyclicBarrier is a synchronization aid that allows a set of threads
    // to wait for all other threads to reach a common point

    private int parties; // threads that need to synchronize their execution
    // TODO Add other useful variables

    private Semaphore flag;
    private int total;
    private int index = 0;

    public SemaphoreCyclicBarrier(int parties) {
        this.parties = parties;
        this.flag = new Semaphore(0); // initialize with 0 permits so that the threads have to wait
        this.total = 0;
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

        // save index of which thread has arrived
        int currIndex = index;

        // each time a party calls await(), total is incremented by 1
        // when total == parties, the last thread has arrived and all threads have reached the barrier and are waiting
        if(++total == parties){
            // semaphore is released with parties - 1 permits to unblock all the waiting parties
            // allows them to continue executing

            // parties - 1 because all prev. threads acquired() so the are waiting for permit. last thread
            // did not enter the else block so it is not waiting. therefore we need parties - 1 permits
            flag.release(parties - 1);

            // reset everything so that CyclicBarrier can be used again
            total = 0;
            index = 0;
        }
        else{
            index++;
            flag.acquire();
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

        // CyclicBarrier is in the active state when total == parties
        // if total < parties, CyclicBarrier is in the reset state
        // that means it's not in active state and is ready for the next set of parties to reach the barrier


        // initial value of the barrier is the number of parties it was initialized with
        // init value = 5 if parties = 5
        if(total < parties){
            total = 0;
            await(); // ??
        }
    }

    /*
     * This method deactivates the cyclic barrier.
     * It also releases any waiting threads
     */
    public void deactivate() throws InterruptedException {
        // TODO Implement this function
        flag.drainPermits();
        total = 0;
    }
}
