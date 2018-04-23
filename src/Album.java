import java.io.File;
import java.util.ArrayList;

public class Album {
	private String title;
	private ArrayList<File> songList;
	
	public Album(String name){
		title = name;
		songList = new ArrayList<File>();
	}
	
	public String getAlbumTitle(){
		return title;
	}
	
	public ArrayList<File> getSongList(){
		return songList;
	}
	
	public void addSong(File song){
		songList.add(song);
	}

}
