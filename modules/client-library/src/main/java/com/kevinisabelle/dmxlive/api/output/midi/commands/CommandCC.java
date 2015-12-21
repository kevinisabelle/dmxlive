package com.kevinisabelle.dmxlive.api.output.midi.commands;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.api.output.midi.TimedMidiEvent;
import java.util.List;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public class CommandCC extends AbstractMidiCommand {

    public CommandCC(String scriptItem) {
        super(scriptItem);
    }

    @Override
    public List<TimedEvent> compute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fromScript(String scriptItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String toScript() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
