package com.kevinisabelle.dmxlive.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Hardcoded configuration for system running.
 *
 * @author kevin
 */
public class Constants {
	
	public static final String VERSION = "1.0.3";

	public static final String METRONOME_WAVES = "wav/metronome";
	public static final int DMX_RATE = 8; // in 1/x of a beat
	public static final int CHECK_RESOLUTION = 16; // in 1/x of a beat
	public static final int DMXRUNNABLE_OFFSET = -30; // in ms
	public static final boolean ENABLE_DMX_LOG = true;
	public static final long ONE_BEAT_MS_TOLERANCE = 50;
	public static final long METRONOME_REFRESH_TIMEOUT = 15;
	public static final String[] metronomeSoundsHi;
	public static final String[] metronomeSoundsLow;
	public static final String PROPERTIES_LOCATION = "conf/dmxlive.properties";
	public static final String LOGO_LOCATION = "conf/DMX-LIVE-logo-small.png";
	public static final String LOG4J_CONFIG = "conf/log4j.properties";
	/**
	 * Theme skin *
	 */
	public static final Font FONT_LOG = new Font("Courier", Font.PLAIN, 12);
	public static final Font FONT_BIG = new Font("Verdana", Font.BOLD, 18);
	public static final Font FONT_SUPER_BIG = new Font("Verdana", Font.BOLD, 28);
	public static final Color THEME_COLOR_1 = new Color(0x4E, 0x66, 0xAF);
	public static final Color THEME_COLOR_2 = new Color(0x95, 0xD6, 0xF0);
	public static final Color THEME_COLOR_3 = Color.BLACK;
	public static BufferedImage LOGO_IMAGE = null;

	static {
		File wavesDirectory = new File(METRONOME_WAVES);
		List<String> metronomeSounds = new ArrayList<String>();
		for (String filename : wavesDirectory.list()) {
			metronomeSounds.add(filename);
		}

		metronomeSoundsHi = metronomeSounds.toArray(new String[0]);
		metronomeSoundsLow = metronomeSounds.toArray(new String[0]);

		try {
			LOGO_IMAGE = ImageIO.read(new File(LOGO_LOCATION));
		} catch (IOException ex) {

		}
	}
	
	protected Constants(){
		
	}
}
