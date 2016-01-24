/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.music;

/**
 *
 * @author Kevin
 */
public class Tempo {
    
    TimeInfo atTime;
    TimeSignature signature;
    float bpm;
 
    public Tempo(TimeInfo atTime, TimeSignature signature, float bpm) {
        this.atTime = atTime;
        this.signature = signature;
        this.bpm = bpm;
    }
    
    public long getTotalDuration(TimeInfo atTime){
        return TimeHelper.getMilliseconds(atTime, signature, bpm);
    }
    
    @Override
    public String toString(){
        
        StringBuilder bl = new StringBuilder();
        bl.append("Tempo: ").append(atTime.toString()).append(" ")
                .append(signature.toString()).append(" @ ").append(bpm);
        
        return bl.toString();
    }
    
  
}
