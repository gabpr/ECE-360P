// EID 1
// EID 2

public class FairUnifanBathroom {
    boolean longhornOnly;
    boolean isEmpty;
    int numhorns;
    int numsooners;
    int ticketNum = 0;
    int bathroomNum = 0;
    int lineCount = 0;

    // Q; how to check that fans will be let in in order? - reva

    public synchronized void enterBathroomUT() {// Called when a UT fan wants to enter bathroom
        ticketNum++; //increment whos in the line
		lineCount++;
        while ((!longhornOnly && !isEmpty) || (bathroomNum == 7) || (ticketNum > lineCount)) { //if not empty and OU in the bathroom or line full
           // wait();
        }
        numhorns++;
        bathroomNum++; //allow longhorn to enter, inc num ppl in bathroom
        longhornOnly = true; //now only longhorns allowed in bathroom
		isEmpty = false;
        notifyAll();
    }

    public synchronized void enterBathroomOU() {
        // Called when a OU fan wants to enter bathroom
        ticketNum++; //increment whos in the line
		lineCount++;
        while ((!isEmpty && longhornOnly) || (bathroomNum == 7)) { //if UT in the bathroom and its not empty or line full wait
          //  wait();
        }
        numsooners++;
        bathroomNum++; //allow OU to enter, inc num ppl in bathroom
        longhornOnly = false; //now only OU allowed in bathroom
		isEmpty = false;
        notifyAll();
    }

    // do not need to wait when leaving the bathroom
    public synchronized void leaveBathroomUT() {
        // Called when a UT fan wants to leave bathroom
        bathroomNum--; //num people in bathroom decreases
        numhorns--;
        if (numhorns == 0) { //if no more longhorns in the bathroom
            longhornOnly = false;
        }
		if(bathroomNum == 0){
			isEmpty = true;
		}
        notifyAll();
    }

    public synchronized void leaveBathroomOU() {
        // Called when a OU fan wants to leave bathroom
        bathroomNum--; //num people in bathroom decreases
        numsooners--;
        if (numsooners == 0) { //if no more OU in the bathroom
            longhornOnly = true;
        }
        if(bathroomNum == 0){
        	isEmpty = true;
		}
        notifyAll();
    }
}
	
