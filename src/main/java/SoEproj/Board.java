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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Board extends JPanel implements Runnable {

    protected final int B_WIDTH = 590;
    protected final int B_HEIGHT = 435;
    protected final int DELAY = 15;
    protected final double BG_SPEED = 0.3;    // background speed

    protected final ImageIcon alienExpl;
    protected final ImageIcon shipExpl;
    protected final File alienExplSound;
    protected final File shipExplSound;
    protected final File powerUpSound;
    protected final File bossHitSound;
    protected File boardSound;

    private static Board istance = null; //Vittorio
    private Boolean isPause = false;

    protected boolean isMusicOn;

    protected ImageIcon bgImgIcon;
    protected Image background;
    protected double bgShiftX;

    protected Thread threadAliensGen;      // alien generator thread
    protected AlienGenerator aliensGen;    // alien generator class
    protected Thread threadPacksGen;
    protected PackGenerator packsGen;
    protected Thread boardAnimator;

    protected GameStateEnum gameState;
    protected JPanel menuPanel;
    protected int keyModality;
    protected int level;
    protected boolean isMultiplayer;

    protected List<SpaceShip> spaceShips; 
    protected List<Alien> aliens;
    protected List<UpgradePack> packs;
    protected MusicManager mumZero;

    private int stage;
    private Timer t;
    private TimerTask task;
    private int interstage;
    private boolean interstageEnd;
    private boolean lock;

    private Board(int shipType, JPanel p, boolean m, int level, int km, boolean mp) {
        this.isMultiplayer = mp;
        this.level = level;
        // Images and soundtracks initialization
        this.isMusicOn = m;          // TODO music may change in each level
        this.menuPanel = p;
        this.keyModality = km;       // game commands switcher

        alienExpl = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionAliens.png");
        shipExpl = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ExplosionShip.png");
        alienExplSound = new File(".\\src\\main\\java\\SoEproj\\Resource\\CollisionSound.wav");
        shipExplSound = new File(".\\src\\main\\java\\SoEproj\\Resource\\FinalCollisionSound.wav");
        powerUpSound = new File(".\\src\\main\\java\\SoEproj\\Resource\\PowerUp.wav");
        bossHitSound = new File(".\\src\\main\\java\\SoEproj\\Resource\\ShoothedBoss.wav");
        boardSound = new File(".\\src\\main\\java\\SoEproj\\Resource\\MusicGame.wav");
        
        if(isMusicOn) {
            mumZero = new MusicManager(boardSound);
            mumZero.loopMusic();
        }

        stage = 0;
        interstage = 0;
        interstageEnd = true;
        t = new Timer();
        lock = false;

        setBackground();
        initGame(shipType);     // shipType may change with level
        gameLaunch();
    }

 

    public static Board setBoard(int shipType, JPanel p, boolean m, int level, int km, boolean mp){

        if(istance == null){
            istance = new Board(shipType,p, m, level,km, mp);
        }
        return istance;

    }

    public void resetBoard(){ //Vittorio ho cambiato da privato a pubblico
        istance = null;
    }

    protected void setBackground() {
        if(level == 1)    
            bgImgIcon = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\BackGround1.png");
        if(level == 2)
            bgImgIcon = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\BackGround2.png");
        if(level == 3)
            bgImgIcon = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\BackGround3.png");

        background = bgImgIcon.getImage();
    }

    public void initGame(int shipType) {
        gameState = GameStateEnum.IN_GAME;
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceShips = new ArrayList<SpaceShip>();
        spaceShips.add( new SpaceShip(0, B_HEIGHT/2, shipType, isMusicOn, keyModality) );
        if(isMultiplayer)
            spaceShips.add( new SpaceShip(0, B_HEIGHT/2 + 60, shipType + 1 % 3 , isMusicOn, keyModality + 1 % 2) ); // +1 % 2 for set a different type

        packs = new LinkedList<UpgradePack>();
        packsGen = new PackGenerator(background.getWidth(null),background.getHeight(null), packs);// TODO MERGE costruttore cambiato da controllare
        packsGen.start();

        aliens = new ArrayList<Alien>();
    }
//---------------------------END GAME INITIALIZATION----------------------------------->

//-------------------STAGE SHIFTER-------------------------------->
public void setInterStageEnd(boolean finish){
    this.interstageEnd = finish;
}

public void setStage(int stg){
    this.stage = stg;
}

public void SetInterStage(int n){
    this.interstage = n;
}
//-------------------END STAGE SHIFTER-------------------------------->

//---------------------GRAPHICS--------------------------------------------------->
    // This method will be executed by the painting subsystem whenever you component needs to be rendered
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameState == GameStateEnum.IN_GAME) {        // draw background and game elements
            DrawInterface(g);
            InterStage(g);
        }
        else if(gameState == GameStateEnum.GAME_LOST) {   // draw game over background gif after the lose condition
            EndGameFunction(0);     // passing 0 to draw game over background
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void DrawShipAndMissiles(Graphics g) {
        for(int i=0; i<spaceShips.size(); i++){
            SpaceShip ship = spaceShips.get(i);
            
            if (ship.isVisible()) {
                g.drawImage(ship.getImage(), ship.getX(), ship.getY(), this);
                if (ship.isDying()) 
                    ship.die();
            }
            synchronized(ship){
                List<Missile> ms = ship.getMissiles();
                for (Missile missile : ms) {
                    if (missile.isVisible()) {
                        g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
                    }
                }
            }
        }
    }

    private void DrawPacks(Graphics g) {
        synchronized(packs) {
            for(UpgradePack pack : packs) {
                if (pack.isVisible())
                    g.drawImage(pack.getImage(), pack.getX(), pack.getY(), this);
                    if (pack.isDying()) 
                        pack.die();
            }
        } 
    }

    private void DrawAliensAndMissiles(Graphics g){
        synchronized(aliens) {              // they are drawn only if they have not been previously destroyed.
            for (Alien alien : aliens) {
                if (alien.isVisible()) {
                    g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
                    if (alien.isDying()) 
                        alien.die();
                }
                synchronized(alien){
                    List<Missile> as = alien.getMissiles();
                    for (Missile missile : as) {
                        if (missile.isVisible()) 
                            g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
                    }
                }
            }
        }        
    }

    private void DrawScores(Graphics g){
        g.setColor(Color.WHITE); // In the top-left corner of the window, we draw how many aliens are left.
        int yPos = 15;
        int xPos = 5;
        int offset = 0;
        for(int i = 0; i < spaceShips.size(); i++) {
            SpaceShip ship = spaceShips.get(i);
            if(i != offset)
                yPos += 16;
            
            synchronized(ship){
                g.drawString("Player " + (i+1) + " --- Lives: " + ship.getLife() + "    Score: " + ship.getScore(), xPos, yPos);
            }
            offset = i;
        }
    }

    private void DrawBackground(Graphics g){
        if (bgShiftX > background.getWidth(null)) {
            bgShiftX = 0;
        } else {
            bgShiftX += BG_SPEED;
        }
        g.drawImage(background, (int) -bgShiftX, 0, null);
        g.drawImage(background, background.getWidth(null) - (int) bgShiftX, 0, null);
    }

    protected void DrawInterface(Graphics g) {
        DrawBackground(g);          //stampa sfondo mobile    
        DrawShipAndMissiles(g);     //stampa spaceship e missili per spaceship
        DrawAliensAndMissiles(g);   //stampa alieni e missili da essi sparati 
        DrawScores(g);              //stampa score 
        DrawPacks(g);               //stampa di tutti i pacchetti
    }

//-------------------------END GRAPHICS METHODS---------------------------->
    
//--------------------------LEVEL SWITCHER--------------------------------->
private void InterStage(Graphics g) {
    if(interstage == 0){            //AGGIUNGI ALIENI PER 2 MIN 
        ;//System.out.println("LEV 1 - SCONTRO CON ALIENI"); 
    }

    if(interstage == 1){            //AGGIUNGI ALIENI PER 2 MIN 
        ;//System.out.println("LEV 2 - SCONTRO CON ALIENI"); 
    }

    if(interstage == 2){            //AGGIUNGI ALIENI PER 2 MIN 
        ;//System.out.println("LEV 3 - SCONTRO CON ALIENI"); 
    }

    if(interstage == 3){            
        ;//System.out.println("YOU WIN!");
    }
}

private void Story(int stage){
    if(stage == 0){
        if(interstage == 0 && !lock){
            lock = true;
            aliensGen = new AlienGenerator(background.getWidth(null),background.getHeight(null), aliens, 1);
            aliensGen.start();
            interStage(125,0);
            
        }else if(interstage == 0 && !interstageEnd){
            aliensGen.Shutdown();
            setStage(1);
            lock = false;
            interstageEnd = true;
        } 
    }

    if(stage == 1){
        if(!lock){
            lock = true;
            aliensGen.generateBoss();
        }else if(aliens.isEmpty()){
            setStage(2);
            SetInterStage(1);
            lock = false;
            interstageEnd = true;
        } 
    }

    if(stage == 2){
        if(interstage == 1 && !lock){
            lock = true;
            this.level = 2;
            setBackground();
            aliensGen = new AlienGenerator(background.getWidth(null),background.getHeight(null), aliens, 2);
            aliensGen.start();
            interStage(125,1);
            
        }else if(interstage == 1 && !interstageEnd){
            aliensGen.Shutdown();
            setStage(3);
            lock = false;
            interstageEnd = true;
        } 
    }

    if(stage == 3){
        if(!lock){
            lock = true;
            aliensGen.generateBoss();
        }else if(aliens.isEmpty()){
            setStage(4);
            SetInterStage(2);
            lock = false;
            interstageEnd = true;
        } 
    }

    if(stage == 4){
        if(interstage == 2 && !lock){
            lock = true;
            this.level = 3;
            setBackground();
            aliensGen = new AlienGenerator(background.getWidth(null),background.getHeight(null), aliens, 3);
            aliensGen.start();
            interStage(125,2);
            
        }else if(interstage == 2 && !interstageEnd){
            aliensGen.Shutdown();
            setStage(5);
            lock = false;
            interstageEnd = true;
        } 
    }

    if(stage == 5){
        if(!lock){
            lock = true;
            aliensGen.generateBoss();
        }else if(aliens.isEmpty()){
            setStage(6);
            SetInterStage(5);
            lock = false;
            interstageEnd = true;
        } 
    }

    if(stage == 6){
        if(!lock){
            lock = true;
            gameState = GameStateEnum.GAME_WON;
        }
    }

}

//--------------------------LEVEL SWITCHER END------------------------------->


   
//-----------------------UPDATE VARIABLES------------------------------->
    protected void updateShip() {
        synchronized(spaceShips) {
            for(int k=0; k < spaceShips.size(); k++){
                SpaceShip ship = spaceShips.get(k);
                synchronized(ship){
                    List<Missile> ms = ship.getMissiles();
                    for (int i=0; i < ms.size(); i++) {
                        Missile m = ms.get(i);
                        if (m.isVisible())
                            m.move();
                        else
                            ms.remove(m);
                    }
                }
                
                if (ship.isVisible())
                    ship.move();
                else
                    synchronized(ship){
                        if(ship.getMissiles().isEmpty())
                            spaceShips.remove(ship);
                    }
            }
        }
    }

    protected void updatePacks() {
        synchronized(packs){
            for (int i=0; i < packs.size(); i++) {
                UpgradePack pack = packs.get(i);
                synchronized(pack){
                    if (pack.isVisible())
                        pack.move();
                    else
                        packs.remove(i);
                }
            }
        }
    }

    protected void updateAliens() {
        synchronized(aliens){
            for (int i=0; i < aliens.size(); i++) {
                Alien alien = aliens.get(i);

                synchronized(alien){               
                    List<Missile> ms = alien.getMissiles();
                    for (int j=0; j < ms.size(); j++) {
                        Missile m = ms.get(j);
                        if (m.isVisible())
                            m.move();
                        else
                            ms.remove(m);
                    }

                    if (alien.isVisible()){
                        alien.move(); 
                        }
                    else{
                        synchronized(alien){
                        if(alien.getMissiles().isEmpty())
                            aliens.remove(alien);
                        }
                    }                                
                }
            }
        }
    }

//-----------------------END UPDATE VARIABLES------------------------------->


    public void checkCollisions() {
        Area alienHitbox;
        Area packHitbox;
        Area shipHitbox;
        Area missileHitbox;
        SpaceShip ship;

        for(int k=0; k < spaceShips.size(); k++) {
            ship = spaceShips.get(k);
            shipHitbox = ship.getShape();

            synchronized (packs) {
                for(int i=0; i < packs.size(); i++){        // checking collisions between upbox and spaceship
                    packHitbox = packs.get(i).getShape();
                    packHitbox.intersect(shipHitbox);       // intersection is empty if shapes aren't collided
                    if (!packHitbox.isEmpty() && !packs.get(i).isDying()) {   // isVisible() to avoid multiple check
                        synchronized(ship){
                            packs.get(i).updateSpaceShip(ship);
                        }

                        packs.get(i).setDying(true);

                        if(isMusicOn) {
                            MusicManager mumOne = new MusicManager(powerUpSound);
                            mumOne.startMusic();
                        }
                    }
                }
            }

            synchronized (aliens) {                 // checking collisions between aliens and spaceship
                for (Alien alien : aliens) {
                    alienHitbox = alien.getShape();
                    alienHitbox.intersect(shipHitbox);
                    if (!alienHitbox.isEmpty() && !alien.isDying()) {   // intersection is empty if shapes aren't collided
                        alien.setDying(true);
                        ship.setDying(true);
                        alien.setImage(alienExpl.getImage());
                        ship.setImage(shipExpl.getImage());

                        if(ship.getLife() <= 0){
                            synchronized(ship){
                                ship.setDying(true);
                                ship.setImage(shipExpl.getImage());
                            }
                            
                            if(isMusicOn){
                            
                                MusicManager mumTwo = new MusicManager(bossHitSound);
                                mumTwo.startMusic();

                            }
                            
                        }

                    }

                    synchronized (alien) {          // checking collisions between alien missiles and spaceship
                        List<Missile> alienMissiles = alien.getMissiles();
                        for (Missile missile : alienMissiles) {
                            missileHitbox = missile.getShape();
                            missileHitbox.intersect(shipHitbox);
                            if (!missileHitbox.isEmpty() && missile.isVisible()) {     // intersection is empty if shapes aren't collided
                                ship.setupLife(-1);
                                missile.setVisible(false);

                                if(ship.getLife() <= 0){
                                    synchronized(ship){
                                        ship.setDying(true);
                                        ship.setImage(shipExpl.getImage());
                                    }
                                    if(isMusicOn) {
                                    
                                        MusicManager mumThree = new MusicManager(shipExplSound);
                                        mumThree.startMusic();
                                    }
                                }
                            }

                        }
                    }

                    alienHitbox = alien.getShape();
                    synchronized (ship) {           // checking collisions between spaceship missiles and aliens
                        List<Missile> shipMissiles = ship.getMissiles();
                        for (Missile missile : shipMissiles) {
                            missileHitbox = missile.getShape();
                            missileHitbox.intersect(alienHitbox);

                            if (!missileHitbox.isEmpty() && !alien.isDying()) {     // intersection is empty if shapes aren't collided
                                alien.setupLife(missile.getDamage());
                                
                                if(alien.getLife() > 0){
                                    if(isMusicOn){
                                        
                                        MusicManager mumFour = new MusicManager(bossHitSound);
                                        mumFour.startMusic();
                                    
                                    }
                                }

                                missile.setVisible(false);

                                if(alien.getLife() <= 0){
                                    synchronized(alien){
                                        ship.setupScore(alien.getPoints());
                                        alien.setImage(alienExpl.getImage());
                                        alien.setDying(true);
                                    }
                                    if(isMusicOn) {
                                        MusicManager mumFive = new MusicManager(alienExplSound);
                                        mumFive.startMusic();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Outcome is passed to the panel to draw the right image (game won or game lost)
    public void EndGameFunction(int outcome) {
        if(mumZero != null)
            mumZero.stopMusic();
        JFrame old = (JFrame) SwingUtilities.getWindowAncestor(this);
        old.getContentPane().remove(this);

        int finalScore = 0;
        for(int k = 0; k < spaceShips.size(); k++) {
            SpaceShip ship = spaceShips.get(k);
            finalScore += ship.getScore();
        }

        GameEndPanel gep = new GameEndPanel(outcome, menuPanel, finalScore, isMusicOn);
        old.add(gep).requestFocusInWindow();
        old.validate();
        old.repaint();
        resetBoard();
    }

    public void gameLaunch() {
        if(gameState == GameStateEnum.IN_GAME) {
            boardAnimator = new Thread(this);
            boardAnimator.start();
        }
    }


    class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            try {
                for(int k=0;k<spaceShips.size();k++){
                    SpaceShip Ship = spaceShips.get(k);
                    Ship.keyReleased(e);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();                       
                if (key == KeyEvent.VK_P){        
                    if (isPause == false){
                        System.out.println("METTI LA PAUSA");
                        isPause = true;
                        packsGen.suspend();
                        aliensGen.suspend();
                        pauseGameFunction();
                    }else{
                        System.out.println("TOGLI LA PAUSA");
                        resumeGame();
                    }
                }

             try{
                for(int k=0; k < spaceShips.size(); k++) {
                    SpaceShip ship = spaceShips.get(k);
                    ship.keyPressed(e);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void GameOverCondition(){   //GAME OVER se tutte le space ship sono morte
        Boolean alive = false;
        for(int i = 0; i < spaceShips.size(); i++){
            SpaceShip ship = spaceShips.get(i);   
            if(!ship.isDying())
                alive = true;
        }
        if(alive == false)
            gameState = GameStateEnum.GAME_LOST;
        // set won condition
    }

    public void resumeGame(){         //Alessio funzione da richiamare per far ripartire il gioco
        isPause = false;
        packsGen.resume();
        aliensGen.resume();
        boardAnimator.resume();
    }


    public void pauseGame(){           
        boardAnimator.suspend();
    }
  
    public void pauseGameFunction(){  //Alessio funzione per il passaggio a PausePanel
        JFrame old = (JFrame) SwingUtilities.getWindowAncestor(this);
        old.getContentPane().remove(this);
        PausePanel gep = new PausePanel(istance,menuPanel,mumZero,isMusicOn);   //Alessio ti passo l'istanza di questa board cosi che puoi lavorarci nel PausePanel
        old.add(gep).requestFocusInWindow();           //TI ho commentato il costruttore e cosa si dovrebbere fare
        old.validate();
        old.repaint();
        pauseGame();  
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (gameState != GameStateEnum.GAME_LOST) {
            if(isPause == true){
                pauseGame();
            }
            else{
                Story(stage);
                updateShip();
                updateAliens();
                updatePacks();
                checkCollisions();
                GameOverCondition();
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
    }




    private void interStage(int s, int n){ 
        SetInterStage(n);
        task = new NextStage(this);
        t.schedule(task, s * 1000);
    }

    class NextStage extends TimerTask  {
        Board d;

        public NextStage(Board board) {
            this.d = board;
        }
   
        @Override
        public void run() {
            d.setInterStageEnd(false);
            
        }
    }




}
