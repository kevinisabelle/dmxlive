package com.kevinisabelle.dmxlive.api.input;

import com.kevinisabelle.dmxlive.api.Driver;

/**
 *
 * @author Kevin
 * @param <T> The input driver
 */
public abstract class ControlFixture<T extends Driver> {
    
    protected T input;
    
    public ControlFixture(T input){
        this.input = input;
    }
    
    public abstract String getName();
}
