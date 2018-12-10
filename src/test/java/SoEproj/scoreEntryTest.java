package SoEproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class scoreEntryTest 
{

    @Test
    public void scoreEntryTest()
    {
        scoreEntry one = new scoreEntry("player",500);
        scoreEntry two = new scoreEntry("player",500);
        assertNotEquals(one,two);
    }

    @Test
    public void getPunteggioTest()
    {
        scoreEntry one = new scoreEntry("player",500);
        scoreEntry two = new scoreEntry("player",500);
        assertEquals(one.getPunteggio(), two.getPunteggio());
    }
   
    @Test
    public void getNomeTest()
    {
        scoreEntry one = new scoreEntry("player",500);
        scoreEntry two = new scoreEntry("player",500);
        assertEquals(one.getNome(), two.getNome());
    }


    @Test
    public void compareToTest()
    {
        scoreEntry one = new scoreEntry("player",500);
        scoreEntry two = new scoreEntry("player",400);
        assertEquals(one.compareTo(two),1);
    }

}
