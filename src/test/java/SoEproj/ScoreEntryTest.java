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
        ScoreEntry one = new ScoreEntry("player", 500);
        ScoreEntry two = new ScoreEntry("player", 500);
        assertNotEquals(one, two);
    }

    @Test
    public void getScoreTest()
    {
        ScoreEntry one = new ScoreEntry("player", 500);
        ScoreEntry two = new ScoreEntry("player", 500);
        assertEquals(one.getScore(), two.getScore());
    }
   
    @Test
    public void getNameTest()
    {
        ScoreEntry one = new ScoreEntry("player", 500);
        ScoreEntry two = new ScoreEntry("player", 500);
        assertEquals(one.getName(), two.getName());
    }


    @Test
    public void compareToTest()
    {
        ScoreEntry one = new ScoreEntry("player", 500);
        ScoreEntry two = new ScoreEntry("player", 400);
        assertEquals(one.compareTo(two), 0);
    }

}
