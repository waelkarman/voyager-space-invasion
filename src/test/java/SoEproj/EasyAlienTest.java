package SoEproj;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class EasyAlienTest {

    private EasyAlien alien1;
    private EasyAlien alien2;


    @Before
    public void initList() {
        alien1 = new EasyAlien(100, 300);
        alien2 = new EasyAlien(200, 500);
    }
    

    @Test
    public void checkCoordinates() {
        assertNotEquals(alien1, alien2);
        assertEquals(alien1.getX(), 100);
        assertEquals(alien2.getX(), 200);
        assertEquals(alien1.getY(), 300);
        assertEquals(alien2.getY(), 500);
        assertEquals(alien1.getX(), 100);
        assertEquals(alien2.getX(), 200);
        assertEquals(alien1.getY(), 300);
        assertEquals(alien2.getY(), 500);
        
    }


    @Test
    public void checkDefaultValue() {
        assertNotEquals(alien1, alien2);
    }

}
