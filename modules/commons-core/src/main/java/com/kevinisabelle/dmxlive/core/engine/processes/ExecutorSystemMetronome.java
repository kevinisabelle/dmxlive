package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.core.engine.MasterClock;

/**
 *
 * @author kisabelle
 */
public class ExecutorSystemMetronome extends ExecutorAudioCommand {

	public ExecutorSystemMetronome(MasterClock clock){
		super(clock);
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
