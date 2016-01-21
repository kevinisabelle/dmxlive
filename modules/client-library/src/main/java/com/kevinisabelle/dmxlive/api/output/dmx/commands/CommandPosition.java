package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.dmx.Color;
import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandPosition extends AbstractDMXCommand {

    public CommandPosition(TimeInfo startOffset, List<DMXFixture> fixtures, int scriptLineRef, String scriptItem, Color color, int dimmer) {
        
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }

}
