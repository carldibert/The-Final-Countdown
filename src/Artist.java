import java.util.ArrayList;

public class Artist {
	private String artistName;
	private ArrayList<Album> albumList;
	
	public Artist(String artist){
		artistName = artist;
		albumList = new ArrayList<Album>();
	}
	
	public void addAlbum(Album album){
		albumList.add(album);
	}
	
	public ArrayList<Album> getAlbumList(){
		return albumList;
	}
	
	public String getArtistName(){
		return artistName;
	}

}
