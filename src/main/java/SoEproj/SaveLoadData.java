package SoEproj;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;


public class SaveLoadData {
    
    File scoreData = new File("./src/main/java/SoEproj/ScoreDataSaves");
    FileOutputStream out;
    FileInputStream in;

    public SaveLoadData(){}
    
    public void SaveData(ArrayList<ScoreEntry> pd) throws IOException{
        out = new FileOutputStream(scoreData);
        ObjectOutputStream ogg = new ObjectOutputStream(out);
        ogg.writeObject(pd);
        ogg.close();
    }
    
    public ArrayList<ScoreEntry> LoadData() throws IOException, ClassNotFoundException {
        
        ArrayList<ScoreEntry> k;
        in = new FileInputStream(scoreData);
        ObjectInputStream ogg = new ObjectInputStream(in);
        
        k = (ArrayList<ScoreEntry>) ogg.readObject();
        
        ogg.close();
        return k;
        
    }
    
    
    
}
