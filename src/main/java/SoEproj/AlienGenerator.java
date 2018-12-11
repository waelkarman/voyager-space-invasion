package SoEproj;

import java.lang.Runnable;
import java.util.List;
import java.util.Random;


public class AlienGenerator implements Runnable {

    private final int ALIEN_NUM = 3;   // total amount of generated aliens
                                  // NOTE: the game duration in seconds is ALIEN_NUM / 2
    private final int bgWidth;
    private final int bgHeight;
    
    private List<Alien> aliens;
    private int level;
    private int ref;

    private Random random = new Random();
    private int maxY = 44;  // maximum (highest) pixel in which an alien can spawn
    private int minY = 389; // minimum (lowest) pixel in which an alien can spawn
    private int range = minY - maxY;    // range in which an alien can appear
    

    public AlienGenerator(int bgWidth, int bgHeight, List<Alien> aliens, int level) {
        this.bgWidth = bgWidth;
        this.bgHeight = bgHeight;
        this.aliens = aliens;
        this.level = level;
        this.ref = 0;
	}


    public void generateAliens() {
        
        int h = random.nextInt(range) + maxY;
        
        switch(this.level){
            case 1: // lev. 1 : Only EasyAliens
                aliens.add(new EasyAlien(bgWidth + 40, h));
                break;

            case 2: // lev. 2 : MediumAliens and EasyAliens, ratio 2:1
                if (ref % 3 == 0) {
                    aliens.add(new EasyAlien(bgWidth + 40, h));
                    ref = 0;
                } else
                    aliens.add(new MediumAlien(bgWidth + 40, h));
                
                break;

            case 3: // lev. 3 : HardAliens and MediumAliens, ratio 2:1
                if (ref % 3 == 0) {
                    aliens.add(new MediumAlien(bgWidth + 40, h));
                    ref = 0;
                } else
                    aliens.add(new HardAlien(bgWidth + 40, h)); 
                
                break;
        }

        ref++;
    }

    public void generateBoss() {
        synchronized(this.aliens){
            this.aliens.add(new Boss1Alien(bgWidth, bgHeight/2, aliens));
        }
    }

    @Override
    public void run(){
        int i=0;
        
        do {
            if (i < ALIEN_NUM) {
                synchronized(this.aliens) {
                    generateAliens(); //1 provvisorio
                }

                i++;
            }

            int sleep = 500;

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("AlienGenerator sleep: " + e);
            }

        } while(!aliens.isEmpty());
        
        generateBoss();
        
    }
}  
 
