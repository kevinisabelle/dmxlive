package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.CommandParameters;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import com.kevinisabelle.dmxLive.objects.TimedMidiValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kevin
 */
public class BossGT100 extends Fixture {


	
	@Override
	public List<TimedDmxValue> convertToDmx(TimeInfo startTime, TimeSignature signature, int bpm, String params) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();
		String[] paramsArr = params.split(":",2);
		CommandParameters parameters = new CommandParameters(paramsArr[1]);

		if (paramsArr[0].equals(OP_MODE)) {

			values.addAll(changePatch(
					startTime,
					signature,
					bpm, parameters.getSpeed()));

		
		} else {
			throw new RuntimeException("Unknown operation: " + paramsArr[0] + " (" + params +")");
		}

		return values;
	}

	private List<TimedDmxValue> changePatch(TimeInfo time, TimeSignature signature, int bpm, int patch) {

		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

		values.add(new TimedMidiValue(timeMillis, this.getChannel(), -1, -1, patch, -1));
		
		return values;
	}

	
}
