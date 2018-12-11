package SoEproj;

import  java.lang.Math;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class HardAlien extends Alien implements Runnable{

    private final int amplitude;    // oscillation dimension
    private final int meanY;        // alien swings around this mean value
    private String imagePath = "./src/main/java/SoEproj/Resource/HeavyAlien.png";

    public HardAlien(int x, int y) {
        super(x, y, 2);
        meanY = y;
        SPACE = 3/2;
        amplitude = 50;
        super.points = 100;
        loadImage(imagePath);
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }

    public void move() {
        
        if (x < 0) {
            setDying(true);
        }
        else{
            x -= SPACE;
        }
        
        // oscillation
        y = (int) (meanY + amplitude * Math.cos(2*3.14*x/360));     // degree to radiants

    }


    public void fire() {
        synchronized(missiles){
            missiles.add(new Missile(x , y + height / 2, "Laser", "rightToLeft"));
        }
    }

    @Override
    public void run() {
        while(isVisible()){
            try {
                fire();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Thread Hard Alien: " + e.getMessage());
            }
        }
    }
    
}
