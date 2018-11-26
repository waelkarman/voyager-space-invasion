/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Rectangle;
import java.awt.geom.Area;

public class Missile extends Sprite {

    private final int BOARD_WIDTH = 585;
    private int damage;
    private boolean leftToRight;

    public Missile(int x, int y, int type, int shape, boolean leftToRight) {
        super(x, y);
        setType(type);
        this.leftToRight = leftToRight;
       
    }
    
    private void setType(int type) {
        String pathImage = "";
        switch(type){
            case 1:{
                damage = -1;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
            } 
            case 2:{
                damage = -2;
                SPACE = 3/2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
            } 
            case 3:{
                damage = -3;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
            }
        }


        loadImage(pathImage);
        getImageDimensions();
    }

    public int getDamage() {
        return this.damage;
    }

    // Missiles move in one direction only. They disappear after 
    // they reach the right window border
    public void move() {   
        if (leftToRight){
            x += SPACE;
            if (x > BOARD_WIDTH)
                visible = false;
        }
        else{
            x -= SPACE + 2;
            if (x < 0)
                visible = false;
        }
    }
    
    
    public void move(int xA, int yA) {   
        if (leftToRight){
            xA += xA + SPACE;
            if (xA > BOARD_WIDTH)
                visible = false;
        }
        else{
            x -= xA+SPACE;
            if (x < 0)
                visible = false;
        }
    }


    @Override
    public Area getShape(){
        Rectangle shape = new Rectangle(x,y,width,height);
        return new Area(shape);
    }

}
