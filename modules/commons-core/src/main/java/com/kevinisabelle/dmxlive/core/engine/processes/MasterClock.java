package com.kevinisabelle.dmxlive.core.engine.processes;

import org.apache.log4j.Logger;

/**
 *
 * @author kisabelle
 */
public class MasterClock implements Runnable {

	Logger logger = Logger.getLogger(MasterClock.class);

	private long resolution;
	private long currentAbsolutePositionMillis;
	private boolean isRunning;

	@Override
	public void run() {

		logger.info("Starting MasterClock at resolution:" + resolution);

		while (isRunning){



		}

		logger.info("Stopping MasterClock.");
	}

	public long getAbsoluteTime(){
		return 0l;
	}

}
