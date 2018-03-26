/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.objects;

import javax.sound.midi.MidiChannel;

/**
 *
 * @author kevin
 */
public class TimedMidiValue extends TimedDmxValue {
	
	private int ccValue;
	private int bankValue;
	private int programValue;
	
	
	public TimedMidiValue(long millis, int channel, int value, int ccValue, int bankValue, int programValue) {
		super(millis, channel, value);
		this.ccValue = ccValue;
		this.bankValue = bankValue;
		this.programValue = programValue;
	}

	/**
	 * @return the ccValue
	 */
	public int getCcValue() {
		return ccValue;
	}

	/**
	 * @param ccValue the ccValue to set
	 */
	public void setCcValue(int ccValue) {
		this.ccValue = ccValue;
	}

	/**
	 * @return the bankValue
	 */
	public int getBankValue() {
		return bankValue;
	}

	/**
	 * @param bankValue the bankValue to set
	 */
	public void setBankValue(int bankValue) {
		this.bankValue = bankValue;
	}

	/**
	 * @return the programValue
	 */
	public int getProgramValue() {
		return programValue;
	}

	/**
	 * @param programValue the programValue to set
	 */
	public void setProgramValue(int programValue) {
		this.programValue = programValue;
	}
	
}
