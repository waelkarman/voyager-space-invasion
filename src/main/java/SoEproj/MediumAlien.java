package SoEproj;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public abstract class MediumAlien extends Alien implements Runnable{

    private List<Missile> missiles;

    public MediumAlien(int x, int y) {
        super(x, y, 2);
        SPACE = 2;

        missiles = new ArrayList<>();

        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\MediumAlien.png");
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }


    public List<Missile> getMissiles() {
        return missiles;
    }

    public void move() {
        
        if (x < 0) {
            setDying(true);
        }

        x -= SPACE;
    }

    public void fire() {
        missiles.add(new Missile(x , y + height / 2, 1, 1, false));
    }

    @Override
    public void run() {
        while(true){
            synchronized(missiles){
                fire();
            }  
            
            int sleep = 8000;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread fire interrupted: %s", e.getMessage());
            }
        }
    }

}
