// EID 1
// EID 2


import java.util.concurrent.Semaphore;

/* Use only semaphores to accomplish the required synchronization */
public class SemaphoreCyclicBarrier implements CyclicBarrier {
    // CyclicBarrier is a synchronization aid that allows a set of threads
    // to wait for all other threads to reach a common point

    private int parties; // threads that need to synchronize their execution
    // TODO Add other useful variables

    private boolean barrierActive;
    private Semaphore flag = new Semaphore(parties);
    private int activeParties;

    public SemaphoreCyclicBarrier(int parties) {
        this.parties = parties;
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
    public int await() throws InterruptedException { // how we indicate a certain thread has reached their barrier?
        // TODO Implement this function
        // when a thread calls this method, it gets blocked until
        // until the number of parties have also called await

        // when all parties have called await all threads are released
//        try{
//            flag.acquire();
//        }catch (InterruptedException e) {
//
//            // Print and display the line number where
//            // exception occurred
//            e.printStackTrace();
//        }
//        finally{
//            while(flag.availablePermits() != 0){
//                flag.release();
//            }
//
//        }
        flag.acquire();
        activeParties++;
        if(activeParties == parties){
            flag.release(parties);
        }
        // ask about arrival index

        return -1;
    }

    /*
     * This method activates the cyclic barrier. If it is already in
     * the active state, no change is made.
     * If the barrier is in the inactive state, it is activated and
     * the state of the barrier is reset to its initial value.
     */
    public void activate() throws InterruptedException {
        // TODO Implement this function
        if(!this.barrierActive){
            this.barrierActive = true;
            // reset to its initial value?
        }

    }

    /*
     * This method deactivates the cyclic barrier.
     * It also releases any waiting threads
     */
    public void deactivate() throws InterruptedException {
        // TODO Implement this function
    }
}
