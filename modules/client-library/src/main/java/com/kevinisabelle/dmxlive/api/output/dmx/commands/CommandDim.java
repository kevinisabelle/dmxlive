package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.dmx.Color;

/**
 *
 * @author kisabelle
 */
public abstract class CommandDim extends AbstractDMXCommand {

  protected Color color;

  protected int dimmer;

    public CommandDim(String scriptItem) {
        super(scriptItem);
    }

}
