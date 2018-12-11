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
public class ScoreEntryTest 
{

    @Test
    public void ScoreEntryTest()
    {
        ScoreEntry one = new ScoreEntry("player",500);
        ScoreEntry two = new ScoreEntry("player",500);
        assertNotEquals(one,two);
    }

    @Test
    public void getPunteggioTest()
    {
        ScoreEntry one = new ScoreEntry("player",500);
        ScoreEntry two = new ScoreEntry("player",500);
        assertEquals(one.getPunteggio(), two.getPunteggio());
    }
   
    @Test
    public void getNomeTest()
    {
        ScoreEntry one = new ScoreEntry("player",500);
        ScoreEntry two = new ScoreEntry("player",500);
        assertEquals(one.getNome(), two.getNome());
    }


    @Test
    public void compareToTest()
    {
        ScoreEntry one = new ScoreEntry("player",500);
        ScoreEntry two = new ScoreEntry("player",400);
        assertEquals(one.compareTo(two),1);
    }

}
