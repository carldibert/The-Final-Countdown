import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Driver extends JFrame{

	/**
	 * Initial version created 4/2/2017
	 */
	
	private MenuBar menuBar;
	private LibraryList libraryList;
	private Playlist playlist;
	private Controller controller;
	private MusicPlayer player;
	private Thread musicPlayerThread;
	private Timer timer;
	private AddFiles newerFiles;
	
	public Driver(){
		
		//base info for the program and will close when the X button is pressed
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Bloatware Free Music Player");
		
		//items to be added to the window
		this.menuBar = new MenuBar();
		this.libraryList = new LibraryList();
		this.playlist = new Playlist();
		this.controller = new Controller();
		
		//sets menu bar to the program
		this.setJMenuBar(menuBar);
		this.add(controller, "North");
		this.add(libraryList, "West");
		this.add(playlist, "East");
		
		//sizing the window
		pack();
		
		//program will start out at half the size of the display by default
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		
		//allows the window to be seen and resized
		setVisible(true);
		setResizable(true);
		
		//listeners for the menu bar
		this.menuBar.getExit().addActionListener(new ExitListener());
		this.menuBar.getLibraryLocation().addActionListener(new LibraryLocationListener());
		this.menuBar.getReloadLibrary().addActionListener(new ReloadLibraryListener());
		this.menuBar.getAbout().addActionListener(new AboutListener());
		this.menuBar.getAddFilesToLibrary().addActionListener(new AddFilesToLibraryListener());
		
		//listeners for the Library List
		this.libraryList.getLibrary().addMouseListener(new LibraryMouseListener());
		
		//listeners for the Playlist
		this.playlist.getPlaylist().addMouseListener(new PlaylistMouseListener());
		
		//listeners for the control buttons
		this.controller.getStop().addActionListener(new StopButtonListener());
		this.controller.getRewind().addActionListener(new RewindActionListener());
		this.controller.getPause().addActionListener(new PauseActionListener());
		this.controller.getPlay().addActionListener(new PlayActionListener());
		this.controller.getFastforward().addActionListener(new FastforwardActionListner());
		
		//calls for the program to start the initial loading
		//currently only loading the library
		loadOnStart();
		
	}
	
	//starts the driver class
	public static void main(String[] args){
		new Driver();
	}
	
	private void loadOnStart(){
		//loads up library and populates the library
		Scanner iner;
		try {
			iner = new Scanner(new FileReader("settings.cfg"));
			String tempString = iner.nextLine().toString();
			File fileName = new File(tempString);
			
			populateLibrary(fileName);
		} catch (FileNotFoundException e) {
			//outputs error if there is an issue loading up the library on start
			System.out.println("Error in loading library on start");
			e.printStackTrace();
		}
		
		//enables the play button to be pressed after the library loads
		controller.getPlay().setEnabled(false);
		controller.getStop().setEnabled(false);
		controller.getRewind().setEnabled(false);
		controller.getFastforward().setEnabled(false);
		
		//starts the second thread for the music player
		musicPlayerThread = new Thread();
		
	}
	
	//populates the library
	//recursively searches through files and their two sub folders to create the library
	//files stores artist/album/song
	private void populateLibrary(File file){
		File[] listOfFile = file.listFiles();
		for(int i=0; i<listOfFile.length; i++){
			if(listOfFile[i].isFile()){
				File album = listOfFile[i].getAbsoluteFile().getParentFile();
				File artist = album.getAbsoluteFile().getParentFile();
				libraryList.addToLibrary(artist.getName()+" - "+album.getName());
			}
			else if(listOfFile[i].isDirectory()){
				populateLibrary(listOfFile[i]);
			}
		}
	}
	
	private class ExitListener implements ActionListener{
		
		//exits the program
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
	
	//library location listener
	private class LibraryLocationListener implements ActionListener{
		
		//starts selection of the file browser and will edit the file saying where the music library is located
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			//only allows directories to be chosen
			//starting directory would be the C:/ drive
			//sets the title for the search box
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setCurrentDirectory(new java.io.File("C:/"));
			fileChooser.setDialogTitle("Select Library Location");
			
			//checks for errors, only will run if the file location is accepted
			if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				//write out here to a setting file where it will host the library
				try{
					PrintWriter text = new PrintWriter ("settings.cfg", "UTF-8");
					text.println(fileChooser.getSelectedFile());
					text.close();
				} catch (IOException e){
					//prints out error if there is any issues in saving to the file
					System.out.println("there is an error saving the location of your library");
				}
			}
			else
			{
				//error code for selecting the library location
				System.out.println("Error in selecting library location");
			}
		}
		
	}
	
	//listener for adding new songs to the library
	private class AddFilesToLibraryListener implements ActionListener{
		
		//starts file selection of the directory to search for all the files to be added
		@Override
		public void actionPerformed(ActionEvent arg0){
			newerFiles = new AddFiles();
			File moreMusicLocation;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setCurrentDirectory(new java.io.File("C:/"));
			fileChooser.setDialogTitle("Select Location to Scan for New Music");
			
			//takes all mp3 files from directory and deletes them after moving them into the library location
			if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				//this will be used for the final product
				moreMusicLocation = fileChooser.getSelectedFile();
				newerFiles.addFilesToList(moreMusicLocation);
			}
		}
	}
	
	//reloads library
	private class ReloadLibraryListener implements ActionListener{
		//reloads library
		@SuppressWarnings({ "resource" })
		@Override
		public void actionPerformed(ActionEvent arg0){
			
			//Outputs the location of the library from settings file
			try {
				//reads information from settings.cfg file
				//populates the library with artist names
				Scanner iner = new Scanner(new FileReader("settings.cfg"));				
				String tempString = iner.nextLine().toString();
				File fileName = new File(tempString);
				populateLibrary(fileName);
				
			} catch (FileNotFoundException e) {
				//shows errors in reading off the file
				System.out.println("Error reading library");
				e.printStackTrace();
			}
		}
	}

	//this just really says that it was created by me
	//nothing else really
	private class AboutListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//informational box showing that it was written by me
			JOptionPane.showMessageDialog(null, "Written by Carl Dibert");
		}
	}
	
	//listener for clicking on the JList in the library section of the program
	private class LibraryMouseListener implements MouseListener{
		
		//response for clicking on the JList
		@SuppressWarnings("resource")
		@Override
		public void mouseClicked(MouseEvent e){
			//response for double clicking
			if(e.getClickCount() == 2){
				
				//splits the file from the -
				String album = libraryList.getLibrary().getSelectedValue().toString();
				String[] parts = album.split(" - ");
				
				try {
					//combines into one long string that forms a file path
					Scanner iner = new Scanner(new FileReader("settings.cfg"));
					String tempString = iner.nextLine().toString();
					String tempAlbum = tempString+"\\"+parts[0]+"\\"+parts[1];
					File fileName = new File(tempAlbum);
					addToPlaylist(fileName);
					
				} catch (FileNotFoundException e1) {
					//error codes and stuff for errors
					System.out.println("error in adding to playlist");
					e1.printStackTrace();
				}				
				
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//not used method
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//not used method
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//not used method
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//not used method
		}
	}
	
	//listeners for the playlist section of the program
	private class PlaylistMouseListener implements MouseListener{
		@SuppressWarnings("deprecation")
		@Override
		public void mouseClicked(MouseEvent e){
			//on mouse double click
			if(e.getClickCount() == 2){
				//enables the play button once the playback has started
				if(controller.getPlay().isEnabled() == false){
					controller.getPlay().setEnabled(true);
				}
				//sets the buttons to the proper visibility settings
				controller.getFastforward().setEnabled(true);
				controller.getRewind().setEnabled(true);
				controller.getPlay().setVisible(false);
				controller.getPause().setVisible(true);
				controller.getStop().setEnabled(true);
				//makes the music player thread has been stopped to not allow multiple threads running
				if(musicPlayerThread.isAlive() == true){
					musicPlayerThread.stop();
				}
				//starts the playback of the music player
				player = new MusicPlayer();
				File song = new File(playlist.getFileLocationOfSelection(playlist.getPlaylist().getSelectedIndex()));
				playlist.setCurrentlyPlaying(playlist.getPlaylist().getSelectedIndex());
				//sets the proper visibility for the previous button
				if(playlist.getCurrentlyPlaying() == 0){
					controller.getRewind().setEnabled(false);
				}
				else{
					controller.getRewind().setEnabled(true);
				}
				//starts playback and starts the second thread
				player.setSong(song);
				musicPlayerThread = new Thread(player);
				musicPlayerThread.start();
				
				//starts scheduler that checks for currently playing song to be finished
				timer = new Timer();
				timer.scheduleAtFixedRate(new UpdatePlaylist(), 0, 1000);
				
				//shows the currently playing song
				controller.setCurrentlyPlaying(playlist.getCurrentlyPlayingSongTitle());
			}
		}
		
		//auto scheduler for the updates
		private class UpdatePlaylist extends TimerTask{
			@Override
			public void run(){
				//checks if the thread is running or not
				if(musicPlayerThread.isAlive() == false){
					//if the current playlist is at the end of it is at the end then the thread closes itself
					if(playlist.getCurrentlyPlaying()+1 <= playlist.playlistSize()){
						playlist.setCurrentlyPlaying(playlist.getCurrentlyPlaying()+1);
						File song = new File(playlist.getFileLocationOfSelection(playlist.getCurrentlyPlaying()));
						player.setSong(song);
						musicPlayerThread = new Thread(player);
						musicPlayerThread.start();
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//not used method
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//not used method
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//not used method
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//not used method
		}
	}
	
	//adds to the current playlist
	private void addToPlaylist(File file){
		//makes file selection based on the file
		File[] listOfFile = file.listFiles();
		for(int i=0; i<listOfFile.length; i++){
			if(listOfFile[i].isFile()){
				//adds the file to the playlist
				playlist.addToPlaylist(listOfFile[i].toString());
			}
		}
	}
	
	//Action listener for the Stop button
	private class StopButtonListener implements ActionListener{

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//stops the current music player and stops the thread clears the currently playing and stops the timer
			musicPlayerThread.stop();
			controller.setCurrentlyPlaying("");
			timer.cancel();
		}
	}
	
	//action listener for the rewind button
	private class RewindActionListener implements ActionListener{
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//makes sure the fast forward button is always active
			controller.getFastforward().setEnabled(true);
			//plays the previous song in the playlist
			if(playlist.getCurrentlyPlaying()-1>=0){
				musicPlayerThread.stop();
				playlist.setCurrentlyPlaying(playlist.getCurrentlyPlaying()-1);
				File song = new File(playlist.getFileLocationOfSelection(playlist.getCurrentlyPlaying()));
				player.setSong(song);
				musicPlayerThread = new Thread(player);
				musicPlayerThread.start();
				controller.setCurrentlyPlaying(playlist.getCurrentlyPlayingSongTitle());
			}
			//if the rewind would send the playlist to the first song then it disables the rewind button
			if(playlist.getCurrentlyPlaying() == 0){
				controller.getRewind().setEnabled(false);
			}
		}
	}
	
	//action listener for the pause button
	private class PauseActionListener implements ActionListener{
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//suspends the music player thread and toggles the play/pause button to its play form
			musicPlayerThread.suspend();
			controller.togglePlayPause();
		}
	}
	
	//action listener for the play button
	private class PlayActionListener implements ActionListener{
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//resumes the music player thread and toggles the play/pause button back to its pause form
			musicPlayerThread.resume();
			controller.togglePlayPause();
		}
	}
	
	//action listener for the fastforward button
	private class FastforwardActionListner implements ActionListener{
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//always switches the rewind button to the on position
			controller.getRewind().setEnabled(true);
			//plays the next song in the playlist
			if(playlist.getCurrentlyPlaying()+1<playlist.playlistSize()){
				musicPlayerThread.stop();
				playlist.setCurrentlyPlaying(playlist.getCurrentlyPlaying()+1);
				File song = new File(playlist.getFileLocationOfSelection(playlist.getCurrentlyPlaying()));
				player.setSong(song);
				musicPlayerThread = new Thread(player);
				musicPlayerThread.start();
				controller.setCurrentlyPlaying(playlist.getCurrentlyPlayingSongTitle());
			}
			//if the song is the final song it disables the fast forward button
			if(playlist.getCurrentlyPlaying()+1 == playlist.playlistSize()){
				controller.getFastforward().setEnabled(false);
			}
		}
	}

}
