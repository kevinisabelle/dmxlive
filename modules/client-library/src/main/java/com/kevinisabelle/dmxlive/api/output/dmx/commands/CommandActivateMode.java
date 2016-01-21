package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import com.kevinisabelle.dmxlive.api.output.dmx.Color;
import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandActivateMode extends AbstractDMXCommand {

    public CommandActivateMode(TimeInfo startOffset, List<DMXFixture> fixtures, int scriptLineRef, String scriptItem, Color color, int dimmer) {
        
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }


}
