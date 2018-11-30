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

public class SpaceShip extends Sprite implements Runnable{

    private final int type;       // spaceship color: 1-Green, 2-Orange, 3-Red
    private float dx;
    private float dy;
    private List<Missile> missiles;
    private Boolean firing = false;
    private int life;
    private String missiletype; //imposta danno, velocita, image
    private boolean music;
    

    public SpaceShip(int x, int y, int color,boolean m) {
        super(x, y);
        missiles = new ArrayList<>();
        this.missiletype = "3Missiles";
        this.life = 1;
        this.type = color;
        this.SPACE = 1; //velocita
        music = m;
        setColor(color);
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

    public int getLife() {
        return this.life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setMissiletype(String missiletype) {
        this.missiletype = missiletype;
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


    public List<Missile> getMissiles() {
        return missiles;
    }


    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {

            if(firing == false){    
                firing = true;
                Thread SpaceshipMissileAnimator = new Thread(this);
                SpaceshipMissileAnimator.start();
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
        if( missiletype != "3Missiles"){
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToRight" ));
            if(music==true){
                InputStream in;
                try {
                    in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/LaserSound.wav"));
                    AudioStream audios;
                    audios = new AudioStream(in);
                    AudioPlayer.player.start(audios);
                } catch (IOException ex) {
                }
            }
        }
        else{
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToRight" ));
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToTop" ));
            missiles.add(new Missile(x + width, y + height / 2, missiletype, "leftToBottom" ));
            if(music==true){
                InputStream in;
                try {
                    in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/LaserSound.wav"));
                    AudioStream audios;
                    audios = new AudioStream(in);
                    AudioPlayer.player.start(audios);
                } catch (IOException ex) {
                }
            }
        }
        
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

    // TODO risolvere spari a raffica
    @Override
    public void run() {
        while(firing){ 
            synchronized(missiles){
                fire();
            }  
            
            int sleep = 500;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread fire interrupted: %s", e.getMessage());
                System.out.println(msg);
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