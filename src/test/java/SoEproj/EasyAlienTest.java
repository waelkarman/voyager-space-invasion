package SoEproj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class EasyAlienTest implements CommonValues{

    private EasyAlien alien1;
    private EasyAlien alienBossHelper;


    @Before
    public void initAlien() {
        alien1 = new EasyAlien(100, 150);
        alienBossHelper = new EasyAlien(B_WIDTH, 300, "BossHelper", true);
    }
    

    @Test
    public void checkCoordinates() {
        assertNotEquals(alien1, alienBossHelper);
        assertEquals(alien1.getX(), 100);
        assertEquals(alien1.getY(), 150);
        assertEquals(alienBossHelper.getX(), B_WIDTH);
        assertEquals(alienBossHelper.getY(), 300);
    }


    @Test
    public void checkDefaultValue() {
        assertNotEquals(alien1, alienBossHelper);
        assertEquals(alien1.getLife(), 1);
        assertEquals(alien1.getPoints(), 50);
        assertNotNull(alien1.getMissiles());
        assertNotNull(alien1.getSPACE());
        assertNotNull(alien1.getShape());
        assertEquals(alienBossHelper.getLife(), 1);
        assertEquals(alienBossHelper.getPoints(), 50);
        assertNotNull(alienBossHelper.getMissiles());
        assertNotNull(alienBossHelper.getSPACE());
        assertNotNull(alienBossHelper.getShape());
    }


    @Test
    public void checkMove() {
        assertNotEquals(alien1, alienBossHelper);
        alien1.move();
        alienBossHelper.move();
        assertEquals(alien1.getX(), 99);
        assertEquals(alien1.getY(), 150);
        assertEquals(alienBossHelper.getX(), B_WIDTH - 1);
        assertEquals(alienBossHelper.getY(), 301);
    }


    @Test(timeout = 5000)
    public void checkIsDying() {
        while(alien1.getX() >= 0)
            alien1.move();

        assertTrue(alien1.isDying());
    }

}
