package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author kisabelle
 */
public class ScriptCommandTest {
	
	public ScriptCommandTest() {

	}

	
	@Test
	@Ignore
	public void testScriptCommandStandard() {
	
		String strScript = "$test\n"
				+ "1:0:0	|Par64_3	|Dim:c=yellow_0,d=100_50,t=1:0:0\n"
				+ "end $\n"
				+ "\n"
				+ "1:0:0	|$test";
		
		Script script = new Script(strScript);
		
		List<TimedDmxEvent> values = script.getTimedDmxEvents(new TimeInfo("0:0:0"), new TimeSignature(4, 4), 120, null);
	
		System.out.println(values);
	}
	
	@Test
	@Ignore
	public void testScriptCommandVariables() {
	
		String strScript = "$test\n"
				+ "1:0:0	|Par64_3	|Dim:c=%1_0,d=%2_50,t=1:0:0\n"
				+ "end $\n"
				+ "\n"
				+ "1:0:0	|$test(yellow,100)	|4,1:0:0";
		
		Script script = new Script(strScript);
		
		List<TimedDmxEvent> values = script.getTimedDmxEvents(new TimeInfo("0:0:0"), new TimeSignature(4, 4), 120, null);
	
		System.out.println(values);
	}
}
