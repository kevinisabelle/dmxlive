package com.kevinisabelle.dmxlive.api.output.midi.commands;

import com.kevinisabelle.dmxlive.api.output.midi.MidiFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author Kevin
 */
public class CommandCC extends AbstractMidiCommand {

    public CommandCC(TimeInfo startOffset, List<MidiFixture> fixtures, int scriptLineRef, String scriptItem) {
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }


    
}
