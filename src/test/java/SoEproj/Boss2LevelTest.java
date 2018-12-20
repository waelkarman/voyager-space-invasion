package SoEproj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class Boss2LevelTest implements CommonValues{

    private Boss2Level boss;
    private List<Alien> aliens;


    @Before
    public void initAlien() {
        aliens = new ArrayList<Alien>();
        boss = new Boss2Level(B_WIDTH, 150, aliens);
    }
    

    @Test
    public void testCoordinates() {
        assertEquals(boss.getX(), B_WIDTH);
        assertEquals(boss.getY(), 150);
    }


    @Test
    public void testDefaultValue() {
        assertEquals(boss.getLife(), 100);
        assertEquals(boss.getPoints(), 800);
        assertNotNull(boss.getMissiles());
        assertNotNull(boss.getSPACE());
        assertNotNull(boss.getShape());
        assertTrue(boss.isVisible());
        assertFalse(boss.isDying());
    }


    @Test
    public void testMove() {
        while (boss.getX() > B_WIDTH - 50) {
            boss.move();
        }
        assertEquals(boss.getX(), B_WIDTH - 50);
        assertEquals(boss.getY(), 150);
        
        boss.move();

        assertNotEquals(boss.getY(), 150);
    }


    @Test
    public void testIsDying() {
        
        assertFalse(boss.isDying());

        boss.setDying(true);
        assertTrue(boss.isDying());
    }


    @Test
    public void testFire() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(boss.getMissiles()){
            assertTrue(boss.getMissiles().size() > 0);
        }
    }
}
