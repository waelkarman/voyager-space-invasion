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
    private String direction;   // direction
    double accumulatore1 =0; 
    double accumulatore2 =0; 
    int CoeffAng = 5; 
    
    public Missile(int x, int y, String type, String direction) {  // the type è il danno, immagine e velocità
        super(x, y);
        this.direction = direction;

        setType(type);
    }
 
    private void setType(String type) {    // tipo di arma (numero e tipo di colpi ad esempio colpo triplo)

        String pathImage = "";
        switch(type){
            case "3Missiles":{
                damage = -1;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
                break;
            } 
            case "fireBall":{
                damage = -2;
                SPACE = 3/2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
                break;
            } 
            case "Laser":{
                damage = -3;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
                break;
            }
        }

        loadImage(pathImage);
        getImageDimensions();
    }

    public int getDamage() {
        return this.damage;
    }

    // Missiles move in one direction only. They disappear after they reach the right window border
    public void move() {   
        
        switch(direction){
            case "leftToRight":{
                x += SPACE;
                if (x > BOARD_WIDTH)
                    visible = false;
                break;
            }
            case "rightToLeft":{
                x -= SPACE + 2;     // the missile must be faster than aliens
                if (x < 0)
                    visible = false;
                break;
            }
            case "leftToTop":{
                accumulatore1 += 1;
                if (accumulatore1 > CoeffAng){ 
                    y += 1;
                    accumulatore1 = 0;
                }
                x += SPACE;
                if (x > BOARD_WIDTH)
                    visible = false;
                break;
            }
            case "leftToBottom":{
                accumulatore2 += 1;
                if (accumulatore2 > CoeffAng){ 
                    y += -1;
                    accumulatore2 = 0;
                }
                x += SPACE;
                if (x > BOARD_WIDTH)
                    visible = false;
                break;
            }
        }
    
    }

    @Override
    public Area getShape(){
        Rectangle shape = new Rectangle(x,y,width,height);
        return new Area(shape);
    }

}
