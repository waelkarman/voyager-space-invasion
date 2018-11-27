package SoEproj;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public abstract class Alien extends Sprite {

    protected List<Missile> missiles;
    protected final int INITIAL_X = 600;
    protected int life;

    public Alien(int x, int y, int life) {
        super(x, y);
        this.life = life;
        missiles = new ArrayList<>();
    }

    
    public int getINITIAL_X() {
        return this.INITIAL_X;
    }

    public int getLife(){
        return this.life;
    }

    public void setLife(int up){
        this.life = this.life + up;
    }
    
    public abstract void move();

    @Override
    public Area getShape(){
        Ellipse2D shape = new Ellipse2D.Double(x, y, width, height);
        return new Area(shape);
    }

  
    public List<Missile> getMissiles() {
        return missiles;
    }
}
