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
public class ElectroCastleDMXRLY001 extends Fixture {

	public static final int RED1 = 0;
	public static final int GREEN1 = 1;
	public static final int BLUE1 = 2;
	public static final int DIMMER = 3;
	public static final int FOG_ON = 4;
	

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
					parameters.getColors()));

		

		} else if (paramsArr[0].equals(OP_FADE)) {

			values.addAll(fade(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDuration()));

		} else if (paramsArr[0].equals(OP_BLINK)) {

			values.addAll(blink(
					startTime,
					signature,
					bpm,
					parameters.getColors(),					
					parameters.getDuration(),
					parameters.getNoteUnit(),
					parameters.getOnTime()));

		} else if (paramsArr[0].equals(OP_PULSE)) {

			values.addAll(pulse(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDuration(),
					parameters.getNoteUnit()));

		} else if (paramsArr[0].equals(OP_MODE)) {

			values.addAll(mode(
					startTime,
					signature,
					bpm, Integer.parseInt(parameters.getMode())));

		
		} else {
			throw new RuntimeException("Unknown operation: " + paramsArr[0] + " (" + params +")");
		}

		return values;
	}

	private List<TimedDmxValue> dim(TimeInfo time, TimeSignature signature, int bpm, Color[] color) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED1, color[0].getR()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN1, color[0].getG()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE1, color[0].getB()));
		
		return values;
	}

	private List<TimedDmxValue> mode(TimeInfo time, TimeSignature signature, int bpm, int fogOn) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + FOG_ON, fogOn * 255));
	
		return values;
	}

	private List<TimedDmxValue> fade(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, TimeInfo executionTime) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long totalTime = TimeHelper.getMilliseconds(executionTime, signature, bpm);
		int numberOfIntervals = Long.valueOf(totalTime * DmxLive.getConfiguration().getDmxRate() / 1000l).intValue();
		long intervalDuration = TimeHelper.getMilliseconds(executionTime, signature, bpm) / numberOfIntervals - 5;

		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);

		double redIncrement1 = Double.valueOf(new Float(colors[1].getR() - colors[0].getR()) / new Float(numberOfIntervals));
		double greenIncrement1 = Double.valueOf(new Float(colors[1].getG() - colors[0].getG()) / new Float(numberOfIntervals));
		double blueIncrement1 = Double.valueOf(new Float(colors[1].getB() - colors[0].getB()) / new Float(numberOfIntervals));
		
		
		double currentRed1 = colors[0].getR();
		double currentGreen1 = colors[0].getG();
		double currentBlue1 = colors[0].getB();
		
		for (int i = 0; i <= numberOfIntervals; i++) {

			
			values.add(new TimedDmxValue(currentTime, this.getChannel() + RED1, Long.valueOf(Math.round(currentRed1)).intValue()));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + GREEN1, Long.valueOf(Math.round(currentGreen1)).intValue()));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + BLUE1, Long.valueOf(Math.round(currentBlue1)).intValue()));

			currentRed1 += redIncrement1;
			currentGreen1 += greenIncrement1;
			currentBlue1 += blueIncrement1;

		
			if (currentRed1 > 255) {
				currentRed1 = 255;
			}
			if (currentGreen1 > 255) {
				currentGreen1 = 255;
			}
			if (currentBlue1 > 255) {
				currentBlue1 = 255;
			}

			
			if (currentRed1 < 0) {
				currentRed1 = 0;
			}
			if (currentGreen1 < 0) {
				currentGreen1 = 0;
			}
			if (currentBlue1 < 0) {
				currentBlue1 = 0;
			}
			
			
			

			currentTime += intervalDuration;

		}

		return values;
	}
	
	private List<TimedDmxValue> pulse(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, TimeInfo executionTime, Double beatFrequency) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		
		long frequency = TimeHelper.getFrequency(signature, bpm, beatFrequency);
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long finalTime = currentTime + TimeHelper.getMilliseconds(executionTime, signature, bpm);
		
		TimeInfo fadeTime = new TimeInfo(signature, bpm, frequency/2);
		
		while (currentTime < finalTime) {
			
			values.addAll(fade(new TimeInfo(signature, bpm, currentTime), signature, bpm, 
					Arrays.asList(colors[0],colors[1]).toArray(new Color[0]), 
				
					fadeTime));
			
			values.addAll(fade(new TimeInfo(signature, bpm, currentTime+(frequency/2)), signature, bpm, 
					Arrays.asList(colors[1], colors[0]).toArray(new Color[0]), 
					
					fadeTime));
			
			currentTime += frequency;
		}
		
		return values;
		
	}
	
	private List<TimedDmxValue> blink(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, TimeInfo executionTime, Double beatFrequency, Double beatOn) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		
		long frequency = TimeHelper.getFrequency(signature, bpm, beatFrequency);
		long onTime = TimeHelper.getOnTime(signature, bpm, beatOn);
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long finalTime = currentTime + TimeHelper.getMilliseconds(executionTime, signature, bpm);
		
		while (currentTime < finalTime) {
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, new Color[] { colors[0], colors[1] }));
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime+onTime), signature, bpm, new Color[] { colors[2], colors[3] }));
			
			currentTime += frequency;
		}
		
		return values;
		
	}
	
	
}
