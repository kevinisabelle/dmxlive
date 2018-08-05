/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.objects;

/**
 *
 * @author kevin
 */
public class TimedLyricsValue extends TimedDmxValue {
	
	private String lyricsContent;
	
	public TimedLyricsValue(long millis, int channel, int value, String lyrics) {
		super(millis, channel, value);
		this.lyricsContent = lyrics;
		
	}

	/**
	 * @return the lyricsContent
	 */
	public String getLyricsContent() {
		return lyricsContent;
	}

	/**
	 * @param lyricsContent the lyricsContent to set
	 */
	public void setLyricsContent(String lyricsContent) {
		this.lyricsContent = lyricsContent;
	}

	
}
