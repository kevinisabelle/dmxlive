package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import java.util.List;

import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractDMXCommand extends Command {

    public AbstractDMXCommand(String scriptItem) {
        super(scriptItem);
    }

    public abstract List<TimedDmxEvent> computeCommand(DMXFixture fixture);
}
