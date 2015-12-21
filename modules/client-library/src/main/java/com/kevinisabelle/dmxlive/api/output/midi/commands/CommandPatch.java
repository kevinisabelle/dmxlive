package com.kevinisabelle.dmxlive.api.output.midi.commands;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
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
    protected void fromScript(String scriptItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String toScript() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TimedEvent> compute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
