package SoEproj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class FireThreadTest implements CommonValues{

    private FireThread ft;
    private SpaceShip s = new SpaceShip(100, 100, 1, true, 1);


    @Before
    public void initAlien() {
        ft = new FireThread(s);
    }
    

    @Test
    public void testFire() {
        List<Missile> lm = s.getMissiles();
        synchronized(lm){
            s.setFiring(true);
            lm.notifyAll();
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized(lm){
            assertTrue(lm.size() > 0);
        }

        s.setFiring(false);
    }

}