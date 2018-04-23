import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.PlaybackListener;;

public class MusicPlayer implements Runnable{
	Player player;
	PlaybackListener listener;
	File song;
	Timer timer;
	
	//super constructor
	public MusicPlayer(){
		super();
	}
	
	public void setSong(File song){
		this.song = song;
	}
	
	//plays the song from the input file
	public void playSong(File song){
		try{
			File file = new File(song.getAbsolutePath());
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			try{
				player = new Player(bis);
				player.play();
				listener.playbackStarted(null);
			} catch(JavaLayerException ex){
				
			}
		}
		catch(IOException e){
		}
		
	}
	
	//checks to see if the song has finished playback
	public boolean checkIfFinished(){
		return player.isComplete();
	}
	
	//plays the current player
	public void playCurrentlyPlayer(){
		try {
			player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	//runs the class on another thread for multithreaded purposes
	@Override
	public void run() {
		playSong(song);
	}
	
}
