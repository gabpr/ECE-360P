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
		private Condition notEmpty;
		private Condition notFull;

		public Node(String name, int priority){
			this.name = name;
			this.priority = priority;
			this.nodeLock = new ReentrantLock();
			this.notEmpty = nodeLock.newCondition();
			this.notFull = nodeLock.newCondition();
		}
	}

	private LinkedList<Node> linkedList;
	private final int maxSize;
	private Node head;

	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
		this.maxSize = maxSize;
		this.linkedList = new LinkedList<>();
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

		Node newNode = new Node(name, priority);
		int index = 0;
		Node prevNode = null;
		Node currNode = head;
		// do we add a lock?

		if(head == null){
			head = newNode;
			return index;
		}

		// find the index of where the priority
		// curr node no longer valid, duplicate, full

		// lock the current node
		while(currNode != null){
			// lock the current node to access its values
			currNode.nodeLock.lock();
			try{
				if(currNode.priority < priority){
					if(prevNode != null){
						prevNode.nodeLock.unlock();
					}
					prevNode = currNode;
					currNode = currNode.next;
				}
				else{
					break;
				}
			} finally {
				currNode.nodeLock.unlock();
			}
		}
		// if it does equal null, then the list is empty



		/*
		TA said to combine these
		//		for(Node currNode : linkedList){
//			if(currNode.name.equals(newNode.name)){
//				return -1;
//			}
//			if(currNode.priority >= priority){
//				break;
//			}
//			index++;
//		}
//
//		for(int i = 0; i < index; i ++) {
//			prevNode = currNode;
//			currNode.nodeLock.lock();
//			currNode = linkedList.get(i + 1);
//		}
//		if(prevNode != null){
//			prevNode.nodeLock.unlock();
//		}
		 */

		while(linkedList.size() == maxSize){
			try {
				// wait until node is removed from linked list when
				// the linked list has reached its maximum size
				currNode.notFull.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		// get lock on current and the next one
		linkedList.add(index, newNode);

		// notEmpty Condition waits until a node has been added to the list
		// to indicate that it is no longer empty
		// this signals to any thread waiting in getFirst() that the list is not empty

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