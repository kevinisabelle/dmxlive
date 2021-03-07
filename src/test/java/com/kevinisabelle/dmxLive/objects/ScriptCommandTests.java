/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.objects;

import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/**
 *
 * @author kisabelle
 */
public class ScriptCommandTests extends TestCase {

	public ScriptCommandTests(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testScriptCommandStandard() {

		String strScript = "$test\n" + "1:0:0	|Par64_3	|Dim:c=yellow_0,d=100_50,t=1:0:0\n" + "end $\n" + "\n"
				+ "1:0:0	|$test";

		Script script = new Script(strScript, null);

		List<TimedDmxValue> values = script.getTimedDmxValues(new TimeInfo("0:0:0"), new TimeSignature(4, 4), 120,
				null);

		System.out.println(values);
	}

	@Test
	public void testScriptCommandVariables() {

		String strScript = "$test\n" + "1:0:0	|Par64_3	|Dim:c=%1_0,d=%2_50,t=1:0:0\n" + "end $\n" + "\n"
				+ "1:0:0	|$test(yellow,100)	|4,1:0:0";

		Script script = new Script(strScript, null);

		List<TimedDmxValue> values = script.getTimedDmxValues(new TimeInfo("0:0:0"), new TimeSignature(4, 4), 120,
				null);

		System.out.println(values);
	}
}
