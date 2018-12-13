package SoEproj;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SaveLoadData {
    
    private final File scoreData;

    public SaveLoadData(){
        scoreData = new File("./src/main/java/SoEproj/ScoreDataSaves");
    }
    
    public void SaveData(List<ScoreEntry> pd) {
        try {
            FileOutputStream out = new FileOutputStream(scoreData);
            ObjectOutputStream ogg = new ObjectOutputStream(out);
            ogg.writeObject(pd);
            ogg.close();
        } 
        catch (Exception e) {
            System.out.println("SaveData: " + e);
        }
      
    }
    
    public ArrayList<ScoreEntry> LoadData() {
        
        ArrayList<ScoreEntry> scores = new ArrayList<>();
        
        try {
            FileInputStream in = new FileInputStream(scoreData);
            ObjectInputStream ogg = new ObjectInputStream(in);
            scores = (ArrayList<ScoreEntry>) ogg.readObject();
            ogg.close();
        } catch (Exception e) {
            System.out.println("LoadData: " + e);
        }
        
        return scores;
    }
    
}
