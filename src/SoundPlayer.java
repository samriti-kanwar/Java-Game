
import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
    public static void play(String path) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + path);
        }
    }
}
