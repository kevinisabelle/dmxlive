package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.core.model.Command;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractCommandExecutor implements Runnable {

	protected MasterClock masterClockRef;
	protected List<Command> commandsQueue;
	protected boolean loop;

	public AbstractCommandExecutor(MasterClock masterClock){
		this.masterClockRef = masterClock;
	}

	@Override
	public void run(){
		

	}
}
