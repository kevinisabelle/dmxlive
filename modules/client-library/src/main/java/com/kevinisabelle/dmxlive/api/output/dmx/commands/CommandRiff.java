package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandRiff extends AbstractDMXCommand {

    public CommandRiff(String scriptItem) {
        super(scriptItem);
    }


    @Override
    protected void fromScript(String scriptItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
