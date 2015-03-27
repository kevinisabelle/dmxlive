package com.kevinisabelle.dmxLive.ui.components.fixtureSimulator;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author kevin
 */
public class LumiLEDPar64Simulator extends AbstractSimulatedFixture {

	public static final int TOTAL_CHANNELS = 7;
	
	public static final int CHANNEL_DIMMER = 0;
	public static final int CHANNEL_RED = 1;
	public static final int CHANNEL_GREEN = 2;
	public static final int CHANNEL_BLUE = 3;
	public static final int CHANNEL_SPEED = 4;
	public static final int CHANNEL_MODE = 5;

	public LumiLEDPar64Simulator(int channel) {
		super(channel);
	}

	@Override
	protected void paintComponent(Graphics g) {

		Color color = new Color(getValue(CHANNEL_RED), getValue(CHANNEL_GREEN), getValue(CHANNEL_BLUE));

		super.paintComponent(g);
		int width = getWidth() / 2;
		int height = getHeight() / 2;
		g.setColor(color);
		g.fillOval(5, 5, width, height);
	}

	@Override
	Integer getTotalChannels() {
		return TOTAL_CHANNELS;
	}
}
