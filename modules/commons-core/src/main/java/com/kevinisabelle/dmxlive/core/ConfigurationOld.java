package com.kevinisabelle.dmxlive.core;

import com.kevinisabelle.dmxlive.core.Constants;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import org.apache.log4j.Logger;

/**
 *
 * @author kisabelle
 */
public class ConfigurationOld {

	
	
	public static AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
	Clip.Info sourceDataLineInfo = new Clip.Info(Clip.class, audioFormat);
	
	private List<Mixer.Info> availableMixers = new LinkedList<Mixer.Info>();
	
	private Mixer drummerMixer = null;
	private Mixer samplesMixer = null;
	
	private int dmxRate = Constants.DMX_RATE; // in 1/x of a beat
	private int dmxRunnableCheckResolution = Constants.CHECK_RESOLUTION; // in 1/x of a beat
	private int dmxRunnableOffset = Constants.DMXRUNNABLE_OFFSET; // in ms
	
	private Properties props;
	private String propsFile;
	
	private static Logger logger = Logger.getLogger(ConfigurationOld.class);

	public ConfigurationOld() {
	}

	public ConfigurationOld(String configFile) throws IOException {

		propsFile = configFile;

		FileReader reader = new FileReader(propsFile);
		props = new Properties();
		props.load(reader);
		reader.close();

		initMixers();

		updateObjectFromProperties();

	}

	public void saveConfig() throws IOException {

		updatePropertiesFromObject();

		FileWriter writer = new FileWriter(propsFile);
		props.store(writer, "Saved on: " + new Date());

	}

	private void updateObjectFromProperties() {

		setDmxRate(props.getProperty(Constants.P_DMX_RATE) == null ? Constants.DMX_RATE : Integer.parseInt(props.getProperty(Constants.P_DMX_RATE)));
		setDmxRunnableCheckResolution(props.getProperty(Constants.P_DMXRUN_CHECK_RESOLUTION) == null ? Constants.CHECK_RESOLUTION : Integer.parseInt(props.getProperty(Constants.P_DMXRUN_CHECK_RESOLUTION)));
		setDmxRunnableOffset(props.getProperty(Constants.P_DMXRUN_OFFSET) == null ? Constants.DMXRUNNABLE_OFFSET : Integer.parseInt(props.getProperty(Constants.P_DMXRUN_OFFSET)));

		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {

			if (mixerInfo.toString().equals(props.getProperty(Constants.P_DRUMMER_MIXER))) {

				setDrummerMixer(AudioSystem.getMixer(mixerInfo));
			}

			if (mixerInfo.toString().equals(props.getProperty(Constants.P_SAMPLES_MIXER))) {

				setSamplesMixer(AudioSystem.getMixer(mixerInfo));
			}
		}
		
		if (drummerMixer == null){
			drummerMixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
		}
		
		if (samplesMixer == null){
			samplesMixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
		}
	}

	private void updatePropertiesFromObject() {

		props.setProperty(Constants.P_DMXRUN_CHECK_RESOLUTION, Integer.valueOf(getDmxRunnableCheckResolution()).toString());
		props.setProperty(Constants.P_DMXRUN_OFFSET, Integer.valueOf(getDmxRunnableOffset()).toString());
		props.setProperty(Constants.P_DMX_RATE, Integer.valueOf(getDmxRate()).toString());
		props.setProperty(Constants.P_DRUMMER_MIXER, drummerMixer.getMixerInfo().toString());
		props.setProperty(Constants.P_SAMPLES_MIXER, samplesMixer.getMixerInfo().toString());

	}

	private void initMixers() {

		availableMixers = new ArrayList<Mixer.Info>();
		List<Mixer.Info> foundMixers = Arrays.asList(AudioSystem.getMixerInfo());

		for (Mixer.Info currentMixerInfo : foundMixers) {

			Mixer currentMixer = AudioSystem.getMixer(currentMixerInfo);

			Line.Info[] tlines = currentMixer.getSourceLineInfo();

			List<Line.Info> availableLines = new LinkedList<Line.Info>();

			for (Line.Info t : tlines) {

				try {
					// test if line is assignable
					@SuppressWarnings("unused")
					Clip source = (Clip) currentMixer.getLine(sourceDataLineInfo);

					availableLines.add(t);

				} catch (Exception ex) {

				}
			}

			if (!availableLines.isEmpty()) {
				logger.debug("Source lines for: " + currentMixerInfo + " (" + availableLines.size() + ")");
				availableMixers.add(currentMixerInfo);
			}
		}
	}

	public synchronized List<Mixer.Info> getMixers() {
		return availableMixers;
	}

	/**
	 * @return the dmxRate
	 */
	public int getDmxRate() {
		return dmxRate;
	}

	/**
	 * @param dmxRate the dmxRate to set
	 */
	public void setDmxRate(int dmxRate) {
		this.dmxRate = dmxRate;
	}

	/**
	 * @return the dmxRunnableCheckResolution
	 */
	public int getDmxRunnableCheckResolution() {
		return dmxRunnableCheckResolution;
	}

	/**
	 * @param dmxRunnableCheckResolution the dmxRunnableCheckResolution to set
	 */
	public void setDmxRunnableCheckResolution(int dmxRunnableCheckResolution) {
		this.dmxRunnableCheckResolution = dmxRunnableCheckResolution;
	}

	/**
	 * @return the dmxRunnableOffset
	 */
	public int getDmxRunnableOffset() {
		return dmxRunnableOffset;
	}

	/**
	 * @param dmxRunnableOffset the dmxRunnableOffset to set
	 */
	public void setDmxRunnableOffset(int dmxRunnableOffset) {
		this.dmxRunnableOffset = dmxRunnableOffset;
	}

	/**
	 * @return the drummerLine
	 */
	public Line.Info getDrummerLine() throws LineUnavailableException {
		return drummerMixer.getLine(sourceDataLineInfo).getLineInfo();
	}

	/**
	 * @return the samplesLine
	 */
	public Line.Info getSamplesLine() throws LineUnavailableException {
		return samplesMixer.getLine(sourceDataLineInfo).getLineInfo();
	}

	/**
	 * @return the drummerMixer
	 */
	public Mixer getDrummerMixer() {
		return drummerMixer;
	}

	/**
	 * @param drummerMixer the drummerMixer to set
	 */
	public void setDrummerMixer(Mixer drummerMixer) {
		this.drummerMixer = drummerMixer;
	}

	/**
	 * @return the samplesMixer
	 */
	public Mixer getSamplesMixer() {
		return samplesMixer;
	}

	/**
	 * @param samplesMixer the samplesMixer to set
	 */
	public void setSamplesMixer(Mixer samplesMixer) {
		this.samplesMixer = samplesMixer;
	}
}
