package com.kevinisabelle.dmxLive;

import com.kevinisabelle.dmxLive.objects.Configuration;
import com.kevinisabelle.dmxlive.core.Constants;

import com.kevinisabelle.dmxLive.ui.components.ConfigurationWindow;
import com.kevinisabelle.dmxLive.ui.DmxLiveEditorWindow;
import com.kevinisabelle.dmxLive.ui.DmxLiveFunctionsTesterWindow;
import com.kevinisabelle.dmxLive.ui.DmxLivePlaylistWindow;
import com.kevinisabelle.dmxLive.ui.components.HelpDialog;
import com.kevinisabelle.dmxlive.api.driver.DmxDriver;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import com.kevinisabelle.dmxlive.core.engine.processes.DmxRunnable;
import com.kevinisabelle.dmxlive.core.engine.processes.DmxRunnableObserver;
import com.kevinisabelle.dmxlive.core.scripting.Script;
import com.kevinisabelle.dmxlive.core.scripting.Song;
import com.kevinisabelle.dmxlive.music.TimeHelper;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.nimrod.NimRODLookAndFeel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This is the main class of the application responsible for starting the proper
 * frame and managing songs and scripts executions.
 *
 * @author Kevin Isabelle
 */
public class DmxLive {

	private static DmxDriver dmxManager;
	private static DmxRunnable dmxExecution = null;
	private static DmxRunnable dmxExecutionStandalone = null;
	private static Song currentPlayedSong = null;
	private static Configuration config;
	private static JDialog configurationDialog = null;
	private static JFrame currentFrame = null;

	private static boolean enableDMXLogging = Constants.ENABLE_DMX_LOG;

	private static Clip currentAudioClip = null;
	private static Clip currentSampleClip = null;

	private static String currentLocation = null;

	private static final Logger logger = Logger.getLogger(DmxLive.class);

	protected DmxLive(){

	}

	public static void main(String[] args) {

		PropertyConfigurator.configure(Constants.LOG4J_CONFIG);

		try {

			loadConfiguration();

		} catch (IOException ioe) {

			logger.fatal("Could not load configuration : " + ioe.toString());

			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {

				System.exit(1);
			}
		}

		try {

			logger.info("Loading look and feel...");
			UIManager.setLookAndFeel( new NimRODLookAndFeel());
			logger.info("Done loading look and feel...");

		} catch (UnsupportedLookAndFeelException ulafe){
			logger.error("Error loading look and feel: " + ulafe.toString());
		}

		String windowToShow = "editor";

		if (args.length > 0) {
			windowToShow = args[0];
		}

		String fileToOpen;

		logger.info("Starting app: " + windowToShow);

		logger.info("Initializing dmx manager...");
		dmxManager = null;//new DmxDriver();
		logger.info("done.");

		logger.info("Opening main window...");

		if (windowToShow.equals("editor")) {
			showEditorWindow();
		} else if (windowToShow.equals("tester")) {
			showTesterWindow();
		} else if (windowToShow.equals("playlist")) {
			showPlaylistWindow();
		}

		if (args.length > 1){
			fileToOpen = args[1];
			//JOptionPane.showMessageDialog(currentFrame, fileToOpen);
			((DmxRunnableObserver)currentFrame).openFile(fileToOpen);
		}

		logger.info("done.");

	}

	private static void loadConfiguration() throws IOException {
		config = new Configuration(Constants.PROPERTIES_LOCATION);
	}

	public static Configuration getConfiguration() {
		return config;
	}

	private static void showEditorWindow() {

		DmxLiveEditorWindow frame = new DmxLiveEditorWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1050, 660);
		frame.setResizable(false);
		frame.setVisible(true);

