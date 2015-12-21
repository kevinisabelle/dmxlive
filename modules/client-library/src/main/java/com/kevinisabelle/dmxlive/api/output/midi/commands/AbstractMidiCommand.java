package com.kevinisabelle.dmxlive.api.output.midi.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.midi.MidiFixture;

import java.util.List;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractMidiCommand extends Command<MidiFixture> {

    public AbstractMidiCommand(String scriptItem) {
        super(scriptItem);
    }

}
