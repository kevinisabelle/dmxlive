package com.kevinisabelle.dmxlive.api.control.midi;

import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public abstract class MidiControl {

    public abstract int getScene(MidiEvent midiEv);
}
