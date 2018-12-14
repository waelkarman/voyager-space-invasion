package SoEproj;


import java.util.List;
import java.util.Random;


public class PackGenerator implements Runnable {

    private final int B_SCORE_SPACE = 30;   // space that is occupied by player's score
    private final int bgWidth;
    
    private List<UpgradePack> packs;
    private Random random;
    private int range;    // range in which a pack can appear


    public PackGenerator(int bgWidth, int B_HEIGHT, List<UpgradePack> packs) {
        this.bgWidth = bgWidth;
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

                packs.add(new UpgradePack(bgWidth + 40 , h, randomUpgrade));
            }

            try {
                randomSleep = (5 + random.nextInt(5)) * 1000;   // next box is generated after 5-10 seconds
                Thread.sleep(randomSleep);
            } catch (InterruptedException e) {
                System.out.println("Pack generation interrupted %s" + e.getMessage());
            }
        }
    }

}
