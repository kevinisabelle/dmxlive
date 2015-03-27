package com.kevinisabelle.dmxlive.api.output.dmx;

public class TimedDmxValue implements Comparable<TimedDmxValue> {

  private long millis;

  private int channel;

  private int value;

  public TimedDmxValue(long millis, int channel, int value) {
    this.millis = millis;
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

  /**
   * @return the millis
   */
  public long getMillis() {
    return millis;
  }

  /**
   * @param millis the millis to set
   */
  public void setMillis(long millis) {
    this.millis = millis;
  }

  @Override
  public String toString() {
    return millis + ": " + channel + "->" + value;
  }

  @Override
  public int compareTo(TimedDmxValue o) {
    return Long.valueOf(millis).compareTo(o.getMillis());
  }

}
