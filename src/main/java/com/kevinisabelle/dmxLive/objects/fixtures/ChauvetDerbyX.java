package com.kevinisabelle.dmxLive.objects.fixtures;

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
 * Fixture implementation for the Chauvet Derby X fixture
 * 
 * Channels 1 to 6 (Dimmer for spots)
 * 000 to 010 = off
 * 011 to 045  = red
 * 046 to 080 = green
 * 081 to 115 = blue
 * 116 to 150 = red/green
 * 151 to 185 = red/blue
 * 186 to 220= green/blue
 * 221 to 255  = red/green/blue
 * 
 * Strobe:
 * 000 to 010 = off
 * 011 to 255 = speed %
 * 
 * Auto:
 * 000 to 010 = no function
 * 011 to 024 = Auto 1
 * 025 to 038 = Auto 2
 * 039 to 052 = Auto 3
 * 053 to 066 = Auto 4
 * 067 to 080 = Auto 5
 * 081 to 094 = Auto 6
 * 095 to 108 = Auto 7
 * 109 to 122 = Auto 8
 * 123 to 136 = Auto 9
 * 137 to 150 = Auto 10
 * 151 to 164 = Auto 11
 * 165 to 178 = Auto 12
 * 179 to 192 = Auto 13
 * 193 to 206 = Auto 14
 * 207 to 220 = Auto 15
 * 221 to 250 = Auto 16
 * 251 to 255 = Audio trigger
 * 
 * 
 * @author kisabelle
 */
public class ChauvetDerbyX extends Fixture {

	private static final int CHANNEL_SPOT_1 = 1;
	private static final int CHANNEL_SPOT_2 = 2;
	private static final int CHANNEL_SPOT_3 = 3;
	private static final int CHANNEL_SPOT_4 = 4;
	private static final int CHANNEL_SPOT_5 = 5;
	private static final int CHANNEL_SPOT_6 = 6;
	private static final int CHANNEL_STROBE = 7;
	private static final int CHANNEL_MODE = 8;
	
	public static final int[] STROBE_ON = new int[]{128, 255};
	public static final int[] STROBE_OFF = new int[]{0, 10};
	
	public static final int COLOR_BLACK = 0;
	public static final int COLOR_RED = 11;
	public static final int COLOR_GREEN = 46;
	public static final int COLOR_BLUE = 81;
	public static final int COLOR_REDGREEN = 116;
	public static final int COLOR_REDBLUE = 151;
	public static final int COLOR_GREENBLUE = 186;
	public static final int COLOR_ALL = 221;
	
	public static final int[] MODE_1 = new int[]{11, 24}; 
	public static final int[] MODE_2 = new int[]{25, 38};
	public static final int[] MODE_3 = new int[]{39, 52};
	public static final int[] MODE_4 = new int[]{53, 66};
	public static final int[] MODE_5 = new int[]{67, 80};
	public static final int[] MODE_6 = new int[]{81, 94};
	public static final int[] MODE_7 = new int[]{95, 108};
	public static final int[] MODE_8 = new int[]{109, 122};
	public static final int[] MODE_9 = new int[]{123, 136}; 
	public static final int[] MODE_10 = new int[]{137, 150};
	public static final int[] MODE_11 = new int[]{151, 164};
	public static final int[] MODE_12 = new int[]{165, 178};
	public static final int[] MODE_13 = new int[]{179, 192};
	public static final int[] MODE_14 = new int[]{193, 206};
	public static final int[] MODE_15 = new int[]{207, 220};
	public static final int[] MODE_16 = new int[]{221, 250};
	public static final int[] MODE_AUDIO = new int[]{251, 255};
	
	public static final Map<String, int[]> modes = new HashMap<String, int[]>();

