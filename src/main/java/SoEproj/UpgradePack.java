/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.List;
import java.util.Random;

public class UpgradePack extends Sprite{

    private final int BOARD_WIDTH = 585;
    private String upPower;
    private int type;


  

    public UpgradePack(int x, int y, int randomUpgrade) {
        super(x, y);
        SPACE = 1/2;
        setPackIcon(randomUpgrade);
    }
    
    private void setPackIcon(int randomUpgrade) {
        String pathImage = "";
       
        //TODO: immagini
        switch(randomUpgrade){ //different pack color based on the upgrade type
            case 0:{
                type = 0;//"3Missiles";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\box.png";
                break;
            } 
            case 1:{
                type = 1;//"fireBall";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\box.png";
                break;
            } 
            case 2:{
                type = 2;//"Laser";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\box.png";
                break;
            }
            case 3:{
                type = 3;//"Life";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\box.png";
                break;
            }
            case 4:{
                type = 4;//"Speed";
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\box.png";
                break;
            }
        }

        loadImage(pathImage);
        getImageDimensions();

    }


    public void updateSpaceShip(SpaceShip s,int skill) {
        

        switch(skill){ //different pack color based on the upgrade type
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
        }
    }


    public int getType() {
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
