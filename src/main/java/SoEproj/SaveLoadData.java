
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wael Karman
 */
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
        System.out.println("Sono dentro LoadData");
        ArrayList<ScoreEntry> k;
        in = new FileInputStream(scoreData);
        ObjectInputStream ogg = new ObjectInputStream(in);
        System.out.println("Sono prima del cast");
        k = (ArrayList<ScoreEntry>) ogg.readObject();
        System.out.println("Sono dopo il cast");
        ogg.close();
        return k;
        
    }
    
    
    
}
