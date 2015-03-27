package com.kevinisabelle.dmxlive.api.driver;

import com.kevinisabelle.dmxlive.api.Driver;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 */
public abstract class MidiDriver implements Driver {

  public abstract void transmit(MidiEvent midiEvt);

  public abstract MidiEvent receive();
}
