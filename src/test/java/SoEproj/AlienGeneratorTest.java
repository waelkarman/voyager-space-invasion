package SoEproj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class AlienGeneratorTest implements CommonValues{

    private AlienGenerator ag;
    private List<Alien> aliens;


    @Before
    public void initAlien() {
        aliens = new ArrayList<>();
        ag = new AlienGenerator(B_WIDTH, B_HEIGHT, aliens, 2);
        ag.start();
    }
    

    @Test
    public void testFire() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(aliens){
            assertTrue(aliens.size() > 0);
        }
    }

}