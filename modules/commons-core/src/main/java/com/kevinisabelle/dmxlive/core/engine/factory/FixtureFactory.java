package com.kevinisabelle.dmxlive.core.engine.factory;

import com.kevinisabelle.dmxlive.api.output.AbstractFixture;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates fixtures from names and set the channels.
 * @author kevin
 */
public class FixtureFactory {
	
	public static Map<String, Class> fixtureClasses = new HashMap<String, Class>();
	
	private static Map<String, AbstractFixture> loadedFixtureInstances = new HashMap<String, AbstractFixture>();
		
	protected FixtureFactory(){
		
	}
	
	private void findDeviceDriversInClasspath(){
		
		
		
	}
	
	public static AbstractFixture getFixture(String fixtureName){
		
		if (loadedFixtureInstances.get(fixtureName) != null){
			return loadedFixtureInstances.get(fixtureName);
		}
		
		String[] nameAndChannel = fixtureName.split("_");
		Class classz = fixtureClasses.get(nameAndChannel[0]);
		
		AbstractFixture fixture = null;
		try {
			
			fixture = (AbstractFixture)classz.newInstance();
			//fixture.setChannel(Integer.parseInt(nameAndChannel[1]));
			
		} catch (InstantiationException ex) {
			
			//JOptionPane.showMessageDialog(DmxLive.getCurrentFrame(), ex);
			
		} catch (IllegalAccessException ex) {
			
			//JOptionPane.showMessageDialog(DmxLive.getCurrentFrame(), ex);
		}
		
		loadedFixtureInstances.put(fixtureName, fixture);
		
		return fixture;
	}
	
}
