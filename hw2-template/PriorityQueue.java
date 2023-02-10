// EID 1
// EID 2
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {
	private static class Node{
		private int priority;
		private String name;
		private Node next;
		private ReentrantLock nodeLock;

		public Node(String name, int priority){
			this.name = name;
			this.priority = priority;
			this.nodeLock = new ReentrantLock();
		}
	}

	private LinkedList<Node> linkedList;
	private final int maxSize;
	private ReentrantLock lock = new ReentrantLock();
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
		lock.lock();
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
		head.nodeLock.lock();
		head.next.nodeLock.lock();
		Node newNode = new Node(name, priority);
		newNode.nodeLock.lock();

		// case 1: queue is empty which is head points to tail and tail points to null
		if((head.next == tail)){
			head.next = newNode;
			newNode.next = tail;
			head.nodeLock.unlock();
			tail.nodeLock.unlock();
			newNode.nodeLock.unlock();
			lock.unlock();
			size++;
			notEmpty.signal();
			lock.lock();
			return 0;
		}


		int index = 0;
		//Node prevNode = null;
		Node nextNode = head.next;
		Node temp = null;
		Node currNode = head;

		// find the index of where the priority
		// curr node no longer valid, duplicate, full
		while(size == maxSize){
			try {
				// wait until node is removed from linked list when
				// the linked list has reached its maximum size
				notFull.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		// lock the current node and next node
		nextNode = currNode.next;
		currNode.nodeLock.lock();
		nextNode.nodeLock.lock();

		while(currNode != tail){
			// lock .next
			// if first == second
			// first.next = newNode
			//node.next = tail
			// unlock first and second
			try{
				if(currNode.name.equals(name)){  //if the object already exists in the list
					currNode.nodeLock.unlock();
					nextNode.nodeLock.unlock();
					return -1;
				}
				if((currNode.priority < priority)  && (nextNode.priority > priority)){
					//there is a space to insert, but check for duplicates
					break;
				}
				else{
					currNode.nodeLock.unlock(); //release curr node
					temp = nextNode.next; //get the one to hand over
					currNode = nextNode; //update the current node to be next
					nextNode = temp;
					nextNode.nodeLock.lock(); //lock the "new" next node
				}
			} finally {
				currNode.nodeLock.unlock();
			}
		}

		//this while loop actually inserts the node if no duplicate
		Node left = nextNode;
		Node right= nextNode.next;
		right.nodeLock.lock();

		while(right != tail){
			if(left.name.equals(name)){ //release everything and leave
				currNode.nodeLock.unlock();
				left.nodeLock.unlock();
				right.nodeLock.unlock();
				return -1;
			}
			else{
				left.nodeLock.unlock();
				temp = right.next;
				left = right;
				right = temp;
				right.nodeLock.lock();
			}
		}
		right.nodeLock.unlock(); // unlock last node

		Node second = currNode.next; // node right next to where the newNode will be inserted
		second.nodeLock.lock(); //lock the two nodes btwn which new node will be inserted
		currNode.next = newNode;
		newNode.next = second;
		currNode.nodeLock.unlock();
		newNode.nodeLock.unlock();
		second.nodeLock.unlock();
		lock.unlock();
		size++;
		newNode.
		lock.lock();


		newNode.notEmpty.signal();
		return index;
	}

	public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.
		return -1;
	}

	public String getFirst() {
        // Retrieves and removes the name with the highest priority in the list,
        // or blocks the thread if the list is empty.
		return "";
	}
}