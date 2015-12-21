package com.kevinisabelle.dmxlive.api.input.midi;

import com.kevinisabelle.dmxlive.api.driver.MidiDriver;
import com.kevinisabelle.dmxlive.api.input.ControlFixture;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public abstract class MidiControl extends ControlFixture<MidiDriver> {

    public MidiControl(MidiDriver input) {
        super(input);
    }

    public abstract int getScene(MidiEvent midiEv);
}
