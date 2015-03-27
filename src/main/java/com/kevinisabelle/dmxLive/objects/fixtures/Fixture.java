package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import java.util.List;

/**
 * Abstract class for all fixtures implementations.
 * @author kevin
 */
public abstract class Fixture {

	private int channel;
	
	public static final String OP_DIM = "Dim";
	public static final String OP_STROBE = "Strobe";
	public static final String OP_FADE = "Fade";
	public static final String OP_MODE = "Mode";
	public static final String OP_BLINK = "Blink";
	public static final String OP_PULSE = "Pulse";
	public static final String OP_RIFF = "Riff";

	public abstract List<TimedDmxValue> convertToDmx(TimeInfo startTime, TimeSignature signature, int bpm, String params);

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	
}
