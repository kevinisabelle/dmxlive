package com.kevinisabelle.dmxlive.api.output.dmx;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;

public class TimedDmxEvent extends TimedEvent {

  private int channel;
  private int value;

  public TimedDmxEvent(long millis, int channel, int value) {
    super(millis);
    this.value = value;
    this.channel = channel;
  }

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

  /**
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "DMX: " + millis + ": " + channel + "->" + value;
  }
}
