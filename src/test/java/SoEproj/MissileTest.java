package SoEproj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class MissileTest implements CommonValues{

    private Missile m;


    @Before
    public void initAlien() {
        m = new Missile(B_WIDTH, 150, "Laser", "rightToLeft");
    }
    

    @Test
    public void testCoordinates() {
        assertEquals(m.getX(), B_WIDTH);
        assertEquals(m.getY(), 150);
    }


    @Test
    public void testDefaultValue() {
        assertEquals(m.getDamage(), -3);
    }


    @Test
    public void testMove() {
        m.move();
        assertEquals(m.getX(), B_WIDTH-4);
        assertEquals(m.getY(), 150);
    }


    @Test
    public void testIsDying() {
        while(m.getX() >= 0)
            m.move();

        assertFalse(m.isVisible());
    }

}
