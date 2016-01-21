package com.kevinisabelle.dmxlive.core.factory;

import com.kevinisabelle.dmxlive.api.output.Fixture;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates fixtures from names and set the channels.
 * @author kevin
 */
public class FixtureFactory {
	
	public static Map<String, Class> fixtureClasses = new HashMap<String, Class>();
	
	private static Map<String, Fixture> loadedFixtureInstances = new HashMap<String, Fixture>();
		
	protected FixtureFactory(){
		
	}
	
	private void findDeviceDriversInClasspath(){
		
		
		
	}
	
	public static Fixture getFixture(String fixtureName){
		
		if (loadedFixtureInstances.get(fixtureName) != null){
			return loadedFixtureInstances.get(fixtureName);
		}
		
		String[] nameAndChannel = fixtureName.split("_");
		Class classz = fixtureClasses.get(nameAndChannel[0]);
		
		Fixture fixture = null;
		try {
			
			fixture = (Fixture)classz.newInstance();
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
