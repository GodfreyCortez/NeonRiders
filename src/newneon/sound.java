package newneon;
//SINCE MP3S ARE NOT NATIVELY SUPPORTED IN JAVA WE DOWNLOADED API FROM http://www.javazoom.net/jlgui/api.html TO PLAY MP3s
import java.io.*; 
import javazoom.jl.decoder.JavaLayerException;
import java.util.*; 
import javazoom.jl.player.advanced.AdvancedPlayer; 
import javazoom.jl.player.advanced.PlaybackEvent; 
import javazoom.jl.player.advanced.PlaybackListener; 
//This class will be responsible for handling all sound
public class sound 
{
    public int num = -1; 
    private int paused_On = 0; 
    private Random rand = new Random(); 
    File[] music = {new File("assets/music/Solar Sailor.mp3"), new File ("assets/music/CLU.mp3"),
                    new File ("assets/music/Derezzed.mp3"), new File ("assets/music/The Grid.mp3")}; 
                    /*THIS MUSIC IS COPYRIGHTED AND THEREFORE DOES NOT BELONG TO ME. THE MUSIC IS SOLELY FOR DEMONSTRATION AND THE GAME
                     * IS NOT FOR SALE!
                     */
    public AdvancedPlayer player = null;
        public void playsong()
        {
            try
            {
                while(true)
                {       
                        num = rand.nextInt(music.length); //find a random track to play
                            if (num > music.length) //if at any point the num is out of bounds default it to 0
                                num = 0;
                        FileInputStream fis = new FileInputStream (music[num]); //open up a fileinput stream with the music in it
                        BufferedInputStream bis = new BufferedInputStream (fis); //create a bufferedinput stream of the music file

                        try
                        {
                               
                            player = new AdvancedPlayer (bis); 
                            player.setPlayBackListener(new PlaybackListener() 
                               {
                                    @Override
                                    public void playbackFinished(PlaybackEvent event)
                                    {
                                         paused_On = event.getFrame();
                                    }
                               });
                               player.play(); 
                        }
                        catch (JavaLayerException ex){}
                }  
            }
            catch (Exception e){} //do nothing if an error occurs
    }
}
