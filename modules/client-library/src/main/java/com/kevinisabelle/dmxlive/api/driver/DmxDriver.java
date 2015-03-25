/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kevinisabelle.dmxlive.api.driver;

import com.kevinisabelle.dmxlive.api.Driver;

/**
 *
 * @author Kevin
 */
public abstract class DmxDriver implements Driver {
    
    public abstract void transmit(int channel, int value);
    
}
