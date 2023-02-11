// EID 1
// EID 2

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {
    private static class Node {
        private int priority;
        private String name;
        private Node next;
        private ReentrantLock nodeLock;
        private boolean lockstatus;

        public Node(String name, int priority) {
            this.name = name;
            this.priority = priority;
            this.nodeLock = new ReentrantLock();
        }

        public void lockStatus() {
            if (nodeLock.isLocked()) {
                lockstatus = true;
            } else {
                lockstatus = false;
            }
            System.out.println(name + lockstatus);
        }
    }

    private LinkedList<Node> linkedList;
    private final int maxSize;
    private ReentrantLock lock;
    Node head;
    Node tail;
    private int size;
    private Condition notEmpty;
    private Condition notFull;


    public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
        this.maxSize = maxSize;
        this.linkedList = new LinkedList<>();
        size = 0;
        // dummy variables
        head = new Node("head", -1);
        tail = new Node("tail", 10);
        head.next = tail;
        tail.next = null;
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    //Q: can i just lock one node and then the next?
    //why lock if searching
    public int add(String name, int priority) {
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.

        // TA said that we can't use global locks meaning we lock
        // at the beggining and end of this method
        // but we can have a lock here that isn't specifically for a node
        // rather a lock that would lock when a change is being made to the queue?

        // if not empty:
        // firt lock the first two
        // unlock head, lock tail

        // lock node we're inserting
        //System.out.println(head.nodeLock.isLocked());
        head.nodeLock.lock();
        head.next.nodeLock.lock();
        Node newNode = new Node(name, priority);
        newNode.nodeLock.lock();

        // case 1: queue is empty which is head points to tail and tail points to null
        if ((head.next == tail)) {
            newNode.next = tail;
            head.next = newNode;
            head.nodeLock.unlock();
            tail.nodeLock.unlock();
            newNode.nodeLock.unlock();
            lock.lock();
            size++;
            notEmpty.signal();
            lock.unlock();
            System.out.println("added node " + name + " with priority " + priority);
            return 0;
        }

        int index = -1;
        Node nextNode;
        Node temp;
        Node currNode = head;
        nextNode = currNode.next;

        currNode.nodeLock.lock();
        nextNode.nodeLock.lock();

        while (size == maxSize) {
            try {
                // wait until node is removed from linked list when
                // the linked list has reached its maximum size
                notFull.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // currNode and nextNode are locked
        // looks for where to insert
        while (nextNode != tail) {
            index++;
            System.out.println("current index: " + index);
            System.out.println("trying to add node " + name);
            System.out.println("current node name : " + currNode.name);
            System.out.println("next node name: " + nextNode.name);
            try {
                if (currNode.name.equals(name)) {  //if the object already exists in the list
                    currNode.nodeLock.unlock();
                    nextNode.nodeLock.unlock();
                    newNode.nodeLock.unlock();
                    return -1;
                }
                if ((currNode.priority < priority) && (nextNode.priority > priority)) {
                    //there is a space to insert, but check for duplicates
                    System.out.println("reached here");
                    break;
                } else {
                    currNode.nodeLock.unlock(); //release curr node
                    temp = nextNode.next; //get the one to hand over
                    currNode = nextNode; //update the current node to be next
                    nextNode = temp;
                    nextNode.nodeLock.lock(); //lock the "new" next node
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("currently here");
        // edge case: nextNode == tail and currNode == highest priority
        if((priority > currNode.priority) && (nextNode == tail)){
            newNode.next = tail;
            currNode.next = newNode;

            newNode.nodeLock.unlock();
            currNode.nodeLock.unlock();
            nextNode.nodeLock.unlock();

            lock.lock();
            size++;
            notEmpty.signal();
            lock.unlock();
            index++;
            System.out.println("added node " + name);
            return index;
        }

        //this while loop actually inserts the node if no duplicate
        if (nextNode != tail) {
            Node left = nextNode;
            Node right = nextNode.next;
            right.nodeLock.lock();
            left.nodeLock.lock();

            while (right != tail) {
                if (left.name.equals(name)) { //release everything and leave
                    currNode.nodeLock.unlock();
                    left.nodeLock.unlock();
                    right.nodeLock.unlock();
                    newNode.nodeLock.unlock();
                    return -1;
                } else {
                    left.nodeLock.unlock();
                    temp = right.next;
                    left = right;
                    right = temp;
                    right.nodeLock.lock();
                }
            }
            // checking that last node
            if(left.name.equals(name)){
                currNode.nodeLock.unlock();
                left.nodeLock.unlock();
                right.nodeLock.unlock();
                newNode.nodeLock.unlock();
                return -1;
            }

            right.nodeLock.unlock();
            left.nodeLock.unlock();
        }

        Node second = currNode.next; // node right next to where the newNode will be inserted
        second.nodeLock.lock(); //lock the two nodes btwn which new node will be inserted
        newNode.next = second;
        currNode.next = newNode;
        System.out.println(name + " points to " + newNode.next.name);
        System.out.println("added node " + name + " with priority " + priority);
        currNode.nodeLock.unlock();
        newNode.nodeLock.unlock();
        second.nodeLock.unlock();
//        head.nodeLock.unlock();
//        head.next.nodeLock.unlock();
//        tail.nodeLock.unlock();
        lock.lock();
        size++;
        System.out.println(size);
        notEmpty.signal();
        lock.unlock();

        return index;
    }

    public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.

        Node currNode = head;
        Node nextNode = head.next;
        currNode.nodeLock.lock();
        nextNode.nodeLock.lock();

        // index is local so we don't have to lock
        // init to -1 because of the dummy head node
        int index = -1;

        while (nextNode != tail) {
            if (currNode.name.equals(name)) {
                currNode.nodeLock.unlock();
                nextNode.nodeLock.unlock();
                return index;
            } else {
                currNode.nodeLock.unlock();
                Node temp = nextNode.next;
                currNode = nextNode;
                nextNode = temp;
                nextNode.nodeLock.lock();
                index++;
            }
        }
        // still need to check that last node
        if (currNode.name.equals(name)) {
            currNode.nodeLock.unlock();
            nextNode.nodeLock.unlock();
            return index;
        }
        return index;
    }

    public String getFirst() {
        // Retrieves and removes the name with the highest priority in the list,
        // or blocks the thread if the list is empty.

        // the one right before the tail is the name with the highest priority in the list
        head.nodeLock.lock();
        head.next.nodeLock.lock();

        Node currNode = head;
        Node nextNode = head.next;

        while (currNode.next == tail) {
            try {
                // wait until there is a node
                notEmpty.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while (nextNode.next != tail) {
            currNode.nodeLock.unlock();
            Node temp = nextNode.next;
            currNode = nextNode;
            nextNode = temp;
            nextNode.nodeLock.lock();
        }

        String name = nextNode.name;
        currNode.next = tail;
        currNode.nodeLock.unlock();
        nextNode.nodeLock.unlock();

        lock.lock();
        size--;
        notFull.signal();
        lock.unlock();
        return name;
    }
}