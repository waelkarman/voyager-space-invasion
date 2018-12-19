package SoEproj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class SpaceShipTest implements CommonValues{

    private SpaceShip s;


    @Before
    public void initAlien() {
        s = new SpaceShip(1, 150, 1, true, 1);
    }
    

    @Test
    public void testCoordinates() {
        assertEquals(s.getX(), 1);
        assertEquals(s.getY(), 150);
    }


    @Test
    public void testDefaultValue() {
        assertEquals(s.getLife(), 1);
        assertEquals(s.getScore(), 0);
        assertNotNull(s.getMissiles());
        assertNotNull(s.getSPACE());
        assertNotNull(s.getShape());
        assertFalse(s.getFiring());
        assertTrue(s.isVisible());
    }


    @Test
    public void testMove() {
        s.move();
        assertEquals(s.getX(), 1);
        assertEquals(s.getY(), 150);
    }


    @Test
    public void testDying() {
        s.setDying(true);
        assertTrue(s.isDying());
        s.setVisible(false);
        assertFalse(s.isVisible());
    }

}
