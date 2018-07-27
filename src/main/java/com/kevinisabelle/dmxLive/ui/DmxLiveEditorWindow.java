package com.kevinisabelle.dmxLive.ui;

import com.kevinisabelle.dmxLive.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.UIHelper;
import com.kevinisabelle.dmxLive.objects.Script;
import com.kevinisabelle.dmxLive.objects.Song;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.processes.DmxRunnableObserver;
import com.kevinisabelle.dmxLive.ui.components.AppButtonsPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
/**
 *
 * @author kevin
 */
public class DmxLiveEditorWindow extends JFrame implements ActionListener, DmxRunnableObserver {

	private static final String WINDOW_NAME = "DMX Live - Script Editor"; 
		
	private Song currentSong = new Song();
	//private File currentSongFile = null;
	private Date lastSaveDate = new Date();
	private final JButton saveButton = new JButton("Save");
	private final JButton saveAsButton = new JButton("Save As...");
	private final JButton openButton = new JButton("Open");
	private final JButton newButton = new JButton("New");
	private final RSyntaxTextArea scriptText = new RSyntaxTextArea(20, 78);
	private final JButton runButton = new JButton("Run script");
	private final JButton stopButton = new JButton("Stop");
	private final TextField songName = new TextField("", 50);
	private final TextField bpm = new TextField("120", 3);
	private final TextField audioFileName = new TextField("", 80);
	private final JButton browseAudioFileButton = new JButton("Browse...");
	private final TextField samplesAudioFileName = new TextField("", 80);
	private final JButton browseSamplesAudioFileButton = new JButton("Browse...");
	private final JCheckBox playSamplesCheckBox = new JCheckBox("Play samples");
	private final JComboBox timeSignature = new JComboBox(new String[]{"4/4", "12/8", "6/8"});
	private final TextField startPosition = new TextField("1:0:0");
	private final TextField currentPosition = new TextField("1:0:0", 8);
	private final JCheckBox useLogCheckBox = new JCheckBox("Show DMX log");
	private final TextArea log = new TextArea("", 5, 30, TextArea.SCROLLBARS_BOTH);
	private final JComboBox playMetronomeCombo = new JComboBox(Song.MetronomeMode.values());
	private final JComboBox metronomeHiSoundCombo = new JComboBox(Constants.metronomeSoundsHi);
	private final JComboBox metronomeLowSoundCombo = new JComboBox(Constants.metronomeSoundsLow);
	
	private static final Logger logger = Logger.getLogger(DmxLiveEditorWindow.class);

