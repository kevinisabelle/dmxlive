package com.kevinisabelle.dmxlive.api.output.midi.commands;

import java.util.List;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public class CommandPatch extends AbstractMidiCommand {

    public CommandPatch(String scriptItem) {
        super(scriptItem);
    }

    @Override
    public List<MidiEvent> computeCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fromScript(String scriptItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
