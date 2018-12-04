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
    private final double BG_SPEED = 0.5; // background speed

    private int score = 0; // variabile punteggio che si aggiorna ad ogni alieno ucciso, dev'essere controllata nel ciclo di update per aggiornare 
    //lo score dinamicamente

    private SpaceShip spaceCraft;
    private List<Alien> aliens;
    private int gameState;
    private Thread animator;
    private int level;

    private Thread thread_gen; //thread per generare gli alieni 
    private AlienGenerator aliengen; // classe per la generazione

    private ImageIcon bgImgIcon;
    private Image background;
    private double bg_x_shift;
    private JPanel menuPanel;

    JLabel start = new JLabel(" START ");
    JLabel setting = new JLabel(" SETTING ");
    boolean music;
    
    //il parametro livello serve per cambiare aspetto, alieni e complessità in base alla situazione
    public Board(int shipType, JPanel p, boolean m,int level) {
        this.level = level;

        //se sono nel livello 1 carico uno specifico background
        if(this.level ==1){
            bgImgIcon = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\BackGround1.png");
            background = bgImgIcon.getImage();
        }
        
        menuPanel=p;
        music=m; //eventualità di cambio musica ad ogni livello 
        

        initGame(shipType); // potrebbe cambiare in base al livello
        animator = new Thread(this);
        gameLaunch();
    }



    public void initGame(int shipType) {
        gameState = 1;
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceCraft = new SpaceShip(ICRAFT_X, ICRAFT_Y, shipType, music);

        //cambia in base al livello 
        aliens = new ArrayList<>();
        aliengen = new AlienGenerator(background.getWidth(null),background.getHeight(null), aliens,this.level);
        System.out.print("Thread creato");
        thread_gen = new Thread(aliengen);
        thread_gen.start();
        //initAliens();
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
        
        
        for (int[] p : pos) {
            aliens.add(new HardAlien(p[0], p[1]));
        }
        //aliens.add(new Boss1Alien(B_WIDTH, B_HEIGHT/2, aliens));
    }


    @Override
   // This method will be executed by the painting subsystem whenever you component needs to be rendered
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw game sprites or write the game over message
        if(gameState == 1) {
            drawBackground(g);
            drawGame(g);
        } 
        else if(gameState == 2) {
            drawGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }


    private void drawBackground(Graphics g) {
        
        if (bg_x_shift > background.getWidth(null)) {
            bg_x_shift = 0;
        } else {
            bg_x_shift += BG_SPEED;

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

        synchronized(aliens){
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
                    if (alien instanceof Boss1Alien){
                        //aliens.clear();
                        if(this.level <3){
                            this.level +=1;

                            aliengen = new AlienGenerator(background.getWidth(null),background.getHeight(null), aliens,this.level);
                            System.out.print("Thread creato");
                            System.out.print(this.level);
                            thread_gen = new Thread(aliengen);
                            thread_gen.start();
                        } else {
                            gameState = 2;
                        }
                        
                    
                    

                        //gameState = 2;
                }
         }}
        }
        // In the top-left corner of the window, we draw how many aliens are left.
        // TODO Correggere scritte sopra allo sfondo
        g.setColor(Color.WHITE);
        g.drawString("Aliens left: " + aliens.size(), 5, 15);
    }

    /*
    draws a game over message in the middle of the window. The message is 
    displayed at the end of the game, either when we destroy all alien 
    ships or when we collide with one of them.
    */
    private void drawGameOver(Graphics g) {
        // g is a graphics context that, in some sense, represents the on-screen pixels
        /*String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, B_HEIGHT / 2);*/

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
        /*
        if (aliens.isEmpty()) {
            gameState = 2;
            return;
        }
        */
        synchronized(aliens){
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
                else{
                    score += aliens.remove(i).points; // chiedere ad emilio come mai mi fa ritornare il punteggio senza un getter
                    System.out.print(score); 
                    
            }
        }
    }
}



    public void checkCollisions() {

        Area r3 = spaceCraft.getShape();

        synchronized(aliens){
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
                Alien alien = aliens.get(i);
    
                List<Missile> missiles = alien.getMissiles();
                synchronized(missiles){
                
                    for (Missile m : missiles) {
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
            
            List<Missile> missiles = spaceCraft.getMissiles();
            synchronized(missiles){
                for (Missile m : missiles) {
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
