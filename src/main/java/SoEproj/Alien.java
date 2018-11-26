package SoEproj;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Alien extends Sprite implements Runnable{

    private final int INITIAL_X = 600;
    private final int type;       // indicates the alien type 1 easy, 2 medium, 3 hard 
    private List<Missile> missiles;
    private int life;

    public Alien(int x, int y, int type, int life) {
        super(x, y);
        missiles = new ArrayList<>();
        this.life = life;
        this.type = type;
        setLevel(type);
        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }

    public int getLife(){
        return this.life;
    }

    public void setLife(int up){
        this.life = this.life + up;
    }
    

    private void setLevel(int level) {
        String pathImage = "";
        switch(level){
            case 1:{
                SPACE = 1;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\MediumAlien.png";
            } 
            case 2:{
                SPACE = 3/2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\MediumAlien.png";
            } 
            case 3:{
                SPACE = 2;
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\MediumAlien.png";
            }
        }
        loadImage(pathImage);
        getImageDimensions();
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

// Aliens return to the screen on the right side after they have disappeared on the left
    public void move() {
        
        if (x < 0) {
            x = INITIAL_X;
        }

        x -= SPACE;
    }

    @Override
    public Area getShape(){
        Ellipse2D shape = new Ellipse2D.Double(x,y,width,height);
        return new Area(shape);
    }

    public void fire() {
        missiles.add(new Missile(x , y + height / 2, type,1,false));
    }

    @Override
    public void run() {
        while(true){
            synchronized(missiles){
                fire();
            }  
            
            int sleep = 7000;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread fire interrupted: %s", e.getMessage());
            }
        }
    }



}
