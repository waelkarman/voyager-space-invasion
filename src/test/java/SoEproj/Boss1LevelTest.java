package SoEproj;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class Boss1LevelTest implements CommonValues{

    private Boss1Level boss;
    private List<Alien> aliens;


    @Before
    public void initAlien() {
        boss = new Boss1Level(B_WIDTH, 150, aliens);
    }
    

    @Test
    public void testCoordinates() {
        assertEquals(boss.getX(), B_WIDTH);
        assertEquals(boss.getY(), 150);
    }


    @Test
    public void testDefaultValue() {
        assertEquals(boss.getLife(), 2);
        assertEquals(boss.getPoints(), 100);
        assertNotNull(boss.getMissiles());
        assertNotNull(boss.getSPACE());
        assertNotNull(boss.getShape());
        assertTrue(boss.isVisible());
        assertFalse(boss.isDying());
    }


    @Test
    public void testMove() {
        boss.move();
        assertEquals(boss.getX(), B_WIDTH - 3/2);
        assertNotEquals(boss.getY(), 150);
    }


    @Test(timeout = 5000)
    public void testIsDying() {
        while(boss.getX() >= 0)
            boss.move();

        assertTrue(boss.isDying());
    }


    @Test
    public void testFire() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(boss.getMissiles()){
            assertTrue(boss.getMissiles().size() > 0);
        }
    }

}
