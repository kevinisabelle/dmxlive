package com.kevinisabelle.dmxlive.api.driver;

import com.kevinisabelle.dmxlive.api.Driver;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

/**
 *
 * @author Kevin
 */
public abstract class AudioDriver implements Driver {
    
    protected Line selectedLine;
    
    public abstract int getNbChannelsOutput();    
    public abstract int getNbChannelsIntput();
    
    public abstract Mixer getMixer();
    
    public abstract Line getLine(int nb);
    
    public abstract void startClip(Clip clip);
    public abstract void stopAllSounds();
    public abstract void pause();
    
}
