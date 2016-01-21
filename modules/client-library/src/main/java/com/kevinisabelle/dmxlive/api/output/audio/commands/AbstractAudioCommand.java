package com.kevinisabelle.dmxlive.api.output.audio.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.audio.AudioFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractAudioCommand extends Command<AudioFixture> {

    public AbstractAudioCommand(TimeInfo startOffset,  List<AudioFixture> fixtures, int absoluteScriptLineNumber, String scriptLine) {
        super(startOffset, fixtures, absoluteScriptLineNumber, scriptLine);
    }

}
