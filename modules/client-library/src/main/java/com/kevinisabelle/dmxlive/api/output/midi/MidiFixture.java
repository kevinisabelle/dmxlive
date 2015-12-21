package com.kevinisabelle.dmxlive.api.output.midi;

import com.kevinisabelle.dmxlive.api.driver.MidiDriver;
import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;

import java.util.List;

import com.kevinisabelle.dmxlive.api.output.midi.commands.AbstractMidiCommand;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author kevin
 */
public abstract class MidiFixture extends Fixture<MidiDriver> {

  private int channel;

  public static final String OP_SET_PATCH = "Patch";

  public static final String OP_SEND_NOTE = "Note";

  public static final String OP_SEND_CC = "CC";

  public MidiFixture(MidiDriver output) {
    super(output);
  }

  public abstract List<MidiEvent> convertToMidiEvents(TimeInfo startTime, TimeSignature signature, int bpm, String params);

  public abstract List<AbstractMidiCommand> getSupportedCommmands();

  /**
   * @return the channel
   */
  public int getChannel() {
    return channel;
  }

  /**
   * @param channel the channel to set
   */
  public void setChannel(int channel) {
    this.channel = channel;
  }

  protected abstract void sendMidi(MidiEvent evt);
}
