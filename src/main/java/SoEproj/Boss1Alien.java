package SoEproj;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Boss1Alien extends Alien implements Runnable{

    private int life;
    private boolean goDown = true;  // to set boss go at first down and then up

    public Boss1Alien(int x, int y) {
        super(x, y, 50);
        SPACE = 3/2;
        // TODO Cambiare immagine boss1
        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\HeavyAlien.png");
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }
    
    
    // Boss enters in scene and then moves up and down
    @Override
    public void move() {

        if (x >= INITIAL_X-30) {
            x -= SPACE;
        }
        else {
            

            if (goDown) {
                y += SPACE;
                if (y > 400) {
                    goDown = false;
                }
            } else {
                y -= SPACE;
                if (y < 0) {
                    goDown = true;
                }
            }
        }
    }

    public void fire() {
        missiles.add(new Missile(x , y, 1, 1, false));
        missiles.add(new Missile(x , y + height / 2, 1, 1, false));
        missiles.add(new Missile(x , y + height, 1, 1, false));
    }

    @Override
    public void run() {
        while(true){
            int sleep = 7000;

            if (life < 50 * 50/100) {
                
            }
            synchronized(missiles){
                fire();
            }  
            
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread fire interrupted: %s", e.getMessage());
            }
        }
    }

}