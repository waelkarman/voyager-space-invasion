package SoEproj;


import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class MusicManager {

    private Clip clip;
    private AudioInputStream inputStream;
        
    public MusicManager(File f) {        
        try {
            clip = AudioSystem.getClip();
            inputStream = AudioSystem.getAudioInputStream(f);
            clip.open(inputStream);
        } catch (Exception e) {
            System.out.println("MusicManager open file Exception: " + e);
        }
    }
    
    public void startMusic(){
        clip.start();
    }
    
    public void stopMusic(){
        clip.stop();
    }
    
    public void loopMusic(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public AudioInputStream getStream(){
        return inputStream;
    }
    
    
}
