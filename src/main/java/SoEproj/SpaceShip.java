package SoEproj;


import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SpaceShip extends Sprite {
    
    private final File laserSound;
    private final File g_r_sound;
    private final File FireballSound;

    private float dx;
    private float dy;
    protected List<Missile> missiles;
    private Boolean firing;
    private String missileType; // set damage, speed and image
    private boolean music;
    private Thread shipMissileFire;
    private int keyModality;
    protected int life;
    private int score;
    private MusicManager mumZero;
    

    public SpaceShip(int x, int y, int color, boolean music, int km) {
        super(x, y);        
        this.score = 0;
        this.life = 1;
        this.missiles = new ArrayList<>();
        this.missileType = "Laser";
        this.SPACE = 3/2;       // speed
        this.music = music;
        this.keyModality = km;
        this.firing = false;
        setColor(color);        // spaceship color: 1-Green, 2-Orange, 3-Red

        laserSound = new File("./src/main/java/SoEproj/Resource/LaserSound.wav");
        g_r_sound = new File("./src/main/java/SoEproj/Resource/R&GSound.wav");
        FireballSound = new File("./src/main/java/SoEproj/Resource/FireballSound.wav");

        shipMissileFire = new Thread(new FireThread(this));
        shipMissileFire.start();
    }


    public synchronized boolean ForceMove(int pos_x, int pos_y){
        boolean posizione = true;
        dx = 0;
        dy = 0; 
        
        if(this.getX() > pos_x && Math.abs(pos_x-this.getX()) > SPACE ){
            dx = -SPACE;
            posizione = false;
        }
        if(this.getX() < pos_x && Math.abs(pos_x-this.getX()) > SPACE){
            dx = +SPACE;
            posizione = false;
        }
        
        if(this.getY() > pos_y && Math.abs(pos_y-this.getY()) > SPACE){
            dy = -SPACE;
            posizione = false;
        }
        if(this.getY() < pos_y && Math.abs(this.getY()-pos_y) > SPACE){
            dy = +SPACE;
            posizione = false;
        }
       
        return posizione;
    }


    private void setColor(int color) {
        String pathImage = "";

        switch(color){
            case 1:{
                pathImage = "./src/main/java/SoEproj/Resource/GreenCraftMove.gif";
                break;
            } 
            case 2:{
                pathImage = "./src/main/java/SoEproj/Resource/OrangeCraftMove.gif";
                break;
            } 
            case 3:{
                pathImage = "./src/main/java/SoEproj/Resource/RedCraftMove.gif";
                break;
            }
            default:{
                pathImage = "./src/main/java/SoEproj/Resource/GreenCraftMove.gif";
                break;
            }
        }

        loadImage(pathImage);
        getImageDimensions();
    }

    public synchronized int getLife() {
        return this.life;
    }
    
    public int getScore() {
        return this.score;
    }

    public void setupScore(int points){
        this.score += points ;
    }

    public void setupSPACE(float SPACE) {
        if(this.SPACE<4 || SPACE < 0)
            this.SPACE += SPACE;
    }

    public synchronized void setupLife(int life) {
        if(this.life < 3 || life < 0)        // max 3 lives
            this.life += life;
    }

    public synchronized Boolean getFiring() {
        return this.firing;
    }

    public synchronized void setFiring(Boolean firing) {
        this.firing = firing;
    }

    public synchronized void setMissileType(String missiletype) {
        this.missileType = missiletype;
    }

    public synchronized String getMissileType() {
        return this.missileType;
    }

    public synchronized  List<Missile> getMissiles() {
        return missiles;
    }

    public synchronized void fire() {
        missiles.add(new Missile(x + width, y + height / 2, missileType, "leftToRight" ));

        if(missileType.equals("3Missiles")) {
            missiles.add(new Missile(x + width, y + height/2, missileType, "leftToTop" ));
            missiles.add(new Missile(x + width, y + height/2, missileType, "leftToBottom" ));            
        } 
        
        if(music){
            if(missileType.equals("3Missiles") || missileType.equals("Laser"))
                mumZero = new MusicManager(laserSound);

            else if(missileType.equals("GreenShoot") || missileType.equals("blueFireball"))
                mumZero = new MusicManager(g_r_sound);
            
            else if(missileType.equals("fireball")) 
                mumZero = new MusicManager(FireballSound);
            
            else
                mumZero = new MusicManager(laserSound);
             
            mumZero.startMusic();
        }
    }


    public void move() {       
        x += dx;
        y += dy;

        if(x < 1)
            x = 1;

        if(x > B_WIDTH - this.width)
            x = B_WIDTH - this.width;

        if(y < B_SCORE_SPACE) 
            y = B_SCORE_SPACE;

        if(y + this.height > B_HEIGHT)
            y = B_HEIGHT - this.height;
    }

    
    public void keyPressed(KeyEvent e)  throws InterruptedException {
        int key = e.getKeyCode();
        
        if(keyModality == 1){
            if (key == KeyEvent.VK_K) {
                synchronized(missiles){
                    setFiring(true);
                    missiles.notifyAll();
                }   
            }
            if (key == KeyEvent.VK_A) 
                dx = -SPACE;
            if (key == KeyEvent.VK_D) 
                dx = SPACE;
            if (key == KeyEvent.VK_W) 
                dy = -SPACE;
            if (key == KeyEvent.VK_S) 
                dy = SPACE;
        }
        else if(keyModality == 0){
            if (key == KeyEvent.VK_SPACE) {
                synchronized(missiles){
                    setFiring(true);
                    missiles.notifyAll();
                }   
            }
            if (key == KeyEvent.VK_LEFT) 
                dx = -SPACE;
            if (key == KeyEvent.VK_RIGHT) 
                dx = SPACE;
            if (key == KeyEvent.VK_UP) 
                dy = -SPACE;
            if (key == KeyEvent.VK_DOWN) 
                dy = SPACE;
        }
    }


    public void keyReleased(KeyEvent e) throws InterruptedException {
        int key = e.getKeyCode();
        
        if(keyModality == 1){
            if (key == KeyEvent.VK_K)
                setFiring(false);
            if (key == KeyEvent.VK_A)
                dx = 0;
            if (key == KeyEvent.VK_D)
                dx = 0;
            if (key == KeyEvent.VK_W)
                dy = 0;
            if (key == KeyEvent.VK_S)
                dy = 0;
        }
        else if (keyModality == 0){
            if (key == KeyEvent.VK_SPACE)
                setFiring(false);
            if (key == KeyEvent.VK_LEFT)
                dx = 0;
            if (key == KeyEvent.VK_RIGHT)
                dx = 0;
            if (key == KeyEvent.VK_UP)
                dy = 0;
            if (key == KeyEvent.VK_DOWN)
                dy = 0;
        }
    }


    @Override
    public Area getShape(){
        int[] xpos = { x, x+width, x};
        int[] ypos = { y, y + height/2, y + height };
        Polygon shape = new Polygon(xpos,ypos,3);
        return new Area(shape);
    }

}