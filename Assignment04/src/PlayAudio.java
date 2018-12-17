import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class PlayAudio {
    AudioInputStream audioInputStream;
    Clip clip;
    Boolean isPlaying = false;

    //Function that plays the song when the start button is pressed.
    //  The song choice was picked because I was scrolling through a
    //  list of free songs when "RunTimeError" was an actual song title.
    //Arguments: None
    //Returns: Sweet music for your entertainment
    public void play() {
        if(isPlaying == false) {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File("PeterSharpRuntimeError.wav").getAbsoluteFile());   //Open the song
                clip = AudioSystem.getClip();   //save to the clip
                clip.open(audioInputStream);    //open the song
                clip.loop(clip.LOOP_CONTINUOUSLY);  //star the song
                isPlaying = true;   //set to true
            } catch (Exception ex) {
                System.out.println("Error with playing sound.");
                ex.printStackTrace();
            }
        }
    }

    //Function that stops the song when the stop buttons is pressed
    //Argument: None
    //Returns: None
    public void stop()
    {
        clip.stop();    //Stop the song
        isPlaying = false;
    }


}
