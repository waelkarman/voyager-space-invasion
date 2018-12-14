package SoEproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.hamcrest.core.Is;
import org.junit.Test;


public class SaveLoadDataTest 
{

    @Test
    public void SaveLoadDataTest()
    {
        SaveLoadData one = new SaveLoadData();
        SaveLoadData two = new SaveLoadData();
        assertNotEquals(one,two);
    }

    @Test(expected = IOException.class)
    public void SaveDateTest() throws IOException
    {
        SaveLoadData one = new SaveLoadData();
        ArrayList anArray = new ArrayList<>();
        one.SaveData(anArray);
    }
   
    @Test(expected = Exception.class)
    public void LoadDataTest() throws IOException, ClassNotFoundException
    {
        SaveLoadData one = new SaveLoadData();
        ArrayList anArray = new ArrayList<>();
        ArrayList sameArray;
        one.SaveData(anArray);
        sameArray = one.LoadData();
        assertEquals(anArray, sameArray);
    }

}
