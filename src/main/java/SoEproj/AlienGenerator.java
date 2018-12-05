package SoEproj;
import java.lang.Runnable;
import java.util.List;
import java.util.Random;


public class AlienGenerator implements Runnable {

    private int ref_width;
    private int ref_heigth;
    private Alien current_alien;
    private List<Alien> aliens;
    private int dim = 24;
    private boolean flag = true;
    private int level;
    private int ref;

    private Random random = new Random();
    private int aa = 44;
    private int bb = 389;
    private int cc = ((bb-aa) + 1);
    

    public AlienGenerator(int bgwidth, int bgheight, List<Alien> aliens,int level) {
        this.ref_width=bgwidth;
        this.ref_heigth=bgheight;
        this.aliens = aliens;
        this.level = level;
	}


    public void generate(){          //Livello!!
        
        int h = random.nextInt(cc)+aa;
        

        switch(this.level){
            case 1: // livello 1 alieni easy
           
                current_alien = new EasyAlien(ref_width+40, h);
                aliens.add(current_alien);
                break;

            case 2: // livello 2 alieni medi/easy rapporto 2:1
                if (ref % 3 == 0){
                    current_alien = new EasyAlien(ref_width + 40, h);
                    
                }
                else {
                    current_alien = new MediumAlien(ref_width + 40, h); 
                     
                }
                aliens.add(current_alien);
                break;

            case 3: // livello 3 alieni medi/hard rapporto 2:1
                if (ref % 3 == 0){
                    current_alien = new MediumAlien(ref_width + 40, h);
                    
                }
                else {
                    current_alien = new HardAlien(ref_width + 20, h); 
                    
                }
                aliens.add(current_alien);
                break;
        }

        /*
        if(level == 1){
        Random random = new Random();
        
        int aa = 44;
        int bb = 389;
        int cc = ((bb-aa) + 1);
        int h = random.nextInt(cc)+aa;
        
        al = new EasyAlien(ref_width+40, h);
        aliens.add(al);
        
        }*/


}

    
    public void run(){
            int i=0;
            ref = 0;
            
            while(i<dim || flag == false){
                
                synchronized(this.aliens){
                    generate(); //1 provvisorio
                }
                i=i+1;
                ref = i;
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

    public void stop(){
        flag = false;
    }

    public void generateBoss(){

        
        synchronized(this.aliens){
            this.aliens.add(new Boss1Alien(ref_width, ref_heigth/2, aliens));
        }
        
        
        
        
    }


}  
 
