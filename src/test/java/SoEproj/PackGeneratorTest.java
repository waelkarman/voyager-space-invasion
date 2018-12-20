package SoEproj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class PackGeneratorTest implements CommonValues{

    private PackGenerator pg;
    private List<UpgradePack> packs;


    @Before
    public void initAlien() {
        packs = new ArrayList<>();
        pg = new PackGenerator(B_WIDTH, B_HEIGHT, packs);
        pg.start();
    }
    

    @Test
    public void testFire() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(packs){
            assertTrue(packs.size() > 0);
        }
    }

}
