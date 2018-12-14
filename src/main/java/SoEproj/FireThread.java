package SoEproj;


import java.util.List;


public class FireThread implements Runnable{

    private SpaceShip s;
    private List<Missile> missiles;

    public FireThread(SpaceShip s) {
        this.s = s;
        this.missiles = s.getMissiles();
    }


    @Override
    public void run() { 
        while(s.isVisible()){    
            if(!s.getFiring()) {
                synchronized(missiles){
                    try { 
                        missiles.wait();
                    } catch (InterruptedException e)  {
                        System.out.println("FireThread wait: " + e); 
                    }
                }
            }

            s.fire();
            
            if(s.getFiring()){
                try {
                    Thread.sleep(350);                                                                                                            
                } catch (InterruptedException e) {
                    System.out.println("FireThread sleep: " + e);
                }
            }
        } 
    }
     
}