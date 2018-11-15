/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {

    private final int ICRAFT_X = 40;
    private final int ICRAFT_Y = 60;
    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 450;
    private final int DELAY = 15;
    private final int BG_SHIFT = 1; // background

    private SpaceShip spaceship;
    private List<Alien> aliens;
    private int gameState = 0;
    private Thread animator;
    private Image background;
    private int bg_x_shift = 0;

    ImageIcon i1 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\explAlien.png");
    ImageIcon i2 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\explShip.png");
    ImageIcon i3 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\explAlien.png");
    JLabel start = new JLabel(" START ");
    JLabel setting = new JLabel(" SETTING ");
    
    // These are the initial positions of alien ships
    private final int[][] pos = {               
        {2380, 129}, {2500, 159}, {1380, 189},
        {780, 109}, {580, 139}, {680, 239},
        {790, 259}, {760, 150}, {790, 150},
        {980, 209}, {560, 145}, {510, 170},
        {930, 159}, {590, 80}, {530, 160},
        {940, 59}, {990, 130}, {920, 200},
        {900, 259}, {660, 50}, {540, 90},
        {810, 220}, {860, 120}, {740, 180},
        {820, 128}, {490, 170}, {700, 130}
    };


    public Board() {
        initMenu();
        animator = new Thread(this);
    }


    public void initMenu() {
        
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
    }


    public void initGame() {
        gameState = 1;
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceship = new SpaceShip(ICRAFT_X, ICRAFT_Y, 3);

        initAliens();
        
    }


    public void initAliens() {
        aliens = new ArrayList<>();

        for (int[] p : pos) {
            aliens.add(new Alien(p[0], p[1], 2));
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

        g.drawImage(background, -bg_x_shift, 0, null);
        g.drawImage(background, background.getWidth(null) - bg_x_shift, 0, null);
    }




















    private void drawGame(Graphics g) {

        if (spaceship.isVisible()) {
            g.drawImage(spaceship.getImage(), spaceship.getX(), spaceship.getY(), this);

            if (spaceship.getIsDying()) {
                
                spaceship.die();
                gameState = 2;
            }
        }

        List<Missile> ms = spaceship.getMissiles();

        for (Missile missile : ms) {
            if (missile.isVisible()) {
                g.drawImage(missile.getImage(), missile.getX(), 
                        missile.getY(), this);
            }
        }
        // they are drawn only if they have not been previously destroyed.
        for (Alien alien : aliens) {
            if (alien.isVisible()) {    
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.getIsDying()) {
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




    private void cycle()  throws InterruptedException {
        updateShip();
        updateMissiles();
        updateAliens(); 
    }


    public void gameLaunch() {
        if(gameState == 1){
            animator.start();
        }

    }


    private void updateShip() {
        if (spaceship.isVisible()) {        
            spaceship.move();
        }
    }


    private void updateMissiles() {
        List<Missile> ms = spaceship.getMissiles();

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


    private void updateAliens() {

        if (aliens.isEmpty()) {
            gameState = 2;
            return;
        }

        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            
            if (a.isVisible()) {
                a.move();
            } 
            else {
                aliens.remove(i);
            }
        }
    }


    public void checkCollisions() throws InterruptedException{

        Rectangle r3 = spaceship.getBounds();

        for (Alien alien : aliens) {
            
            Rectangle r2 = alien.getBounds();

            if (r3.intersects(r2)) {             
                alien.setDying(true);
<<<<<<< HEAD
                spaceship.setDying(true);
                alien.setImage(i1.getImage());
=======
                ImageIcon i1 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionAliens.png");
                alien.setImage(i1.getImage());

                spaceship.setDying(true);
                ImageIcon i2 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionShip.png");
>>>>>>> a9269b2244f968fe2784ba9945b82b91cb23fcca
                spaceship.setImage(i2.getImage());
            }
        }

        List<Missile> ms = spaceship.getMissiles();

        for (Missile m : ms) {
            Rectangle r1 = m.getBounds();
            
            for (Alien alien : aliens) {
                Rectangle r2 = alien.getBounds();

                if (r1.intersects(r2)) {                   
                    m.setVisible(false);
                    //TODO : in caso di piu livelli qui và solo decrementata una variabile
                    alien.setDying(true);
<<<<<<< HEAD
=======
                    ImageIcon i3 = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionAliens.png");
>>>>>>> a9269b2244f968fe2784ba9945b82b91cb23fcca
                    alien.setImage(i3.getImage());
                }
            }
        }
    }








    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            try {
                spaceship.keyReleased(e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            try {
                spaceship.keyPressed(e);
            } catch (InterruptedException e1) {
                System.out.println("Vittorio fa");
                e1.printStackTrace();
            }
        }
    }




    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (gameState == 1) {
            
            try{
                checkCollisions();
            }
            catch (InterruptedException e) {
                System.out.println("Vittorio fa cagare");
            }


            try{
                cycle();
                repaint();
            }
            catch (InterruptedException e) {
                System.out.println("Vittorio cagare");
            }
            



            

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                    sleep = 2;
            }

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    System.out.println("Vittorio ");
                    String msg = String.format("Thread interrupted: %s", e.getMessage());
                    
                    JOptionPane.showMessageDialog(this, msg, "Error", 
                    JOptionPane.ERROR_MESSAGE);
                }

                beforeTime = System.currentTimeMillis();
        }
    }
}