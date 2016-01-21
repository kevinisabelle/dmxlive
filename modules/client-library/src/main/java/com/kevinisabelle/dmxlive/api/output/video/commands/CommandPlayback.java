package com.kevinisabelle.dmxlive.api.output.video.commands;

import com.kevinisabelle.dmxlive.api.output.video.VideoFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandPlayback extends AbstractVideoCommand {

    public CommandPlayback(TimeInfo startOffset, List<VideoFixture> fixtures, int scriptLineRef, String scriptItem) {
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }

}
