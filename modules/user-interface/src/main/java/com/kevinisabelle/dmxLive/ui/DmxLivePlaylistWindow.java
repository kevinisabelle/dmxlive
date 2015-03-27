package com.kevinisabelle.dmxLive.ui;

import com.kevinisabelle.dmxlive.core.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.ExportsManager;
import com.kevinisabelle.dmxLive.helper.UIHelper;
import com.kevinisabelle.dmxlive.core.music.Playlist;
import com.kevinisabelle.dmxlive.core.music.Song;
import com.kevinisabelle.dmxlive.core.music.TimeInfo;
import com.kevinisabelle.dmxlive.dmxengine.processes.DmxRunnableObserver;
import com.kevinisabelle.dmxLive.ui.components.AppButtonsPanel;
import com.kevinisabelle.dmxLive.ui.components.SongDisplay;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;

/**
 *
 * @author kevin
 */
public class DmxLivePlaylistWindow extends JFrame implements ActionListener, DmxRunnableObserver {

	private static final String WINDOW_NAME = "DMX Live - Playlist Editor"; 
	
	private Playlist currentPlaylist = null;
	private Song currentSong = null;
	private boolean isPlaying = false;
	private boolean isPaused = false;
	private int currentListIndex = 0;

	private final List playlistList = new List(22);
	private final TextField playlistTitle = new TextField(75);
	private final SongDisplay songDisplay;
	private Date lastSaveDate = new Date();
	private final JButton saveButton = new JButton("Save");
	private final JButton saveAsButton = new JButton("Save As...");
	private final JButton openButton = new JButton("Open");
	private final JButton newButton = new JButton("New");
	
	private final JButton addButton = new JButton("Add");
	private final JButton removeButton = new JButton("Remove");
	private final JButton upButton = new JButton("UP");
	private final JButton downButton = new JButton("DOWN");
	private final JButton exportPdfButton = new JButton("Export PDF...");
	
	private final Label queuedSong = new Label("No songs in queue.");
	private final Button loadSongButton = new Button("Load");
	private final JButton bigPlayButton = new JButton("Play");

	private final JPanel queuedSongPanel;
	private final JButton stopButton = new JButton("Stop");
	private final JButton nextButton = new JButton("Next >>");
	private final JButton previousButton = new JButton("<< Previous");
	private final JCheckBox useLogCheckBox = new JCheckBox("Show DMX log");
	private final TextArea log = new TextArea("", 1, 50, TextArea.SCROLLBARS_BOTH);
	
	private final JPanel buttonsAndSongDisplayPanel;
	
	private static final Logger logger = Logger.getLogger(DmxLivePlaylistWindow.class);

