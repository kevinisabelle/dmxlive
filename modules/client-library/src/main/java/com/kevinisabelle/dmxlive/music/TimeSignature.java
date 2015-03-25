package com.kevinisabelle.dmxlive.music;

/**
 *
 * @author kevin
 */
public class TimeSignature {

	private int numberOfBeats;
	private int beatUnit;

	public TimeSignature(String signature) {
		
		String[] parts = signature.split("/");
		this.numberOfBeats = Integer.parseInt(parts[0]);
		this.beatUnit = Integer.parseInt(parts[1]);
	}

	public TimeSignature(int numberOfBeats, int beatUnit) {
		this.numberOfBeats = numberOfBeats;
		this.beatUnit = beatUnit;
	}

	/**
	 * @return the numberOfBeats
	 */
	public int getNumberOfBeats() {
		return numberOfBeats;
	}

	/**
	 * @param numberOfBeats the numberOfBeats to set
	 */
	public void setNumberOfBeats(int numberOfBeats) {
		this.numberOfBeats = numberOfBeats;
	}

	/**
	 * @return the beatUnit
	 */
	public int getBeatUnit() {
		return beatUnit;
	}

	/**
	 * @param beatUnit the beatUnit to set
	 */
	public void setBeatUnit(int beatUnit) {
		this.beatUnit = beatUnit;
	}

	public double getQuartersInMeasures() {
		return (Double.valueOf(this.getNumberOfBeats()) * (4.0 / Double.valueOf(this.getBeatUnit())));
	}

	@Override
	public String toString() {
		return this.getNumberOfBeats() + "/" + this.getBeatUnit();
	}
}
