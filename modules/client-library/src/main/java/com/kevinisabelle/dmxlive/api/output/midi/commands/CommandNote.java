/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.api.output.midi.commands;

import java.util.List;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public class CommandNote extends AbstractMidiCommand {

    public CommandNote(String scriptItem) {
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
