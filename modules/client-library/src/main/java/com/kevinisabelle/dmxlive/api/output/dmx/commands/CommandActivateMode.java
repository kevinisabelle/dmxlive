package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandActivateMode extends AbstractDMXCommand {

    public CommandActivateMode(String scriptItem) {
        super(scriptItem);
    }

    @Override
    public List<TimedEvent> compute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fromScript(String scriptItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String toScript() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
