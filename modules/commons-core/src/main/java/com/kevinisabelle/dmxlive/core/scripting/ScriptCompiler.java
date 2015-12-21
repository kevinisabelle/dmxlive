package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.api.general.ResourceUtils;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author kisabelle
 */
public class ScriptCompiler {

	public static final String FIELD_SONG_NAME = "//Song Name:";
	public static final String FIELD_BPM = "//BPM:";
	public static final String FIELD_TIME_SIGNATURE = "//Time Signature:";
	public static final String FIELD_AUDIO_FILE = "//Audio file path=";
	public static final String FIELD_SAMPLES_FILE = "//Samples file path=";
	public static final String FIELD_METRONOME_INFO = "//Metronome=";

	public static Song parseSongFile(String text, String path) {

		Song song = new Song();

		text = text.replaceAll("\r", "");
		String[] lines = text.split("\n");
		StringBuilder scriptOnlyText = new StringBuilder();

		for (String line : lines) {

			if (line.startsWith(FIELD_SONG_NAME)) {

				song.setTitle(line.split("\\:")[1]);

			} else if (line.startsWith(FIELD_BPM)) {

				song.setBpm(Integer.parseInt(line.split("\\:")[1]));

			} else if (line.startsWith(FIELD_TIME_SIGNATURE)) {

				song.setSignature(new TimeSignature(line.split("\\:")[1]));

			} else if (line.startsWith(FIELD_AUDIO_FILE)) {

				if (line.split(FIELD_AUDIO_FILE).length == 0) {
					song.setAudioFilename(null);
				} else {
					song.setAudioFilename(new File(path + File.separator + line.split(FIELD_AUDIO_FILE)[1]).getAbsolutePath());
				}

			} else if (line.startsWith(FIELD_SAMPLES_FILE)) {

				if (line.split(FIELD_SAMPLES_FILE).length == 0) {
					song.setSamplesFilename(null);
				} else {
					song.setSamplesFilename(new File(path + File.separator + line.split(FIELD_SAMPLES_FILE)[1]).getAbsolutePath());
				}

			} else if (line.startsWith(FIELD_METRONOME_INFO)) {

				String[] metroInfos = line.split(FIELD_METRONOME_INFO)[1].split(",");
				song.setMetronomeMode(com.kevinisabelle.dmxlive.core.scripting.Song.MetronomeMode.valueOf(metroInfos[0]));
				song.setMetronomeSoundHi(metroInfos[1]);
				song.setMetronomeSoundLow(metroInfos[2]);

			} else {
				scriptOnlyText.append(line).append("\n");
			}

		}

		song.setScript(new Script(scriptOnlyText.toString()));

		return song;
	}

	public void saveSong(Song song, File file) throws FileNotFoundException {

		StringBuilder dmxScript = new StringBuilder();

		dmxScript.append(FIELD_SONG_NAME).append(song.getTitle()).append("\n");
		dmxScript.append(FIELD_BPM).append(song.getBpm()).append("\n");
		dmxScript.append(FIELD_TIME_SIGNATURE).append(song.getSignature()).append("\n");
		dmxScript.append(FIELD_AUDIO_FILE).append(
				song.getAudioFilename() == null ? "" :
				ResourceUtils.getRelativePath(song.getAudioFilename(), file.getAbsolutePath(), File.separator)).append("\n");
		dmxScript.append(FIELD_SAMPLES_FILE).append(
				song.getSamplesAudioFilename() == null ? "" :
				ResourceUtils.getRelativePath(song.getSamplesAudioFilename(), file.getAbsolutePath(), File.separator)).append("\n");
		dmxScript.append(FIELD_METRONOME_INFO).append(song.getMetronomeMode())
				.append(",").append(song.getMetronomeSoundHi()).append(",").append(song.getMetronomeSoundLow()).append("\n");

		dmxScript.append(song.getScript().getScriptText());

		PrintWriter out = new PrintWriter(file.getAbsolutePath());
		out.write(dmxScript.toString());
		out.close();

		song.setFilename(ResourceUtils.getRelativePath(file.getAbsolutePath(), System.getProperty("user.dir"), File.separator));
	}

}
