/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Rectangle;
import java.awt.geom.Area;

public class UpgradePack extends Sprite {

    private final int BOARD_WIDTH = 585;
    private int upPower;
    

    public UpgradePack(int x, int y, int type) {
        super(x, y);
        SPACE = 2;
        setType(type);
    }
    
    private void setType(int type) {
        String pathImage = "";
        switch(type){
            case 1:{
                upPower = 1;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
            } 
            case 2:{
                upPower = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
            } 
            case 3:{
                upPower = 3;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
            }
        }

        loadImage(pathImage);
        getImageDimensions();
    }



    public void move() {   
        
        x -= SPACE + 2;
        if (x < 0)
            visible = false;
    
    }
    
    

    @Override
    public Area getShape(){
        Rectangle shape = new Rectangle(x,y,width,height);
        return new Area(shape);
    }

}
