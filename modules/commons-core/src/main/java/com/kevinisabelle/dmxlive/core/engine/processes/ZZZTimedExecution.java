package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.core.Constants;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import javax.sound.sampled.Clip;

/**
 * A thread that can run in parallel of a song or dmx process
 * @author kevin
 */
public abstract class ZZZTimedExecution implements Runnable {

	protected Clip playedSongAudio;
	protected TimeSignature signature;
	protected IExecutoObserver observer;
	protected int bpm = 120;

	public ZZZTimedExecution(Clip plClip, TimeSignature timeSignature, IExecutoObserver observer, int bpm){
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

				currentAbsoluteTime = (playedSongAudio.getMicrosecondPosition() / 1000) - Constants.DMXRUNNABLE_OFFSET;
			}

		return currentAbsoluteTime;
	}
}
