package SoEproj;


import java.util.List;
import java.util.Random;


public class PackGenerator implements Runnable {

    private final int bgWidth;
    protected List<UpgradePack> packs;

    private Random random;
    private int minX = 44;  // maximum (highest) pixel in which a pack can spawn
    private int maxY = 389; // minimum (lowest) pixel in which a pack can spawn
    private int range = maxY - minX;    // range in which a pack can appear


    public PackGenerator(int bgWidth, List<UpgradePack> packs) {
        this.bgWidth = bgWidth;
        this.packs = packs;
        random = new Random();
	}


    @Override
    public void run() {
        while(true){
            synchronized(packs){
                int h = random.nextInt(range) + minX;
                int randomUpgrade = random.nextInt(9);

                packs.add(new UpgradePack(bgWidth + 40 , h, randomUpgrade));
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Pack generation interrupted %s" + e.getMessage());
            }
        }
    }

}
