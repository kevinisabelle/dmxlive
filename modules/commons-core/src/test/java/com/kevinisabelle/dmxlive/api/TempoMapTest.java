
package com.kevinisabelle.dmxlive.api;

import com.kevinisabelle.dmxlive.music.Tempo;
import com.kevinisabelle.dmxlive.music.TempoMap;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kevin
 */
public class TempoMapTest {
	
	public TempoMapTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void TestCreate(){
		
		TempoMap map = new TempoMap();
		
		/**
		 * 1:1:0|T:4/4@128
		 * 14:1:0|T:12/4@68
		 * ...
		 * 
		 */
		
		map.Add(new Tempo(new TimeInfo(1,1,0), new TimeSignature("4/4"), 128));
		map.Add(new Tempo(new TimeInfo(14,1,0), new TimeSignature("12/8"), 68));
		map.Add(new Tempo(new TimeInfo(28,1,0), new TimeSignature("5/4"), 128));
		map.Add(new Tempo(new TimeInfo(32,1,0), new TimeSignature("2/4"), 128));
		map.Add(new Tempo(new TimeInfo(33,1,0), new TimeSignature("4/4"), 128));
		
		long millis = map.getAbsoluteTimeAt(new TimeInfo(32,1,0));
		
		TimeInfo info = map.getTimeInfoAt(millis);
	}
	
}
