package SoEproj;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class PackGenerator implements Runnable {

    protected List<UpgradePack> pack;
    private int bgWidth;
    private Random random;
    private int minX = 44;  // maximum (highest) pixel in which a pack can spawn
    private int maxY = 389; // minimum (lowest) pixel in which a pack can spawn
    private int range = maxY - minX;    // range in which a pack can appear


    public PackGenerator(int bgWidth, List<UpgradePack> pack) {
        this.bgWidth = bgWidth;
        this.pack = pack;
        random = new Random();
	}


    @Override
    public void run() {
        while(true){
            synchronized(pack){
                int h = random.nextInt(range) + minX;
                int randomUpgrade = random.nextInt(9);

                pack.add(new UpgradePack(bgWidth + 40 , h, randomUpgrade));
            }

            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                System.out.println("Pack generation interrupted %s" + e.getMessage());
            }
        }
    }

}
