package SoEproj;


import java.lang.Runnable;
import java.util.List;
import java.util.Random;


public class AlienGenerator implements Runnable {

    private final int B_SCORE_SPACE = 30;   // space that is occupied by player's score
    private final int ALIEN_NUM = 20;       // total amount of generated aliens (the game duration in seconds is ALIEN_NUM / 2)                      
    private final int B_WIDTH;
    
    private List<Alien> aliens;
    private int level;
    private int ref;

    private Random r = new Random();
    private int range;    // range in which an alien can appear
    

    public AlienGenerator(int B_WIDTH, int B_HEIGHT, List<Alien> aliens, int level) {
        this.B_WIDTH = B_WIDTH;
        this.aliens = aliens;
        this.level = level;
        this.ref = 0;
        this.range = B_HEIGHT - B_SCORE_SPACE;
	}


    public void generateAliens() {
        int x = B_WIDTH;
        int h = r.nextInt(range) + B_SCORE_SPACE;
        
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
            if(level == 1){
                this.aliens.add(new Boss1Level(B_WIDTH, range/2, aliens));
            }
            if(level == 2){
                this.aliens.add(new Boss2Level(B_WIDTH, range/2, aliens));
            }
            if(level == 3){
                this.aliens.add(new Boss3Level(B_WIDTH, range/2, aliens));
            }
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
 
