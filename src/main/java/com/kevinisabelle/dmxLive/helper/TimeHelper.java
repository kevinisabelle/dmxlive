package com.kevinisabelle.dmxLive.helper;

import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;

/**
 * Couple of helper functions to deal with absolute time conversion based on
 * time signature and timeInfo
 *
 * @author kevin
 */
public class TimeHelper {

	public static final int NOTE_WHOLE = 1;
	public static final int NOTE_HALF = 2;
	public static final int NOTE_QUARTER = 4;
	public static final int NOTE_EIGHT = 8;
	public static final int NOTE_SIXTEENTH = 16;

	protected TimeHelper(){
		
	}
	
	/**
	 * Get the absolute milliseconds value for a quarter beat at a certain bpm
	 *
	 * @param bpm
	 * @return
	 */
	public static double getQuarterMilliseconds(int bpm) {
		return (60000.0) / Integer.valueOf(bpm).doubleValue();
	}

	/**
	 * Get the absolute number of quarters required to produce that timeInfo
	 *
	 * @param timeInfo
	 * @param signature
	 * @return
	 */
	public static double getTimeInQuarters(TimeInfo timeInfo, TimeSignature signature) {

		double beatFraction = timeInfo.getSubBeat();
		double leftOverBeats = timeInfo.getBeat();
		double measureBeats = timeInfo.getMeasure() * signature.getNumberOfBeats();

		double totalBeats = beatFraction + leftOverBeats + measureBeats;
		double beatsInQuaters = totalBeats * (4.0 / Double.valueOf(signature.getBeatUnit()));

		return beatsInQuaters;

	}

	/**
	 * Get the absolute time in milliseconds for that timeInfo based on the time
	 * signature and bpm
	 *
	 * @param timeInfo
	 * @param signature
	 * @param bpm
	 * @return
	 */
	public static long getMilliseconds(TimeInfo timeInfo, TimeSignature signature, int bpm) {
		
		double quarters = getTimeInQuarters(timeInfo, signature);
		return Double.valueOf(quarters * Double.valueOf(getQuarterMilliseconds(bpm))).longValue();

	}
	
	public static long getFrequency(TimeSignature signature, int bpm, double beatUnit){
		return Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm) / (beatUnit / 4)).longValue();
	}
	
	public static long getOnTime(TimeSignature signature, int bpm, double percentBeatOn){
		return Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm) / (signature.getBeatUnit() / 4.0) * (percentBeatOn)).longValue();
	}
}
