package com.kevinisabelle.dmxlive.core.engine;

import com.kevinisabelle.dmxlive.core.model.Command;
import com.kevinisabelle.dmxlive.core.engine.processes.MasterClock;
import com.kevinisabelle.dmxlive.core.Configuration;
import com.kevinisabelle.dmxlive.core.scripting.Song;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author kevin
 */
public class ExecutionEngine {

	Configuration config;
	ThreadPoolExecutor executor;
	MasterClock clock;

	public ExecutionEngine(){

		clock = new MasterClock();
	}

	public void init(Configuration configuration){

		this.config = configuration;
	}

	public void start(){
		
	}

	public void stop(){

	}

	public void pause(){

	}



	public void execute(List<Command> commands, long offset) {

		// Create an executor

		// Run the executor



	}

}