	static {
		modes.put("mode1", MODE_1);
		modes.put("mode2", MODE_2);
		modes.put("mode3", MODE_3);
		modes.put("mode4", MODE_4);
		modes.put("mode5", MODE_5);
		modes.put("mode6", MODE_6);
		modes.put("mode7", MODE_7);
		modes.put("mode8", MODE_8);
		modes.put("mode9", MODE_9);
		modes.put("mode10", MODE_10);
		modes.put("mode11", MODE_11);
		modes.put("mode12", MODE_12);
		modes.put("mode13", MODE_13);
		modes.put("mode14", MODE_14);
		modes.put("mode15", MODE_15);
		modes.put("mode16", MODE_16);
		modes.put("audio", MODE_AUDIO);
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
					false,
					0,
					0));

		} else if (paramsArr[0].equals(OP_STROBE)) {

			values.addAll(dim(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					true,
					parameters.getSpeed() == -1 ? 100 : parameters.getSpeed(),
					0));

		} else if (paramsArr[0].equals(OP_MODE)) {

			int[] modeGrad = modes.get(parameters.getMode());

			int mode = Double.valueOf(((parameters.getSpeed() == -1 ? 100 : parameters.getSpeed()) / 100.0) * Integer.valueOf(modeGrad[1] - modeGrad[0]).doubleValue() + Integer.valueOf(modeGrad[0]).doubleValue()).intValue();

			values.addAll(mode(
					startTime,
					signature,
					bpm,					
					mode));

		} else if (paramsArr[0].equals(OP_BLINK)) {

			values.addAll(blink(
					startTime,
					signature,
					bpm,
					parameters.getColors(),
					parameters.getDuration(),
					parameters.getNoteUnit(),
					parameters.getOnTime()));

		}  else if (paramsArr[0].equals(OP_RIFF)) {

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

	private List<TimedDmxValue> mode(TimeInfo startTime, TimeSignature signature, int bpm, int mode) {
		return dim(startTime, signature, bpm, new Color[]{new Color(Color.ColorEnum.BLUE)}, false, 0, mode);
	}

	private List<TimedDmxValue> dim(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, boolean isStrobe, int strobeSpeed, int mode) {

		// Always reset stobe mode when applying dim.
		
		int[] spotValues = new int[6];
		int i = 0;
		
		for (Color color : colors){
			
			if (color.getR() == 255 && color.getG() == 255 && color.getB() == 255){
				spotValues[i] = COLOR_ALL;
			} else if (color.getR() == 255 && color.getB() == 255){
				spotValues[i] = COLOR_REDBLUE;
			} else if (color.getR() == 255 && color.getG() == 255){
				spotValues[i] = COLOR_REDGREEN;
			} else if (color.getB() == 255 && color.getG() == 255){
				spotValues[i] = COLOR_GREENBLUE;
			} else if (color.getB() == 255){
				spotValues[i] = COLOR_BLUE;
			} else if (color.getG() == 255){
				spotValues[i] = COLOR_GREEN;
			} else if (color.getR() == 255){
				spotValues[i] = COLOR_RED;
			} else {
				spotValues[i] = 0;
			}
			
			i++;
		}
		
		switch (colors.length){
			case 1:
			case 4:
			case 5:
				spotValues[1] = spotValues[0];
				spotValues[2] = spotValues[0];
				spotValues[3] = spotValues[0];
				spotValues[4] = spotValues[0];
				spotValues[5] = spotValues[0];
				break;
				
			case 2:
				
				spotValues[5] = spotValues[1];
				spotValues[4] = spotValues[0];
				spotValues[3] = spotValues[1];
				spotValues[2] = spotValues[0];
				break;
				
			case 3:
				
				spotValues[5] = spotValues[2];
				spotValues[4] = spotValues[2];
				spotValues[3] = spotValues[1];
				spotValues[2] = spotValues[1];
				spotValues[1] = spotValues[0];
				break;
		}

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);
		
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_MODE, mode));

		if (isStrobe){
			
			values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_STROBE, (STROBE_ON[1] - STROBE_ON[0]) * strobeSpeed / 100 + STROBE_ON[0]));
		
		} else {

			values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_STROBE, STROBE_OFF[0]));
		}
		
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_SPOT_1, spotValues[0]));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_SPOT_2, spotValues[1]));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_SPOT_3, spotValues[2]));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_SPOT_4, spotValues[3]));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_SPOT_5, spotValues[4]));
		values.add(new TimedDmxValue(timeMillis, this.getChannel() + CHANNEL_SPOT_6, spotValues[5]));
		

		return values;
	}
	
	private List<TimedDmxValue> blink(TimeInfo time, TimeSignature signature, int bpm, Color[] colors, TimeInfo executionTime, Double beatFrequency, Double beatOn) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		
		long frequency = TimeHelper.getFrequency(signature, bpm, beatFrequency);
		long onTime = TimeHelper.getOnTime(signature, bpm, beatOn);
		long currentTime = TimeHelper.getMilliseconds(time, signature, bpm);
		long finalTime = currentTime + TimeHelper.getMilliseconds(executionTime, signature, bpm);
		
		while (currentTime < finalTime) {
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, new Color[]{colors[0]}, false, 0, 0));
			
			values.addAll(dim(new TimeInfo(signature, bpm, currentTime+onTime), signature, bpm, new Color[]{colors[1]}, false, 0, 0));
			
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
				
				values.addAll(dim(new TimeInfo(signature, bpm, currentTime), signature, bpm, Arrays.asList(colors[toExecute]).toArray(new Color[0]), false, 0, 0));
			}
			
			currentNote = toExecute;
			currentTime += riffTime;
		}
		
		return values;
		
	}
}
