/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacebattle;

public class Missile extends Sprite {

    private final int BOARD_WIDTH = 390;
    private final int MISSILE_SPEED = 2;

    public Missile(int x, int y) {
        super(x, y);
        initMissile();
    }
    
    private void initMissile() {
        loadImage("./missile.png");
        getImageDimensions();        
    }

    // Missiles move in one direction only. They disappear after 
    // they reach the right window border
    public void move() {        
        x += MISSILE_SPEED;

        if (x > BOARD_WIDTH)
            visible = false;
    }
}
