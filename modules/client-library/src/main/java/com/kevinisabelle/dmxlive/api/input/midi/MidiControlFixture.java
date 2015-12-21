package com.kevinisabelle.dmxlive.api.input.midi;

import com.kevinisabelle.dmxlive.api.driver.MidiDriver;
import com.kevinisabelle.dmxlive.api.input.ControlFixture;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public abstract class MidiControlFixture extends ControlFixture<MidiDriver> {

    public MidiControlFixture(MidiDriver input) {
        super(input);
    }

    public abstract int getScene(MidiEvent midiEv);
}
