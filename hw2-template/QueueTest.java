import java.util.ArrayList;

public class QueueTest implements  Runnable {
    static int maxSize = 5;
    static PriorityQueue ourList;
    String name;
    int priority;

    public QueueTest(PriorityQueue ourList, String name, int priority){
        this.ourList = ourList;
        this.name = name;
        this.priority = priority;
    }

    static String[] strArr = new String[]{"A", "b", "C", "D", "E"};
    static Integer[] prior = new Integer[]{1,2,3,4,5};


    public static void main(String[] args) throws InterruptedException {
        PriorityQueue Queue = new PriorityQueue(maxSize);
        Thread[] t = new Thread[maxSize];
        for (int i = 0; i < maxSize; i++) {
            t[i] = new Thread((Runnable) new QueueTest(Queue, strArr[i], prior[i]));
        }
        for (int i = 0; i < maxSize; ++i) {
            t[i].start();
            Thread.sleep(50);
        }
        for (int i = 0; i < maxSize; ++i) {
            t[i].join();
        }
    }

    @Override
    public void run() {
        ourList.add(name, priority);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
