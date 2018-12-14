package SoEproj;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Test;


public class SaveLoadDataTest {
    SaveLoadData data1;
    SaveLoadData data2;

    @Test
    public void SaveLoadDataTest() {
        data1 = new SaveLoadData();
        data2 = new SaveLoadData();
        assertNotEquals(data1,data2);
    }

    @Test
    public void SaveDateTest() {
        ScoreEntry entry1 = new ScoreEntry("Player1", 10);
        ScoreEntry entry2 = new ScoreEntry("Player2", 15);
        ScoreEntry entry3 = new ScoreEntry("Player3", 20);
        List<ScoreEntry> list = Arrays.asList(entry1, entry2, entry3);
        data1.SaveData(list);
        data2.SaveData(list);
        assertEquals(data1.LoadData(), data2.LoadData());
    }
   
    @Test
    public void LoadDataTest() {
        SaveLoadData one = new SaveLoadData();
        ArrayList anArray = new ArrayList<>();
        ArrayList sameArray;
        one.SaveData(anArray);
        sameArray = one.LoadData();
        assertEquals(anArray, sameArray);
    }

}
