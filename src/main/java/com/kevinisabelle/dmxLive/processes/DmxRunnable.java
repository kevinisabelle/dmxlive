package com.kevinisabelle.dmxLive.processes;

import com.kevinisabelle.dmxLive.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.dmx.DmxManager;
import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.Song;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import com.kevinisabelle.dmxLive.objects.TimedLyricsValue;
import com.kevinisabelle.dmxLive.objects.TimedMidiValue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.Clip;
import org.apache.log4j.Logger;

/**
 * This runnable will send the TimedDmxValues when the time arrive based on the
 * playedSongAudio clip's reference as absolute position. This runnable also
 * provide an observer mechanism to publish messages and current position.
 *
 * @author kevin
 */
public class DmxRunnable extends SongParallelRunnable {

	private boolean isRunning = true;

	private List<TimedDmxValue> dmxProgram;
	private Iterator<TimedDmxValue> dmxProgramIterator = null;
	private TimedDmxValue lastValue = null;
	private boolean logDMXEvents = false;
	private DmxManager dmxManager;
	private MidiDevice midiDevice;
	private List<Long> executionTimes = new LinkedList<Long>();
	private MetronomePlayerRunnable metronomeRunnable;
	
	private static Logger logger = Logger.getLogger(DmxRunnable.class);
	
	private boolean isRunOnSong = false;

	/**
	 * Initializes a new DmxRunnable process
	 * @param signature Time signature used for the process
	 * @param bpm Beats per minute used for the process
	 * @param dmxProgram The dmx program to execute
	 * @param manager The dmx manager to send the signals
	 * @param playedSongAudio The reference audio clip
	 * @param startTime The start time of the process.
	 * @param observer The observer used to publish information
	 * @param cleanEventsBeforeStarTtime If set to true, will remove all events before the start time.
	 * @param metronomeMode Mode to use with the metronome thread
	 * @param metronomeSoundHi Wav sound to use to play metronome hi beats
	 * @param metronomeSoundLow Wav sound to use to play metronome low beats.
	 */

	public DmxRunnable(TimeSignature signature, int bpm, List<TimedDmxValue> dmxProgram, 
			DmxManager manager, Clip playedSongAudio, TimeInfo startTime, 
			DmxRunnableObserver observer, boolean cleanEventsBeforeStarTtime,
			Song.MetronomeMode metronomeMode, String metronomeSoundHi, String metronomeSoundLow, MidiDevice midiDevie) {

		super(playedSongAudio, signature, observer, bpm);
		this.midiDevice = midiDevie;
		this.dmxProgram = dmxProgram;
		this.dmxManager = manager;
		
		isRunOnSong = playedSongAudio != null;

		// Removes all the dmx events before the start position if set to true.
		if (cleanEventsBeforeStarTtime) {
			synchronized (this) {

				long startTimeMillis = TimeHelper.getMilliseconds(startTime, signature, bpm);
				List<TimedDmxValue> toRemove = new ArrayList<TimedDmxValue>();

				for (TimedDmxValue value : dmxProgram) {
					if (value.getMillis() < startTimeMillis) {
						toRemove.add(value);
					}
				}

				dmxProgram.removeAll(toRemove);
			}
		}
		
		logger.debug("dmx signal to execute: " + dmxProgram.size());
		
		if (metronomeMode != null && metronomeMode != metronomeMode.OFF && metronomeSoundHi != null){
			metronomeRunnable = new MetronomePlayerRunnable(Constants.METRONOME_REFRESH_TIMEOUT, metronomeMode, metronomeSoundHi, metronomeSoundLow, bpm, signature, observer, playedSongAudio);
		}		
	}

	@Override
	public void run() {
		
		long totalFrames = 0;

		long waitTime = Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm)).longValue() / DmxLive.getConfiguration().getDmxRunnableCheckResolution();

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
			
			popCurrentValuesFromListAndSendDmx(currentAbsoluteTime, DmxLive.getConfiguration().getDmxRunnableCheckResolution());

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
				
				DmxLive.stopSong();
				
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
		
	}

	/**
	 * Get the DMX events till the absolute position passed in parameters.
	 * Once the events are found, they are sent to OpenDMX and removed from the list.
	 * @param absolutePosition
	 * @param tolerance 
	 */
	private void popCurrentValuesFromListAndSendDmx(long absolutePosition, long tolerance) {

		if (dmxProgramIterator == null) {
			dmxProgramIterator = dmxProgram.iterator();
		}

		TimedDmxValue value = null;

		while (dmxProgramIterator.hasNext()) {

			if (lastValue == null) {
				value = dmxProgramIterator.next();
			} else {
				value = lastValue;
			}

			if (value.getMillis() <= absolutePosition) {
				
				dmxProgramIterator.remove();
				
				if (value instanceof TimedMidiValue){
				
					try {
						SendMidiEvent((TimedMidiValue)value);
						
						lastValue = null;
						
						if (logDMXEvents){
							observer.logMessage("Midi patch change: " + (((TimedMidiValue) value)).getBankValue(), absolutePosition);
						}
						
					} catch (InvalidMidiDataException ex) {
						logger.error(ex);
					} catch (MidiUnavailableException ex) {
						logger.error(ex);
					}
					
				} else if (value instanceof TimedLyricsValue){
				
						BluetoothServer.SetAvailableText(((TimedLyricsValue)value).getLyricsContent());
						if (logDMXEvents){
							observer.logMessage("Lyrics: " + ((TimedLyricsValue)value).getLyricsContent(), absolutePosition);
						}
						lastValue = null;
					
				} else {
				
					if (logDMXEvents){
						observer.logMessage("DMX: " + value, absolutePosition);
					}
					try {
						dmxManager.sendSignal(value.getChannel() - 1, value.getValue());
					} catch (Exception ex) {
						logger.error("Cannot send dmx signal: " + ex.getMessage());
					}
					lastValue = null;
				}
				
			} else {
				lastValue = value;
				break;
			}
		}
	}

	private void SendMidiEvent(TimedMidiValue event) throws InvalidMidiDataException, MidiUnavailableException{
		
		MidiMessage message = null;
		MidiMessage message2 = null;
		
		if (event.getBankValue() != -1){
			observer.logMessage("Midi bank change: " + event.getBankValue(), event.getMillis());
			message = new ShortMessage(ShortMessage.CONTROL_CHANGE, event.getChannel(), 0, 0);
			message2 = new ShortMessage(ShortMessage.PROGRAM_CHANGE, event.getChannel(), 0, event.getBankValue());
		}
		
		if (message2 != null){
			
			if (!midiDevice.isOpen()){
				midiDevice.open();
				
			}
			
			midiDevice.getReceiver().send(message, -1);
			midiDevice.getReceiver().send(message2, -1);
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
