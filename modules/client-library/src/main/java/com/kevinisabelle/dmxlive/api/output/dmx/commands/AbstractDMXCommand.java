package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.Command;

import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractDMXCommand extends Command<DMXFixture> {

    public AbstractDMXCommand(String scriptItem) {
        super(scriptItem);
    }

}
