/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.geom.Area;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SpaceShip extends Sprite implements Runnable{

    private final int type;       // indicates the spaceship type 1 normal, 2 fast 
    private float dx;
    private float dy;
    private List<Missile> missiles;
    private Boolean firing = false;

    public SpaceShip(int x, int y, int level) {
        super(x, y);
        initCraft();
        this.type = level;
        SPACE = level;
    }

    private void initCraft() {       
        missiles = new ArrayList<>();
        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\OrangeCraft.png");
        getImageDimensions();
    }

    // in order to set the right speed 
    public void move() {       
        x += dx;
        y += dy;

        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }
    }


    public List<Missile> getMissiles() {
        return missiles;
    }


    public void keyPressed(KeyEvent e) throws InterruptedException{

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if(firing == false){    
                firing = true;
                Thread MissileAnimator = new Thread(this);
                MissileAnimator.start();
            }     
        }

        if (key == KeyEvent.VK_LEFT) {
            dx = -SPACE;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = SPACE;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -SPACE;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = SPACE;
        }
    }

    public void fire() {
            missiles.add(new Missile(x + width, y + height / 2, 1));
    }

    public void keyReleased(KeyEvent e) throws InterruptedException {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            firing = false;
            //MissileAnimator.wait();
        }

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    
    // return the shape of the image
    @Override
    public Area getShape() {
        int[] xpos = {x, x + width, x};
        int[] ypos = {y, y + height/2, y + height};
        Polygon shape = new Polygon(xpos, ypos, 3);

        return new Area(shape); 
    }


    @Override
    public void run() {
        while(firing){   
            fire();
            int sleep = 200;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread interrupted: %s", e.getMessage());
            }
        }
    }
}