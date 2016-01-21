package com.kevinisabelle.dmxlive.core.engine;

import com.kevinisabelle.dmxlive.core.Constants;
import com.kevinisabelle.dmxlive.music.TempoMap;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.logging.Level;
import javax.sound.sampled.Clip;
import org.apache.log4j.Logger;

/**
 *
 * @author kisabelle
 */
public class MasterClock implements Runnable {

	public enum ClockState {
		NOT_STARTED,
		RUNNING,
		STOPPED,
		PAUSED,
		INVALIDATED
	};
	
	Logger logger = Logger.getLogger(MasterClock.class);

	private long resolutionHertz;
	private long currentAbsolutePositionMillis;
	private long absoluteStartTimeMillis;
	private ClockState state = ClockState.NOT_STARTED;
	
	protected Clip audioTimeReference = null;
	protected TempoMap tempoMap;
	
	public MasterClock(TempoMap map){
		this.audioTimeReference = null;
		this.tempoMap = map;
		this.absoluteStartTimeMillis = -1;
	}

	public MasterClock(TempoMap map, Clip pClip){
		this.audioTimeReference = pClip;
		this.tempoMap = map;
		this.absoluteStartTimeMillis = -1;
	}
	
	public void start(){
		absoluteStartTimeMillis = System.currentTimeMillis();
		state = ClockState.RUNNING;
	}

	public void stop(){
		state = ClockState.STOPPED;
	}
	
	public void pause(){
		state = ClockState.PAUSED;
	}
	
	public void invalidate(){
		state = ClockState.INVALIDATED;
	}
		
	private Long getCurrentAbsoluteTime(long absoluteStartTime){

		long currentAbsoluteTime = 0;

		if (audioTimeReference == null){
				currentAbsoluteTime = System.currentTimeMillis() - absoluteStartTime;
			} else {

				currentAbsoluteTime = (audioTimeReference.getMicrosecondPosition() / 1000) - Constants.DMXRUNNABLE_OFFSET;
			}

		return currentAbsoluteTime;
	}

	@Override
	public void run() {

		logger.info("Starting MasterClock at resolution:" + resolutionHertz + "Hz");

		while (state != ClockState.INVALIDATED){

			try {
				Thread.sleep(1000l / resolutionHertz);
			} catch (InterruptedException ex) {
				java.util.logging.Logger.getLogger(MasterClock.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			currentAbsolutePositionMillis = getCurrentAbsoluteTime(absoluteStartTimeMillis);
		}

		logger.info("Terminated MasterClock.");
	}

	public long getAbsoluteTime(){
		return currentAbsolutePositionMillis;
	}
	
	public TimeInfo getAbsoluteTimeMusical(){
		return tempoMap.getTimeInfoAt(currentAbsolutePositionMillis);
	}

}
