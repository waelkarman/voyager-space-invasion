package SoEproj;


import java.awt.geom.Area;
import java.awt.Rectangle;


public class Missile extends Sprite {

    private int damage;
    private String direction;
    private double countX = 0; 
    private final int CoeffAng = 3; 
    
    public Missile(int x, int y, String type, String direction) {  
        super(x, y);
        this.direction = direction;

        setType(type);
    }
 
    private void setType(String type) {     // type is damage, speed and image

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
                SPACE = 4;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\fireBall.png";
                break;
            } 
            case "Laser":{
                damage = -3;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\Laser.png";
                break;
            }
            case "blueFireball":{
                damage = -3;
                SPACE = 1;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\BluetFireball.png";
                break;
            }
            case "GreenShoot":{
                damage = -1;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\GreenShott.png";
                break;
            }
            case "VioletLaser":{
                damage = -2;
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\VioletLaser.png";
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
