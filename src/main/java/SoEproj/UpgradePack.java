package SoEproj;


import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.Timer;
import java.util.TimerTask;


public class UpgradePack extends Sprite{

    private int type;
    private Timer t;
    private TimerTask task;

    public UpgradePack(int x, int y, int randomUpgrade) {
        super(x, y);
        SPACE = 1;
        this.t = new Timer();

        setPackIcon(randomUpgrade);
    }
    
    private void setPackIcon(int randomUpgrade) {
        String pathImage = "";
       
        //TODO: immagini
        switch(randomUpgrade){      //different pack color based on the upgrade type
            case 0:{
                type = 0;   //"3Missiles";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            } 
            case 1:{
                type = 1;   //"fireBall";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            } 
            case 2:{
                type = 2;   //"Laser";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 3:{
                type = 3;   //"Life";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 4:{
                type = 4;   //"Speed";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 5:{
                type = 5;   //"BlueFireball";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 6:{
                type = 6;   //"GreenShoot";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 7:{
                type = 7;   //"VioletLaser";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 8:{
                type = 8;   //"pollo";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
        }

        loadImage(pathImage);
        getImageDimensions();
    }

    private void scheduleResetUpgrade(SpaceShip s){
        t.cancel();
        t = new Timer();
        task = new ResetUpgradeAmmo(s);
        t.schedule(task, 10 * 1000);
    }

    private void scheduleResetSpace(SpaceShip s, float space){
        t.cancel();
        t = new Timer();
        task = new ResetUpgradeSpace(s, space);
        t.schedule(task, 10 * 1000);
    }

    public synchronized void updateSpaceShip(SpaceShip s) {
        switch(type){                          //different pack color based on the upgrade type
            case 0:{
                scheduleResetUpgrade(s);
                s.setMissileType("3Missiles");
                break;
            } 
            case 1:{
                scheduleResetUpgrade(s);
                s.setMissileType("fireBall");
                break;
            } 
            case 2:{
                scheduleResetUpgrade(s);
                s.setMissileType("Laser");  
                break;
            }
            case 3:{
                s.setupLife(1);  
                break;
            }
            case 4:{
                s.setupSPACE(3/2);
                scheduleResetSpace(s, -3/2);
                break;
            }
            case 5:{
                scheduleResetUpgrade(s);
                s.setMissileType("blueFireball");
                break;
            }
            case 6:{
                scheduleResetUpgrade(s);
                s.setMissileType("GreenShoot");
                break;
            }
            case 7:{
                scheduleResetUpgrade(s);
                s.setMissileType("VioletLaser");
                break;
            }
            case 8:{
                scheduleResetUpgrade(s);
                s.setMissileType("pollo");
                break;
            }
        }
    }


    public synchronized int getType() {
        return this.type;
    }

    public void move() {   
        x -= SPACE;

        if (x < 0)
            visible = false;
    }
    

    @Override
    public Area getShape(){
        Rectangle shape = new Rectangle(x,y,width,height);
        return new Area(shape);
    }


    class ResetUpgradeAmmo extends TimerTask  {
        SpaceShip s;
   
        public ResetUpgradeAmmo(SpaceShip s) {
            this.s = s;
        }
   
        @Override
        public void run() {
            s.setMissileType("Laser"); 
        }
    }


    class ResetUpgradeSpace extends TimerTask  {
        SpaceShip s;
        float space;
   
        public ResetUpgradeSpace(SpaceShip s, float space) {
            this.s = s;
            this.space = space;
        }
   
        @Override
        public void run() {
            s.setupSPACE(space);
        }
    }
}
