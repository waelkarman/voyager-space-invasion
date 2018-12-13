package SoEproj;

import java.lang.Runnable;
import java.util.List;
import java.util.Random;


public class AlienGenerator implements Runnable {

    
    private final int ALIEN_NUM = 3;   // total amount of generated aliens (the game duration in seconds is ALIEN_NUM / 2)                        
    private final int bgWidth;
    
    private List<Alien> aliens;
    private int level;
    private int ref;

    private Random r = new Random();
    private int minY = 44;  // max (highest) pixel in which an alien can spawn
    private int maxY = 389; // min (lowest) pixel in which an alien can spawn
    private int range = maxY - minY;    // range in which an alien can appear
    

    public AlienGenerator(int bgWidth, List<Alien> aliens, int level) {
        this.bgWidth = bgWidth;
        this.aliens = aliens;
        this.level = level;
        this.ref = 0;
	}


    public void generateAliens() {
        int x = bgWidth;
        int h = r.nextInt(range) + minY;
        
        switch(this.level){
            case 1: // lev. 1 : Only EasyAliens
                aliens.add(new EasyAlien(x, h));
                break;

            case 2: // lev. 2 : MediumAliens and EasyAliens, ratio 2:1
                if (ref % 3 == 0) {
                    aliens.add(new EasyAlien(x, h));
                    ref = 0;
                } else
                    aliens.add(new MediumAlien(x, h));
                break;

            case 3: // lev. 3 : HardAliens and MediumAliens, ratio 2:1
                if (ref % 3 == 0) {
                    aliens.add(new MediumAlien(x, h));
                    ref = 0;
                } else
                    aliens.add(new HardAlien(x, h)); 
                
                break;
        }

        ref++;
    }


    public void generateBoss() {
        synchronized(this.aliens){
            // TODO fare 3 boss diversi per i 3 livelli
            this.aliens.add(new Boss1Alien(bgWidth, range/2, aliens));
        }
    }


    @Override
    public void run(){
        int i = 0;
        
        while(true){
            synchronized(aliens) {
                if(i < ALIEN_NUM) {
                    generateAliens();
                    i++;
                }
                else if(aliens.isEmpty()) {
                    break;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("AlienGenerator sleep: " + e);
            }
        }
        
        generateBoss();
        
    }
}  
 
