package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.helper.TimeHelper;
import com.kevinisabelle.dmxLive.objects.TimeInfo;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.TimedDmxValue;
import com.kevinisabelle.dmxLive.objects.TimedLyricsValue;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class BluetoothLyricsFixture  extends Fixture {

	@Override
	public List<TimedDmxValue> convertToDmx(TimeInfo startTime, TimeSignature signature, int bpm, String params) {
		
		List<TimedDmxValue> values = new ArrayList<TimedDmxValue>();

		long timeMillis = TimeHelper.getMilliseconds(startTime, signature, bpm);

		values.add(new TimedLyricsValue(timeMillis, this.getChannel(), 0, params));
		
		return values;
		
	}
	
}
