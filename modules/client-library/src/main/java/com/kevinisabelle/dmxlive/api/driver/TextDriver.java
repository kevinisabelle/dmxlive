package com.kevinisabelle.dmxlive.api.driver;

import com.kevinisabelle.dmxlive.api.Driver;

/**
 *
 * @author Kevin
 */
public abstract class TextDriver implements Driver {

    public abstract void transmit(String text);

}
