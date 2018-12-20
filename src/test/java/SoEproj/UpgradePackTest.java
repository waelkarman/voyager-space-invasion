package SoEproj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class UpgradePackTest implements CommonValues{

    private UpgradePack p;


    @Before
    public void initAlien() {
        p = new UpgradePack(B_WIDTH, 150, 1);
    }
    

    @Test
    public void testCoordinates() {
        assertEquals(p.getX(), B_WIDTH);
        assertEquals(p.getY(), 150);
    }


    @Test
    public void testType() {
        assertEquals(p.getType(), 1);
    }


    @Test
    public void testMove() {
        p.move();
        assertEquals(p.getX(), B_WIDTH-1);
        assertEquals(p.getY(), 150);
    }


    @Test
    public void testIsDying() {
        while(p.getX() >= 0)
            p.move();

        assertFalse(p.isVisible());
    }


    @Test(timeout = 15000)
    public void testResetAmmo(){
        SpaceShip s = new SpaceShip(0, 0, 1, false, 1);

        p.updateSpaceShip(s);
        assertEquals(s.getMissileType(), "fireBall");

        try {
            Thread.sleep(11 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(s.getMissileType(), "Laser");
    }


    @Test(timeout = 15000)
    public void testResetSpeed(){
        SpaceShip s = new SpaceShip(0, 0, 1, false, 1);

        p.setType(4);
        p.updateSpaceShip(s);
        assertTrue(s.getSPACE() == (3/2 + 3/2));

        try {
            Thread.sleep(11 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(s.getSPACE() == 3/2);
    }

}
