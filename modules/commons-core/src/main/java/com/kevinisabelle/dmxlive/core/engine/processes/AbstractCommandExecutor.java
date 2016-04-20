package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.api.Driver;
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

	private static final Logger LOGGER = Logger.getLogger(AbstractCommandExecutor.class);
	
	protected MasterClock masterClockRef;
	protected List<Command> commandsQueue;
	protected List<Command> runningCommands;
	
	protected boolean isRunning = true;
	protected boolean debug = false;
	protected long dutyCycleTime = 30;
	protected long resolution = 24;

	private final Iterator<TimedEvent> programIterator = null;
	private TimedEvent lastValue = null;
	
	private Driver outputDriver;
	
	public AbstractCommandExecutor(MasterClock masterClock, long dutyCycleTime, long resolution){
		this.masterClockRef = masterClock;
		this.dutyCycleTime = dutyCycleTime;
		this.resolution = resolution;
	}

	protected abstract void SendEventsToDrivers(List<TimedEvent> events);
	
	private void ProcessCommandsToCurrentList(){
		
	}
	
	private List<TimedEvent> GetReadyTimedEvent(long toTime){
		
		List<TimedEvent> result = new LinkedList<>();
		
		for (Command command : runningCommands){
			command.getTimedEventAt(0, toTime, masterClockRef.getTempoMap());
		}
		
		return result;
	}
	
	@Override
	public void run(){
		
		long endCycle = -1;
		long startCycle = -1;
		long waitTime;
		
		while (isRunning){

			//Get position from master clock
			startCycle = masterClockRef.getAbsoluteTime();
			
			//Transfer commands to current execution if needed
			ProcessCommandsToCurrentList();
			
			//Get timedEvent to send now based on current time
			List<TimedEvent> events = GetReadyTimedEvent(startCycle);
			
			//Send events to drivers
			SendEventsToDrivers(events);
			
			processQueuedCommands();
			
			sendTimedEventsToDrivers();
			
			endCycle = masterClockRef.getAbsoluteTime();
			
			waitTime = dutyCycleTime - (endCycle - startCycle);
			
			if (debug && waitTime < 1){
				LOGGER.debug("lag: " + -waitTime);
			}
			
			try {
				Thread.sleep(waitTime < 1 ? 1 : waitTime);
			} catch (InterruptedException ex) {
				
			}
			
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
					LOGGER.error("Cannot send dmx signal: " + ex.getMessage());
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
		LOGGER.debug("Stopping dmx execution.");
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
