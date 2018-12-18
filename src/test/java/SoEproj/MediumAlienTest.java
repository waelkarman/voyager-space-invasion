package SoEproj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class MediumAlienTest implements CommonsValues{

    private MediumAlien alien;


    @Before
    public void initAlien() {
        alien = new MediumAlien(B_WIDTH, 150);
    }
    

    @Test
    public void checkCoordinates() {
        assertEquals(alien.getX(), B_WIDTH);
        assertEquals(alien.getY(), 150);
    }


    @Test
    public void checkDefaultValue() {
        assertEquals(alien.getLife(), 2);
        assertEquals(alien.getPoints(), 75);
        assertNotNull(alien.getMissiles());
        assertNotNull(alien.getSPACE());
        assertNotNull(alien.getShape());
    }


    @Test
    public void checkMove() {
        alien.move();
        assertEquals(alien.getX(), B_WIDTH-2);
        assertEquals(alien.getY(), 150);
    }


    @Test(timeout = 5000)
    public void checkIsDying() {
        while(alien.getX() >= 0)
            alien.move();

        assertTrue(alien.isDying());
    }


    @Test
    public void checkFire() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(alien.getMissiles()){
            assertTrue(alien.getMissiles().size() > 0);
        }
    }

}
