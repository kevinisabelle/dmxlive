package com.kevinisabelle.dmxlive.api.output.video.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.video.VideoFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractVideoCommand extends Command<VideoFixture> {

    public AbstractVideoCommand(TimeInfo startOffset, List<VideoFixture> fixtures, int scriptLineRef, String scriptItem) {
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }

}
