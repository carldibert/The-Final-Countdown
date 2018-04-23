import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class Playlist extends JPanel{

	@SuppressWarnings("rawtypes")
	private JList playlist;
	private ArrayList<String> files;
	private ArrayList<String> display;
	private int currentlyPlaying;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Playlist(){
		//Initialized the library
		files = new ArrayList<String>();
		display = new ArrayList<String>();
		playlist = new JList(display.toArray());
		JScrollPane playlistScrollPane = new JScrollPane(playlist);
				
		//sets up the default size for the library portion of the program
		playlistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		playlistScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		playlist.setBorder(BorderFactory.createTitledBorder("Playlist"));
		playlist.setVisibleRowCount(30);
		playlist.setFixedCellWidth(400);
		playlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		//adds to the program
		this.add(playlistScrollPane);
	}
	
	//adds files to the playlist section of the program
	@SuppressWarnings("unchecked")
	public void addToPlaylist(String song){
		//adds the files to an ArrayList of the file locations
		files.add(song);
		
		//makes a File location from the song String and adds the direct name of it to a separate
		//ArrayList used for displaying on the playlist section
		File fileName = new File(song);
		display.add(fileName.getName());
		
		//sets the JList to display just the files
		playlist.setListData(display.toArray());
	}
	
	//getter for the JList
	@SuppressWarnings("rawtypes")
	public JList getPlaylist(){
		return playlist;
	}
	
	//keeps track of the currently playing index for playback purposes
	public void setCurrentlyPlaying(int n){
		currentlyPlaying = n;
	}
	
	//gets the currently playing song
	public String getCurrentlyPlayingSongTitle(){
		return display.get(currentlyPlaying);
	}
	
	public int playlistSize(){
		return files.size();
	}
	
	public int getCurrentlyPlaying(){
		return currentlyPlaying;
	}
	
	//gets the file location for playback purposes
	public String getFileLocationOfSelection(int n){
		String fileLocation = files.get(n);
		return fileLocation;
	}

}

