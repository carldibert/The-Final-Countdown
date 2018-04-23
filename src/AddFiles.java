import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import helliker.id3.CorruptHeaderException;
import helliker.id3.ID3v2FormatException;
import helliker.id3.MP3File;
import helliker.id3.NoMPEGFramesException;

public class AddFiles {
	
	private ArrayList<String> artistList;
	private ArrayList<String> albumList;
	private ArrayList<Artist> artists;
	private ArrayList<Album> albums;
	private Artist artist;
	private Album album;
	private ArrayList<File> rawFiles;
	private MP3File song;
	private File library;
	
	//used when initializing
	public void addFilesToList(File location){
		//initializing the array lists
		artistList = new ArrayList<String>();
		albumList = new ArrayList<String>();
		artists = new ArrayList<Artist>();
		albums = new ArrayList<Album>();
		rawFiles = new ArrayList<File>();
		
		//loads the library from file
		Scanner iner;
		try{
			iner = new Scanner(new FileReader("settings.cfg"));
			String tempString = iner.nextLine().toString();
			library = new File(tempString);
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		//runs the sorting program
		addFilesToArrayList(location);
		populateTree();
		moveFilesIntoPermaLibrary();
	}
	
	//searches all of the files within the directory and adds every single file to an array list
	private void addFilesToArrayList(File location){
		//loads all of the files into an arraylist
		File[] listOfFile = location.listFiles();
		for(int i=0; i<listOfFile.length; i++){
			if(listOfFile[i].isFile()){
				rawFiles.add(listOfFile[i]);
			}
			else if(listOfFile[i].isDirectory()){
				addFilesToArrayList(listOfFile[i]);
			}
		}
	}

	//searches through all of the listed files and adds them to a list of artists
	private void populateTree(){
		//creates an album list of all of the possible albums from metadata
		for(int i=0; i<rawFiles.size(); i++){
			try {
				song = new MP3File(rawFiles.get(i));
				if(!albumList.contains(song.getAlbum())){
					albumList.add(song.getAlbum());
					album = new Album(song.getAlbum());
					albums.add(album);
				}
			} catch (NoMPEGFramesException | ID3v2FormatException | CorruptHeaderException | IOException e) {
				e.printStackTrace();
			}
		}
		//adds the raw music files into the albums
		for(int i=0; i<rawFiles.size(); i++){
			try {
				song = new MP3File(rawFiles.get(i));
				for(int j=0; j<albums.size(); j++){
					if(song.getAlbum().equals(albums.get(j).getAlbumTitle())){
						albums.get(j).addSong(rawFiles.get(i));
					}
				}
			} catch (NoMPEGFramesException | ID3v2FormatException | CorruptHeaderException | IOException e) {
				e.printStackTrace();
			}
		}
		//creates all of the artists from the metadata stored on the mp3 files
		for(int i=0; i<rawFiles.size(); i++){
			try {
				song = new MP3File(rawFiles.get(i));
				if(!artistList.contains(song.getArtist())){
					artistList.add(song.getArtist());
					artist = new Artist(song.getArtist());
					artists.add(artist);
				}
			} catch (NoMPEGFramesException | ID3v2FormatException | CorruptHeaderException | IOException e) {
				e.printStackTrace();
			}
		}
		//adds albums to the artists forming the tree
		for(int i=0; i<rawFiles.size(); i++){
			try {
				song = new MP3File(rawFiles.get(i));
				for(int j=0; j<artists.size(); j++){
					if(artists.get(j).getArtistName().equals(song.getArtist())){
						for(int l=0; l<albums.size(); l++){
							if(song.getAlbum().equals(albums.get(l).getAlbumTitle())){
								artists.get(j).addAlbum(albums.get(l));
								albums.remove(l);
							}
						}
					}
				}
			} catch (NoMPEGFramesException | ID3v2FormatException | CorruptHeaderException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//moves the files from ram into the hard drive location stored in the library
	private void moveFilesIntoPermaLibrary(){
		for(int i=0; i<artists.size(); i++){
			for(int j=0; j<artists.get(i).getAlbumList().size(); j++){
				for(int l=0; l<artists.get(i).getAlbumList().get(j).getSongList().size(); l++){
					//file locations for ram storage
					File destination = null;
					File source = null;
					
					//source and destination locations
					source = new File(artists.get(i).getAlbumList().get(j).getSongList().get(l).getAbsolutePath());
					destination = new File(library.getAbsolutePath().toString()+"\\"+artists.get(i).getArtistName()+"\\"
							+artists.get(i).getAlbumList().get(j).getAlbumTitle()+"\\"+artists.get(i).getAlbumList().get(j).getSongList().get(l).getName());
					
					//creates the artist folder if it is not directly on the library right now
					if(!destination.getParentFile().getParentFile().exists()){
						destination.getParentFile().getParentFile().mkdir();
					}
					//creates the album folder if it is not directly on the library right now
					if(!destination.getParentFile().exists()){
						destination.getParentFile().mkdir();
					}
					//creates a song file if it is not directly on the library right now
					if(!destination.exists()){
						try {
							destination.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					//moves the song from being stored on the ram to being stored on the hard drive
					try {
						Files.move(source.toPath(), destination.toPath(), REPLACE_EXISTING);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
