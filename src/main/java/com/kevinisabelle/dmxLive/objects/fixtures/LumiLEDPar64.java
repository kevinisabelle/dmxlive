package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.CommandParameters;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kevin
 */
public class LumiLEDPar64 extends Fixture {

	public static final int DIMMER = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int STROBE = 4;
	public static final int MACRO = 5;
	public static final int MODE_DIM = 0;
	public static final int MODE_FADE_IN = 50;
	public static final int MODE_FADE_OUT = 85;
	public static final int MODE_PULSE = 100;
	public static final int MODE_CYCLE = 150;
	public static final int MODE_CYCLE2 = 200;
	
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
					parameters.getColors()[0],
					parameters.getDimmers()[0]));

		} else if (paramsArr[0].equals(OP_STROBE)) {

			values.addAll(strobe(
					startTime,
					signature,
					bpm,
					parameters.getColors()[0],
					parameters.getDimmers()[0],
					parameters.getSpeed() == -1 ? 100 : parameters.getSpeed()));

		} else if (paramsArr[0].equals(OP_FADE)) {

			values.addAll(fade(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDimmers(),
					parameters.getDuration()));

		} else if (paramsArr[0].equals(OP_BLINK)) {

			values.addAll(blink(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDimmers(),
					parameters.getDuration(),
					parameters.getNoteUnit(),
					parameters.getOnTime()));

		} else if (paramsArr[0].equals(OP_PULSE)) {

			values.addAll(pulse(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDimmers(),
					parameters.getDuration(),
					parameters.getNoteUnit()));

		} else if (paramsArr[0].equals(OP_MODE)) {

			int mode = 0;
			String modeStr = parameters.getMode();

			if (modeStr.equals("pulse")) {
				mode = MODE_PULSE;
			} else if (modeStr.equals("cycle")) {
				mode = MODE_CYCLE;
			} else if (modeStr.equals("cycle2")) {
				mode = MODE_CYCLE2;
			} else if (modeStr.equals("fadein")) {
				mode = MODE_FADE_IN;
			} else if (modeStr.equals("fadeout")) {
				mode = MODE_FADE_OUT;
			}

			values.addAll(mode(
					startTime,
					signature,
					bpm,
					parameters.getColors()[0],
					parameters.getDimmers()[0],
					parameters.getSpeed() == -1 ? 100 : parameters.getSpeed(),
					mode));

		} else if (paramsArr[0].equals(OP_RIFF)) {

			values.addAll(riff(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDimmers(),
					parameters.getNoteUnit(),
					parameters.getRiffNotes()));

		} else {
			throw new RuntimeException("Unknown operation: " + paramsArr[0] + " (" + params +")");
		}

		return values;
	}

	private List<TimedDmxValue> dim(TimeInfo time, TimeSignature signature, int bpm, Color color, int percent) {

		// Always reset stobe mode when applying dim.

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + DIMMER, percent * 255 / 100));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + STROBE, 0));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + MACRO, MODE_DIM));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED, color.getR()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN, color.getG()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE, color.getB()));

		return values;
	}

	private List<TimedDmxValue> strobe(TimeInfo time, TimeSignature signature, int bpm, Color color, int percent, int speed) {
		return mode(time, signature, bpm, color, percent, speed, MODE_DIM);
	}

	private List<TimedDmxValue> mode(TimeInfo time, TimeSignature signature, int bpm, Color color, int percent, int speed, int mode) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + DIMMER, percent * 255 / 100));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + STROBE, speed * 255 / 100));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + MACRO, mode));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED, color.getR()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN, color.getG()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE, color.getB()));

		return values;
	}

	private List<TimedDmxValue> fade(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, Integer[] dimmerPercent, TimeInfo executionTime) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long totalTime = TimeHelper.getMilliseconds(executionTime, signature, bpm);
		int numberOfIntervals = Long.valueOf(totalTime * DmxLive.getConfiguration().getDmxRate() / 1000l).intValue();
		long intervalDuration = TimeHelper.getMilliseconds(executionTime, signature, bpm) / numberOfIntervals - 5;

		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(currentTime, this.getChannel() + STROBE, 0));

		double dimmerIncrement = Double.valueOf(new Float(dimmerPercent[1] - dimmerPercent[0]) / new Float(numberOfIntervals));
		double redIncrement = Double.valueOf(new Float(colors[1].getR() - colors[0].getR()) / new Float(numberOfIntervals));
		double greenIncrement = Double.valueOf(new Float(colors[1].getG() - colors[0].getG()) / new Float(numberOfIntervals));
		double blueIncrement = Double.valueOf(new Float(colors[1].getB() - colors[0].getB()) / new Float(numberOfIntervals));

		double currentDimmer = dimmerPercent[0];
		double currentRed = colors[0].getR();
		double currentGreen = colors[0].getG();
		double currentBlue = colors[0].getB();

		for (int i = 0; i <= numberOfIntervals; i++) {

			values.add(new TimedDmxValue(currentTime, this.getChannel() + DIMMER, Long.valueOf(Math.round(currentDimmer)).intValue() * 255 / 100));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + MACRO, MODE_DIM));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + RED, Long.valueOf(Math.round(currentRed)).intValue()));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + GREEN, Long.valueOf(Math.round(currentGreen)).intValue()));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + BLUE, Long.valueOf(Math.round(currentBlue)).intValue()));

			currentDimmer += dimmerIncrement;
			currentRed += redIncrement;
			currentGreen += greenIncrement;
			currentBlue += blueIncrement;

			if (currentDimmer > 100) {
				currentDimmer = 100;
			}
			if (currentRed > 255) {
				currentRed = 255;
			}
			if (currentGreen > 255) {
				currentGreen = 255;
			}
			if (currentBlue > 255) {
				currentBlue = 255;
			}

			if (currentDimmer < 0) {
				currentDimmer = 0;
			}
			if (currentRed < 0) {
				currentRed = 0;
			}
			if (currentGreen < 0) {
				currentGreen = 0;
			}
			if (currentBlue < 0) {
				currentBlue = 0;
			}

			currentTime += intervalDuration;

		}

		return values;
	}
	
	private List<TimedDmxValue> pulse(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, Integer[] dimmerPercent, TimeInfo executionTime, Double beatFrequency) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		
		long frequency = TimeHelper.getFrequency(signature, bpm, beatFrequency);
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long finalTime = currentTime + TimeHelper.getMilliseconds(executionTime, signature, bpm);
		
		TimeInfo fadeTime = new TimeInfo(signature, bpm, frequency/2);
		
		while (currentTime < finalTime) {
			
			values.addAll(fade(new TimeInfo(signature, bpm, currentTime), signature, bpm, 
					Arrays.asList(colors[0],colors[1]).toArray(new Color[0]), 
					new Integer[]{dimmerPercent[0],dimmerPercent[1]},
					fadeTime));
			
			values.addAll(fade(new TimeInfo(signature, bpm, currentTime+(frequency/2)), signature, bpm, 
					Arrays.asList(colors[1],colors[0]).toArray(new Color[0]), 
					new Integer[]{dimmerPercent[1],dimmerPercent[0]},
					fadeTime));
			
			currentTime += frequency;
		}
		
		return values;
		
	}
	
	private List<TimedDmxValue> blink(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, Integer[] dimmerPercent, TimeInfo executionTime, Double beatFrequency, Double beatOn) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		
		long frequency = TimeHelper.getFrequency(signature, bpm, beatFrequency);
		long onTime = TimeHelper.getOnTime(signature, bpm, beatOn);
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long finalTime = currentTime + TimeHelper.getMilliseconds(executionTime, signature, bpm);
		
		while (currentTime < finalTime) {
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, colors[0], dimmerPercent[0]));
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime+onTime), signature, bpm, colors[1], dimmerPercent[1]));
			
			currentTime += frequency;
		}
		
		return values;
		
	}
	
	private List<TimedDmxValue> riff(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, Integer[] dimmerPercent, Double unit, char[] riffStr) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
	
		int currentNote = -1;
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long riffTime = Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm) / (unit.doubleValue() / 4.0)).longValue();
		
		for (char c : riffStr){
			
			int toExecute = Integer.parseInt(new Character(c).toString());
			
			if (currentNote != toExecute){
				
				values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, colors[toExecute], dimmerPercent[toExecute]));
			}
			
			currentNote = toExecute;
			currentTime += riffTime;
		}
		
		return values;
	}
}
