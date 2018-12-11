package SoEproj;


import java.util.LinkedList;
import java.util.Random;


public class PackGenerator implements Runnable {

    protected LinkedList<UpgradePack> upack;
    private int ref_width;
    private int ref_heigth;
    private int level;
   
    private Random random = new Random();
    private int maxY = 44;  // maximum (highest) pixel in which an alien can spawn
    private int minY = 389; // minimum (lowest) pixel in which an alien can spawn
    private int range = minY - maxY;    // range in which an alien can appear


    public PackGenerator(int bgwidth, int bgheight, LinkedList<UpgradePack> upack,int level) {
        this.ref_width=bgwidth;
        this.ref_heigth=bgheight;
        this.upack = upack;
        this.level = level;
	}


    public LinkedList<UpgradePack> getUpack() {
        return this.upack;
    }




    @Override
    public void run() {
        while(true){
            
            synchronized(upack){
                int h = random.nextInt(range);
                int randomUpgrade = random.nextInt(9);

                upack.add(new UpgradePack(600,h,randomUpgrade));
            }
            

            //TODO settare opportunamente il tempo 
            int sleep = 25000;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Pack generation interrupted %s", e.getMessage());
                System.out.println(msg);
            }
        }




    }



}
