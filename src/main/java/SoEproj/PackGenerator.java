package SoEproj;


import java.util.List;
import java.util.Random;


public class PackGenerator extends Thread {

    private final int B_SCORE_SPACE = 30;   // space that is occupied by player's score
    private final int B_WIDTH;
    
    private List<UpgradePack> packs;
    private Random random;
    private int range;    // range in which a pack can appear


    public PackGenerator(int B_WIDTH, int B_HEIGHT, List<UpgradePack> packs) {
        this.B_WIDTH = B_WIDTH;
        this.packs = packs;
        random = new Random();
        range = B_HEIGHT - B_SCORE_SPACE;
	}


    @Override
    public void run() {
        int h, randomUpgrade, randomSleep;
        
        while(true){
            synchronized(packs){
                h = random.nextInt(range) + B_SCORE_SPACE;
                randomUpgrade = random.nextInt(9);

                packs.add(new UpgradePack(B_WIDTH + 40 , h, randomUpgrade));
            }

            try {
                randomSleep = (5 + random.nextInt(10)) * 1000;   // next box is generated after 5-15 seconds
                Thread.sleep(randomSleep);
            } catch (InterruptedException e) {
                System.out.println("Pack generation interrupted %s" + e.getMessage());
            }
        }
    }

}
