package com.kevinisabelle.dmxlive.core;

import com.kevinisabelle.dmxlive.api.driver.AudioDriver;
import com.kevinisabelle.dmxlive.api.driver.DmxDriver;
import com.kevinisabelle.dmxlive.api.driver.MidiDriver;
import com.kevinisabelle.dmxlive.api.driver.TextDriver;
import com.kevinisabelle.dmxlive.api.driver.VideoDriver;
import com.kevinisabelle.dmxlive.api.input.ControlFixture;
import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.core.factory.DriverFactory;
import com.kevinisabelle.dmxlive.core.factory.FixtureFactory;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;


/**
 *
 * @author kevin
 */
public class Configuration {
	
	private static Logger logger = Logger.getLogger(Configuration.class);
	
	private DriverFactory driversFactory;
	
	private Map<String, DmxDriver> dmxDrivers;
	private Map<String, MidiDriver> midiDrivers;
	private Map<String, TextDriver> textDrivers;
	private Map<String, AudioDriver> audioDrivers;
	private Map<String, VideoDriver> videoDrivers;
	
	private Map<String, ControlFixture> controllersDefinitions;	
	private Map<String, Fixture> fixtureDefinitions;
	
	protected Configuration(){
		
		logger.info("Init configuration...");
		
		try {
			driversFactory = new DriverFactory();
		} catch (IOException ex) {
			
		}
		
		logger.info("Done configuration init.");
	}
}
