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
public class ElectroCastleDLM001 extends Fixture {

	public static final int RED1 = 0;
	public static final int GREEN1 = 1;
	public static final int BLUE1 = 2;
	public static final int RED2 = 3;
	public static final int GREEN2 = 4;
	public static final int BLUE2 = 5;
	
	public static final int RELEASE = 6;
	public static final int ANIMATION = 7;
	public static final int ANIMATION_TRIGGER = 8;
	public static final int RELEASE_CURVE = 9;
	
	public static final int ANIMATION_PULSE = 0;
	public static final int ANIMATION_FILLLEFT = 50;
	
	public static final int TRIGGER_TRIG = 0;
	public static final int TRIGGER_AUTO_ALL = 100;
	public static final int TRIGGER_AUTO_SINGLE = 150;
	public static final int TRIGGER_ON = 200;
	
	public static final int CURVE_LINEAR = 150;
	public static final int CURVE_EXP = 200;
	public static final int CURVE_LOG = 200;
	public static final int CURVE_SINEFADE = 200;
	
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
					bpm, parameters.getSpeed(), parameters.getAnimation(), parameters.getAnimationTrigger(), parameters.getCurve()));

		
		} else {
			throw new RuntimeException("Unknown operation: " + paramsArr[0] + " (" + params +")");
		}

		return values;
	}

	private List<TimedDmxValue> dim(TimeInfo time, TimeSignature signature, int bpm, Color[] color) {

		// Always reset stobe mode when applying dim.

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED1, color[0].getR()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN1, color[0].getG()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE1, color[0].getB()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED2, color[1].getR()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN2, color[1].getG()));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE2, color[1].getB()));

		return values;
	}

	private List<TimedDmxValue> mode(TimeInfo time, TimeSignature signature, int bpm, int release, int animation, int animTrigger, int releaseCurve) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		if (release != -1){
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RELEASE, release * 255 / 100));
		}
		
		if (animation != -1){
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + ANIMATION, animation * 255 / 9));
		}
		
		if (animTrigger != -1){
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + ANIMATION_TRIGGER, animTrigger * 255 / 7));
		}
		
		if (releaseCurve != -1){
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RELEASE_CURVE, releaseCurve * 255 / 4));
		}
		
		return values;
	}

	private List<TimedDmxValue> fade(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, TimeInfo executionTime) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long totalTime = TimeHelper.getMilliseconds(executionTime, signature, bpm);
		int numberOfIntervals = Long.valueOf(totalTime * DmxLive.getConfiguration().getDmxRate() / 1000l).intValue();
		long intervalDuration = TimeHelper.getMilliseconds(executionTime, signature, bpm) / numberOfIntervals - 5;

		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);

		double redIncrement1 = Double.valueOf(new Float(colors[2].getR() - colors[0].getR()) / new Float(numberOfIntervals));
		double greenIncrement1 = Double.valueOf(new Float(colors[2].getG() - colors[0].getG()) / new Float(numberOfIntervals));
		double blueIncrement1 = Double.valueOf(new Float(colors[2].getB() - colors[0].getB()) / new Float(numberOfIntervals));
		double redIncrement2 = Double.valueOf(new Float(colors[3].getR() - colors[1].getR()) / new Float(numberOfIntervals));
		double greenIncrement2 = Double.valueOf(new Float(colors[3].getG() - colors[1].getG()) / new Float(numberOfIntervals));
		double blueIncrement2 = Double.valueOf(new Float(colors[3].getB() - colors[1].getB()) / new Float(numberOfIntervals));
		
		
		double currentRed1 = colors[0].getR();
		double currentGreen1 = colors[0].getG();
		double currentBlue1 = colors[0].getB();
		
		double currentRed2 = colors[1].getR();
		double currentGreen2 = colors[1].getG();
		double currentBlue2 = colors[1].getB();

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
			
			
			
			values.add(new TimedDmxValue(currentTime, this.getChannel() + RED2, Long.valueOf(Math.round(currentRed2)).intValue()));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + GREEN2, Long.valueOf(Math.round(currentGreen2)).intValue()));
			values.add(new TimedDmxValue(currentTime, this.getChannel() + BLUE2, Long.valueOf(Math.round(currentBlue2)).intValue()));

			currentRed2 += redIncrement2;
			currentGreen2 += greenIncrement2;
			currentBlue2 += blueIncrement2;

		
			if (currentRed2 > 255) {
				currentRed2 = 255;
			}
			if (currentGreen2 > 255) {
				currentGreen2 = 255;
			}
			if (currentBlue2 > 255) {
				currentBlue2 = 255;
			}

			
			if (currentRed2 < 0) {
				currentRed2 = 0;
			}
			if (currentGreen2 < 0) {
				currentGreen2 = 0;
			}
			if (currentBlue2 < 0) {
				currentBlue2 = 0;
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
					Arrays.asList(colors[0],colors[1], colors[2], colors[3]).toArray(new Color[0]), 
				
					fadeTime));
			
			values.addAll(fade(new TimeInfo(signature, bpm, currentTime+(frequency/2)), signature, bpm, 
					Arrays.asList(colors[2], colors[3], colors[0],colors[1]).toArray(new Color[0]), 
					
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
