/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.api.output;

import com.kevinisabelle.dmxlive.music.TimeInfo;

/**
 *
 * @author Kevin
 */
public abstract class Command {
    
    protected Fixture fixture;
    protected TimeInfo startOffset;
    
    public Command(String scriptItem){
        this.fromScript(scriptItem);
    }
    
    protected abstract void fromScript(String scriptItem);
}
