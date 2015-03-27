package com.kevinisabelle.dmxlive.api.output.dmx.commands;

import java.util.List;

import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxValue;
import com.kevinisabelle.dmxlive.music.TimeInfo;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractDMXCommand {

  protected TimeInfo startOffset;

  public abstract List<TimedDmxValue> computeCommand(DMXFixture fixture);
}
