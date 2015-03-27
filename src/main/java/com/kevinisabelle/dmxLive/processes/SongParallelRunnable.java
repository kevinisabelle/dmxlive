package com.kevinisabelle.dmxLive.processes;

import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import javax.sound.sampled.Clip;

/**
 * A thread that can run in parallel of a song or dmx process
 * @author kevin
 */
public abstract class SongParallelRunnable implements Runnable{
	
	protected Clip playedSongAudio;
	protected TimeSignature signature;
	protected DmxRunnableObserver observer;
	protected int bpm = 120;
	
	public SongParallelRunnable(Clip plClip, TimeSignature timeSignature, DmxRunnableObserver observer, int bpm){
		this.playedSongAudio = plClip;
		this.signature = timeSignature;
		this.observer = observer;
		this.bpm = bpm;
	}
	
	protected Long getCurrentAbsoluteTime(long absoluteStartTime){
		
		long currentAbsoluteTime = 0;
		
		if (playedSongAudio == null){
				currentAbsoluteTime = System.currentTimeMillis() - absoluteStartTime;
			} else {
				
				currentAbsoluteTime = (playedSongAudio.getMicrosecondPosition() / 1000) - DmxLive.getConfiguration().getDmxRunnableOffset();
			}
		
		return currentAbsoluteTime;
	}
}
