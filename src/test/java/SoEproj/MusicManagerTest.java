package SoEproj;

import static org.junit.Assert.*;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.junit.Before;
import org.junit.Test;

public class MusicManagerTest {
    MusicManager muM1, muM2, muM3;
    File f1 = new File("./src/main/java/SoEproj/Resource/PowerUp.wav");
    File f2 = new File("./src/main/java/SoEproj/Resource/FinalCollisionSound.wav");

    @Before
    public void initScores() {
        muM1 = new MusicManager(f1);
        muM2 = new MusicManager(f2);
    }

    @Test
    public void testClipOpening() {
        assertNotNull(muM1);
    }

    @Test
    public void testStartMusic() {
        muM2.startMusic();

        assertNotNull(muM2);
    }

    @Test
    public void testStopMusic() {
        muM2.stopMusic();

        assertNotNull(muM2);
    }

    @Test
    public void testGetStream() {
        AudioInputStream inputStream;
        try {
            inputStream = AudioSystem.getAudioInputStream(f2);
            assertEquals(inputStream, muM2.getStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
