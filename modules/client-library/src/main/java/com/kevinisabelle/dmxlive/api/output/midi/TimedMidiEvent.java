package com.kevinisabelle.dmxlive.api.output.midi;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import javax.sound.midi.MidiEvent;

public class TimedMidiEvent extends TimedEvent {

  private MidiEvent event;

  public TimedMidiEvent(long millis, MidiEvent event) {
    super(millis);
    this.event = event;
  }

  @Override
  public String toString() {
    return "Midi: " + millis + ": " + getEvent().toString();
  }

    /**
     * @return the event
     */
    public MidiEvent getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(MidiEvent event) {
        this.event = event;
    }
}
