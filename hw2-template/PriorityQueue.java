// EID 1
// EID 2
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {
	int maxSize;
	int count = 0 ; //how many in list 0 - 9
	Node head = null;
	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
		this.maxSize = maxSize;
	}

	//Q: can i just lock one node and then the next?
	//why lock if searching
	public int add(String name, int priority) {
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.
		Node newNode = new Node(name, priority);
		if(count == 0){
			head = newNode; //insert node directly
			count++;
		}
		while(count == (maxSize -1)){
		//	wait(); //can i do this??? - reva
		}

		return -1;
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

	//node object
	public class Node{
		ReentrantLock nodeLock = new ReentrantLock();
		public String name;
		public int priority;
		public Node next;

		public Node(String name, int priority){
			this.name = name;
			this.priority = priority;
			this.next = null;
		}
	}

}