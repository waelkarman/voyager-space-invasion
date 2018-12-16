package SoEproj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//TODO pulizia codice
//TODO risolvere bug proiettili sparati e tipo
//TODO correggere path windows in linux
public class PlayOrganizzation extends Board implements Runnable {

    private Boolean posizionato;
    private int stage;
    private Timer t;
    private TimerTask task;
    private int interstage;
    private boolean interstageEnd;
    private boolean lock;

    public PlayOrganizzation(int shipType, JPanel p, boolean m, int level, int km, boolean mp){
        super(shipType, p, m, level, km, mp);       
        posizionato = false;
        stage = 0;
        interstage = 0;
        interstageEnd = true;
        t = new Timer();
        lock = false;
    }
    

    @Override
    public void initGame(int shipType) {
        gameState = GameStateEnum.IN_GAME;
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        spaceShips = new ArrayList<SpaceShip>();
        spaceShips.add( new SpaceShip(0, B_HEIGHT/2, shipType, isMusicOn, keyModality) );
        if(isMultiplayer)
            spaceShips.add( new SpaceShip(0, B_HEIGHT/2 + 60, shipType + 1 % 3 , isMusicOn, keyModality + 1 % 2) ); // +1 % 2 for set a different type

        packs = new LinkedList<UpgradePack>();
        packsGen = new PackGenerator(background.getWidth(null), packs);
        //threadPacksGen = new Thread(packsGen);
//
        aliens = new ArrayList<Alien>();
        aliensGen = new AlienGenerator(background.getWidth(null), aliens, this.level);
        threadAliensGen = new Thread(aliensGen);
//
        //threadAliensGen.start();
        threadPacksGen.start();
    }




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

    private void DrawInterface(Graphics g) {
            DrawShipAndMissiles(g);     //stampa spaceship e missili per spaceship
            DrawPacks(g);               //stampa di tutti i pacchetti
            DrawAliensAndMissiles(g);   //stampa alieni e missili da essi sparati 
            DrawScores(g);              //stampa score 
            DrawBackground(g);          //stampa sfondo mobile
    }

//-------------------------END GRAPHICS METHODS---------------------------->
    
//--------------------------LEVEL SWITCHER--------------------------------->
    private void InterStage(Graphics g) {
        if(interstage == 0){            //AGGIUNGI ALIENI PER 2 MIN 
            System.out.println("wa");
            threadAliensGen.start();
        }
        if(interstage == 1){
            //
        }

    }

    private void Story(int stage){
        
        if(stage == 0){
            if(interstage == 0 && !lock){
                lock = true;
                interStage(128,0);
            }else if(interstage == 0 && !interstageEnd){
                //aliensGen.shutdown();
                setStage(1);
                lock = false;
                interstageEnd = true;
            } 
        }

        if(stage == 1){
            aliensGen.generateBoss();
        }//set interstage to 1

        if(stage == 2){
            if(interstage == 1 && !lock){
                lock = true;
                interStage(120,1);
            }else if(interstage == 1 && !interstageEnd){
                setStage(3);
                lock = false;
                interstageEnd = true;
            } 
        }

        if(stage == 3){
            //boss 2
        }//set interstage to 2

        if(stage == 4){
            if(interstage == 2 && !lock){
                lock = true;
                interStage(4,2);
            }else if(interstage == 2 && !interstageEnd){
                SetInterStage(3);
                lock = false;
                interstageEnd = true;
            } 

        }

        

    }

//--------------------------LEVEL SWITCHER END------------------------------->
   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // TODO Rendere gameState un enum
        if(gameState == GameStateEnum.IN_GAME) {        // draw background and game elements
            DrawInterface(g);
        }

        Toolkit.getDefaultToolkit().sync();
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
    }


    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (gameState != GameStateEnum.GAME_LOST) {
            
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


    
    private void interStage(int s, int n){ 
        SetInterStage(n);
        task = new NextStage(this);
        t.schedule(task, s * 1000);
    }

    class NextStage extends TimerTask  {
        PlayOrganizzation d;

        public NextStage(PlayOrganizzation playOrganizzation) {
            this.d = playOrganizzation;
        }
   
        @Override
        public void run() {
            d.setInterStageEnd(false);
            
        }
    }


}


