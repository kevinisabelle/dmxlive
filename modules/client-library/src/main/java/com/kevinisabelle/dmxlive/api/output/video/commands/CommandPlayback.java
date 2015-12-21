package com.kevinisabelle.dmxlive.api.output.video.commands;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandPlayback extends AbstractVideoCommand {

    public CommandPlayback(String scriptItem) {
        super(scriptItem);
        
    }

    @Override
    protected void fromScript(String scriptItem) {
        
    }

    @Override
    protected String toScript() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TimedEvent> compute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
