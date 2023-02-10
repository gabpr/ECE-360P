// EID 1
// EID 2

public class FairUnifanBathroom {
    boolean longhornOnly = true;
    boolean isEmpty =  true;
    int numhorns = 0;
    int numsooners = 0;
    int ticketNum = 0;
    int bathroomNum = 0;
    int lineCount = 1;

    public FairUnifanBathroom(){
        this.numsooners = 0;
        this.numhorns = 0;
        this.bathroomNum = 0;
        this.lineCount = 1;
        this.ticketNum = 0;
    }
    public synchronized void enterBathroomUT() {// Called when a UT fan wants to enter bathroom
        int thisticket;
        ticketNum++; //increment whos in the line
        System.out.println("UT requests: ticket " + ticketNum);
        thisticket = ticketNum;
        //next number to give out
        // next number to go into bathroom
        while ((!longhornOnly && !isEmpty) || (bathroomNum == 7)  || (thisticket != lineCount)) { //if not empty and OU in the bathroom or line full
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lineCount++;
        numhorns++;
        bathroomNum++; //allow longhorn to enter, inc num ppl in bathroom
        longhornOnly = true; //now only longhorns allowed in bathroom
		isEmpty = false;
		System.out.println("UT Fan entered using ticket " + thisticket + " " + numhorns + " horns in the bathroom now");
        notifyAll();
    }

    public synchronized void enterBathroomOU() {
        // Called when a OU fan wants to enter bathroo

        int thisticket;
        ticketNum++; //increment whos in the line
        thisticket = ticketNum;
       System.out.println("OU requests ticket " + ticketNum);
        while ((!isEmpty && longhornOnly) || (bathroomNum == 7) || (thisticket != lineCount)) { //if UT in the bathroom and its not empty or line full wait
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numsooners++;
        lineCount++;
        bathroomNum++; //allow OU to enter, inc num ppl in bathroom
        longhornOnly = false; //now only OU allowed in bathroom
		isEmpty = false;
        System.out.println("OU Fan entered with " + thisticket + " " + numsooners + " sooners in the bathroom now");
        notifyAll();
    }

    // do not need to wait when leaving the bathroom
    public synchronized void leaveBathroomUT() {
        // Called when a UT fan wants to leave bathroom
        if(!isEmpty) {
            bathroomNum--; //num people in bathroom decreases
            numhorns--;
            if (numhorns == 0) { //if no more longhorns in the bathroom
                longhornOnly = false;
            }
            if (bathroomNum == 0) {
                isEmpty = true;
            }
            System.out.println("UT Fan left, longhorns left: " + numhorns);
            //notifyAll();
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
        System.out.println("OU Fan left, number OU left: " + numsooners);
        notifyAll();
    }
}
	
