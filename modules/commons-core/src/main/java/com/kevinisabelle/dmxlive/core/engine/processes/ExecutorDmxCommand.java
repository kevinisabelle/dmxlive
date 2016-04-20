/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kevinisabelle.dmxlive.core.engine.processes;

import com.kevinisabelle.dmxlive.api.driver.DmxDriver;
import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import com.kevinisabelle.dmxlive.core.engine.MasterClock;
import com.kevinisabelle.dmxlive.core.factory.DriverFactory;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class ExecutorDmxCommand extends AbstractCommandExecutor {

	private DmxDriver driver;
	
	public ExecutorDmxCommand(MasterClock clock){
		super(clock, 1000l, 24l);
		
		driver = 
	}

	@Override
	protected void SendEventsToDrivers(List<TimedEvent> events) {
	
		for (TimedEvent event : events){
		
			TimedDmxEvent dmxEvent  = (TimedDmxEvent)event;
			
			driver.transmit(dmxEvent.getChannel(), dmxEvent.getValue());
			
		}
	}

	
}
