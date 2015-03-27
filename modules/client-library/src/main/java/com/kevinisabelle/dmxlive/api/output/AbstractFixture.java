package com.kevinisabelle.dmxlive.api.output;

import com.kevinisabelle.dmxlive.api.Driver;

/**
 * A Fixture exists as an instance of a physical device
 * @author Kevin
 * @param <T> the type of output this fixture is using.
 */
public abstract class AbstractFixture<T extends Driver> {
    
    protected T output;
    
    public AbstractFixture(T output){
        this.output = output;
    }
    
    public abstract String getName();
}