	public DmxLivePlaylistWindow() {

		super("Live Show - " + Constants.VERSION);
		
		DmxLive.enableDMXLogging(false);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_3, 10));

		this.add(Box.createVerticalStrut(3));
		JPanel fileManagementPanel = new JPanel();
		fileManagementPanel.setLayout(new BoxLayout(fileManagementPanel, BoxLayout.X_AXIS));
		fileManagementPanel.setMaximumSize(new Dimension(1200, 30));
		fileManagementPanel.add(new AppButtonsPanel());
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(saveButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(saveAsButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(openButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(newButton);
		
		saveButton.setPreferredSize(new Dimension(80, 20));
		saveAsButton.setPreferredSize(new Dimension(90, 20));
		openButton.setPreferredSize(new Dimension(80, 20));
		newButton.setPreferredSize(new Dimension(80, 20));
		
		this.add(fileManagementPanel);

		this.add(Box.createVerticalStrut(3));
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_3, 10));

		this.add(Box.createVerticalStrut(3));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setMaximumSize(new Dimension(1200, 30));
		buttonsPanel.add(Box.createHorizontalStrut(5));
		buttonsPanel.add(addButton);
		buttonsPanel.add(Box.createHorizontalStrut(5));
		buttonsPanel.add(removeButton);
		buttonsPanel.add(Box.createHorizontalStrut(5));
		buttonsPanel.add(upButton);
		buttonsPanel.add(Box.createHorizontalStrut(5));
		buttonsPanel.add(downButton);
		buttonsPanel.add(Box.createHorizontalStrut(5));
		buttonsPanel.add(exportPdfButton);
		buttonsPanel.add(Box.createHorizontalStrut(5));
		
		addButton.setPreferredSize(new Dimension(80, 20));
		removeButton.setPreferredSize(new Dimension(90, 20));
		upButton.setPreferredSize(new Dimension(80, 20));
		downButton.setPreferredSize(new Dimension(80, 20));
		exportPdfButton.setPreferredSize(new Dimension(80, 20));
		Label playlistNameLabel = new Label("Playlist:");
		playlistNameLabel.setFont(Constants.FONT_BIG);
		buttonsPanel.add(playlistNameLabel);
		buttonsPanel.add(playlistTitle);
		playlistTitle.setFont(Constants.FONT_BIG);
		this.add(buttonsPanel);
		
		this.add(Box.createVerticalStrut(3));

		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));

		JPanel listAndCurrentSong = new JPanel(new GridLayout(1, 2, 5, 5));
		playlistList.setSize(new Dimension(400, 200));
		listAndCurrentSong.add(playlistList);
		playlistList.setFont(Constants.FONT_SUPER_BIG);
		playlistList.setBackground(Color.black);
		playlistList.setForeground(Color.white);

		buttonsAndSongDisplayPanel = new JPanel();
		buttonsAndSongDisplayPanel.setLayout(new BoxLayout(fileManagementPanel, BoxLayout.Y_AXIS));
		buttonsAndSongDisplayPanel.setFont(Constants.FONT_BIG);
		buttonsAndSongDisplayPanel.setBackground(Color.white);
		
		queuedSongPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		queuedSongPanel.setBackground(Constants.THEME_COLOR_3);
		queuedSongPanel.setForeground(Constants.THEME_COLOR_2);
		queuedSongPanel.setFont(Constants.FONT_BIG);
		queuedSongPanel.add(queuedSong);	
		queuedSongPanel.add(loadSongButton);
		queuedSongPanel.setMinimumSize(new Dimension(400, 35));
		queuedSongPanel.setPreferredSize(new Dimension(400, 35));

		loadSongButton.setBackground(Constants.THEME_COLOR_3);
		loadSongButton.setForeground(Constants.THEME_COLOR_2);
		loadSongButton.setBounds(380, 3, 60, 30);
		queuedSong.setBounds(0, 3, 400, 30);
				
		buttonsAndSongDisplayPanel.add(queuedSongPanel);
		
		songDisplay = new SongDisplay(this);
		buttonsAndSongDisplayPanel.add(songDisplay);
		songDisplay.setFont(Constants.FONT_SUPER_BIG);

		buttonsAndSongDisplayPanel.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));
		buttonsAndSongDisplayPanel.add(bigPlayButton);
		bigPlayButton.setPreferredSize(new Dimension(100, 70));
		bigPlayButton.setFont(Constants.FONT_SUPER_BIG);
		buttonsAndSongDisplayPanel.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 5));
		buttonsAndSongDisplayPanel.add(stopButton);
		stopButton.setPreferredSize(new Dimension(100, 40));
		stopButton.setFont(Constants.FONT_SUPER_BIG);
		buttonsAndSongDisplayPanel.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 5));

		/*JPanel previousNextPanel = new JPanel(new GridLayout(1, 2));

		previousNextPanel.add(previousButton);
		previousNextPanel.add(nextButton);
		previousNextPanel.setFont(buttonsAndSongDisplayPanel.getFont());
		buttonsAndSongDisplayPanel.add(previousNextPanel);
		buttonsAndSongDisplayPanel.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));*/

		listAndCurrentSong.add(buttonsAndSongDisplayPanel);
		this.add(listAndCurrentSong);

		JPanel logTitlePanel = (UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));
		logTitlePanel.add(useLogCheckBox);
		useLogCheckBox.setSelected(DmxLive.isDMXLoggingEnabled());
		this.add(logTitlePanel);
		logTitlePanel.setVisible(false);
		
		this.add(log);
		log.setForeground(Constants.THEME_COLOR_2);
		log.setBackground(Constants.THEME_COLOR_3);
		log.setFont(Constants.FONT_LOG);
		log.setVisible(false);

		bigPlayButton.addActionListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		saveButton.addActionListener(this);
		openButton.addActionListener(this);
		saveAsButton.addActionListener(this);
		newButton.addActionListener(this);
		stopButton.addActionListener(this);
		nextButton.addActionListener(this);
		previousButton.addActionListener(this);
		useLogCheckBox.addActionListener(this);
		loadSongButton.addActionListener(this);
		exportPdfButton.addActionListener(this);
		playlistList.addActionListener(this);

		createNewPlaylist();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == openButton) {
			openPlaylist();
		} else if (e.getSource() == saveAsButton) {
			saveCurrentPlaylist(true);
		} else if (e.getSource() == saveButton) {
			saveCurrentPlaylist(false);
		} else if (e.getSource() == newButton) {
			createNewPlaylist();
		} else if (e.getSource() == addButton) {
			addSong();
		} else if (e.getSource() == removeButton) {
			removeSong();
		} else if (e.getSource() == upButton) {
			moveDown();
		} else if (e.getSource() == downButton) {
			moveUp();
		} else if (e.getSource() == bigPlayButton) {
			play();
		} else if (e.getSource() == stopButton) {
			stop();
		} else if (e.getSource() == nextButton) {
			next();
		} else if (e.getSource() == previousButton) {
			previous();
		} else if (e.getSource() == useLogCheckBox) {
			updateLogState();
		} else if (e. getSource() == loadSongButton){
			loadSong();
		} else if (e.getSource() == exportPdfButton){
			exportToPDF();
		} else if (e.getSource() == playlistList){
			
			currentListIndex = Integer.parseInt(e.getActionCommand().split("\\)")[0]) - 1;
			loadSong();
			
			next();			
		}
	}

	private void addSong() {

		updatePlaylistFromUI();

		JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DMX scripts (*.dmxScript)", "dmxScript");

		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal != JFileChooser.CANCEL_OPTION) {

			try {
				Song song = new Song(fileChooser.getSelectedFile().getAbsolutePath());
				DmxLive.setCurrentLocation(fileChooser.getSelectedFile().getPath());
				currentPlaylist.addSong(song);

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Could not add song: " + e.toString());
			}
		}

		updateUIFromPlaylist();
	}

	private void removeSong() {

		updatePlaylistFromUI();

		currentPlaylist.removeSong(playlistList.getSelectedIndex());

		updateUIFromPlaylist();

	}

	private void moveUp() {

		java.util.List<Song> songs = currentPlaylist.getSongs();

		int indexToMoveUp = playlistList.getSelectedIndex();

		if (indexToMoveUp == -1) {
			return;
		}

		if (indexToMoveUp == songs.size() - 1) {
			return;
		}

		Song movedUp = songs.remove(indexToMoveUp);
		songs.add(indexToMoveUp + 1, movedUp);

		updateUIFromPlaylist();

		playlistList.select(indexToMoveUp + 1);

	}

	private void moveDown() {

		java.util.List<Song> songs = currentPlaylist.getSongs();

		int indexToMoveDown = playlistList.getSelectedIndex();

		if (indexToMoveDown == -1) {
			return;
		}

		if (indexToMoveDown == 0) {
			return;
		}

		Song movedDown = songs.remove(indexToMoveDown);
		songs.add(indexToMoveDown - 1, movedDown);

		updateUIFromPlaylist();

		playlistList.select(indexToMoveDown - 1);

	}

	private void loadSong() {

		if (currentListIndex == -1) {
			currentListIndex = 0;
		}
		
		if (currentPlaylist.getSongs().isEmpty()){
			JOptionPane.showMessageDialog(this, "No songs in the current playlist!");
		}

		currentSong = currentPlaylist.getSongs().get(currentListIndex);
		
		logger.info("Loading song: " + currentSong.getTitle());

		songDisplay.setSong(currentSong);
		
		stop();
				
		playlistList.select(currentListIndex);
	}
	
	private void exportToPDF(){
		
		if (currentPlaylist != null){
			
			JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files (*.pdf)", "pdf");

			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal != JFileChooser.CANCEL_OPTION) {

				try {
					
					File selectedFile = fileChooser.getSelectedFile();
					
					if (!selectedFile.getAbsolutePath().endsWith(".pdf")) {
						File newFile = new File(selectedFile.getAbsolutePath() + ".pdf");
						selectedFile = newFile;
					}
					
					ExportsManager.convertToPDF(
							ExportsManager.convertOrderToHTML(currentPlaylist),
							selectedFile,
							".\\");
					
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + selectedFile);

				} catch (Exception e){
					logMessage("", WIDTH);
				}
			}
			
		}
		
	}

	private void play() {

		if (currentSong == null){
			return;
		}
		
		if (isPlaying) {

			pause();
			bigPlayButton.setText("Play");
			isPlaying = false;
			isPaused = true;
				
		} else {
			
			if (isPaused) {
				 
				DmxLive.resumePlayback();
				isPaused = false;
				isPlaying = true;
				bigPlayButton.setText("Pause");
				songDisplay.setStatus("Playing");
				
			} else {

				songDisplay.setStatus("Playing");

				DmxLive.startSong(new TimeInfo("0:0:0"), currentSong, this, true);

				isPlaying = true;
				bigPlayButton.setText("Pause");
				isPaused = false;
			}
		}
	}

	private void pause() {

		songDisplay.setStatus("Paused");
		DmxLive.pauseSong();
	}

	private void stop() {

		bigPlayButton.setText("Play");
		songDisplay.setStatus("Stopped");
		isPlaying = false;
		isPaused = false;
		DmxLive.stopSong();
	}

	private void next() {

		if (playlistList.getItemCount() == 0){
			return;
		}
		
		queuedSongPanel.revalidate();
		
		if (currentListIndex == -1) {
			currentListIndex = 0;
		} else if (currentListIndex != playlistList.getItemCount() - 1) {
			currentListIndex++;
			playlistList.select(currentListIndex);
		}

		queuedSong.setText("Next: " + currentPlaylist.getSongs().get(currentListIndex).getTitle());
	}

	private void previous() {

		if (playlistList.getItemCount() == 0){
			return;
		}
		
		queuedSongPanel.revalidate();
		
		if (currentListIndex == -1) {
			currentListIndex = 0;
		} else if (currentListIndex != 0) {		
			currentListIndex--;
			playlistList.select(currentListIndex);
		}
		queuedSong.setText("Next: " + currentPlaylist.getSongs().get(currentListIndex).getTitle());
	}

	public void setPlaylist(Playlist playlist) {

		this.currentPlaylist = playlist;

		updateUIFromPlaylist();
	}

	private void updateLogState() {
		DmxLive.enableDMXLogging(useLogCheckBox.isSelected());
	}

	private void updateUIFromPlaylist() {

		playlistList.removeAll();
		int i = 0;
		for (Song song : currentPlaylist.getSongs()) {
			i++;
			playlistList.add(i + ") " + song.getTitle());
		}

		playlistTitle.setText(currentPlaylist.getTitle());
	}

	private void updatePlaylistFromUI() {
		currentPlaylist.setTitle(playlistTitle.getText());
	}

	private void updateFilelabel(String state) {
		this.setTitle(WINDOW_NAME + " - " + state + " ("
				+ (lastSaveDate == null ? "new file)" : lastSaveDate + "):" ) + " "
				+ (currentPlaylist.getFilename() == null ? "" : currentPlaylist.getFilename()));
	}

	public void createNewPlaylist() {

		currentPlaylist = new Playlist();
		updateUIFromPlaylist();
		updateFilelabel("new");
	}
	
	@Override
	public void openFile(String filename) {
		openPlaylist(filename);
	}
	
	public void openPlaylist(){
		openPlaylist(null);
	}

	public void openPlaylist(String filename) {

		int returnVal = JFileChooser.APPROVE_OPTION;
		
		if (filename == null){
			
			JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("DMX playlist (*.dmxLive)", "dmxLive");

			fileChooser.setFileFilter(filter);
			returnVal = fileChooser.showOpenDialog(this);
		
			filename = fileChooser.getSelectedFile().getAbsolutePath();
		
		}

		if (returnVal != JFileChooser.CANCEL_OPTION) {
			
			File file = new File(filename);

			try {
				currentPlaylist = new Playlist(filename);
				lastSaveDate = new Date();
				DmxLive.setCurrentLocation(file.getPath());

			} catch (Exception e) {

				logMessage("ERROR OPENING FILE: " + e.toString(), 0);
				return;
			}

			updateUIFromPlaylist();
			
			if (!currentPlaylist.getSongs().isEmpty()){
				currentListIndex = 0;
				loadSong();
				next();
			}

			updateFilelabel("Opened");
		}

	}

	public void saveCurrentPlaylist(boolean forceNewFile) {

		if (currentPlaylist.getFilename() == null || forceNewFile) {

			JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("DMX Live playlist (*.dmxLive)", "dmxLive");

			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);

			if (fileChooser.getSelectedFile() == null){
				return;
			}
			
			currentPlaylist.setFilename(fileChooser.getSelectedFile().getAbsolutePath());
			DmxLive.setCurrentLocation(fileChooser.getSelectedFile().getPath());
			
			if (!currentPlaylist.getFilename().endsWith(".dmxLive")) {
				File newFile = new File(currentPlaylist.getFilename() + ".dmxLive");
				currentPlaylist.setFilename(newFile.getAbsolutePath());
			}
		}

		try {

			updatePlaylistFromUI();

			logMessage("Saving into file: " + currentPlaylist.getFilename(), 0);
			currentPlaylist.save(new File(currentPlaylist.getFilename()));
			lastSaveDate = new Date();

			updateFilelabel("Saved");

		} catch (Exception e) {
			logger.error("Cannot save file: " + e.getMessage(), e);
			logMessage("ERROR SAVING FILE: " + e.toString(), 0);
		}
	}

	public boolean closePlaylist() {

		if (confirmCloseCurrentSong() != JOptionPane.CANCEL_OPTION) {

			currentPlaylist = null;
			lastSaveDate = null;

			return true;
		} else {
			return false;
		}
	}

	private int confirmCloseCurrentSong() {

		if (currentPlaylist != null) {
			int response = JOptionPane.showConfirmDialog(this, "Do you want to save your playlist before closing it?", "Closing song", JOptionPane.YES_NO_CANCEL_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				saveCurrentPlaylist(false);
			}

			return response;
		}
		return JOptionPane.YES_OPTION;
	}

	@Override
	public void logMessage(String message, long millis) {
		log.setText(millis + "ms: " + message + "\n" + (log.getText().length() > 2000 ? log.getText().substring(0, 2000) : log.getText()));
	}

	@Override
	public void updatePosition(long millis) {

		TimeInfo info = new TimeInfo(currentSong.getSignature(), currentSong.getBpm(), millis);

		if (info.getSubBeat() > 0.8) {
			info = info.add(new TimeInfo("1:1:0"), currentSong.getSignature());
			info.setSubBeat(0.0);
		} else if (info.getSubBeat() < 0.3) {
			info.setSubBeat(0.0);
		}
		
		info.setBeat(info.getBeat()+1);
		songDisplay.setPosition(info.toString());
	}
			
	@Override
	public void stopNotify(boolean isComplete) {
		if (isComplete){
			loadSong();
			next();
		}
	}

}
