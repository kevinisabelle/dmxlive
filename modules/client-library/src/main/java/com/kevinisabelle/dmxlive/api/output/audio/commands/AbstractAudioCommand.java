package com.kevinisabelle.dmxlive.api.output.audio.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.audio.AudioFixture;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractAudioCommand extends Command<AudioFixture> {

    public AbstractAudioCommand(String scriptItem) {
        super(scriptItem);
    }

}
