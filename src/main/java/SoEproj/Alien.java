package SoEproj;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Alien extends Sprite {

    private final int INITIAL_X = 400;
    private final int type;       // indicates the alien type 1 easy, 2 medium, 3 hard 

    public Alien(int x, int y, int level) {
        super(x, y);
        
        initAlien();
        this.type = level;
        setSpace(level);
    }

    private void setSpace(int level) {
        switch(level){
            case 1: SPACE = 1;
            case 2: SPACE = 3/2;
            case 3: SPACE = 2;
        }
    }

    private void initAlien() {
        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\MediumAlien_1.png");
        getImageDimensions();
    }

// Aliens return to the screen on the right side after they have disappeared on the left
    public void move() {
        
        if (x < 0) {
            x = INITIAL_X;
        }

        x -= SPACE;
    }
}
