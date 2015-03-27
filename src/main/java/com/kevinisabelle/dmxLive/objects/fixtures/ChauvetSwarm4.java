package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.CommandParameters;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fixture implementation for the Chauvet Swarm 4
 * 
 * 
 * @author kevin
 */
public class ChauvetSwarm4  extends Fixture {

	public static final int CHANNEL_COLOR = 0;
	public static final int CHANNEL_AUTOSPEED = 1;
	public static final int CHANNEL_STROBE = 2;
	public static final int CHANNEL_MOTOR_ROTATION = 3;
	
	public static final int COLOR_BLACK = 0;
	public static final int COLOR_RED = 10;
	public static final int COLOR_GREEN = 15;
	public static final int COLOR_BLUE = 20;
	public static final int COLOR_AMBER = 25;
	public static final int COLOR_REDGREEN = 40;
	public static final int COLOR_REDBLUE = 35;
	public static final int COLOR_GREENBLUE = 30;
	public static final int COLOR_REDAMBER = 45;
	public static final int COLOR_GREENAMBER = 50;
	public static final int COLOR_BLUEAMBER = 55;
	public static final int COLOR_REDGREENBLUE = 60;
	public static final int COLOR_REDGREENAMBER = 116;
	public static final int COLOR_REDBLUEAMBER = 70;
	public static final int COLOR_GREENBLUEAMBER = 65;
	public static final int COLOR_ALL = 75;
	public static final int COLOR_SOUND_ACTIVE = 250;
	
	public static final int STROBE_OFF = 0;
	public static final int[] STROBE_SPEED = new int[]{5,254};
	public static final int STROBE_SOUND_ACTIVE = 255;
	
	public static final int ROTATION_OFF = 0;
	public static final int[] ROTATION_CLOCKWISE = new int[]{5,255};
	public static final int[] ROTATION_COUNTER_CLOCKWISE = new int[]{134,255};
		
	@Override
	public List<TimedDmxValue> convertToDmx(TimeInfo startTime, TimeSignature signature, int bpm, String params) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		String[] paramsArr = params.split(":",2);
		CommandParameters parameters = new CommandParameters(paramsArr[1]);

		if (paramsArr[0].equals(OP_DIM)) {

			values.addAll(dim(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					0,
					parameters.getSpeed()));

		} else if (paramsArr[0].equals(OP_STROBE)) {

			values.addAll(dim(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getSpeed(),
					parameters.getDimmers()[0]));

		} else if (paramsArr[0].equals(OP_BLINK)) {

			values.addAll(blink(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDuration(),
					parameters.getNoteUnit(),
					parameters.getOnTime(),
					parameters.getSpeed()));

		} else if (paramsArr[0].equals(OP_RIFF)) {

			values.addAll(riff(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getSpeed(),
					parameters.getNoteUnit(),
					parameters.getRiffNotes()));

		} else {
			throw new RuntimeException("Unknown operation: " + paramsArr[0] + " (" + params +")");
		}

		return values;
	}

	private List<TimedDmxValue> dim(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, int strobeSpeed, int motorSpeed) {

		// Always reset stobe mode when applying dim.
		
		boolean isRed = false;
		boolean isGreen = false;
		boolean isBlue = false;
		boolean isAmber = false;
		
		int colorValue = 0;
		
		for (Color color : colors){
			
			if (color.getR() == 255 && color.getG() == 0 && color.getB() == 0){
				isRed = true;
			} else if (color.getR() == 0 && color.getG() == 255 && color.getB() == 0){
				isGreen = true;
			} else if (color.getR() == 0 && color.getG() == 0 && color.getB() == 255){
				isBlue = true;
			} else if (color.getR() == 255 && color.getG() == 255 && color.getB() == 0){
				isAmber = true;
			}
		}
		
		if (isRed && isGreen && isBlue && isAmber){
			colorValue = COLOR_ALL;
		} else if (isRed && isGreen && isBlue){
			colorValue = COLOR_REDGREENBLUE;
		} else if (isRed && isGreen && isAmber){
			colorValue = COLOR_REDGREENAMBER;
		} else if (isRed && isBlue && isAmber){
			colorValue = COLOR_REDBLUEAMBER;
		} else if (isGreen && isBlue && isAmber){
			colorValue = COLOR_GREENBLUEAMBER;
		} else if (isRed && isAmber){
			colorValue = COLOR_REDAMBER;
		} else if (isRed && isBlue){
			colorValue = COLOR_REDBLUE;
		} else if (isRed && isGreen){
			colorValue = COLOR_REDGREEN;
		} else if (isGreen && isBlue){
			colorValue = COLOR_GREENBLUE;
		} else if (isGreen && isAmber){
			colorValue = COLOR_GREENAMBER;
		} else if (isBlue && isAmber){
			colorValue = COLOR_BLUEAMBER;
		} else if (isRed){
			colorValue = COLOR_RED;
		} else if (isGreen){
			colorValue = COLOR_GREEN;
		} else if (isBlue){
			colorValue = COLOR_BLUE;
		} else if (isAmber){
			colorValue = COLOR_AMBER;
		} else {
			colorValue = COLOR_BLACK;
		}
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		if (strobeSpeed != 0){
			
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_STROBE, (STROBE_SPEED[1] - STROBE_SPEED[0]) * strobeSpeed / 100 + STROBE_SPEED[0]));
		
		} else {

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_STROBE, STROBE_OFF));
		}
		
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_COLOR, colorValue));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_MOTOR_ROTATION, (ROTATION_CLOCKWISE[1] - ROTATION_CLOCKWISE[0]) * motorSpeed / 100 + ROTATION_CLOCKWISE[0]));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_AUTOSPEED, 100));
		
		return values;
	}	
	
	private List<TimedDmxValue> blink(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, TimeInfo executionTime, Double beatFrequency, Double beatOn, Integer motorSpeed) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		
		long frequency = TimeHelper.getFrequency(signature, bpm, beatFrequency);
		long onTime = TimeHelper.getOnTime(signature, bpm, beatOn);
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long finalTime = currentTime + TimeHelper.getMilliseconds(executionTime, signature, bpm);
		
		while (currentTime < finalTime) {
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, new Color[]{colors[0]}, 0, motorSpeed));
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime+onTime), signature, bpm, new Color[]{colors[1]}, 0, motorSpeed));
			
			currentTime += frequency;
		}
		
		return values;
		
	}		
	
	private List<TimedDmxValue> riff(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, int motorSpeed, Double unit, char[] riffStr) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		int currentNote = -1;
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long riffTime = Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm) / (unit.doubleValue() / 4.0)).longValue();

		for (char c : riffStr){
			
			int toExecute = Integer.parseInt(new Character(c).toString());
			
			if (currentNote != toExecute){
				
				values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, Arrays.asList(colors[toExecute]).toArray(new Color[0]), 0, motorSpeed));
			}
			
			currentNote = toExecute;
			currentTime += riffTime;
		}
		
		return values;
		
	}
}
