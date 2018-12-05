/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SpaceShip extends Sprite{

    private final int type;       // spaceship color: 1-Green, 2-Orange, 3-Red
    private float dx;
    private float dy;
    private List<Missile> missiles;
    private Boolean firing = false;
    private int life;
    private String missiletype; //imposta danno, velocita, image
    private boolean music;
    private Thread shipMissileFire;
    private int keyModality;
                 
    

    public SpaceShip(int x, int y, int color, boolean m, int km) {
        super(x, y);
        missiles = new ArrayList<>();
        this.missiletype = "Laser";
        this.life = 1;
        this.type = color;
        this.SPACE = 1;     // velocità
        music = m;
        keyModality = km;
        setColor(color);

        shipMissileFire = new Thread(new FireThread(this));
        shipMissileFire.start();
    }

    private void setColor(int color) {
        String pathImage = "";
        switch(color){
            case 1:{
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\GreenCraft.png";
                break;
            } 
            case 2:{
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\OrangeCraft.png";
                break;
            } 
            case 3:{
                pathImage = ".\\src\\main\\java\\SoEproj\\Resource\\RedCraft.png";
                break;
            }
        }

        loadImage(pathImage);
        getImageDimensions();
    }


    public synchronized int getLife() {
        return this.life;
    }


    public synchronized void setLife(int life) {
        this.life = life;
    }
    

    public synchronized Boolean getFiring() {
        return this.firing;
    }


    public synchronized void setFiring(Boolean firing) {
        this.firing = firing;
    }


    public synchronized void setMissiletype(String missiletype) {
        this.missiletype = missiletype;
    }


    public synchronized  List<Missile> getMissiles() {
        return missiles;
    }


    public synchronized void fire() {
        if( missiletype != "3Missiles"){
            
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToRight" ));
            
            
            if(music) {
                try {
                    InputStream in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/LaserSound.wav"));
                    AudioStream audios = new AudioStream(in);
                    AudioPlayer.player.start(audios);
                } catch (IOException e) {
                    System.out.println("Spaceship Music: " + e);
                }
            }
        }
        else{
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToRight" ));
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToTop" ));
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToBottom" ));
            
            if(music){
                try {
                    InputStream in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/LaserSound.wav"));
                    AudioStream audios = new AudioStream(in);
                    AudioPlayer.player.start(audios);
                } catch (IOException e) {
                    System.out.println("Spaceship Music: " + e);
                }
            }
        }  
    }


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


    // TODO risolvere certe combinazioni di tasti che non funzionano (es. space+down+right)
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        if(keyModality == 2){
            if (key == KeyEvent.VK_SPACE) {
                setFiring(true);
                synchronized(missiles){
                    missiles.notifyAll();
                }   
            }

            if (key == KeyEvent.VK_A) {
                dx = -SPACE;
            }

            if (key == KeyEvent.VK_D) {
                dx = SPACE;
            }

            if (key == KeyEvent.VK_W) {
                dy = -SPACE;
            }

            if (key == KeyEvent.VK_S) {
                dy = SPACE;
            }
        }else{

            if (key == KeyEvent.VK_SPACE) {
                setFiring(true);
                synchronized(missiles){
                    missiles.notifyAll();
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
    }

    public void keyReleased(KeyEvent e) throws InterruptedException {

        int key = e.getKeyCode();
        
        if(keyModality == 2){
            if (key == KeyEvent.VK_SPACE) {
                setFiring(false);
            }

            if (key == KeyEvent.VK_A) {
                dx = 0;
            }

            if (key == KeyEvent.VK_D) {
                dx = 0;
            }

            if (key == KeyEvent.VK_W) {
                dy = 0;
            }

            if (key == KeyEvent.VK_S) {
                dy = 0;
            }
        }else{

            if (key == KeyEvent.VK_SPACE) {
                setFiring(false);
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


    @Override
    public Area getShape(){
        int[] xpos = { x, x+width, x};
        int[] ypos = { y, y + height/2, y + height };
        Polygon shape = new Polygon(xpos,ypos,3);
        return new Area(shape);
    }

}