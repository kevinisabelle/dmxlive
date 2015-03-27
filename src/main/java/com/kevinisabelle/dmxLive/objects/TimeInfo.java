package com.kevinisabelle.dmxLive.objects;

import com.kevinisabelle.dmxLive.helper.TimeHelper;

/**
 *
 * @author kevin
 */
public class TimeInfo implements Comparable<TimeInfo>{

	public static final int BEAT_UNIT_HALF = 2;
	public static final int BEAT_UNIT_QUARTER = 4;
	public static final int BEAT_UNIT_EIGHT = 8;
	public static final int BEAT_UNIT_SIXTEENTH = 16;
	
	public static final String PERCENT = "%";
	public static final String FRACTION = "/";
	
	private int measure;
	private int beat;
	private double subBeat;

	/**
	 * Required format is measure:beat:subbeat Subbeat can be written as
	 * percentage or fraction (50% or 15/16) Examples: 4:0:0 4:3:0 4:3:50%
	 * 4:3:3/4 4:3
	 *
	 * @param timeInfoString
	 */
	public TimeInfo(String timeInfoString) {
		
		String[] timeArray = timeInfoString.split(ScriptCommand.TIME_DELIMITER);
		this.measure = Integer.parseInt(timeArray[0]);
		this.beat = Integer.parseInt(timeArray[1]);

		if (timeArray.length > 2) {
			if (timeArray[2].contains(PERCENT)) {
				this.subBeat = Double.valueOf(timeArray[2].replaceAll(PERCENT, "")) / 100.0;
			} else if (timeArray[2].contains(FRACTION)) {
				String[] subBeatSections = timeArray[2].split(FRACTION);
				this.subBeat = Double.parseDouble(subBeatSections[0]) / Double.parseDouble(subBeatSections[1]);
			} else {
				this.subBeat = Double.parseDouble(timeArray[2]);
			}
		}
	}

	public TimeInfo(int measure, int beat, double subBeat) {
		this.measure = measure;
		this.beat = beat;
		this.subBeat = subBeat;
	}
	
	public TimeInfo(TimeSignature signature, int bpm, long millis){
		
		double beatTime = TimeHelper.getQuarterMilliseconds(bpm);
		
		switch (signature.getBeatUnit()){
			case BEAT_UNIT_EIGHT:
				
				beatTime /= 2.0;
				break;
				
			case BEAT_UNIT_SIXTEENTH:
				
				beatTime /= 4.0;
				break;
		}
		
		double beats = millis / beatTime;
		
		this.measure = Double.valueOf(Double.valueOf(beats) / signature.getNumberOfBeats()).intValue();
		this.beat = 0;
		this.subBeat = 0.0;
		Double beatsDecimal = Double.valueOf((millis - TimeHelper.getMilliseconds(this, signature, bpm))) / Double.valueOf(beatTime);
				
		this.beat = beatsDecimal.intValue();
		this.subBeat = (beatsDecimal - Math.floor(this.beat)) % beatTime;
		
	}

	/**
	 * Add timeInfo values together while respecting timeSignature
	 *
	 * @param toAdd
	 * @param signature
	 * @return
	 */
	public TimeInfo add(TimeInfo toAdd, TimeSignature signature) {

		TimeInfo newtime = new TimeInfo(this.measure, this.beat, this.subBeat);

		double totalBeats = this.getInBeats(signature) + toAdd.getInBeats(signature);

		newtime.setMeasure(Double.valueOf(Math.floor(totalBeats / Double.valueOf(signature.getNumberOfBeats()))).intValue());
		newtime.setBeat(Double.valueOf(Math.floor(totalBeats)).intValue() - newtime.getMeasure() * signature.getNumberOfBeats());
		newtime.setSubBeat(totalBeats - Math.floor(totalBeats));

		return newtime;
	}

	@Override
	public String toString() {
		return (this.measure) + ":" + this.getBeat() + ":" + (this.getSubBeat() == 0.0 ? "0" : Math.floor(this.getSubBeat() * 10000.0) / 100.0 + "%");
	}

	/**
	 * Get this timeInfo value in beats (based on the time signature).
	 *
	 * @param signature
	 * @return
	 */
	public double getInBeats(TimeSignature signature) {

		double beats = this.measure * signature.getNumberOfBeats();
		beats += this.beat;
		beats += this.subBeat;

		return beats;
	}

	/**
	 * @return the measure
	 */
	public int getMeasure() {
		return measure;
	}

	/**
	 * @param measure the measure to set
	 */
	public void setMeasure(int measure) {
		this.measure = measure;
	}

	/**
	 * @return the beat
	 */
	public int getBeat() {
		return beat;
	}

	/**
	 * @param beat the beat to set
	 */
	public void setBeat(int beat) {
		this.beat = beat;
	}

	/**
	 * @return the subBeat
	 */
	public double getSubBeat() {
		return subBeat;
	}

	/**
	 * @param subBeat the subBeat to set
	 */
	public void setSubBeat(double subBeat) {
		this.subBeat = subBeat;
	}

	@Override
	public int compareTo(TimeInfo o) {
		
		Double beats = getInBeats(new TimeSignature("4/4"));
		Double beatsToCompare = o.getInBeats(new TimeSignature("4/4"));
		
		return beats.compareTo(beatsToCompare);
	}
	
}
