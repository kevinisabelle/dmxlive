package com.kevinisabelle.dmxLive.dmx;

import com.juanjo.openDmx.OpenDmx;
import org.apache.log4j.Logger;

/**
 * Wrapper class for the OpenDMX driver.
 * @author kevin
 */
public class DmxManager {
	
	private static Logger logger = Logger.getLogger(DmxManager.class);
	private boolean isSendSimulatorValues = false;
	
	public DmxManager() {

		try {

			boolean isOpen = OpenDmx.connect(OpenDmx.OPENDMX_TX);
			
			logger.info("OpenDMX box: " + (isOpen ? "* CONNECTED *" : "***** NOT CONNECTED *****"));

		} catch (Exception e) {
			logger.error("Cannot open OpenDMX driver: " + e.getMessage(), e);
		}
	}
	
	public void stop() throws Exception {
		OpenDmx.disconnect();
	}
	
	public void setIsSendSimulatorValues(boolean isEnabled){
		isSendSimulatorValues = isEnabled;
	}
	
	public void sendSignal(int channel, int data) throws Exception {
		
		if (isSendSimulatorValues){
			//FixtureSimulator.setValue(channel, data);
		}
		
		OpenDmx.setValue(channel, data);
	}
}
