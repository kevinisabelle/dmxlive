package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public  class CommandMove extends AbstractDMXCommand {

    public CommandMove(String scriptItem) {
        super(scriptItem);
    }

    @Override
    public List<TimedDmxEvent> computeCommand(DMXFixture fixture) {
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
