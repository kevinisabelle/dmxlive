package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.api.output.Command;

/**
 * Observer interface for the executor process.
 * @author kevin
 */
public interface IExecutorObserver {
	
	void executorReport(AbstractCommandExecutor executor, String message, long millis);
	
	void commandExecuted(AbstractCommandExecutor executor, Command command);
	
	void processStarted(AbstractCommandExecutor executor);
	
	void processEnded(AbstractCommandExecutor executor, boolean isComplete);
		
}
