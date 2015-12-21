package com.kevinisabelle.dmxlive.api.output.video.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.video.VideoFixture;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractVideoCommand extends Command<VideoFixture> {

    public AbstractVideoCommand(String scriptItem) {
        super(scriptItem);
    }

}
