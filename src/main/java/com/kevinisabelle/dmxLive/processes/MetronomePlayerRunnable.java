package com.kevinisabelle.dmxLive.processes;

import com.kevinisabelle.dmxLive.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.Song;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This runnable will refresh at intervals and see if it needs to play the
 * metronome sound. If it is the case, it will play the clip, wait till it is
 * played and continue on the checking process. This runnable must be stopped to
 * exit thread execution.
 *
 * @author kisabelle
 */
public class MetronomePlayerRunnable extends SongParallelRunnable {

	private Clip metronomeHiClip = null;
	private Clip metronomeLowClip = null;
	private String metronomeSoundHi = null;
	private String metronomeSoundLow = null;
	private Song.MetronomeMode mode;
	private boolean isRunning = true;
	private long refreshTimeout = Constants.ONE_BEAT_MS_TOLERANCE;
	private long sleepTimeAfterBeat = 0l;
	private boolean playHi = false;
	private boolean playLow = false;
	
	private double metronomeOneBeatModulo;
	private double metronomeOneBeatTolerance;
	
	private boolean isFirstBeatPlayed = false;

	public MetronomePlayerRunnable(long refreshTimeout, Song.MetronomeMode mode, String metronomeSoundHi, String metronomeSoundLow, int bpm, TimeSignature signature, DmxRunnableObserver observer, Clip playedAuClip) {

		super(playedAuClip, signature, observer, bpm);
		
		double oneBeatMs = TimeHelper.getQuarterMilliseconds(bpm);
		
		this.refreshTimeout = refreshTimeout;
		this.metronomeSoundHi = metronomeSoundHi;
		this.metronomeSoundLow = metronomeSoundLow;
		this.mode = mode;
		this.sleepTimeAfterBeat = Double.valueOf(oneBeatMs / 2.0).longValue();
		
		// Time signature is based on eight notes, divide the oneBeatMs per 2
		if (this.signature.getBeatUnit() == 8){
			oneBeatMs /= 2.0;
			this.refreshTimeout = 12;
			this.signature = new TimeSignature(3, 8);
			sleepTimeAfterBeat = sleepTimeAfterBeat / 2;
		}
		
		this.metronomeOneBeatModulo = (oneBeatMs * (mode == Song.MetronomeMode.HALF_SPEED ? 2.0 : (mode == Song.MetronomeMode.DOUBLE_SPEED ? 0.5 : 1.0)));
		this.metronomeOneBeatTolerance = oneBeatMs / 4.0;

		try {
			initClips();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initClips() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		AudioInputStream metronomeHiAIS;

		metronomeHiAIS = AudioSystem.getAudioInputStream(new File(Constants.METRONOME_WAVES + File.separator + metronomeSoundHi).getAbsoluteFile());
		metronomeHiClip = AudioSystem.getClip(DmxLive.getConfiguration().getDrummerMixer().getMixerInfo());
		metronomeHiClip.open(metronomeHiAIS);

		AudioInputStream metronomeLowAIS;

		metronomeLowAIS = AudioSystem.getAudioInputStream(new File(Constants.METRONOME_WAVES + File.separator + metronomeSoundLow).getAbsoluteFile());
		metronomeLowClip = AudioSystem.getClip(DmxLive.getConfiguration().getDrummerMixer().getMixerInfo());
		metronomeLowClip.open(metronomeLowAIS);
		
	}

	@Override
	public void run() {
		
		long absoluteStartTime = System.currentTimeMillis();
		
		long currentAbsoluteTime;
		long lastPlayedTime = absoluteStartTime;

		while (isRunning) {
			
			currentAbsoluteTime = getCurrentAbsoluteTime(absoluteStartTime);
			
			if ((!isFirstBeatPlayed || 
					((currentAbsoluteTime) % metronomeOneBeatModulo <= metronomeOneBeatTolerance) &&
					currentAbsoluteTime != lastPlayedTime)) {

				play(currentAbsoluteTime);

				observer.updatePosition(currentAbsoluteTime);

				isFirstBeatPlayed = true;
				lastPlayedTime = currentAbsoluteTime;
			}
			
			playSoundAndSleep();
		}

	}

	private void playSoundAndSleep() {

		if (playHi) {

			metronomeHiClip.start();

			try {
				Thread.sleep(sleepTimeAfterBeat);
			} catch (InterruptedException ignore) {
			}

			playHi = false;
			playLow = false;

			metronomeHiClip.setFramePosition(0);


		} else if (playLow) {

			metronomeLowClip.start();
			try {
				Thread.sleep(sleepTimeAfterBeat);
			} catch (InterruptedException ignore) {
			}

			playLow = false;
			playHi = false;

			metronomeLowClip.setFramePosition(0);

		} else {

			try {
				Thread.sleep(refreshTimeout);
			} catch (InterruptedException ignore) {
			}

		}
	}

	public void play(long millis) {
		TimeInfo info = new TimeInfo(signature, bpm, millis);
		if (info.getBeat() == 0 && (info.getSubBeat() < 0.25 || info.getSubBeat() > 0.75)) {
			playHi();
		} else {
			playLow();
		}
	}

	/**
	 * Plays the first beat sound of the metronome.
	 */
	public void playHi() {
		playHi = true;
	}

	/**
	 * Plays the minor beat sound of the metronome.
	 */
	public void playLow() {
		playLow = true;
	}

	/**
	 * Stop the execution of the metronome.
	 */
	public synchronized void stop() {
		isRunning = false;
	}

	public synchronized void setMode(Song.MetronomeMode mode) {
		this.mode = mode;
	}
}
