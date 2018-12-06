

package SoEproj;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class packGenerator implements Runnable {

    protected LinkedList<UpgradePack> upack;
    private int ref_width;
    private int ref_heigth;
    private int level;
   
    private Random random = new Random();
    private int aa = 44;
    private int bb = 389;
    private int cc = ((bb-aa) + 1);


    public packGenerator(int bgwidth, int bgheight, LinkedList<UpgradePack> upack,int level) {
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
                int y = random.nextInt(430);
                Random r = new Random();
                int randomUpgrade = r.nextInt(9);
                upack.add(new UpgradePack(600,y,randomUpgrade));
                
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