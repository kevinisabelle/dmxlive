package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.CommandParameters;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class LumiMKII extends Fixture {

	/**
	 * CH1 0——10 Dimmer Mode 11——51 Dimmer + Strobe Mode 52——91 Gradual Change
	 * Mode 92——131 Mode 1 132——150 Mode 2 151——169 Mode 3 170——189 Mode 4
	 * 190——209 Mode 5 210——229 Mode 6 230——249 Mode 7 250——255 Mixed Mode 1-7
	 * CH2 0-9 Red Pixel 10-255 0-100% Red (Pixel invalid) 0-100% CH3 0-9 Green
	 * Pixel 10-255 0-100% Green (Pixel invalid) 0-100% CH4 0-9 Blue Pixel
	 * 10-255 0-100% Blue (Pixel invalid) 0-100%
	 *
	 * CH5 0-255 0-100% Pixel 1 Red Dim 0-100% CH6 0-255 0-100% Pixel 1 Green
	 * Dim 0-100% CH7 0-255 0-100% Pixel 1 Blue Dim 0-100%
	 *
	 * CH8 0-255 0-100% Pixel 2 Red Dim 0-100% CH9 0-255 0-100% Pixel 2 Green
	 * Dim 0-100% CH10 0-255 0-100% Pixel 2 Blue Dim 0-100%
	 *
	 * CH11 0-255 0-100% Pixel 3 Red Dim 0-100% CH12 0-255 0-100% Pixel 3 Green
	 * Dim 0-100% CH13 0-255 0-100% Pixel 3 Blue Dim 0-100%
	 */
	public static final int MODE = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int RED_1 = 4;
	public static final int GREEN_1 = 5;
	public static final int BLUE_1 = 6;
	public static final int RED_2 = 7;
	public static final int GREEN_2 = 8;
	public static final int BLUE_2 = 9;
	public static final int RED_3 = 10;
	public static final int GREEN_3 = 11;
	public static final int BLUE_3 = 12;
	public static final int MODE_DIM = 0;
	
	public static final int[] MODE_STROBE = new int[]{11, 51};
	public static final int[] MODE_GRADUAL = new int[]{52, 91};
	public static final int[] MODE_1 = new int[]{92, 131}; 
	public static final int[] MODE_2 = new int[]{132, 150};
	public static final int[] MODE_3 = new int[]{151, 169};
	public static final int[] MODE_4 = new int[]{170, 189};
	public static final int[] MODE_5 = new int[]{190, 209};
	public static final int[] MODE_6 = new int[]{210, 229};
	public static final int[] MODE_7 = new int[]{230, 249};
	public static final int[] MODE_MIXED = new int[]{250, 255};
	
	public static final Map<String, int[]> modes = new HashMap<String, int[]>();

	static {
		modes.put("mode1", MODE_1);
		modes.put("mode2", MODE_2);
		modes.put("mode3", MODE_3);
		modes.put("mode4", MODE_4);
		modes.put("mode5", MODE_5);
		modes.put("mode6", MODE_6);
		modes.put("mode7", MODE_7);
		modes.put("mixed", MODE_MIXED);
		modes.put("grad", MODE_GRADUAL);
		modes.put("strobe", MODE_STROBE);
	}

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
					parameters.getDimmers()[0]));

		} else if (paramsArr[0].equals(OP_STROBE)) {

			values.addAll(strobe(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
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
			
			int[] modeGrad = modes.get(parameters.getMode());

			int mode = Double.valueOf(((parameters.getSpeed() == -1 ? 100 : parameters.getSpeed()) / 100.0) * Integer.valueOf(modeGrad[1] - modeGrad[0]).doubleValue() + Integer.valueOf(modeGrad[0]).doubleValue()).intValue();

			values.addAll(mode(
					startTime,
					signature,
					bpm,
					mode,
					parameters.getColors(),
					parameters.getDimmers()[0],
					true));

		} else if (paramsArr[0].equals(OP_RIFF)) {

			values.addAll(riff(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDimmers(),
					parameters.getNoteUnit(),
					parameters.getRiffNotes()));

		} 

		return values;
	}

	private List<TimedDmxValue> dim(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, int percent) {
		return mode(time, signature, bpm, MODE_DIM, colors, percent, true);
	}

	private List<TimedDmxValue> mode(TimeInfo time, TimeSignature signature, int bpm, int mode, Color[] colors, int percent, boolean sendBlacks) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + MODE, mode));

		if (colors.length == 1) {
			
			if (sendBlacks){
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_1, 0));
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_1, 0));
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_1, 0));

				values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_2, 0));
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_2, 0));
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_2, 0));

				values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_3, 0));
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_3, 0));
				values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_3, 0));
			}
			
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE, percent * colors[0].getB() / 100));

		} else if (colors.length == 2) {
			
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED, 0));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN, 0));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE, 0));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_1, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_1, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_1, percent * colors[0].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_2, percent * colors[1].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_2, percent * colors[1].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_2, percent * colors[1].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_3, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_3, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_3, percent * colors[0].getB() / 100));

		} else if (colors.length == 3) {
			
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED, 0));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN, 0));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE, 0));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_1, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_1, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_1, percent * colors[0].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_2, percent * colors[1].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_2, percent * colors[1].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_2, percent * colors[1].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_3, percent * colors[2].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_3, percent * colors[2].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_3, percent * colors[2].getB() / 100));

		}

		return values;
	}

	private List<TimedDmxValue> strobe(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, int percent, int speed) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(timeMillis, this.getChannel() + MODE, 40 * speed / 100 + 11));

		if (colors.length == 1) {

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_1, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_1, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_1, percent * colors[0].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_2, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_2, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_2, percent * colors[0].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_3, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_3, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_3, percent * colors[0].getB() / 100));

		} else if (colors.length == 2) {

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_1, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_1, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_1, percent * colors[0].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_2, percent * colors[1].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_2, percent * colors[1].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_2, percent * colors[1].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_3, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_3, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_3, percent * colors[0].getB() / 100));

		} else if (colors.length == 3) {

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_1, percent * colors[0].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_1, percent * colors[0].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_1, percent * colors[0].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_2, percent * colors[1].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_2, percent * colors[1].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_2, percent * colors[1].getB() / 100));

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + RED_3, percent * colors[2].getR() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + GREEN_3, percent * colors[2].getG() / 100));
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + BLUE_3, percent * colors[2].getB() / 100));

		}

		return values;
	}

	private List<TimedDmxValue> fade(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, Integer[] dimmerPercent, TimeInfo executionTime) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long totalTime = TimeHelper.getMilliseconds(executionTime, signature, bpm);
		int numberOfIntervals = Long.valueOf(totalTime * DmxLive.getConfiguration().getDmxRate() / 1000l).intValue();
		long intervalDuration = TimeHelper.getMilliseconds(executionTime, signature, bpm) / numberOfIntervals - 5;

		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedDmxValue(currentTime, this.getChannel() + MODE, MODE_DIM));

		double dimmerIncrement = Double.valueOf(new Float(dimmerPercent[1] - dimmerPercent[0]) / new Float(numberOfIntervals));
		double currentDimmer = dimmerPercent[0];
		
		int turns = colors.length == 2 ? 1 : 3;

		double redIncrements[] = new double[turns];
		double greenIncrements[] = new double[turns];
		double blueIncrements[] = new double[turns];

		double currentReds[] = new double[turns];
		double currentGreens[] = new double[turns];
		double currentBlues[] = new double[turns];
		
		for (int i = 0; i < turns; i++) {
			if (colors.length == 2){
				
				redIncrements[i] = Double.valueOf(new Float(colors[1].getR() - colors[0].getR()) / new Float(numberOfIntervals));
				greenIncrements[i] = Double.valueOf(new Float(colors[1].getG() - colors[0].getG()) / new Float(numberOfIntervals));
				blueIncrements[i] = Double.valueOf(new Float(colors[1].getB() - colors[0].getB()) / new Float(numberOfIntervals));
				
				currentReds[i] = colors[0].getR();
				currentGreens[i] = colors[0].getG();
				currentBlues[i] = colors[0].getB();
			
			} else if (colors.length == 4){
				
				int colorNb1 = (i == 1 ? 1 : 0);
				int colorNb2 = (i == 1 ? 3 : 2);
				
				redIncrements[i] = Double.valueOf(new Float(colors[colorNb2].getR() - colors[colorNb1].getR()) / new Float(numberOfIntervals));
				greenIncrements[i] = Double.valueOf(new Float(colors[colorNb2].getG() - colors[colorNb1].getG()) / new Float(numberOfIntervals));
				blueIncrements[i] = Double.valueOf(new Float(colors[colorNb2].getB() - colors[colorNb1].getB()) / new Float(numberOfIntervals));
				
				currentReds[i] = colors[colorNb1].getR();
				currentGreens[i] = colors[colorNb1].getG();
				currentBlues[i] = colors[colorNb1].getB();
				
			} else if (colors.length == 6){
				
				redIncrements[i] = Double.valueOf(new Float(colors[i+3].getR() - colors[i].getR()) / new Float(numberOfIntervals));
				greenIncrements[i] = Double.valueOf(new Float(colors[i+3].getG() - colors[i].getG()) / new Float(numberOfIntervals));
				blueIncrements[i] = Double.valueOf(new Float(colors[i+3].getB() - colors[i].getB()) / new Float(numberOfIntervals));
				
				currentReds[i] = colors[i].getR();
				currentGreens[i] = colors[i].getG();
				currentBlues[i] = colors[i].getB();
			}

			
		}

		for (int i = 0; i <= numberOfIntervals; i++) {

			Color[] currentColors = new Color[turns];

			for (int j = 0; j < turns; j++) {
				currentColors[j] = new Color(
						Long.valueOf(Math.round(currentReds[j])).intValue(),
						Long.valueOf(Math.round(currentGreens[j])).intValue(),
						Long.valueOf(Math.round(currentBlues[j])).intValue());
			}

			values.addAll(mode(time, signature, bpm, MODE_DIM, currentColors, Double.valueOf(currentDimmer).intValue(), i == 0));

			currentDimmer += dimmerIncrement;
			if (currentDimmer > 100) {
				currentDimmer = 100;
			}
			if (currentDimmer < 0) {
				currentDimmer = 0;
			}

			for (int j = 0; j < turns; j++) {
				currentReds[j] += redIncrements[j];
				currentGreens[j] += greenIncrements[j];
				currentBlues[j] += blueIncrements[j];

				if (currentReds[j] > 255) {
					currentReds[j] = 255;
				}
				if (currentGreens[j] > 255) {
					currentGreens[j] = 255;
				}
				if (currentBlues[j] > 255) {
					currentBlues[j] = 255;
				}

				if (currentReds[j] < 0) {
					currentReds[j] = 0;
				}
				if (currentGreens[j] < 0) {
					currentGreens[j] = 0;
				}
				if (currentBlues[j] < 0) {
					currentBlues[j] = 0;
				}
			}

			time = time.add(new TimeInfo(signature, bpm, intervalDuration), signature);
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
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, Arrays.asList(colors[0]).toArray(new Color[0]), dimmerPercent[0]));
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime+onTime), signature, bpm, Arrays.asList(colors[1]).toArray(new Color[0]), dimmerPercent[1]));
			
			currentTime += frequency;
		}
		
		return values;
		
	}
	
	private List<TimedDmxValue> riff(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, Integer[] dimmerPercent, Double unit, char[] riffStr) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		int currentNote = -1;
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long riffTime = Double.valueOf(TimeHelper.getQuarterMilliseconds(bpm) / (unit.doubleValue() / 4.0)).longValue();
		
		boolean isFirstSent = false;
		
		for (char c : riffStr){
			
			int toExecute = Integer.parseInt(new Character(c).toString());
			
			if (currentNote != toExecute){
				
				values.addAll(mode(new TimeInfo(signature, bpm, currentTime), signature, bpm, MODE_DIM, Arrays.asList(colors[toExecute]).toArray(new Color[0]), dimmerPercent[toExecute], !isFirstSent));
			}
			
			currentNote = toExecute;
			currentTime += riffTime;
			isFirstSent = true;
		}
		
		return values;
		
	}
}
