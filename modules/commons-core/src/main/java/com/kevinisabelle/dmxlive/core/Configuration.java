package com.kevinisabelle.dmxlive.core;

import com.kevinisabelle.dmxlive.api.input.midi.MidiControl;
import com.kevinisabelle.dmxlive.api.driver.AudioDriver;
import com.kevinisabelle.dmxlive.api.driver.DmxDriver;
import com.kevinisabelle.dmxlive.api.driver.MidiDriver;
import com.kevinisabelle.dmxlive.api.driver.TextDriver;
import com.kevinisabelle.dmxlive.api.output.AbstractFixture;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class Configuration {
	
	private Map<String, DmxDriver> dmxDrivers;
	private Map<String, MidiDriver> midiDrivers;
	private Map<String, TextDriver> textDrivers;
	private Map<String, AudioDriver> audioDrivers;
	
	private Map<String, MidiControl> midiControllers;
	
	private Map<String, AbstractFixture> fixtureDefinitions;
}
