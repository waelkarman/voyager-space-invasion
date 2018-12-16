package SoEproj;


import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.ArrayList;


public abstract class Alien extends Sprite {

    protected List<Missile> missiles;
    protected int points;
    protected int life;

    public Alien(int x, int y, int life) {
        super(x, y);
        missiles = new ArrayList<>();
        this.life = life;
    }

    public synchronized List<Missile> getMissiles() {
        return this.missiles;
    }

    public int getPoints() {
        return this.points;
    }

    public synchronized int getLife(){
        return this.life;
    }

    public synchronized void setupLife(int up){
        this.life = this.life + up;
    }
    
    public abstract void move();

    @Override
    public Area getShape(){
        Ellipse2D shape = new Ellipse2D.Double(x, y, width, height);
        return new Area(shape);
    }

}
