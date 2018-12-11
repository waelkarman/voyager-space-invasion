package SoEproj;
import java.lang.Runnable;
import java.util.List;
import java.util.Random;


public class AlienGenerator implements Runnable {

    private int bgWidth;
    private int bgHeight;
    private Alien currentAlien;
    private List<Alien> aliens;
    private int dim = 60;
    private int level;
    private int ref;

    private Random random = new Random();
    private int maxY = 44;  // maximum (highest) pixel in which an alien can spawn
    private int minY = 389; // minimum (lowest) pixel in which an alien can spawn
    private int seed = minY - maxY;
    

    public AlienGenerator(int bgWidth, int bgHeight, List<Alien> aliens, int level) {
        this.bgWidth = bgWidth;
        this.bgHeight = bgHeight;
        this.aliens = aliens;
        this.level = level;
        this.ref = 0;
	}


    public void generate() {
        
        int h = random.nextInt(seed) + maxY;
        
        switch(this.level){
            case 1: // lev. 1 : Only EasyAliens
                currentAlien = new EasyAlien(bgWidth+40, h);
                aliens.add(currentAlien);
                break;

            case 2: // lev. 2 : MediumAliens and EasyAliens, ratio 2:1
                if (ref % 3 == 0) {
                    currentAlien = new EasyAlien(bgWidth + 40, h);
                    ref = 0;
                }
                else {
                    currentAlien = new MediumAlien(bgWidth + 40, h); 
                }
                aliens.add(currentAlien);
                break;

            case 3: // lev. 3 : HardAliens and MediumAliens, ratio 2:1
                if (ref % 3 == 0) {
                    currentAlien = new MediumAlien(bgWidth + 40, h);
                    ref = 0;
                }
                else {
                    currentAlien = new HardAlien(bgWidth + 20, h); 
                }
                aliens.add(currentAlien);
                break;
        }

        ref++;
    }

    @Override
    public void run(){
            int i=0;
            
            while(i < dim) {
                
                synchronized(this.aliens) {
                    generate(); //1 provvisorio
                }

                i++;

                int sleep = 500;

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    String msg = String.format("Alien generation interrupted %s", e.getMessage());
                    System.out.println(msg);
                }
            }
        
        generateBoss();
    }

    public void generateBoss() {
        synchronized(this.aliens){
            this.aliens.add(new Boss1Alien(bgWidth, bgHeight/2, aliens));
        }
    }
}  
 
