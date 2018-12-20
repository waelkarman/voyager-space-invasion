package SoEproj;


import  java.lang.Math;


public class HardAlien extends Alien implements Runnable{

    private final int amplitude;    // oscillation dimension
    private final int meanY;        // alien swings around this mean value
    private String imagePath;

    public HardAlien(int x, int y) {
        super(x, y, 2);
        meanY = y;
        SPACE = 2;
        amplitude = 50;
        super.points = 100;

        imagePath = "./src/main/java/SoEproj/Resource/HeavyAlien.png";
        loadImage(imagePath);
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }

    public void move() {
        if (x  + width < 0)
            setDying(true);
        
        x -= SPACE;

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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Thread Hard Alien: " + e.getMessage());
        }

        while(isVisible()){
            try {
                fire();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Thread Hard Alien: " + e.getMessage());
            }
        }
    }
    
}
