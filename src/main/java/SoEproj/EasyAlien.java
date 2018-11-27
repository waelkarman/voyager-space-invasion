package SoEproj;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class EasyAlien extends Alien {


    public EasyAlien(int x, int y) {
        super(x, y, 1);
        SPACE = 1;

        loadImage(".\\src\\main\\java\\SoEproj\\Resource\\EasyAlien.png");
        getImageDimensions();
    }


    public void move() {  
        if (x < 0) {
            setDying(true);
        }

        x -= SPACE;
    }

    @Override
    public List<Missile> getMissiles() {
        return null;
    }

}
