package com.kevinisabelle.dmxlive.api.output.midi.commands;

import com.kevinisabelle.dmxlive.api.output.Command;

import java.util.List;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractMidiCommand extends Command {

    public AbstractMidiCommand(String scriptItem) {
        super(scriptItem);
    }

    public abstract List<MidiEvent> computeCommand();
}
