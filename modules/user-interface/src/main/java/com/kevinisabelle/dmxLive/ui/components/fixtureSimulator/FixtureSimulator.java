/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.ui.components.fixtureSimulator;

import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.core.scripting.Script;
import com.kevinisabelle.dmxlive.core.scripting.ScriptCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author kevin
 */
public class FixtureSimulator extends JFrame {
	
	private static Map<String, AbstractSimulatedFixture> fixtures = new HashMap<String, AbstractSimulatedFixture>();
	
	public static void setValue(int channel, int value){
	
		for (AbstractSimulatedFixture fixture : fixtures.values()){
			fixture.setValue(channel, value);
		}		
	}
	
	public FixtureSimulator(Script script){
		
		Map<String, Fixture> fixturesAll = new HashMap<String, Fixture>();
		
		for (ScriptCommand command: script.getCommands()){
			//command.getFixtures().
		}
		//script.getCommands()
		
		
	}
	
}
