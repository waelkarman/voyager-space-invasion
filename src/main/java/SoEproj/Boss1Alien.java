package SoEproj;

import java.util.List;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Boss1Alien extends Alien implements Runnable{

    // TODO Cambiare immagine boss1
    private String imagePath = ".\\src\\main\\java\\SoEproj\\Resource\\HeavyAlien.png";
    private boolean goDown = true;  // to set boss go at first down and then up
    private final int TOT_LIFE = life;
    private List<Alien> aliens;

    public Boss1Alien(int x, int y, List<Alien> aliens) {
        super(x, y, 10);
        System.out.println("Prima dell'inizializzazione della lista");
        this.aliens = aliens;
        System.out.println("Dopo l'inizializzazione della lista");
        SPACE = 3/2;
        super.points = 800;
        loadImage(imagePath);
        getImageDimensions();

        Thread AlienMissileAnimator = new Thread(this);
        AlienMissileAnimator.start();
    }
    
    
    // Boss enters in scene and then moves up and down
    @Override
    public void move() {

        if (x >= INITIAL_X-30) {
            x -= SPACE;
        }
        else {
            if (goDown) {
                y += SPACE;
                if (y > 400) {
                    goDown = false;
                }
            } else {
                y -= SPACE;
                if (y < 0) {
                    goDown = true;
                }
            }
        }
    }

    public void fire() {
        missiles.add(new Missile(x , y + height/2, "Laser", "rightToTop"));
        missiles.add(new Missile(x , y + height/2, "Laser", "rightToLeft"));
        missiles.add(new Missile(x , y + height/2, "Laser", "rightToBottom"));
    }

    @Override
    public void run() {
        while(true){
            int sleep = 7000;

            // TODO i BossHelper devono uscire solo se gli altri sono già morti prima
            if (life < TOT_LIFE * 0.5) {
                aliens.add(new EasyAlien(x, y - 30, "BossHelper", goDown));
                aliens.add(new EasyAlien(x, y, "BossHelper", goDown));
                aliens.add(new EasyAlien(x, y + 30, "BossHelper", goDown));
            }

            synchronized(missiles){
                fire();
            }  
            
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread fire interrupted: %s", e.getMessage());
                System.out.println(msg);
            }
        }
    }

}