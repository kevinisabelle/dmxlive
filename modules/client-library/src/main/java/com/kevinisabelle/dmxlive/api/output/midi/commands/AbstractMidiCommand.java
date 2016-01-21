package com.kevinisabelle.dmxlive.api.output.midi.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.midi.MidiFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;

import java.util.List;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractMidiCommand extends Command<MidiFixture> {

    public AbstractMidiCommand(TimeInfo startOffset, List<MidiFixture> fixtures, int scriptLineRef, String scriptItem) {
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }

}
