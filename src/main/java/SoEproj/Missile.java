/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Rectangle;
import java.awt.geom.Area;

public class Missile extends Sprite {

    private int damage;
    private String direction;               // direction
    private double countX = 0; 
    private final int CoeffAng = 3; 
    
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
                pathImage = "./src/main/java/SoEproj/Resource/Laser.png";
                break;
            } 
            case "fireBall":{
                damage = -2;
                SPACE = 4;
                pathImage = "./src/main/java/SoEproj/Resource/fireBall.png";
                break;
            } 
            case "Laser":{
                damage = -3;
                SPACE = 2;
                pathImage = "./src/main/java/SoEproj/Resource/Laser.png";
                break;
            }
            case "Vecchia":{
                damage = -3;
                SPACE = 1;
                pathImage = "./src/main/java/SoEproj/Resource/vecchia.png";
                break;
            }
            case "Banana":{
                damage = -1;
                SPACE = 2;
                pathImage = "./src/main/java/SoEproj/Resource/banana.png";
                break;
            }
            case "memas":{
                damage = -2;
                SPACE = 2;
                pathImage = "./src/main/java/SoEproj/Resource/memas.png";
                break;
            }
            case "pollo":{
                damage = 0;
                SPACE = 2;
                pathImage = "./src/main/java/SoEproj/Resource/pollo.png";
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
                if (x > B_WIDTH)
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
                countX += 1;
                if (countX > CoeffAng){ 
                    y += 1;
                    countX = 0;
                }
                x += SPACE;
                if (x > B_WIDTH)
                    visible = false;
                break;
            }
            case "leftToBottom":{
                countX += 1;
                if (countX > CoeffAng){ 
                    y -= 1;
                    countX = 0;
                }
                x += SPACE;
                if (x > B_WIDTH)
                    visible = false;
                break;
            }
            case "rightToTop":{
                countX += 1;
                if (countX > CoeffAng){ 
                    y += 1;
                    countX = 0;
                }
                x -= SPACE + 2;
                if (x > B_WIDTH)
                    visible = false;
                break;
            }
            case "rightToBottom":{
                countX += 1;
                if (countX > CoeffAng){ 
                    y -= 1;
                    countX = 0;
                }
                x -= SPACE + 2;
                if (x > B_WIDTH)
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
