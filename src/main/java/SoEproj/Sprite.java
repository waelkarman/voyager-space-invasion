/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.Image;
import javax.swing.ImageIcon;

// The term sprite has several meanings. It is used to denote an image or an 
// animation in a scene.

public abstract class Sprite {

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
        visible = true;
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
        visible = false;
    }

    public void setImage(Image image) {   
        this.image = image;
    }

    // returns the bounding rectangle of the sprite image
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public abstract Area getShape();

}
