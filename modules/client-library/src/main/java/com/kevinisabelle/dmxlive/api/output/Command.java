package com.kevinisabelle.dmxlive.api.output;

import com.kevinisabelle.dmxlive.music.TimeInfo;

/**
 *
 * @author Kevin
 */
public abstract class Command {

    protected Fixture fixtureRef;
    protected TimeInfo startOffset;

    public Command(String scriptItem) {
        this.fromScript(scriptItem);
    }

    protected abstract void fromScript(String scriptItem);
    
    protected abstract String toScript();
}
