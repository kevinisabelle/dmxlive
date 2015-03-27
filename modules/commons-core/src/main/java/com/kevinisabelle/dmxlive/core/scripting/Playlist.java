package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.api.general.ResourceUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class Playlist {
	
	private static final String FIELD_TITLE = "//Title:";
	private static final String DELIMITER_SONG = "\\=";
	private static final String FIELD_SONG = "song=";
	
	private String playlistTitle = "New Playlist";
	private File playlistFile = null;
	private List<Song> songs = new ArrayList<Song>();
	
	public Playlist(){
		
	}
	
	public Playlist(String playlistFilename) throws FileNotFoundException, IOException{
		
		parsePlaylistFile(playlistFilename);	
	}
	
	private void parsePlaylistFile(String playlistFilename) throws IOException{
		
		playlistFile = new File(playlistFilename);
		BufferedReader br = new BufferedReader(new FileReader(playlistFilename));
        
		String line;
		
		while((line = br.readLine()) != null) {
			
			if (line.startsWith(FIELD_TITLE)){
				
				playlistTitle = line.split("\\:")[1];
				
			} else if (line.startsWith(FIELD_SONG)) {
				
				File songfileAbs = new File(playlistFile.getParent() + File.separator + line.split(DELIMITER_SONG)[1]);
				
				try {
					songs.add(new Song(songfileAbs.getAbsolutePath()));
				} catch (Exception e){
					throw new RuntimeException("Could not open file for playlist: " + line, e);
				}
			}
			
		}
		
		br.close();
	}
	
	public void save(File file) throws FileNotFoundException {
		
		StringBuilder playlistfileContent = new StringBuilder();
		
		playlistfileContent.append(FIELD_TITLE).append(playlistTitle).append("\n");

		for (Song song : songs){
			playlistfileContent.append(FIELD_SONG).append(
					                           ResourceUtils.getRelativePath(song.getFilename(), file.getAbsolutePath(), File.separator))
					.append("\n");
		}
		
		PrintWriter out = new PrintWriter(file.getAbsolutePath());
		out.write(playlistfileContent.toString());
		out.close();
		
		playlistFile = file;
	}
	
	public void addSong(Song song){
		songs.add(song);
	}
	
	public void removeSong(int i){
		songs.remove(i);
	}
	
	public void removeSong(Song song){
		songs.remove(song);
	}
	
	public List<Song> getSongs(){
		return songs;
	}
	
	public String getTitle(){
		return playlistTitle;
	}
	
	public void setTitle(String title){
		this.playlistTitle = title;
	}
	
	public String getFilename(){
		return playlistFile == null ? null : playlistFile.getAbsolutePath();
	}
	
	public void setFilename(String filename){
		this.playlistFile = new File(filename);
	}
}
