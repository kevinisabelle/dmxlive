package com.kevinisabelle.dmxLive.processes;

/**
 * Observer interface for the DMXRunnable process.
 * @author kevin
 */
public interface DmxRunnableObserver {
	
	void logMessage(String message, long millis);
	
	void updatePosition(long millis);
	
	void stopNotify(boolean isComplete);
		
	void openFile(String filename);
}
