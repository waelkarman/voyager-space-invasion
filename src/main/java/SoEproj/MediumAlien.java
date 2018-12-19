package SoEproj;


public class MediumAlien extends Alien implements Runnable{

    private final int SHIFT_BOSS = 65;   // move shift of boss helper aliens
    private final String imagePath;
    private final String moveType;  // changes alien movements 
    private boolean goDown;         // to set boss helpers go up and down as thier boss

    public MediumAlien(int x, int y) {
        super(x, y, 2);
        SPACE = 2;
        super.points = 75;
        this.moveType = "";
        
        imagePath = "./src/main/java/SoEproj/Resource/MediumAlien.png";
        loadImage(imagePath);
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }

    public MediumAlien(int x, int y, String moveType, boolean goDown) {
        super(x, y, 1);
        SPACE = 1;
        points = 50;
        this.moveType = moveType;
        this.goDown = goDown;

        imagePath = "./src/main/java/SoEproj/Resource/MediumAlien.png";
        loadImage(imagePath);
        getImageDimensions();
        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }


    public void move() {  
        if(moveType == "BossHelper") {
            if (x >= B_WIDTH - SHIFT_BOSS)
                x -= SPACE;
            
            if (goDown) {
                y += SPACE;
                if (y > B_HEIGHT - this.height)  // the alien touches the down board limit
                    goDown = false;
            } 
            else {
                y -= SPACE;
                if (y < B_SCORE_SPACE)
                    goDown = true;
            }
        }
        else{
            x -= SPACE;
            if (x + width < 0)
                setDying(true);
        }
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
