package com.kevinisabelle.dmxlive.core.engine;

import com.kevinisabelle.dmxlive.core.scripting.Command;
import com.kevinisabelle.dmxlive.core.Configuration;
import com.kevinisabelle.dmxlive.core.engine.factory.CommandExectorFactory;
import com.kevinisabelle.dmxlive.core.engine.processes.AbstractCommandExecutor;
import com.kevinisabelle.dmxlive.music.TempoMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import javax.sound.sampled.Clip;

/**
 *
 * @author kevin
 */
public class ExecutionEngine {

	Configuration config;
	ThreadPoolExecutor executor;
	List<AbstractCommandExecutor> processes;
	MasterClock clock;
	
	CommandExectorFactory commandExecutorFactory;

	public ExecutionEngine(){

		
	}

	public void init(Configuration configuration, TempoMap tempoMap, Clip audioReference){

		this.config = configuration;
		this.clock = new MasterClock(tempoMap, audioReference);
		
		
		
	}

	public void start(){
		
	}

	public void stop(){
		
	}

	public void pause(){
		clock.pause();
	}

	public void executeQueued(List<Command> commands, long offset) {

		// Create an executor

		// Run the executor



	}
	
	public void executeNow(List<Command> commands){
		
	}

}
