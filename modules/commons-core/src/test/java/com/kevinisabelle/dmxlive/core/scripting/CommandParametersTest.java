/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.api.output.dmx.Color;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kisabelle
 */
public class CommandParametersTest {
	
	public CommandParametersTest() {

	}
	
	
	@Test
	public void testFullCommandParameters() {
	
		CommandParameters params1 = new CommandParameters("c=red_blue_green,d=100%_100_100,s=50,t=2_2_1/2,m=modeKev,o=0.5,u=16,r=0111 0101 101 10 12110");
	
		assertEquals(3, params1.getColors().length);
		assertEquals(3, params1.getDimmers().length);
		assertEquals(50, params1.getSpeed().intValue());
		assertEquals(new TimeInfo("2:2:0.5").getMeasure(), params1.getDuration().getMeasure());
		assertEquals(new TimeInfo("2:2:0.5").getBeat(), params1.getDuration().getBeat());
		assertEquals(new TimeInfo("2:2:0.5").getSubBeat(), params1.getDuration().getSubBeat());
		assertEquals("011101011011012110", new String(params1.getRiffNotes()));
		assertEquals(16, params1.getNoteUnit().intValue());
		assertEquals(0.5, params1.getOnTime());
		assertEquals("modeKev", params1.getMode());
		
	}
	
	@Test
	public void testPartialCommandParameters() {
	
		CommandParameters params1 = new CommandParameters("c=red_blue_green");
	
		assertEquals(3, params1.getColors().length);
		assertEquals(6, params1.getDimmers().length);
		assertEquals(100, params1.getSpeed().intValue());
		assertEquals(new TimeInfo("1:0:0").getMeasure(), params1.getDuration().getMeasure());
		assertEquals(new TimeInfo("1:0:0").getBeat(), params1.getDuration().getBeat());
		assertEquals(new TimeInfo("1:0:0").getSubBeat(), params1.getDuration().getSubBeat());
		assertEquals("0", new String(params1.getRiffNotes()));
		assertEquals(4, params1.getNoteUnit().intValue());
		assertEquals(0.5, params1.getOnTime());
		assertEquals("Mode1", params1.getMode());
		
	}
	
	@Test
	public void testColorsAsIndex(){
		
		CommandParameters params1 = new CommandParameters("c=0_1_2_3_4_5_6");
	
		assertEquals(7, params1.getColors().length);
		
		assertEquals(params1.getColors()[0], new Color(Color.ColorEnum.BLACK));
		assertEquals(params1.getColors()[1], new Color(Color.ColorEnum.RED));
		assertEquals(params1.getColors()[2], new Color(Color.ColorEnum.GREEN));
		assertEquals(params1.getColors()[3], new Color(Color.ColorEnum.BLUE));
		assertEquals(params1.getColors()[4], new Color(Color.ColorEnum.YELLOW));
		assertEquals(params1.getColors()[5], new Color(Color.ColorEnum.WHITE));
		assertEquals(params1.getColors()[6], new Color(Color.ColorEnum.ORANGE));
		
		assertEquals(6, params1.getDimmers().length);
		assertEquals(100, params1.getSpeed().intValue());
		assertEquals(new TimeInfo("1:0:0").getMeasure(), params1.getDuration().getMeasure());
		assertEquals(new TimeInfo("1:0:0").getBeat(), params1.getDuration().getBeat());
		assertEquals(new TimeInfo("1:0:0").getSubBeat(), params1.getDuration().getSubBeat());
		assertEquals("0", new String(params1.getRiffNotes()));
		assertEquals(4, params1.getNoteUnit().intValue());
		assertEquals(0.5, params1.getOnTime());
		assertEquals("Mode1", params1.getMode());
	}
	
	@Test
	public void testDimmers(){
		
		CommandParameters params1 = new CommandParameters("d=99%_45");
	
		assertEquals(2, params1.getDimmers().length);
		
		assertEquals(params1.getDimmers()[0].intValue(), 99);
		assertEquals(params1.getDimmers()[1].intValue(), 45);
		
		assertEquals(100, params1.getSpeed().intValue());
		assertEquals(new TimeInfo("1:0:0").getMeasure(), params1.getDuration().getMeasure());
		assertEquals(new TimeInfo("1:0:0").getBeat(), params1.getDuration().getBeat());
		assertEquals(new TimeInfo("1:0:0").getSubBeat(), params1.getDuration().getSubBeat());
		assertEquals("0", new String(params1.getRiffNotes()));
		assertEquals(4, params1.getNoteUnit().intValue());
		assertEquals(0.5, params1.getOnTime());
		assertEquals("Mode1", params1.getMode());
	}
	
	@Test
	public void testUnknownParam(){
		
		boolean isException = false;
		
		try {
			
			CommandParameters params1 = new CommandParameters("notd=test");
			System.out.println(params1);
			
		} catch (IllegalArgumentException e){
			System.out.println(e.getMessage());
			isException = true;
		}
		
		if (!isException){
			fail();
		}		
	}
}
