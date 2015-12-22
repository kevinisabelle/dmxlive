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
    }
    
    private List<Item> map;
    
    
}
