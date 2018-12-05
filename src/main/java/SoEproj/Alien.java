package SoEproj;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;


public abstract class Alien extends Sprite {

    protected List<Missile> missiles;
    protected final int INITIAL_X = 600;
    protected int life;
    protected int points;


    public Alien(int x, int y, int life) {
        super(x, y);
        this.life = life;
        missiles = new ArrayList<>();
    }

    public synchronized List<Missile> getMissiles() {
        return this.missiles;
    }

    public int getPoints() {
        return this.points;
    }
    
    public int getINITIAL_X() {
        return this.INITIAL_X;
    }

    public int getLife(){
        return this.life;
    }

    public void setupLife(int up){
        this.life = this.life + up;
    }
    
    public abstract void move();

    @Override
    public Area getShape(){
        Ellipse2D shape = new Ellipse2D.Double(x, y, width, height);
        return new Area(shape);
    }

}
