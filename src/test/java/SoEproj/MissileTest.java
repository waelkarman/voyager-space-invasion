package SoEproj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class MissileTest implements CommonsValues{

    private Missile m;


    @Before
    public void initAlien() {
        m = new Missile(B_WIDTH, 150, "Laser", "rightToLeft");
    }
    

    @Test
    public void checkCoordinates() {
        assertEquals(m.getX(), B_WIDTH);
        assertEquals(m.getY(), 150);
    }


    @Test
    public void checkDefaultValue() {
        assertEquals(m.getDamage(), -3);
    }


    @Test
    public void checkMove() {
        m.move();
        assertEquals(m.getX(), B_WIDTH-4);
        assertEquals(m.getY(), 150);
    }


    @Test
    public void checkIsDying() {
        while(m.getX() >= 0)
            m.move();

        assertFalse(m.isVisible());
    }

}
