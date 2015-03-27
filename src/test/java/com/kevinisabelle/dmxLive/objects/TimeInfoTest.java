/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.objects;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 *
 * @author kevin
 */
public class TimeInfoTest extends TestCase{
	
	public TimeInfoTest(String testName) {
		super(testName);
	}
	
	public void testTimeInfoFromMillis(){
		
		long millis = 5750l;
		TimeSignature signature = new TimeSignature("4/4");
		int bpm = 120;
		
		TimeInfo timeInfo = new TimeInfo(signature, bpm, millis);
		System.out.println("Parsed time from millis: " + timeInfo);
		
		TimeInfo timeInfo3 = new TimeInfo("2:1:3/4");
		assertEquals(0.75, timeInfo3.getSubBeat());
		
		TimeInfo timeInfo2 = new TimeInfo("2:1:0.5");
		assertEquals(0.5, timeInfo2.getSubBeat());
		
	}
	
	public void testArithmerics(){
		
		TimeInfo timeInfo = new TimeInfo("2:1:3/4");
		TimeInfo timeInfo2 = new TimeInfo("1:0:0");
		
		TimeInfo result = timeInfo.add(timeInfo2, new TimeSignature(4, 4));
		
		assertEquals(3, result.getMeasure());
		assertEquals(1, result.getBeat());
		assertEquals(0.75, result.getSubBeat());
		
	}
	
}
