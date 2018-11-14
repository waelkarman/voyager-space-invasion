package SoEproj;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Alien extends Sprite {

    private final int INITIAL_X = 400;
    private final int level;       // indicates the spaceship type 1 hard, 2 medium, 3 easy 

    public Alien(int x, int y, int level) {
        super(x, y);
        initAlien();
        SPACE = 4;
        countRefresh = 0;
        this.level = level;
    }

    private void initAlien() {
        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\MediumAlien_1.png");
        getImageDimensions();
    }

// Aliens return to the screen on the right side after they have disappeared on the left
    public void move() {
        
        if(countRefresh == level){
            if (x < 0) {
                x = INITIAL_X;
            }

            x -= SPACE;
            countRefresh = 0;
        }

        countRefresh += 1;
    }
}
