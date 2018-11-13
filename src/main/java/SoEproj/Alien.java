package SoEproj;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Alien extends Sprite {

    private final int INITIAL_X = 400;

    public Alien(int x, int y) {
        super(x, y);
        initAlien();
    }

    private void initAlien() {
        loadImage("C:\\Users\\waelk\\MEGA\\GitHub\\SoE\\SoE-Voyager_on_the_edge_of_the_solar_system\\src\\main\\java\\SoEproj\\Resource\\alien.png");
        getImageDimensions();
    }

// Aliens return to the screen on the right side after they have disappeared on the left
    public void move() {
        if (x < 0) {
            x = INITIAL_X;
        }

        x -= 1;
    }
}
