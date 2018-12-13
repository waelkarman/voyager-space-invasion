package SoEproj;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/*[09:35, 13/12/2018] Francesco Riva: import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
[09:35, 13/12/2018] Francesco Riva: I nuovi import che ti servono
[09:36, 13/12/2018] Francesco Riva: Clip clip = AudioSystem.getClip();
AudioInputStream inputStream = AudioSystem.getAudioInputStream(laserSound);
clip.open(inputStream);
clip.start();
[09:37, 13/12/2018] Francesco Riva: Il nuovo codice, al quale devi aggiungere: clip.loop(Clip.LOOP_CONTINUOUSLY); se deve andare in loop*/
/**
 *
 * @author aless
 */
public class MusicManager {

    private Clip clip;
    private AudioInputStream inputStream;
        
    public MusicManager(File f) {
        
        try {
            clip = AudioSystem.getClip();
            inputStream = AudioSystem.getAudioInputStream(f);
            clip.open(inputStream);
        } catch (LineUnavailableException ex) {
           
        } catch (UnsupportedAudioFileException ex) {
            
        } catch (IOException ex) {
            
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
