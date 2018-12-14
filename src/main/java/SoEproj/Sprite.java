/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.geom.Area;
import java.awt.Image;
import javax.swing.ImageIcon;

// The term sprite has several meanings. It is used to denote an image or an 
// animation in a scene.

public abstract class Sprite {

    protected final int B_WIDTH = 590;
    protected final int B_HEIGHT = 435;
    protected final int B_SCORE_SPACE = 30; // space that is occupied by player's score
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean visible;
    protected boolean dying; 
    protected Image image;
    protected float SPACE;
 

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
    }

    public float getSPACE() {
        return this.SPACE;
    }

    public void setSPACE(float SPACE) {
        this.SPACE = SPACE;
    }

    protected void getImageDimensions() {
        width = image.getWidth(null);
        height = image.getHeight(null);

        if(y + height > B_HEIGHT)
            y = B_HEIGHT - height;
    }

    protected void loadImage(String imageName) {
        ImageIcon ii = new ImageIcon(imageName);
        image = ii.getImage();
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public synchronized boolean isDying() {
        return dying;
    }

    public synchronized boolean isVisible() {
        return visible;
    }

    public synchronized void setVisible(boolean visible) {
        this.visible = visible;
    }

    public synchronized void setDying(boolean dying) {
        this.dying = dying;
    }

    public synchronized void die() {    
        this.visible = false;
    }

    public void setImage(Image image) {   
        this.image = image;
    }

    public abstract Area getShape();

}
