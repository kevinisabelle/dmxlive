package com.kevinisabelle.dmxlive.api.output.audio;

import com.kevinisabelle.dmxlive.api.driver.AudioDriver;
import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import com.kevinisabelle.dmxlive.api.output.audio.commands.AbstractAudioCommand;

import java.util.List;

import com.kevinisabelle.dmxlive.api.output.dmx.commands.AbstractDMXCommand;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;

/**
 *
 * @author kevin
 */
public abstract class AudioFixture extends Fixture<AudioDriver, AbstractAudioCommand> {

  private Line channel;

  public AudioFixture(AudioDriver output) {
    super(output);
  }

  
  public abstract List<AbstractDMXCommand> getSupportedCommmands();

  /**
   * @return the channel
   */
  public Line getChannel() {
    return channel;
  }

  /**
   * @param channel the channel to set
   */
  public void setChannel(Line channel) {
    this.channel = channel;
  }

 
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TimedEvent> processCommandToTimedEvents(AbstractAudioCommand command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AbstractAudioCommand> getSupportedCommands() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void startAudio(Clip clip){
        
    }
}
