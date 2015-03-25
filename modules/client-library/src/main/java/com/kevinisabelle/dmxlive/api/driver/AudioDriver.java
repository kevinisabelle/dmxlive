/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kevinisabelle.dmxlive.api.driver;

import com.kevinisabelle.dmxlive.api.Driver;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

/**
 *
 * @author Kevin
 */
public abstract class AudioDriver implements Driver {
    
    public abstract int getNbChannelsOutput();    
    public abstract int getNbChannelsIntput();
    
    public abstract Mixer getMixer();
    
    public abstract Line getLine(int nb);
    
}
