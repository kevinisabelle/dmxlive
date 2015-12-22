/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.music;

import java.util.List;

/**
 *
 * @author Kevin
 */
public class TempoMap {
    
    public class Item {
        
        TimeInfo atTime;
        TimeSignature signature;
        float bpm;
        
        public Item(TimeInfo atTime, TimeSignature signature, float bpm){
            this.atTime = atTime;
            this.signature = signature;
            this.bpm = bpm;
        }
    }
    
    private List<Item> map;
    
    public void Add(Item item){
        map.add(item);
    }
    
    public void fromScript(String scriptItem){
        
    }
    
    public String toScript(){
        return "";
    }
    
    public TimeInfo getTimeInfoAt(long absoluteTime){
        return new TimeInfo(1, 1, 0);
    } 
    
    public long getAbsoluteTimeAt(TimeInfo time){
        return 0l;
    }
   
    
}
