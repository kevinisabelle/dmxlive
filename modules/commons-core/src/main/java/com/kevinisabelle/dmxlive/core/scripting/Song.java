package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.core.Constants;
import com.kevinisabelle.dmxlive.music.TimeSignature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Song {

	public enum MetronomeMode {

		OFF,
		ON,
		DOUBLE_SPEED,
		HALF_SPEED;
	}


	private int bpm = 120;
	private TimeSignature signature = new TimeSignature("4/4");
	private String title = "New Song";

	private File audioFilename;
	private File samplesAudioFilename = null;

	private Script script = new Script("");
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

		// Get this out of the song.
		ScriptCompiler.parseSongFile(builder.toString(), new File(filename).getParent());
	}

	public Song() {
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
