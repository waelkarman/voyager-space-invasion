/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SpaceShip extends Sprite {

    private final int level;       // indicates the spaceship type 1 hard, 2 medium, 3 easy 
    private int dx;
    private int dy;
    private boolean firing;
    private List<Missile> missiles;

    public SpaceShip(int x, int y, int level) {
        super(x, y);
        initCraft();
        firing = false;
        this.level = level;
        countRefresh = 0;
        SPACE = 3;
    }

    private void initCraft() {       
        missiles = new ArrayList<>();
        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\OrangeCraft_1.png");
        getImageDimensions();
    }

    // in order to set the right speed 
    public void move() {
        
        if(countRefresh == level){
            x += dx;
            y += dy;
    
            if (x < 1) {
                x = 1;
            }
    
            if (y < 1) {
                y = 1;
            }

            countRefresh = 0;
        }

        countRefresh += 1;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            firing = true;
            fire();
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
        if(firing) {
            missiles.add(new Missile(x + width, y + height / 2, 3));
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            firing = false;
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
}