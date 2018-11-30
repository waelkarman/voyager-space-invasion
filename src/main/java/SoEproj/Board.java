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
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;
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
    private final double BG_SHIFT = 0.5; // background

    private SpaceShip spaceCraft;
    private List<Alien> aliens;
    private int gameState = 0;
    private Thread animator;
    private Image background;
    private double bg_x_shift;
    private JPanel menuPanel;

    JLabel start = new JLabel(" START ");
    JLabel setting = new JLabel(" SETTING ");
    boolean music;
    
    public Board(int shipType, JPanel p, boolean m) {
        menuPanel=p;
        music=m;
        initGame(shipType);
        animator = new Thread(this);
        gameLaunch();
    }


    /*public void initMenu() {
        
        gameState = 0;
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);

        this.add(start);
        MenuInteract clkStart = new MenuInteract(this, start);
        start.addMouseListener(clkStart);
        
        this.add(setting);  // to implement
    }
    
    public void destroyMenu() {
        start.setVisible(false);
        setting.setVisible(false);
    }*/


    public void initGame(int shipType) {
        gameState = 1;
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceCraft = new SpaceShip(ICRAFT_X, ICRAFT_Y, shipType, music);

        initAliens();
        
    }


    public void initAliens() {

        int[][] pos = new int [18][2];
        Random random = new Random();
        int a = 735; // numero minimo
        int b = 3570; // numero massimo
        int aa = 44;
        int bb = 389;
        int cc = ((bb-aa) + 1);
        int c = ((b-a) + 1);
        int l;

        for(l=0;l<18;l++){
            pos[l][0]=random.nextInt(c)+a;
            pos[l][1]=random.nextInt(cc)+aa;
        }

        aliens = new ArrayList<>();
        
        //aliens.add(new Boss1Alien(background.getWidth(null),background.getHeight(null)/2, aliens));
        for (int[] p : pos) {
            aliens.add(new HardAlien(p[0], p[1]));
        }
    }


    @Override
   // This method will be executed by the painting subsystem whenever you component needs to be rendered
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw game sprites or write the game over message
        if(gameState == 0){
            drawBackground(g);
        }
        else if(gameState == 1) {
            drawBackground(g);
            drawGame(g);
        } 
        else if(gameState == 2) {
            drawGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }


    private void loadBackground() {
        ImageIcon ii = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\BackGround1.png");
        background = ii.getImage();
    }


    private void drawBackground(Graphics g) {
        loadBackground();

        if (bg_x_shift > background.getWidth(null)) {
            bg_x_shift = 0;
        } else {
            bg_x_shift += BG_SHIFT;

        }

        g.drawImage(background, (int) -bg_x_shift, 0, null);
        g.drawImage(background, background.getWidth(null) - (int) bg_x_shift, 0, null);
    }


    private void drawGame(Graphics g) {

        if (spaceCraft.isVisible()) {
            
            g.drawImage(spaceCraft.getImage(), spaceCraft.getX(), spaceCraft.getY(), this);

            if (spaceCraft.isDying()) {
                spaceCraft.die();
                gameState = 2;
            }
        }


        List<Missile> ms = spaceCraft.getMissiles();
        synchronized(ms){
            
            for (Missile missile : ms) {
                if (missile.isVisible()) {
                    g.drawImage(missile.getImage(), missile.getX(), 
                            missile.getY(), this);
                }
            }
        }


        // they are drawn only if they have not been previously destroyed.
        for (Alien alien : aliens) {
            if (alien.isVisible()) {    
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            List<Missile> as = alien.getMissiles();
            synchronized(as){
                for (Missile missile : as) {
                    if (missile.isVisible()) {
                        g.drawImage(missile.getImage(), missile.getX(), 
                                missile.getY(), this);
                    }
                }
            }

            if (alien.isDying()) {
                alien.die();
            }
        }

        // In the top-left corner of the window, we draw how many aliens are left.
        g.setColor(Color.WHITE);
        g.drawString("Aliens left: " + aliens.size(), 5, 15);
    }

    //  draws a game over message in the middle of the window. The message is 
    // displayed at the end of the game, either when we destroy all alien 
    // ships or when we collide with one of them.
    private void drawGameOver(Graphics g) {
// g is a graphics context that, in some sense, represents the on-screen pixels
        /*String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2,
                B_HEIGHT / 2);*/
        Image gamover;
        ImageIcon gamo = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\GameOver.gif");
        gamover = gamo.getImage();
        g.drawImage(gamover, 0, 0, null);
    }


    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            cycle();
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
                
                String msg = String.format("Thread interrupted: %s", e.getMessage());
                
                JOptionPane.showMessageDialog(this, msg, "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }


    private void cycle() {
        updateShip();
        updateMissiles();
        updateAliens(); 
    }


    public void gameLaunch() {
        if(gameState == 1){
            animator.start();
            if(music==true){
                InputStream in;
                try {
                    in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/ThemeLevelSound1.wav"));
                    AudioStream audios = new AudioStream(in);
                    AudioPlayer.player.start(audios);
                }catch (IOException ex) {
                }
            }
        }

    }


    private void updateShip() {
        if (spaceCraft.isVisible()) {        
            spaceCraft.move();
        }
    }


    private void updateMissiles() {
        List<Missile> ms = spaceCraft.getMissiles();
        synchronized(ms){
            for (int i = 0; i < ms.size(); i++) {
                Missile m = ms.get(i);
                
                if (m.isVisible()) {
                    m.move();
                } 
                else {
                    ms.remove(i);
                }
            }
        }
    
    }


    private void updateAliens() {

        if (aliens.isEmpty()) {
            gameState = 2;
            return;
        }

        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            
            if (alien.isVisible()) {
                
                List<Missile> alienMissiles = alien.getMissiles();
                synchronized(alienMissiles){
                    for (int k = 0; k < alienMissiles.size(); k++) {
                        Missile m = alienMissiles.get(k);
                        
                        if (m.isVisible()) {
                            m.move();
                        } 
                        else {
                            alienMissiles.remove(k);
                        }
                    }
                }
                alien.move();
            } 
            else {
                if (alien instanceof Boss1Alien)  // if boss is dead
                    aliens.clear();               // destroys all aliens to end the game

                aliens.remove(i);
            }
        }
    }

    public void checkCollisions() {

        Area r3 = spaceCraft.getShape();

        for (Alien alien : aliens) {
            Area r2 = alien.getShape();
            r2.intersect(r3);
            if (!r2.isEmpty()) {   
                alien.setDying(true);
                ImageIcon i1 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionAliens.png");
                alien.setImage(i1.getImage());

                spaceCraft.setDying(true);
                ImageIcon i2 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionShip.png");
                spaceCraft.setImage(i2.getImage());
                if(music==true){
                    InputStream in;
                    try {
                        in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/FinalCollisionSound.wav"));
                        AudioStream audios = new AudioStream(in);
                        AudioPlayer.player.start(audios);   
                    } catch (IOException ex) {
                    }
                }
                
            }
        }



        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            

                
            List<Missile> as = a.getMissiles();
            synchronized(as){
            
                for (Missile m : as) {
                    Area r1 = m.getShape();
                    
                    
                    Area r2 = spaceCraft.getShape();
                    r2.intersect(r1);
                    
                    if (!r2.isEmpty()) {
                        spaceCraft.setLife(-1);
                        m.setVisible(false);
                        if(spaceCraft.getLife() <= 0){
                            
                            spaceCraft.setDying(true);
                            ImageIcon i3 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionShip.png");
                            spaceCraft.setImage(i3.getImage());
                            if(music==true){
                                InputStream in;
                                try {
                                    in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/FinalCollisionSound.wav"));
                                    AudioStream audios = new AudioStream(in);
                                    AudioPlayer.player.start(audios);   
                                } catch (IOException ex) {
                                }
                            }
                        }   
                    }
                    


                }
            }
        }




        List<Missile> ms = spaceCraft.getMissiles();
        synchronized(ms){
            for (Missile m : ms) {
                Area r1 = m.getShape();
                
                for (Alien alien : aliens) {
                    Area r2 = alien.getShape();
                    r2.intersect(r1);
                    
                    if (!r2.isEmpty()) {
                        alien.setLife(m.getDamage());
                        m.setVisible(false);
                        if(alien.getLife() <= 0){
                            
                            alien.setDying(true);
                            ImageIcon i3 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionAliens.png");
                            alien.setImage(i3.getImage());
                            if(music==true){
                                InputStream in;
                                try {
                                    in = new FileInputStream(new File("./src/main/java/SoEproj/Resource/CollisionSound.wav"));
                                    AudioStream audios = new AudioStream(in);
                                    AudioPlayer.player.start(audios);   
                                } catch (IOException ex) {
                                }
                            }
                        }   
                    }
                }
            }
        }

       
    }
    
    
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
                spaceCraft.keyReleased(e);
            } catch (InterruptedException e1) {
            
                e1.printStackTrace();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            spaceCraft.keyPressed(e);
        }
    }
}
