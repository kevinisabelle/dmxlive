package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import com.kevinisabelle.dmxlive.core.engine.MasterClock;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractCommandExecutor implements Runnable {

	protected boolean isRunning = true;
	protected Integer refreshRate;
	protected MasterClock masterClockRef;
	protected List<Command> commandsQueue;
	protected List<Command> runningCommands;
	protected boolean loop;

	private Iterator<TimedEvent> programIterator = null;
	private TimedEvent lastValue = null;

	private List<Long> executionTimes = new LinkedList<Long>();
	
	private static Logger logger = Logger.getLogger(AbstractCommandExecutor.class);

	
	public AbstractCommandExecutor(MasterClock masterClock, Integer refreshRate){
		this.masterClockRef = masterClock;
		this.refreshRate = refreshRate;
	}

	protected abstract void SendEventsToDrivers(List<TimedEvent> events);
	
	private void ProcessCommandsToCurrentList(){
		
	}
	
	private List<TimedEvent> GetReadyTimedEvent(long from, long to){
		List<TimedEvent> result = new LinkedList<>();
		
		return result;
	}
	
	@Override
	public void run(){
		
		long lastPosition = -1;
		long currentPosition = -1;
		
		while (isRunning){
			
			lastPosition = currentPosition;
			
			//Get position from master clock
			currentPosition = masterClockRef.getAbsoluteTime();
			
			//Transfer commands to current execution if needed
			ProcessCommandsToCurrentList();
			
			//Get timedEvent to send now based on current time
			List<TimedEvent> events = GetReadyTimedEvent(lastPosition, currentPosition);
			
			//Send events to drivers
			SendEventsToDrivers(events);
			
			
			
			
			
			
			
			
			/*
			
			long totalFrames = 0;

		long waitTime = Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm)).longValue() / Constants.CHECK_RESOLUTION;

		observer.logMessage("DmxRunnable using: Waittime: " + waitTime, 0);
		observer.logMessage("Total number of DMX events to play: " + dmxProgram.size(), 0);

		long absoluteStartTime = System.currentTimeMillis();

		// Start the metronome SongParallelRunnable
		if (metronomeRunnable != null){
			Thread metronomeThread = new Thread(metronomeRunnable, "MetronomeThread");
			metronomeThread.start();
		}

		logger.info("Process started: Wait time: " + waitTime + "ms");

		Long currentAbsoluteTime;

		while (isRunning) {

			totalFrames++;

			long startTime = System.currentTimeMillis();

			currentAbsoluteTime = getCurrentAbsoluteTime(absoluteStartTime);

			popCurrentValuesFromListAndSendDmx(currentAbsoluteTime, Constants.CHECK_RESOLUTION);

			long executionDelay = System.currentTimeMillis() - startTime;
			if (executionDelay > waitTime) {
				executionTimes.add(executionDelay);
			}

			if (playedSongAudio != null && playedSongAudio.getMicrosecondPosition() >= playedSongAudio.getMicrosecondLength()){
				playedSongAudio = null;
			}

			if (dmxProgram.isEmpty() && playedSongAudio == null){

				logger.info("Completed execution (end of script + end of song)");
				isRunning = false;

				//DmxLive.stopSong();

				observer.stopNotify(isRunOnSong);
			}

			try {
				if (waitTime - executionDelay <= 0) {
					continue;
				}
				Thread.sleep(waitTime - executionDelay);
			} catch (Exception e) {
			}
		}

		if (metronomeRunnable != null){
			metronomeRunnable.stop();
		}

		// Calculate stats
		Long totalExecutionTimes = 0l;
		for (Long longValue : executionTimes) {
			totalExecutionTimes += longValue;
		}

		if (executionTimes.size() > 0){
			totalExecutionTimes = totalExecutionTimes / executionTimes.size();
		}

		Long numberOverTime = Math.round(Integer.valueOf(executionTimes.size()).doubleValue() / Long.valueOf(totalFrames).doubleValue() * 100.0);

		logger.info("Completed execution (thread stopped):");
		logger.info("Frame execution average (over wait time only): " + totalExecutionTimes + "ms ");
		logger.info(executionTimes.size() + " / " + totalFrames + " (" + numberOverTime + "%)");

		observer.stopNotify(false);
			
			*/
			
			try {
				Thread.sleep(1000 / refreshRate);
			} catch (InterruptedException ex) {
				//Logger.getLogger(AbstractCommandExecutor.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			processQueuedCommands();
			
			sendTimedEventsToDrivers();
					
		}
	}
	
	private void cleanEventList(){
		
		/*synchronized (this) {

			long startTimeMillis = TimeHelper.getMilliseconds(startTime, signature, bpm);
			List<TimedDmxEvent> toRemove = new ArrayList<TimedDmxEvent>();

			for (TimedEvent value : dmxProgram) {
				if (value.getMillis() < startTimeMillis) {
					toRemove.add(value);
				}
			}

			dmxProgram.removeAll(toRemove);
		}*/
	}
	
	private void processQueuedCommands(){
		
		for (Command command  : commandsQueue){
			eventsQueue.addAll(command.compute());
		}
		
	}
	
	private void sendTimedEventsToDrivers(){
		
		
	}
	
	/**
	 * Get the DMX events till the absolute position passed in parameters.
	 * Once the events are found, they are sent to OpenDMX and removed from the list.
	 * @param absolutePosition
	 * @param tolerance
	 */
	private void popCurrentValuesFromListAndSend(long absolutePosition, long tolerance) {

		if (dmxProgramIterator == null) {
			dmxProgramIterator = dmxProgram.iterator();
		}

		TimedDmxEvent value = null;

		while (dmxProgramIterator.hasNext()) {

			if (lastValue == null) {
				value = dmxProgramIterator.next();
			} else {
				value = lastValue;
			}

			if (value.getMillis() <= absolutePosition) {
				dmxProgramIterator.remove();

				if (logDMXEvents){
					observer.logMessage("DMX: " + value, absolutePosition);
				}
				try {
					dmxManager.transmit(value.getChannel() - 1, value.getValue());
				} catch (Exception ex) {
					logger.error("Cannot send dmx signal: " + ex.getMessage());
				}
				lastValue = null;
			} else {
				lastValue = value;
				break;
			}
		}
	}

	/**
	 * Stops the dmx process.
	 */
	public void stop() {
		logger.debug("Stopping dmx execution.");
		isRunning = false;
		synchronized (this) {
			dmxProgram.clear();
			dmxProgramIterator = dmxProgram.iterator();
		}
	}

	/**
	 * Set the enabling status of the DMX logging publish to observer.
	 * @param isEnabled
	 */
	public void enableDMXLogging(boolean isEnabled){
		this.logDMXEvents = isEnabled;
	}

	
	
	
	
}