	public DmxLiveEditorWindow() {

		super("DMX Live Script Editor - " + Constants.VERSION);
		
		DmxLive.enableDMXLogging(true);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_3, 2));
		
		this.add(Box.createVerticalStrut(3));
		
		JPanel fileManagementPanel = new JPanel();
		fileManagementPanel.setLayout(new BoxLayout(fileManagementPanel, BoxLayout.X_AXIS));
		fileManagementPanel.setMaximumSize(new Dimension(4200, 30));
		fileManagementPanel.add(new AppButtonsPanel());
		fileManagementPanel.add(saveButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(saveAsButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(openButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		fileManagementPanel.add(newButton);
		fileManagementPanel.add(Box.createHorizontalStrut(5));
		
		
		saveButton.setPreferredSize(new Dimension(80, 20));
		saveAsButton.setPreferredSize(new Dimension(90, 20));
		openButton.setPreferredSize(new Dimension(80, 20));
		newButton.setPreferredSize(new Dimension(80, 20));
		this.add(fileManagementPanel);
		this.add(Box.createVerticalStrut(3));
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_3, 2));
		
		this.add(Box.createVerticalStrut(3));

		JPanel songNamePanel = new JPanel();
		songNamePanel.setLayout(new BoxLayout(songNamePanel, BoxLayout.X_AXIS));
		songNamePanel.setMaximumSize(new Dimension(4200, 20));
		Font songNamefont = new Font("Verdana", Font.BOLD, 18);
		Label songNameLabel = new Label(" Song Name:  ");
		songNameLabel.setSize(100, 20);
		songNameLabel.setMaximumSize(new Dimension(100, 20));
		songNameLabel.setFont(songNamefont);
		songNamePanel.add(songNameLabel);
		songNamePanel.add(songName);
		songName.setFont(songNamefont);
		//songNamePanel.add(Box.createHorizontalStrut(400));
		this.add(songNamePanel);
		
		this.add(Box.createVerticalStrut(3));
		//this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 2));
		this.add(Box.createVerticalStrut(3));

		JPanel timeInfoPanel = new JPanel();
		timeInfoPanel.setLayout(new BoxLayout(timeInfoPanel, BoxLayout.X_AXIS));
		timeInfoPanel.setMaximumSize(new Dimension(4200, 20));
		timeInfoPanel.add(new Label("  BPM: "));
		timeInfoPanel.add(bpm);
		timeInfoPanel.add(Box.createHorizontalStrut(20));
		timeInfoPanel.add(new Label("Time Signature: "));
		timeInfoPanel.add(timeSignature);
		timeInfoPanel.add(Box.createHorizontalStrut(20));

		timeInfoPanel.add(new Label("Metronome: "));
		timeInfoPanel.add(playMetronomeCombo);
		timeInfoPanel.add(Box.createHorizontalStrut(20));

		timeInfoPanel.add(new Label("Hi: "));
		timeInfoPanel.add(metronomeHiSoundCombo);
		timeInfoPanel.add(Box.createHorizontalStrut(20));

		timeInfoPanel.add(new Label("Low: "));
		timeInfoPanel.add(metronomeLowSoundCombo);
		timeInfoPanel.add(Box.createHorizontalStrut(150));

		
		this.add(Box.createVerticalStrut(3));

		JPanel audioFilePanel = new JPanel();
		audioFilePanel.setLayout(new BoxLayout(audioFilePanel, BoxLayout.X_AXIS));
		audioFilePanel.setMaximumSize(new Dimension(4200, 20));
		
		Label audioFileLabel = new Label(" Audio file:");
		audioFileLabel.setSize(100, 20);
		audioFileLabel.setMaximumSize(new Dimension(100, 20));
		
		audioFilePanel.add(audioFileLabel);
		audioFilePanel.add(audioFileName);
		audioFilePanel.add(Box.createHorizontalStrut(10));
		audioFilePanel.add(browseAudioFileButton);
		browseAudioFileButton.setPreferredSize(new Dimension(100, 20));
		audioFilePanel.add(Box.createHorizontalStrut(220));
		this.add(audioFilePanel);
		this.add(Box.createVerticalStrut(3));
		
		JPanel samplesAudioFilePanel = new JPanel();
		samplesAudioFilePanel.setLayout(new BoxLayout(samplesAudioFilePanel, BoxLayout.X_AXIS));
		samplesAudioFilePanel.setMaximumSize(new Dimension(4200, 20));
		Label audioLabel = new Label(" Samples Audio file: ");
		audioLabel.setSize(100, 20);
		audioLabel.setMaximumSize(new Dimension(100, 20));
		samplesAudioFilePanel.add(audioLabel);
		samplesAudioFilePanel.add(samplesAudioFileName);
		samplesAudioFilePanel.add(Box.createHorizontalStrut(10));
		samplesAudioFilePanel.add(browseSamplesAudioFileButton);
		samplesAudioFilePanel.add(Box.createHorizontalStrut(10));
		samplesAudioFilePanel.add(playSamplesCheckBox);
		samplesAudioFilePanel.add(Box.createHorizontalStrut(150));
		playSamplesCheckBox.setSelected(true);
		playSamplesCheckBox.setPreferredSize(new Dimension(120, 20));
		browseSamplesAudioFileButton.setPreferredSize(new Dimension(100, 20));
		this.add(samplesAudioFilePanel);
		
		this.add(Box.createVerticalStrut(3));

		//this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 2));
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
		actionsPanel.setMaximumSize(new Dimension(4200, 30));
		actionsPanel.add(Box.createHorizontalStrut(10));
		actionsPanel.add(runButton);
		actionsPanel.add(Box.createHorizontalStrut(10));
		actionsPanel.add(stopButton);
		actionsPanel.add(Box.createHorizontalStrut(10));
		runButton.setFont(songNamefont);
		stopButton.setFont(songNamefont);
		runButton.setPreferredSize(new Dimension(150, 30));
		stopButton.setPreferredSize(new Dimension(100, 30));

		actionsPanel.add(new Label("Start position: "));
		actionsPanel.add(startPosition);
		startPosition.setFont(songNamefont);
		actionsPanel.add(new Label("Current position: "));
		actionsPanel.add(currentPosition);
		currentPosition.setFont(songNamefont);
		actionsPanel.add(useLogCheckBox);
		useLogCheckBox.setPreferredSize(new Dimension(120, 10));
		actionsPanel.add(Box.createHorizontalStrut(150));
		this.add(actionsPanel);
		
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 2));
		
		this.add(Box.createVerticalStrut(3));
		
		this.add(timeInfoPanel);
		
		JPanel scriptsAndInstructionsPanel = new JPanel();
		scriptsAndInstructionsPanel.setLayout(new BoxLayout(scriptsAndInstructionsPanel, BoxLayout.X_AXIS));
		Font scriptFont = new Font("Lucida Console", Font.PLAIN, 13);
		
		RTextScrollPane scrollPane = new RTextScrollPane(scriptText, true, Color.lightGray);
		scrollPane.setPreferredSize(new Dimension(700, 500));
		scriptsAndInstructionsPanel.add(scrollPane);
		
		setupSyntaxHighlight();
		
		scriptText.setFont(scriptFont);
		this.add(scriptsAndInstructionsPanel);

		scriptsAndInstructionsPanel.add(log);
		Font logFont = new Font("Courier", Font.PLAIN, 12);
		log.setForeground(Constants.THEME_COLOR_2);
		log.setBackground(Constants.THEME_COLOR_3);
		log.setFont(logFont);

		//Functions mapping
		runButton.addActionListener(this);
		stopButton.addActionListener(this);
		openButton.addActionListener(this);
		newButton.addActionListener(this);
		saveButton.addActionListener(this);
		saveAsButton.addActionListener(this);
		browseAudioFileButton.addActionListener(this);
		browseSamplesAudioFileButton.addActionListener(this);
		useLogCheckBox.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == runButton) {
			runScript();
		} else if (e.getSource() == stopButton) {
			stopSong();
		} else if (e.getSource() == openButton) {
			openSong();
		} else if (e.getSource() == saveButton) {
			saveCurrentSong(false);
		} else if (e.getSource() == saveAsButton) {
			saveCurrentSong(true);
		} else if (e.getSource() == newButton) {
			createNewSong();
		} else if (e.getSource() == browseAudioFileButton) {
			selectAudioFile();
		} else if (e.getSource() == browseSamplesAudioFileButton) {
			selectSamplesAudioFile();
		} else if (e.getSource() == useLogCheckBox){
			updateLogState();
		}
	}

	public void setSong(Song song) {
		this.currentSong = song;
		updateInterfaceFromSong();
	}

	private void updateInterfaceFromSong() {

		songName.setText(currentSong.getTitle());
		bpm.setText(Integer.toString(currentSong.getBpm()));

		audioFileName.setText(currentSong.getAudioFilename());
		
		if (currentSong.getSamplesAudioFilename() != null){
			samplesAudioFileName.setText(currentSong.getSamplesAudioFilename());
		} else {
			samplesAudioFileName.setText("");
		}
		
		scriptText.setText(currentSong.getScript().getScriptText());

		timeSignature.setSelectedItem(currentSong.getSignature().toString());

		if (currentSong.getMetronomeMode() != null) {
			playMetronomeCombo.setSelectedItem(currentSong.getMetronomeMode());
			metronomeHiSoundCombo.setSelectedItem(currentSong.getMetronomeSoundHi());
			metronomeLowSoundCombo.setSelectedItem(currentSong.getMetronomeSoundLow());
			
			playMetronomeCombo.revalidate();
			metronomeHiSoundCombo.revalidate();
			metronomeLowSoundCombo.revalidate();
		}
		
		useLogCheckBox.setSelected(DmxLive.isDMXLoggingEnabled());
	}
	
	private void setupSyntaxHighlight(){
		
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/dmxScript", "com.kevinisabelle.dmxLive.ui.components.DmxScriptTokenMaker");
		scriptText.setSyntaxEditingStyle("text/dmxScript");
		
		scriptText.setAntiAliasingEnabled(true);
		//scriptText.setSyntaxScheme(SyntaxScheme.);
		
	}

	private void updateFilelabel(String state) {
		this.setTitle(WINDOW_NAME + " - " + state + " ("
				+ (lastSaveDate == null ? "new file)" : lastSaveDate + "):" ) + " "
				+ (currentSong.getFilename() == null ? "" : currentSong.getFilename()));
	}
	
	private void updateLogState(){
		DmxLive.enableDMXLogging(useLogCheckBox.isSelected());
	}

	private void updateSongFromInterface() {

		currentSong.setTitle(songName.getText());
		currentSong.setBpm(Integer.parseInt(bpm.getText()));
		currentSong.setScript(new Script(scriptText.getText()));
		currentSong.setSignature(new TimeSignature((String) timeSignature.getSelectedItem()));
		currentSong.setAudioFilename(audioFileName.getText());
		currentSong.setSamplesFilename(samplesAudioFileName.getText());
		currentSong.setMetronomeMode((Song.MetronomeMode) playMetronomeCombo.getSelectedItem());
		currentSong.setMetronomeSoundHi((String) metronomeHiSoundCombo.getSelectedItem());
		currentSong.setMetronomeSoundLow((String) metronomeLowSoundCombo.getSelectedItem());
	}

	private void runScript() {

		updateSongFromInterface();

		TimeInfo startTime = new TimeInfo(startPosition.getText());
		startTime.setMeasure(startTime.getMeasure()-1);

		if (currentSong.getAudioFilename() == null){
			DmxLive.startScene(currentSong.getSignature(), currentSong.getBpm(), currentSong.getScript(), this);
		} else {
			DmxLive.startSong(startTime, currentSong, this, playSamplesCheckBox.isSelected());
		}
	}

	private void stopSong() {
		DmxLive.stopSong();
	}

	@Override
	public void logMessage(String message, long millis) {
		log.setText(millis + "ms: " + message + "\n" + (log.getText().length() > 5000 ? log.getText().substring(0, 5000) : log.getText()));
	}

	@Override
	public void updatePosition(long millis) {
		
		TimeInfo info = new TimeInfo(currentSong.getSignature(), currentSong.getBpm(), millis);
		
		if (info.getSubBeat() > 0.8){
			info = info.add(new TimeInfo("1:1:0"), currentSong.getSignature());
			info.setSubBeat(0.0);
		} else if (info.getSubBeat() < 0.3){
			info.setSubBeat(0.0);
		}
		currentPosition.setText(info.toString());
	}
	
	@Override
	public void stopNotify(boolean isComplete) {
		
	}

	public void createNewSong() {

		currentSong = new Song();
		updateInterfaceFromSong();
		updateFilelabel("new");
	}
	
	@Override
	public void openFile(String filename) {
		openSong(filename);
	}
	
	public void openSong(){
		openSong(null);
	}

	public void openSong(String filename) {

		int returnVal = JFileChooser.APPROVE_OPTION;
		
		if (filename == null){
			
			JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("DMX Script files (*.dmxScript)", "dmxScript");

			fileChooser.setFileFilter(filter);
			returnVal = fileChooser.showOpenDialog(this);
		
			filename = fileChooser.getSelectedFile().getAbsolutePath();
		
		}

		if (returnVal != JFileChooser.CANCEL_OPTION) {
			
			File file = new File(filename);

			try {
				currentSong = new Song(filename);
				lastSaveDate = new Date();
				DmxLive.setCurrentLocation(file.getPath());

			} catch (IOException e) {
				logger.error("Cannot open file: " + e.getMessage(), e);
				JOptionPane.showMessageDialog(this, "ERROR OPENING FILE: " + e.toString());
				return;
			}

			updateInterfaceFromSong();

			updateFilelabel("Opened");
		}

	}

	public boolean closeSong() {

		if (confirmCloseCurrentSong() != JOptionPane.CANCEL_OPTION) {

			currentSong = null;
			lastSaveDate = null;

			return true;
		} else {
			return false;
		}
	}

	public void selectAudioFile() {

		String filename = selectFile();
		audioFileName.setText(filename);	
	}

	public void selectSamplesAudioFile() {

		String filename = selectFile();
		samplesAudioFileName.setText(filename);
	}

	private String selectFile() {

		JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Wave files (*.wav)", "wav");

		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);
		
		if (fileChooser.getSelectedFile() != null){
			DmxLive.setCurrentLocation(fileChooser.getSelectedFile().getPath());
			return fileChooser.getSelectedFile().getAbsolutePath();
		}
		
		return null;
	}

	public void saveCurrentSong(boolean forceNewFile) {

		if (currentSong.getFilename() == null || forceNewFile) {

			JFileChooser fileChooser = new JFileChooser(DmxLive.getCurrentLocation());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("DMX Script files (*.dmxScript)", "dmxScript");

			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			
			if (fileChooser.getSelectedFile() == null){
				return;
			}
			
			currentSong.setFilename(fileChooser.getSelectedFile().getAbsolutePath());

			if (!currentSong.getFilename().endsWith(".dmxScript")) {
				File newFile = new File(currentSong.getFilename() + ".dmxScript");
				currentSong.setFilename(newFile.getAbsolutePath());
				DmxLive.setCurrentLocation(fileChooser.getSelectedFile().getPath());
			}
		}

		try {

			updateSongFromInterface();

			logMessage("Saving into file: " + currentSong.getFilename(), 0);
			currentSong.save(new File(currentSong.getFilename()));
			lastSaveDate = new Date();

			updateFilelabel("Saved");

		} catch (FileNotFoundException e) {
			logger.error("Cannot save file: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "ERROR SAVING FILE: " + e.toString());
		}
	}

	private int confirmCloseCurrentSong() {

		if (currentSong != null) {
			int response = JOptionPane.showConfirmDialog(this, "Do you want to save your song before closing it?", "Closing song", JOptionPane.YES_NO_CANCEL_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				saveCurrentSong(false);
			}

			return response;
		}
		return JOptionPane.YES_OPTION;
	}

	


}
