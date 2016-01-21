package com.kevinisabelle.dmxlive.api.output;

public class TimedEvent implements Comparable<TimedEvent> {

  protected long millis;
  protected Fixture fixtureDestination;

  public TimedEvent(long millis) {
    this.millis = millis;
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
    return "" + millis;
  }

  @Override
  public int compareTo(TimedEvent o) {
    return Long.valueOf(millis).compareTo(o.getMillis());
  }

}
