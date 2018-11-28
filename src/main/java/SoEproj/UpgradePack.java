/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.Random;

public class UpgradePack extends Sprite {

    private final int BOARD_WIDTH = 585;
    private String upPower;
    private String type;
    

    public UpgradePack(int x, int y) {
        super(x, y);
        SPACE = 1/2;

        setPackIcon(type);
    }
    
    private void setPackIcon(String type) {
        String pathImage = "";
        Random r = new Random();
        int randomUpgrade = r.nextInt(5);
        
        //TODO: immagini
        switch(randomUpgrade){ //different pack color based on the upgrade type
            case 0:{
                type = "3Missiles";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\3Missiles.png";
                break;
            } 
            case 1:{
                type = "fireBall";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\fireBall.png";
                break;
            } 
            case 2:{
                type = "Laser";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
                break;
            }
            case 3:{
                type = "Life";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Life.png";
                break;
            }
            case 4:{
                type = "Speed";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Speed.png";
                break;
            }
        }

        loadImage(pathImage);
        getImageDimensions();

    }


    public void updateSpaceShip(SpaceShip s) {
        switch(type){ //different pack color based on the upgrade type
            case "3Missiles":{
                s.setMissiletype("3Missiles");
                break;
            } 
            case "fireBall":{
                s.setMissiletype("fireBall");
                break;
            } 
            case "Laser":{
                s.setMissiletype("Laser");  
                break;
            }
            case "Life":{
                s.setMissiletype("Life");  
                break;
            }
            case "Speed":{
                s.setMissiletype("Speed");
                break;
            }
        }
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
