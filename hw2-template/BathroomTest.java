public class BathroomTest implements Runnable{
    static int numFans = 12;
    FairUnifanBathroom bathroom;
    int action;
    boolean longhorn;

    public BathroomTest(FairUnifanBathroom bathroom, boolean longhorn) {
        this.bathroom = bathroom;
        this.action = action;
        this.longhorn = longhorn;
    }
    public void test1(){

    }

    public static void main(String[] args) throws InterruptedException {
        FairUnifanBathroom CottonBowl = new FairUnifanBathroom();
        boolean type = false;
        Thread[] t = new Thread[numFans];

        for (int i = 0; i < numFans; ++i) {
            /*if(i%2 == 0){
                type = true;
            }else{
                type = false;
            }*/
            if(i == 0){
                type = true;
            }
            if(i == 1){
                type = true;
            }
            if(i == 2){
                type = false;
            }
            t[i] = new Thread(new BathroomTest(CottonBowl, type));
        }
        for (int i = 0; i < numFans; ++i) {
            t[i].start();
            Thread.sleep(50);
        }
        for (int i = 0; i < numFans; ++i) {
            t[i].join();
        }

    }

    @Override
    public void run() {
        if (longhorn == true){
            bathroom.enterBathroomUT();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bathroom.leaveBathroomUT();
        }
        else{
            bathroom.enterBathroomOU();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bathroom.leaveBathroomOU();
        }

            //     bathroom.enterBathroomUT();

        /*
        if (action == 0){
            bathroom.enterBathroomUT();
        }
        if (action == 1){
            bathroom.enterBathroomUT();
        }
        if (action == 2){
            bathroom.enterBathroomUT();
        }
        if (action == 3){
            bathroom.enterBathroomUT();
        }
        if (action == 4){
            bathroom.enterBathroomUT();
        }
        if (action == 5){
            bathroom.enterBathroomUT();
        }
        if (action == 6){
            bathroom.enterBathroomUT();
        }
        if (action == 7){
            bathroom.enterBathroomUT();
        }
        if(action == 8){
            bathroom.leaveBathroomUT();
        }
        if(action == 9){
       //     bathroom.enterBathroomUT();
        }*/
    }
}
