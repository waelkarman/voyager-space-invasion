/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.util.List;

public class FireThread implements Runnable{

    private SpaceShip s;
    private List<Missile> missiles;

    public FireThread(SpaceShip s) {
        this.s = s;
        this.missiles = s.getMissiles();
    }

    // TODO risolvere spari a raffica
    @Override
    public void run() {
        
            while(true){    
                if (!s.getFiring()) {
                    synchronized(missiles){
                        try { 
                        missiles.wait();
                        } catch (InterruptedException e)  {
                            System.out.println(e); 
                        }
                    }
                } 

                s.fire();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        
    }

}