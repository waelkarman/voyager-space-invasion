package SoEproj;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.*;
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

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 450;
    private final int DELAY = 15;
    private final double BG_SPEED = 0.5;    // background speed
    private final ImageIcon alienExpl;
    private final ImageIcon shipExpl;
    private final File alienExplSound;
    private final File shipExplSound;

    // TODO Lo score deve essere controllato nel ciclo di update per aggiornarsi dinamicamente
    private int scoreS1 = 0;      // every kill updates the score
    private int scoreS2 = 0;
    private boolean MULTIPLAYER;
    private SpaceShip spaceShip1, spaceShip2;
    private List<Alien> aliens;
    private LinkedList<UpgradePack> packs;
    private int gameState;
    private Thread boardAnimator;
    private int level;

    private Thread threadAliensGen;     // alien generator thread      
    private AlienGenerator aliensGen;    // alien generator class
    private Thread threadPacksGen; 
    private PackGenerator packsGen;

    private ImageIcon bgImgIcon;
    private Image background;
    private double bgShiftX;

    private JPanel menuPanel;

    private boolean isMusicOn;
    private int keyModality;
    
    private int ship1Life;
    private int ship2Life;
    private int alienLife;
    
    private InputStream in;
    private AudioStream audios;
    private File boardSound;


    public Board(int shipType, JPanel p, boolean m, int level, int km, boolean mp) {
        this.MULTIPLAYER = mp;
        this.level = level;
        // Images and soundtracks initialization
        this.isMusicOn = m;          // eventualità di cambio musica ad ogni livello 
        this.menuPanel = p;
        this.keyModality = km;       // game commands switcher

        if(isMusicOn){
            boardSound = new File("./src/main/java/SoEproj/Resource/MusicGame.wav");
            try {
                in = new FileInputStream(boardSound);
                audios = new AudioStream(in);
                AudioPlayer.player.start(audios);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        alienExpl = new ImageIcon("./src/main/java/SoEproj/Resource/ExplosionAliens.png");
        shipExpl = new ImageIcon("./src/main/java/SoEproj/Resource/ExplosionShip.png");
        alienExplSound = new File("./src/main/java/SoEproj/Resource/CollisionSound.wav");
        shipExplSound = new File("./src/main/java/SoEproj/Resource/FinalCollisionSound.wav");

        setBackground();
        initGame(shipType);     // shipType may change with level
        gameLaunch();
    }


    private void setBackground() {
        // level 1 background image
        bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround1.png");
        
        if(level == 2)
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround2.png");
        if(level == 3)
            bgImgIcon = new ImageIcon("./src/main/java/SoEproj/Resource/BackGround3.png");

        background = bgImgIcon.getImage();
    }


    public void initGame(int shipType) {
        gameState = 1;
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceShip1 = new SpaceShip(0, B_HEIGHT/2, shipType, isMusicOn, keyModality);

        if(MULTIPLAYER == true)
            spaceShip2 = new SpaceShip(0, B_HEIGHT/2 + spaceShip1.height, shipType + 1 % 3 , isMusicOn, keyModality + 1 % 2); // +1 % 2 for set a different type
        
        packs = new LinkedList<>();
        packsGen = new PackGenerator(background.getWidth(null), packs, this.level);
        threadPacksGen = new Thread(packsGen);
        
        aliens = new ArrayList<>();
        aliensGen = new AlienGenerator(background.getWidth(null), aliens, this.level);
        threadAliensGen = new Thread(aliensGen);

        threadAliensGen.start();
        threadPacksGen.start();
    }

    // This method will be executed by the painting subsystem whenever you component needs to be rendered
    @Override
    public void paintComponent(Graphics g) {    
        super.paintComponent(g);

        // TODO Rendere gameState un enum
        if(gameState == 1) {        // draw background and game elements
            drawBackground(g);
            drawGame(g);
        } 
        else if(gameState == 2) {   // draw game over background gif after the lose condition 
            EndGameFunction(0);     // passing 0 to draw game over background 
        }
        // TODO fare la condizione di win

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
        if (spaceShip1.isVisible()) {
            g.drawImage(spaceShip1.getImage(), spaceShip1.getX(), spaceShip1.getY(), this);
            
            if (spaceShip1.isDying()) {
                spaceShip1.die();
            }
        }

        synchronized(spaceShip1){
            List<Missile> ms1 = spaceShip1.getMissiles();
            for (Missile missile : ms1) {
                if (missile.isVisible()) {
                    g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
                }
            }
        }

        if(MULTIPLAYER == true){
            if (spaceShip2.isVisible()) {
                g.drawImage(spaceShip2.getImage(), spaceShip2.getX(), spaceShip2.getY(), this);
                
                if (spaceShip2.isDying()) {
                    spaceShip2.die();
                }
            }

            synchronized(spaceShip2){
                List<Missile> ms2 = spaceShip2.getMissiles();
                for (Missile missile : ms2) {
                    if (missile.isVisible()) {
                        g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
                    }
                }
            }
        }
        
        //GAME OVER condition set
        if(MULTIPLAYER == true){
            if (spaceShip1.isDying() && spaceShip2.isDying()){
                gameState = 2;
            }
        }
        else{
            if (spaceShip1.isDying()){
                gameState = 2;
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

                    if (alien instanceof Boss1Alien){ // i'm starting a new level
                        if(this.level < 3){
                            this.level += 1;
                            setBackground();

                            aliensGen = new AlienGenerator(background.getWidth(null), aliens, this.level);
                            threadAliensGen = new Thread(aliensGen);
                            threadAliensGen.start();
                        } 
                        else {
                            gameState = 2;
                        }
                    }
                }
            }
        }

        // In the top-left corner of the window, we draw how many aliens are left.
        g.setColor(Color.WHITE);
        g.drawString("Score 1 : " + scoreS1, 5, 15);
        synchronized(spaceShip1){
            g.drawString("Life 1: " + spaceShip1.getLife(),5,35);
            g.drawString("Speed 1: " + spaceShip1.getSPACE(),5,55);
        }
        
        if(MULTIPLAYER == true){
            g.drawString("Score 2 : " + scoreS2, 5, 75);
            synchronized(spaceShip2){
                g.drawString("Life 2: " + spaceShip2.getLife(),5,95);
                g.drawString("Speed 2: " + spaceShip2.getSPACE(),5,115);
            }
        }

    }


    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (gameState != 2) {
            updateShip();
            updateAliens();
            updatePacks();
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

        repaint();
    }


    public void gameLaunch() {
        if(gameState == 1) {
            boardAnimator = new Thread(this);
            boardAnimator.start();
        }

    }


    private void updateShip() {
        if (spaceShip1.isVisible()) {        
            synchronized(spaceShip1){
                List<Missile> ms = spaceShip1.getMissiles();
                for (int i=0; i < ms.size(); i++) {
                    Missile m = ms.get(i);
                    if (m.isVisible()) {
                        m.move();
                    } else {
                        ms.remove(m);
                    }
                }
            }
            spaceShip1.move();
        }

        if(MULTIPLAYER == true){
            if (spaceShip2.isVisible()) {        
                synchronized(spaceShip2){
                    List<Missile> ms = spaceShip2.getMissiles();
                    for (int i=0; i < ms.size(); i++) {
                        Missile m = ms.get(i);
                        if (m.isVisible()) {
                            m.move();
                        } else {
                            ms.remove(m);
                        }
                    }
                }
                spaceShip2.move();
            }
        }

    }


    private void updatePacks() {
        synchronized(packs){
            for (int i=0; i < packs.size(); i++) {
                UpgradePack pack = packs.get(i);
                synchronized(pack){
                    if (pack.isVisible()) {                         
                        pack.move();
                    }  
                    else {
                        packs.poll();
                    }
                } 
            }
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
        Area alienHitboxfor1;
        Area alienHitboxfor2;
        Area packsHitbox;
        Area ship2Hitbox = spaceShip1.getShape(); // è corretto settare a spaceship 1 ma bisogna rivisionare
        Area ship1Hitbox = spaceShip1.getShape();
        synchronized (packs) { 
            int i=0;
            for(i=0;i < packs.size();i++){// (UpgradePack pack : packs) {
                  // checking collisions between aliens and spaceship
                packsHitbox = packs.get(i).getShape();
                // intersection is empty if shapes aren't collided
                packsHitbox.intersect(ship1Hitbox);
                if (!packsHitbox.isEmpty()) {   
                    synchronized(spaceShip1){
                        packs.get(i).updateSpaceShip(spaceShip1); 
                    
                    }
                    
                    packs.get(i).setDying(true);
                    packs.poll();
                    
                    if(isMusicOn){
                        try {
                            InputStream in = new FileInputStream("./src/main/java/SoEproj/Resource/PowerUp.wav");
                            AudioStream audios = new AudioStream(in);
                            AudioPlayer.player.start(audios);   
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }

        
        if(MULTIPLAYER == true){
            ship2Hitbox = spaceShip2.getShape();
            synchronized (packs) { 
                int i=0;
                for(i=0;i < packs.size();i++){// (UpgradePack pack : packs) {
                      // checking collisions between aliens and spaceship
                    packsHitbox = packs.get(i).getShape();
                    // intersection is empty if shapes aren't collided
                    packsHitbox.intersect(ship2Hitbox);
                    if (!packsHitbox.isEmpty()) {   
                        synchronized(spaceShip2){
                            packs.get(i).updateSpaceShip(spaceShip2); 
                        
                        }
    
                        packs.get(i).setDying(true);
                        packs.poll();
                        
                    }
                }
            }   
        }

        synchronized (aliens) {                 // checking collisions between aliens and spaceship
            for (Alien alien : aliens) {
                alienHitboxfor1 = alien.getShape();
                // intersection is empty if shapes aren't collided
                alienHitboxfor1.intersect(ship1Hitbox);
                if (!alienHitboxfor1.isEmpty()) {   
                    alien.setDying(true);
                    alien.setImage(alienExpl.getImage());
                    

                    synchronized(spaceShip1){
                        spaceShip1.setupLife(-1);
                        ship1Life = spaceShip1.getLife();
                    }
                    if(ship1Life <= 0){
                        synchronized(spaceShip1){
                            spaceShip1.setDying(true);
                            spaceShip1.setImage(shipExpl.getImage());
                            spaceShip1.getMissiles().clear();//TODO faccio sparire i missili del P2 perche non avanzano
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

                if(MULTIPLAYER == true){
                    alienHitboxfor2 = alien.getShape(); //A Shape variable cannot be assigned as a common variable
                    alienHitboxfor2.intersect(ship2Hitbox);
                    if (!alienHitboxfor2.isEmpty()) {   
                        alien.setDying(true);
                        alien.setImage(alienExpl.getImage());
                        

                        synchronized(spaceShip2){
                            spaceShip2.setupLife(-1);
                            ship2Life = spaceShip2.getLife();
                        }
                        if(ship2Life <= 0){
                            synchronized(spaceShip2){
                                spaceShip2.setDying(true);
                                spaceShip2.setImage(shipExpl.getImage());
                                spaceShip2.getMissiles().clear();//TODO faccio sparire i missili del P2 perche non avanzano
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


                synchronized (alien) {  // checking collisions between alien missiles and spaceship
                    List<Missile> alienMissiles = alien.getMissiles();
                    for (Missile missile : alienMissiles) {
                        Area missile1Hitbox = missile.getShape();
                        // intersection is empty if shapes aren't collided
                        missile1Hitbox.intersect(ship1Hitbox);

                        if (!missile1Hitbox.isEmpty()) {
                            synchronized(spaceShip1){
                                spaceShip1.setupLife(-1);
                                ship1Life = spaceShip1.getLife();
                            }

                            //TODO lasciare i missili degli alieni proseguire nel caso si uccide un alieno
                            missile.setVisible(false);

                            if(ship1Life <= 0){
                                synchronized(spaceShip1){
                                    spaceShip1.setDying(true);
                                    spaceShip1.setImage(shipExpl.getImage());
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


                        if(MULTIPLAYER == true){
                            Area missile2Hitbox = missile.getShape();
                            missile2Hitbox.intersect(ship2Hitbox);
                            if (!missile2Hitbox.isEmpty()) {
                                synchronized(spaceShip2){
                                    spaceShip2.setupLife(-1);
                                    ship2Life = spaceShip2.getLife();
                                }
    
                                //TODO lasciare i missili degli alieni in campo nel caso si uccide un alieno
                                missile.setVisible(false);
    
                                if(ship2Life <= 0){
                                    synchronized(spaceShip2){
                                        spaceShip2.setDying(true);
                                        spaceShip2.setImage(shipExpl.getImage());
                                        spaceShip2.getMissiles().clear();
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
                }

                alienHitboxfor1 = alien.getShape();
                alienHitboxfor2 = alien.getShape();


                //TODO MULTIPLAYER valutare di trasformare il seguente controllo in una funzione ridondo il codice per l impossibilità di 
                //sincronizzare su più oggetti
                synchronized (spaceShip1) {   // checking collisions between spaceship missiles and aliens
                    List<Missile> ship1Missiles = spaceShip1.getMissiles();
                    for (Missile missile : ship1Missiles) {
                        Area missileHitbox = missile.getShape();
                        // intersection is empty if shapes aren't collided
                        missileHitbox.intersect(alienHitboxfor1);

                        if (!missileHitbox.isEmpty()) {
                            synchronized(alien){
                                alien.setupLife(missile.getDamage());
                                alienLife = alien.getLife();
                            }
                            if(alienLife>0){
                                if(isMusicOn){
                                    try {
                                        InputStream in = new FileInputStream("./src/main/java/SoEproj/Resource/ShoothedBoss.wav");
                                        AudioStream audios = new AudioStream(in);
                                        AudioPlayer.player.start(audios);   
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                            missile.setVisible(false);

                            if(alienLife <= 0){
                                synchronized(alien){
                                    alien.setDying(true);
                                    scoreS1 += alien.getPoints();
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

                if(MULTIPLAYER == true){
                    synchronized (spaceShip2) {   // checking collisions between spaceship missiles and aliens
                        List<Missile> ship2Missiles = spaceShip2.getMissiles();
                        for (Missile missile : ship2Missiles) {
                            Area missileHitbox = missile.getShape();
                            // intersection is empty if shapes aren't collided
                            missileHitbox.intersect(alienHitboxfor2);

                            if (!missileHitbox.isEmpty()) {
                                synchronized(alien){
                                    alien.setupLife(missile.getDamage());
                                    alienLife = alien.getLife();
                                }
                                missile.setVisible(false);

                                if(alienLife <= 0){
                                    synchronized(alien){
                                        alien.setDying(true);
                                        scoreS2 += alien.getPoints();
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
    }

    //Outcome viene passato al pannello per disegnare la foto giusta in caso i vittoria o di sconfitta 
    public void EndGameFunction(int outcome){
        AudioPlayer.player.stop(audios);
        int finalScore;
        JFrame old = (JFrame) SwingUtilities.getWindowAncestor(this);
        old.getContentPane().remove(this);
        if(MULTIPLAYER == true)
            finalScore = scoreS1 + scoreS2;
        else
            finalScore = scoreS1;
        GameEndPanel gep = new GameEndPanel(outcome,menuPanel,finalScore,isMusicOn);
        old.add(gep).requestFocusInWindow();
        old.validate();
        old.repaint();
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            try {
                spaceShip1.keyReleased(e);
                if(MULTIPLAYER == true){
                    spaceShip2.keyReleased(e);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            spaceShip1.keyPressed(e);
            if(MULTIPLAYER == true){
                spaceShip2.keyPressed(e);
            }
        }
    }

   

}
