package com.kevinisabelle.dmxlive.api.output.video;

import com.kevinisabelle.dmxlive.api.driver.VideoDriver;
import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.api.output.video.commands.AbstractVideoCommand;

/**
 *
 * @author kevin
 */
public abstract class  VideoFixture extends Fixture<VideoDriver, AbstractVideoCommand>{

    public VideoFixture(VideoDriver output) {
        super(output);
    }
	
}
