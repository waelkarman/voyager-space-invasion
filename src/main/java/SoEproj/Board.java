/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class Board extends JPanel implements Runnable {

    private final int ICRAFT_X = 40;
    private final int ICRAFT_Y = 60;
    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 450;
    private final int DELAY = 15;
    private final double BG_SPEED = 0.5;    // background speed
    
    // TODO Lo score deve essere controllato nel ciclo di update per aggiornarsi dinamicamente
    private int score = 0;      // every kill updates the score

    private final File boardSound;
    private final ImageIcon alienExpl;
    private final ImageIcon shipExpl;
    private final File alienExplSound;
    private final File shipExplSound;

    private SpaceShip spaceShip;
    private List<Alien> aliens;
    private LinkedList<UpgradePack> packs;
    private int gameState;
    private Thread animator;
    private int level;

    private Thread threadGen;  
    private Thread threadpackGen;         // alien generator thread 
    private AlienGenerator alienGen;    // alien generator class
    private packGenerator packsGen;

    private ImageIcon bgImgIcon;
    private Image background;
    private double bgShiftX;

    private JPanel menuPanel;

    private boolean isMusicOn;
    private int keyModality;
    
    private int slife;
    private int alife;

    public Board(int shipType, JPanel p, boolean m, int level, int km) {
        this.level = level;
        // Images and soundtracks initialization
        boardSound = new File("./src/main/java/SoEproj/Resource/ThemeLevelSound1.wav");
        alienExpl = new ImageIcon("./src/main/java/SoEproj/Resource/ExplosionAliens.png");
        shipExpl = new ImageIcon("./src/main/java/SoEproj/Resource/ExplosionShip.png");
        alienExplSound = new File("./src/main/java/SoEproj/Resource/CollisionSound.wav");
        shipExplSound = new File("./src/main/java/SoEproj/Resource/FinalCollisionSound.wav");

        // level 1 background image
        if(this.level == 1){
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround1.png");
            background = bgImgIcon.getImage();
        }
        if(this.level == 2){
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround2.png");
            background = bgImgIcon.getImage();
        }
        if(this.level == 3){
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround3.png");
            background = bgImgIcon.getImage();
        }

        
        if(this.level == 2){
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround2.png");
            background = bgImgIcon.getImage();
        }

        if(this.level == 3){
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround3.png");
            background = bgImgIcon.getImage();
        }
        
        menuPanel = p;
        isMusicOn = m;          // eventualità di cambio musica ad ogni livello 
        keyModality = km;       // game commands switcher
        
        initGame(shipType);     // shipType may change with level
        gameLaunch();
    }



    public void initGame(int shipType) {
        gameState = 1;
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y, shipType, isMusicOn, keyModality);

        // changes with level
        
        packs = new LinkedList<>();
        packsGen = new packGenerator(background.getWidth(null),background.getHeight(null), packs,this.level);
        threadpackGen = new Thread(packsGen);
        
        aliens = new ArrayList<>();
        alienGen = new AlienGenerator(background.getWidth(null),background.getHeight(null), aliens,this.level);
        threadGen = new Thread(alienGen);

        threadGen.start();
        threadpackGen.start();
    }


    @Override
    // This method will be executed by the painting subsystem whenever you component needs to be rendered
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw game sprites or write the game over message
        // TODO Rendere gameState un enum
        if(gameState == 1) {        // draw background and game elements
            drawBackground(g);
            drawGame(g);
        } 
        else if(gameState == 2) {   // draw game over background gif after the lose condition
            EndGameFunction(0);     // passing 0 to draw game over background 
        }

        Toolkit.getDefaultToolkit().sync();
    }







    private void drawBackground(Graphics g) {
        
        if (bgShiftX > background.getWidth(null)) {
            bgShiftX = 0;
        } else {
            bgShiftX += BG_SPEED;
        }

        g.drawImage(background, (int) -bgShiftX, 0, null);
        g.drawImage(background, background.getWidth(null) - (int) bgShiftX, 0, null);
    }


    private void drawGame(Graphics g) {
        if (spaceShip.isVisible()) {
            g.drawImage(spaceShip.getImage(), spaceShip.getX(), spaceShip.getY(), this);
            
            if (spaceShip.isDying()) {
                spaceShip.die();
                gameState = 2;
            }
        }

        synchronized(spaceShip){
            List<Missile> ms = spaceShip.getMissiles();
            for (Missile missile : ms) {
                if (missile.isVisible()) {
                    g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
                }
            }
        }


        synchronized(packs){
            for(UpgradePack pack : packs){
                if (pack.isVisible()) {
                    g.drawImage(pack.getImage(), pack.getX(), pack.getY(), this);
                }
            }

        }
        

        synchronized(aliens) {   // they are drawn only if they have not been previously destroyed.
            for (Alien alien : aliens) {
                if (alien.isVisible()) {    
                    g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
                }
 
                synchronized(alien){
                    List<Missile> as = alien.getMissiles();
                    for (Missile missile : as) {
                        if (missile.isVisible()) {
                            g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
                        }
                    }
                }

                if (alien.isDying()) {
                    alien.die();

                    if (alien instanceof Boss1Alien){ // i'm starting level 2
                        aliens.clear();
                    if(this.level < 3){

                            this.level += 1;
                            if(this.level == 2){
                                //background updating
                                bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround2.png");
                                background = bgImgIcon.getImage();
                            }
                            if(this.level == 3){
                                //background updating
                                bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround3.png");
                                background = bgImgIcon.getImage();
                            }
                            threadGen.start();
                        } 
                        else {
                            gameState = 2;
                        }
                    }
                }
            }
        }

        // In the top-left corner of the window, we draw how many aliens are left.
        // TODO Correggere scritte sopra allo sfondo
        g.setColor(Color.WHITE);
        g.drawString("Score : " + score, 5, 15);
        synchronized(spaceShip){
            g.drawString("Life: " + spaceShip.getLife(),5,35);
            g.drawString("Speed: " + spaceShip.getSPACE(),5,55);
        }
        
    }










    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            
            updateShip();
            updateAliens();
            checkCollisions();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Thread Board: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }


    public void gameLaunch() {
        if(gameState == 1) {
            animator = new Thread(this);
            animator.start();

            if(isMusicOn) {
                try {
                    InputStream in = new FileInputStream(boardSound);
                    AudioStream audios = new AudioStream(in);
                    AudioPlayer.player.start(audios);
                }catch (IOException e) {
                    System.out.println("Audio Board:" + e.getMessage());
                }
            }
        }

    }


    private void updateShip() {
        if (spaceShip.isVisible()) {        
            synchronized(spaceShip){
                List<Missile> ms = spaceShip.getMissiles();
                for (int i=0; i < ms.size(); i++) {
                    Missile m = ms.get(i);
                    if (m.isVisible()) {
                        m.move();
                    } else {
                        ms.remove(m);
                    }
                }
            }

            spaceShip.move();
        }
    }




    private void updateAliens() {
        synchronized(aliens){
            for (int i=0; i < aliens.size(); i++) {
                Alien alien = aliens.get(i);

                synchronized(alien){
                    if (alien.isVisible()) {
                        List<Missile> ms = alien.getMissiles();
                        for (int j=0; j < ms.size(); j++) { 
                            Missile m = ms.get(j);                          
                            if (m.isVisible()) {
                                m.move();
                            } else {
                                ms.remove(m);
                            }
                        }

                        alien.move();
                    }  
                    else {
                        //score += alien.getPoints();
                        aliens.remove(alien);
                    }
                } 
            }
        }
    }


    public void checkCollisions() {
        Area shipHitbox = spaceShip.getShape();
        Area alienHitbox;
        Area packsHitbox;

        synchronized (packs) { 
            int i=0;
            for(i=0;i < packs.size();i++){// (UpgradePack pack : packs) {
                  // checking collisions between aliens and spaceship
                packsHitbox = packs.get(i).getShape();
                // intersection is empty if shapes aren't collided
                packsHitbox.intersect(shipHitbox);
                if (!packsHitbox.isEmpty()) {   
                    synchronized(spaceShip){
                        packs.get(i).updateSpaceShip(spaceShip,packs.get(i).getType()); 
                    
                    }

                    packs.get(i).setDying(true);
                    packs.poll();
                    
                }
            
            }

        }


        synchronized (aliens) {                 // checking collisions between aliens and spaceship
            for (Alien alien : aliens) {
                alienHitbox = alien.getShape();
                // intersection is empty if shapes aren't collided
                alienHitbox.intersect(shipHitbox);

                if (!alienHitbox.isEmpty()) {   
                    alien.setDying(true);
                    alien.setImage(alienExpl.getImage());
                    

                    synchronized(spaceShip){
                        spaceShip.setupLife(-1);
                        slife = spaceShip.getLife();
                    }
                    if(slife <= 0){
                        synchronized(spaceShip){
                            spaceShip.setDying(true);
                            spaceShip.setImage(shipExpl.getImage());
                        }
                        if(isMusicOn){
                            try {
                                InputStream in = new FileInputStream(shipExplSound);
                                AudioStream audios = new AudioStream(in);
                                AudioPlayer.player.start(audios);   
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }


                synchronized (alien) {  // checking collisions between alien missiles and spaceship
                    List<Missile> alienMissiles = alien.getMissiles();
                    for (Missile missile : alienMissiles) {
                        Area missileHitbox = missile.getShape();
                        // intersection is empty if shapes aren't collided
                        missileHitbox.intersect(shipHitbox);

                        if (!missileHitbox.isEmpty()) {
                            synchronized(spaceShip){
                                spaceShip.setupLife(-1);
                                slife = spaceShip.getLife();
                            }
                            missile.setVisible(false);

                            if(slife <= 0){
                                synchronized(spaceShip){
                                    spaceShip.setDying(true);
                                    spaceShip.setImage(shipExpl.getImage());
                                }
                                if(isMusicOn){
                                    try {
                                        InputStream in = new FileInputStream(shipExplSound);
                                        AudioStream audios = new AudioStream(in);
                                        AudioPlayer.player.start(audios);   
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }

                
                alienHitbox = alien.getShape();
                synchronized (spaceShip) {   // checking collisions between spaceship missiles and aliens
                    List<Missile> shipMissiles = spaceShip.getMissiles();
                    for (Missile missile : shipMissiles) {
                        Area missileHitbox = missile.getShape();
                        // intersection is empty if shapes aren't collided
                        missileHitbox.intersect(alienHitbox);

                        if (!missileHitbox.isEmpty()) {
                            synchronized(alien){
                                alien.setupLife(missile.getDamage());
                                alife = alien.getLife();
                            }
                            missile.setVisible(false);

                            if(alife <= 0){
                                synchronized(alien){
                                    alien.setDying(true);
                                    score += alien.getPoints();
                                    alien.setImage(alienExpl.getImage());
                                }
                                if(isMusicOn) {
                                    try {
                                        InputStream in = new FileInputStream(alienExplSound);
                                        AudioStream audios = new AudioStream(in);
                                        AudioPlayer.player.start(audios);   
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Outcome viene passato al pannello per disegnare la foto giusta in caso i vittoria o di sconfitta 
    public void EndGameFunction(int outcome){
        JFrame old = (JFrame) SwingUtilities.getWindowAncestor(this);
        old.getContentPane().remove(this);
        GameEndPanel gep = new GameEndPanel(outcome, menuPanel);
        old.add(gep).requestFocusInWindow();
        old.validate();
        old.repaint();
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            try {
                spaceShip.keyReleased(e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            spaceShip.keyPressed(e);
        }
    }


}
