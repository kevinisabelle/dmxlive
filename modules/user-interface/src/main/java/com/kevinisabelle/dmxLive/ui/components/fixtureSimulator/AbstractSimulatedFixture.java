/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.ui.components.fixtureSimulator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author kevin
 */
public abstract class AbstractSimulatedFixture extends JPanel {
	
	private Map<Integer, Integer> dmxValues = new HashMap<Integer, Integer>();
	private Integer channel = 0;
	
	public AbstractSimulatedFixture(Integer channel){
		this.channel = channel;
	}
	
	void setValue(int channel, int value) {
		if (channel >= this.channel && channel <= this.channel + getTotalChannels()){
			dmxValues.put(channel, value);
		}
	}
	
	protected Integer getValue(int channel){
		return dmxValues.get(channel);
	}
	
	abstract Integer getTotalChannels();
	
	public Integer getChannel(){
		return channel;
	}
}
