// EID 1
// EID 2

public class FairUnifanBathroom {
  boolean longhornOnly;
  int numhorns;
  int numsooners;
  int ticketNum = 0;
  int bathroomNum = 0;


  public synchronized void enterBathroomUT() {// Called when a UT fan wants to enter bathroom
	ticketNum++; //increment whos in the line
  	while (!longhornOnly || (bathroomNum == 7)){ //if OU in the bathroom or line full
		wait();
	}
  	numhorns++;
  	bathroomNum++; //allow longhorn to enter, inc num ppl in bathroom
	longhornOnly = true; //now only longhorns allowed in bathroom
	notifyAll();
  }
	
	public synchronized void enterBathroomOU() {
    // Called when a OU fan wants to enter bathroom
	}
	
	public synchronized void leaveBathroomUT() {
    // Called when a UT fan wants to leave bathroom
		bathroomNum --; //num people in bathroom decreases
		numhorns--;
		if(numhorns == 0){ //if no more longhorns in the bathroom
			longhornOnly = false;
		}
		notifyAll();

	}

	public synchronized void leaveBathroomOU() {
    // Called when a OU fan wants to leave bathroom
	}
}
	
