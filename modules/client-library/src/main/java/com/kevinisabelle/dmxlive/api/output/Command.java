package com.kevinisabelle.dmxlive.api.output;

import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author Kevin
 * @param <T>
 */
public abstract class Command<T extends Fixture> {

    protected List<T> fixturesRef;
    protected TimeInfo startOffset;
    protected int scriptLineRef;

    public Command(String scriptItem) {
        this.fromScript(scriptItem);
    }
    
    public abstract List<TimedEvent> compute();
   
    protected abstract void fromScript(String scriptItem);
    
    protected abstract String toScript();

    /**
     * @return the fixtureRef
     */
    public List<T> getFixturesRef() {
        return fixturesRef;
    }

    /**
     * @return the startOffset
     */
    public TimeInfo getStartOffset() {
        return startOffset;
    }

    /**
     * @return the scriptLineRef
     */
    public int getScriptLineRef() {
        return scriptLineRef;
    }

    /**
     * @param scriptLineRef the scriptLineRef to set
     */
    public void setScriptLineRef(int scriptLineRef) {
        this.scriptLineRef = scriptLineRef;
    }
}
