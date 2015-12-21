package com.kevinisabelle.dmxlive.api.output.audio;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import javax.sound.sampled.Clip;

public class TimedAudioEvent extends TimedEvent {

  private Clip audioClip;

  public TimedAudioEvent(long millis, Clip audioClip) {
    super(millis);
    this.audioClip = audioClip;
  }

  @Override
  public String toString() {
    return "Audio: " + millis + ": " + getAudioClip().getLineInfo().toString();
  }

    /**
     * @return the audioClip
     */
    public Clip getAudioClip() {
        return audioClip;
    }

    /**
     * @param audioClip the audioClip to set
     */
    public void setAudioClip(Clip audioClip) {
        this.audioClip = audioClip;
    }
}