		currentFrame = frame;
		centerCurrentFrame(currentFrame);
	}

	private static void showPlaylistWindow() {

		DmxLivePlaylistWindow frame = new DmxLivePlaylistWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(1300, 690);
		frame.setResizable(false);
		frame.setVisible(true);

		currentFrame = frame;
		centerCurrentFrame(currentFrame);
	}

	private static void showTesterWindow() {

		DmxLiveFunctionsTesterWindow frame = new DmxLiveFunctionsTesterWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		currentFrame = frame;
		centerCurrentFrame(currentFrame);
	}

	private static void centerCurrentFrame(Container frame) {
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		frame.setLocation(x, y);
	}

	public static void showConfiguration() {

		if (configurationDialog == null) {
			ConfigurationWindow frame = new ConfigurationWindow(getCurrentFrame());
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.pack();
			configurationDialog = frame;

		}

		centerCurrentFrame(configurationDialog);
		configurationDialog.setVisible(true);
	}

	public static void showHelp() {

		HelpDialog help = new HelpDialog(currentFrame);
		help.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		help.pack();
		centerCurrentFrame(help);
		help.setVisible(true);
	}

	/**
	 * Get the current main application frame used.
	 *
	 * @return
	 */
	public static JFrame getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * Start a song script execution, audio and dmx.
	 *
	 * @param startTime Start the song at that time
	 * @param song The song to play
	 * @param observer The dmx process observer for logging.
	 * @param playSamples	if set to true, will run the samples audio as well.
	 */
	public static void startSong(TimeInfo startTime, Song song, DmxRunnableObserver observer, boolean playSamples) {

		logger.info("Starting song: " + startTime + ", " + song.getTitle() + ", Samples: " + playSamples);

		if (dmxExecution == null) {

			currentPlayedSong = song;

			observer.logMessage("Loading script", -1);

			long startTimeLoadScript = System.currentTimeMillis();

			observer.logMessage("Converting to DMX events...", -1);

			List<TimedDmxEvent> valuesFromScript = song.getScript().getTimedDmxEvents(new TimeInfo("0:0:0"), song.getSignature(), song.getBpm(), null);

			observer.logMessage("Converted " + valuesFromScript.size() + " dmx values in " + (System.currentTimeMillis() - startTimeLoadScript) + " ms", -1);

			try {

				AudioInputStream audioTrack = AudioSystem.getAudioInputStream(new File(song.getAudioFilename()));

				currentAudioClip = (Clip) config.getDrummerMixer().getLine(config.getDrummerLine());

				observer.logMessage("Opening audio... ", 0);
				currentAudioClip.open(audioTrack);
				observer.logMessage("Done opening audio.", 0);

				currentAudioClip.setMicrosecondPosition(TimeHelper.getMilliseconds(startTime, song.getSignature(), song.getBpm()) * 1000);

				if (song.getSamplesAudioFilename() != null && !song.getSamplesAudioFilename().equals("") && playSamples){

					AudioInputStream sampleTrack = AudioSystem.getAudioInputStream(new File(song.getSamplesAudioFilename()));

					currentSampleClip = (Clip) config.getSamplesMixer().getLine(config.getSamplesLine());

					observer.logMessage("Opening samples audio... ", 0);
					currentSampleClip.open(sampleTrack);
					observer.logMessage("Done opening samples audio.", 0);

					currentSampleClip.setMicrosecondPosition(TimeHelper.getMilliseconds(startTime, song.getSignature(), song.getBpm()) * 1000);

				}

				dmxExecution = new DmxRunnable(
						song.getSignature(),
						song.getBpm(),
						valuesFromScript,
						dmxManager,
						currentAudioClip,
						startTime,
						observer,
						true,
						song.getMetronomeMode(),
						song.getMetronomeSoundHi(),
						song.getMetronomeSoundLow());

				dmxExecution.enableDMXLogging(enableDMXLogging);

				Thread dmxRun = new Thread(dmxExecution, "DmxExecutionThread-Song");

				if (currentSampleClip != null){
					currentSampleClip.start();

					while(!currentSampleClip.isRunning()){
						try {
							Thread.sleep(10);
						} catch (InterruptedException ignore) {

						}
					}
				}

				currentAudioClip.start();

				dmxRun.start();

			} catch (LineUnavailableException ex) {
				logger.error("The line is not available: " + ex.getMessage(), ex);
			} catch (UnsupportedAudioFileException ex) {
				logger.error("The audio file is not supported: " + ex.getMessage(), ex);
			} catch (IOException ex) {
				logger.error("IO Error: " + ex.getMessage(), ex);
			}

		} else {
			observer.logMessage("Song is already running. Stop it before starting another one", -1);
			logger.error("Cannot start song, another one is in progress.");
		}
	}

	/**
	 * Stops any running songs.
	 */
	public static void stopSong() {

		logger.debug("Stopping song.");

		if (dmxExecution != null) {
			dmxExecution.stop();

			if (currentAudioClip != null){

				currentAudioClip.stop();
				currentAudioClip.close();
				currentAudioClip = null;

				if (currentSampleClip != null){
					currentSampleClip.stop();
					currentSampleClip.close();
					currentSampleClip = null;
				}
			}

			dmxExecution = null;
		}

		if (dmxExecutionStandalone != null){
			dmxExecutionStandalone.stop();
		}
	}

	/**
	 * Resumes a paused song playback.
	 */
	public static void resumePlayback() {

		logger.debug("Resume playback.");

		if (currentSampleClip != null){
			currentSampleClip.start();
		}
		currentAudioClip.start();
	}

	/**
	 * Pause the current playback.
	 */
	public static void pauseSong() {

		logger.debug("Pause song.");

		if (currentSampleClip != null){
			currentSampleClip.stop();
		}
		currentAudioClip.stop();
	}

	/**
	 * Enable DMX runnable to publish DMX logs to the observer. (Performance cost)
	 * @param isEnabled
	 */
	public static void enableDMXLogging(boolean isEnabled){

		enableDMXLogging = isEnabled;

		if (dmxExecution != null){
			dmxExecution.enableDMXLogging(enableDMXLogging);
		}
	}

	/**
	 * Checks if the DMX logging parameter is activated.
	 * @return
	 */
	public static boolean isDMXLoggingEnabled(){
		return enableDMXLogging;
	}

	/**
	 * Returns the current Song object being played.
	 * @return
	 */
	public static Song getCurrentlyPlayedSong() {
		return currentPlayedSong;
	}

	/**
	 * Start a scene without audio. Will run synchronized on the internal
	 * clock of the computer
	 * @param signature
	 * @param bpm
	 * @param script
	 * @param observer
	 */
	public static void startScene(TimeSignature signature, int bpm, Script script, DmxRunnableObserver observer) {

		logger.info("Start scene: " + signature + "@" + bpm + ", " + script.getScriptText());

		List<TimedDmxEvent> valuesFromScript = script.getTimedDmxEvents(new TimeInfo("0:0:0"), signature, bpm, null);



		dmxExecutionStandalone = new DmxRunnable(signature, bpm, valuesFromScript, dmxManager, null, null, observer, false, null, null, null);
		Thread dmxRun = new Thread(dmxExecutionStandalone, "DmxExecutionThread-Scenes");

		dmxExecutionStandalone.enableDMXLogging(enableDMXLogging);

		dmxRun.start();
	}

	public static String getCurrentLocation(){
		if (currentLocation == null){
			currentLocation = System.getProperty("user.dir");
		}

		return currentLocation;
	}

	public static void setCurrentLocation(String location){
		currentLocation = location;
	}
}
