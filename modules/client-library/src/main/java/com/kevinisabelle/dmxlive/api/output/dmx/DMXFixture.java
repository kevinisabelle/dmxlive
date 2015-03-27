package com.kevinisabelle.dmxlive.api.output.dmx;

import com.kevinisabelle.dmxlive.api.driver.DmxDriver;
import com.kevinisabelle.dmxlive.api.output.AbstractFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;

import java.util.List;

import com.kevinisabelle.dmxlive.api.output.dmx.commands.AbstractDMXCommand;

/**
 *
 * @author kevin
 */
public abstract class DMXFixture extends AbstractFixture<DmxDriver> {

  private int channel;

  public static final String OP_DIM = "Dim";

  public static final String OP_STROBE = "Strobe";

  public static final String OP_FADE = "Fade";

  public static final String OP_MODE = "Mode";

  public static final String OP_BLINK = "Blink";

  public static final String OP_PULSE = "Pulse";

  public static final String OP_RIFF = "Riff";

  public static final String OP_POSITION = "Position";

  public DMXFixture(DmxDriver output) {
    super(output);
  }

  public abstract List<TimedDmxValue> convertToDmx(TimeInfo startTime, TimeSignature signature, int bpm, String params);

  public abstract List<AbstractDMXCommand> getSupportedCommmands();

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

  protected abstract void sendDMX(int channel, int value);
}
