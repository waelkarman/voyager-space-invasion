package SoEproj;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class SaveLoadDataTest {

    SaveLoadData data1;
    SaveLoadData data2;
    List<ScoreEntry> list;


    @Before
    public void initList() {
        assertNull(data1);
        assertNull(data2);
        data1 = new SaveLoadData();
        data2 = new SaveLoadData();
        ScoreEntry entry1 = new ScoreEntry("Player1", 10);
        ScoreEntry entry2 = new ScoreEntry("Player2", 15);
        ScoreEntry entry3 = new ScoreEntry("Player3", 20);
        list = Arrays.asList(entry1, entry2, entry3);
    }


    @Test
    public void SaveDateTest() {
        assertNotEquals(data1, data2);
        data1.SaveData(list);
        data2.SaveData(list);
        assertEquals(data1.LoadData(), data2.LoadData());
    }


    @Test
    public void LoadDataTest() {
        assertEquals(data1.LoadData(), data2.LoadData());
    }

}
