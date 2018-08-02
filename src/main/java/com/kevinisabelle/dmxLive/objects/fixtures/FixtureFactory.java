package com.kevinisabelle.dmxLive.objects.fixtures;

import com.kevinisabelle.dmxLive.DmxLive;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Creates fixtures from names and set the channels.
 * @author kevin
 */
public class FixtureFactory {
	
	public static final Map<String, Class> fixtureClasses = new HashMap<String, Class>();
	static {
		fixtureClasses.put("Par64", LumiLEDPar64.class);
		fixtureClasses.put("LEDBar", LumiMKII.class);
		fixtureClasses.put("Swarm4", ChauvetSwarm4.class);
		fixtureClasses.put("DerbyX", ChauvetDerbyX.class);
		fixtureClasses.put("DLM001", ElectroCastleDLM001.class);
		fixtureClasses.put("DMXRLY001", ElectroCastleDMXRLY001.class);
		fixtureClasses.put("GT100", BossGT100.class);
	}
	
	private static Map<String, Fixture> loadedFixtureInstances = new HashMap<String, Fixture>();
		
	protected FixtureFactory(){
		
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
			fixture.setChannel(Integer.parseInt(nameAndChannel[1]));
			
		} catch (InstantiationException ex) {
			
			JOptionPane.showMessageDialog(DmxLive.getCurrentFrame(), ex);
			
		} catch (IllegalAccessException ex) {
			
			JOptionPane.showMessageDialog(DmxLive.getCurrentFrame(), ex);
		}
		
		loadedFixtureInstances.put(fixtureName, fixture);
		
		return fixture;
	}
	
}
