package com.kevinisabelle.dmxLive.objects;

import com.kevinisabelle.dmxLive.Constants;
import com.kevinisabelle.dmxLive.helper.ResourceUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Song {

	public enum MetronomeMode {

		OFF,
		ON,
		DOUBLE_SPEED,
		HALF_SPEED;
	}
	public static final String FIELD_SONG_NAME = "//Song Name:";
	public static final String FIELD_BPM = "//BPM:";
	public static final String FIELD_TIME_SIGNATURE = "//Time Signature:";
	public static final String FIELD_AUDIO_FILE = "//Audio file path=";
	public static final String FIELD_SAMPLES_FILE = "//Samples file path=";
	public static final String FIELD_METRONOME_INFO = "//Metronome=";
	
	private int bpm = 120;
	private TimeSignature signature = new TimeSignature("4/4");
	private String title = "New Song";
	
	private File audioFilename;
	private File samplesAudioFilename = null;
	
	private Script script = new Script("", this);
	private String filename = null;
	
	private String metronomeSoundHi = Constants.metronomeSoundsHi[0];
	private String metronomeSoundLow = Constants.metronomeSoundsLow[0];
	private MetronomeMode metronomeMode = MetronomeMode.ON;

	public Song(String songFileName) throws FileNotFoundException, IOException {

		filename = songFileName;
		BufferedReader br = new BufferedReader(new FileReader(getFilename()));
		String line;
		StringBuilder builder = new StringBuilder();

		while ((line = br.readLine()) != null) {
			builder.append(line).append("\n");
		}

		parseSongFile(builder.toString(), new File(filename).getParent());
	}

	public Song() {
	}

	private void parseSongFile(String text, String path) {

		text = text.replaceAll("\r", "");
		String[] lines = text.split("\n");
		StringBuilder scriptOnlyText = new StringBuilder();

		for (String line : lines) {

			if (line.startsWith(FIELD_SONG_NAME)) {

				title = line.split("\\:")[1];

			} else if (line.startsWith(FIELD_BPM)) {

				bpm = Integer.parseInt(line.split("\\:")[1]);

			} else if (line.startsWith(FIELD_TIME_SIGNATURE)) {

				signature = new TimeSignature(line.split("\\:")[1]);

			} else if (line.startsWith(FIELD_AUDIO_FILE)) {
				
				if (line.split(FIELD_AUDIO_FILE).length == 0){
					setAudioFilename(null);
				} else {
					setAudioFilename(new File(path + File.separator + line.split(FIELD_AUDIO_FILE)[1]).getAbsolutePath());
				}
				
			} else if (line.startsWith(FIELD_SAMPLES_FILE)) {

				if (line.split(FIELD_SAMPLES_FILE).length == 0){
					setSamplesFilename(null);
				} else {
					setSamplesFilename(new File(path + File.separator + line.split(FIELD_SAMPLES_FILE)[1]).getAbsolutePath());
				}
				
			} else if (line.startsWith(FIELD_METRONOME_INFO)) {

				String[] metroInfos = line.split(FIELD_METRONOME_INFO)[1].split(",");
				metronomeMode = MetronomeMode.valueOf(metroInfos[0]);
				metronomeSoundHi = metroInfos[1];
				metronomeSoundLow = metroInfos[2];

			} else {
				scriptOnlyText.append(line).append("\n");
			}

		}

		script = new Script(scriptOnlyText.toString(), this);
	}

	public void save(File file) throws FileNotFoundException {

		StringBuilder dmxScript = new StringBuilder();

		dmxScript.append(FIELD_SONG_NAME).append(title).append("\n");
		dmxScript.append(FIELD_BPM).append(bpm).append("\n");
		dmxScript.append(FIELD_TIME_SIGNATURE).append(signature).append("\n");
		dmxScript.append(FIELD_AUDIO_FILE).append(
				audioFilename == null ? "" : 				
				ResourceUtils.getRelativePath(audioFilename.getAbsolutePath(), file.getAbsolutePath(), File.separator)).append("\n");
		dmxScript.append(FIELD_SAMPLES_FILE).append(
				samplesAudioFilename == null ? "" : 
				ResourceUtils.getRelativePath(samplesAudioFilename.getAbsolutePath(), file.getAbsolutePath(), File.separator)).append("\n");
		dmxScript.append(FIELD_METRONOME_INFO).append(metronomeMode)
				.append(",").append(metronomeSoundHi).append(",").append(metronomeSoundLow).append("\n");

		dmxScript.append(script.getScriptText());

		PrintWriter out = new PrintWriter(file.getAbsolutePath());
		out.write(dmxScript.toString());
		out.close();

		setFilename(ResourceUtils.getRelativePath(file.getAbsolutePath(), System.getProperty("user.dir"), File.separator));
	}

	/**
	 * @return the bpm
	 */
	public int getBpm() {
		return bpm;
	}

	/**
	 * @param bpm the bpm to set
	 */
	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	/**
	 * @return the signature
	 */
	public TimeSignature getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(TimeSignature signature) {
		this.signature = signature;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAudioFilename() {
		return audioFilename == null ? null : audioFilename.getAbsolutePath();
	}

	public void setAudioFilename(String audioFile) {

		if (audioFile != null && !audioFile.isEmpty()) {
			audioFilename = new File(audioFile);
		} else {
			audioFilename = null;
		}
	}

	public String getSamplesAudioFilename() {

		return samplesAudioFilename == null ? null : samplesAudioFilename.getAbsolutePath();
	}

	public void setSamplesFilename(String samplesAudioFilename) {

		if (samplesAudioFilename != null && !samplesAudioFilename.equals("")) {
			this.samplesAudioFilename = new File(samplesAudioFilename);
		} else {
			this.samplesAudioFilename = null;
		}
	}

	/**
	 * @return the script
	 */
	public Script getScript() {
		return script;
	}

	/**
	 * @param script the script to set
	 */
	public void setScript(Script script) {
		this.script = script;
	}

	/**
	 * @return the metronomeSoundHi
	 */
	public String getMetronomeSoundHi() {
		return metronomeSoundHi;
	}

	/**
	 * @param metronomeSoundHi the metronomeSoundHi to set
	 */
	public void setMetronomeSoundHi(String metronomeSoundHi) {
		this.metronomeSoundHi = metronomeSoundHi;
	}

	/**
	 * @return the metronomeSoundLow
	 */
	public String getMetronomeSoundLow() {
		return metronomeSoundLow;
	}

	/**
	 * @param metronomeSoundLow the metronomeSoundLow to set
	 */
	public void setMetronomeSoundLow(String metronomeSoundLow) {
		this.metronomeSoundLow = metronomeSoundLow;
	}

	/**
	 * @return the metronomeMode
	 */
	public MetronomeMode getMetronomeMode() {
		return metronomeMode;
	}

	/**
	 * @param metronomeMode the metronomeMode to set
	 */
	public void setMetronomeMode(MetronomeMode metronomeMode) {
		this.metronomeMode = metronomeMode;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return title + "(" + signature + " @ " + bpm + " bpm)";
	}
}
