package SoEproj;


import java.awt.Rectangle;
import java.awt.geom.Area;


public class UpgradePack extends Sprite{

    private int type;

    public UpgradePack(int x, int y, int randomUpgrade) {
        super(x, y);
        SPACE = 1;
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
                type = 5;   //"Vecchia";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 6:{
                type = 6;   //"Banana";
                pathImage = "./src/main/java/SoEproj/Resource/box.png";
                break;
            }
            case 7:{
                type = 7;   //"memas";
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


    public synchronized void updateSpaceShip(SpaceShip s) {
        switch(type){                          //different pack color based on the upgrade type
            case 0:{
                s.setMissiletype("3Missiles");
                break;
            } 
            case 1:{
                s.setMissiletype("fireBall");
                break;
            } 
            case 2:{
                s.setMissiletype("Laser");  
                break;
            }
            case 3:{
                s.setupLife(1);  
                break;
            }
            case 4:{
                s.setupSPACE(1);
                break;
            }
            case 5:{
                s.setMissiletype("Vecchia");
                break;
            }
            case 6:{
                s.setMissiletype("Banana");
                break;
            }
            case 7:{
                s.setMissiletype("memas");
                break;
            }
            case 8:{
                s.setMissiletype("pollo");
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

}
