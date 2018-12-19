package SoEproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class ScoreEntryTest {
    ScoreEntry score1, score2, score3, score4;

    @Before
    public void initScores() {
        score1 = new ScoreEntry("player", 500);
        score2 = new ScoreEntry("player", 350);
        score3 = new ScoreEntry("player1", 350);
        score4 = new ScoreEntry("player", 500);
    }
    
    @Test
    public void testGetName() {
        assertEquals(score1.getName(), score2.getName());
    }

    @Test
    public void testGetScore() {
        assertEquals(score2.getScore(), score3.getScore());
    }

    @Test
    public void testToString() {
        assertEquals(score1.toString(), score4.toString());
    }

    @Test
    public void testCompareTo() {
        assertNotEquals(score2.compareTo(score4), 0);
    }
}
