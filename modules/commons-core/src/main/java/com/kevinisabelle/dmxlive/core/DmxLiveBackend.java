package com.kevinisabelle.dmxlive.core;

import com.kevinisabelle.dmxlive.core.engine.ExecutionEngine;
import com.kevinisabelle.dmxlive.core.model.Project;

public class DmxLiveBackend {

	private Project currentProject;
	private Configuration configuration;
	private ExecutionEngine executionEngine;
	
	public DmxLiveBackend(){
		
		currentProject = null;
		//configuration = Configuration.Load();
		executionEngine = new ExecutionEngine();
		
	}

	public void loadProject(){

	}

	public void saveProject(){

	}

	public void startEngine(){

	}

	public void pauseEngine(){

	}

	public void stopEngine(){

	}

	public void seekToPosition(long clockMillis){

	}



}
