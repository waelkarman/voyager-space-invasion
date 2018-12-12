package SoEproj;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class MediumAlien extends Alien implements Runnable{

    private String imagePath;

    public MediumAlien(int x, int y) {
        super(x, y, 2);
        SPACE = 2;
        super.points = 75;
        
        imagePath = "./src/main/java/SoEproj/Resource/MediumAlien.png";
        loadImage(imagePath);
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }


    public void move() {        
        if (x < 0) {
            setDying(true);
        }

        x -= SPACE;
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
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                System.out.println("Thread Medium Alien: " + e.getMessage());
            }
        }
    }

}
