/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.fixture.lumiledpar64;

import com.kevinisabelle.dmxlive.api.output.dmx.Color;
import com.kevinisabelle.dmxlive.api.output.dmx.DMXFixture;
import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import com.kevinisabelle.dmxlive.api.output.dmx.commands.CommandDim;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.BLUE;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.DIMMER;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.GREEN;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.MACRO;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.MODE_DIM;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.RED;
import static com.kevinisabelle.dmxlive.fixture.lumiledpar64.LumiLEDPar64.STROBE;
import com.kevinisabelle.dmxlive.music.TimeHelper;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kevin
 */
public class CommandDimPar64 extends CommandDim {

  public CommandDimPar64(String scriptItem) {
    super(scriptItem);
  }

  @Override
  public List<TimedDmxEvent> computeCommand(DMXFixture dmxf) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  private List<TimedDmxEvent> dim(TimeInfo time, TimeSignature signature, int bpm, Color color, int percent) {

		// Always reset stobe mode when applying dim.
    List<TimedDmxEvent> values = new ArrayList<>();

    long timeMillis = TimeHelper.getMilliseconds(time, signature, bpm);

    values.add(new TimedDmxEvent(timeMillis, ((DMXFixture) fixtureRef).getChannel() + DIMMER, percent * 255 / 100));
    values.add(new TimedDmxEvent(timeMillis, ((DMXFixture) fixtureRef).getChannel() + STROBE, 0));
    values.add(new TimedDmxEvent(timeMillis, ((DMXFixture) fixtureRef).getChannel() + MACRO, MODE_DIM));
    values.add(new TimedDmxEvent(timeMillis, ((DMXFixture) fixtureRef).getChannel() + RED, color.getR()));
    values.add(new TimedDmxEvent(timeMillis, ((DMXFixture) fixtureRef).getChannel() + GREEN, color.getG()));
    values.add(new TimedDmxEvent(timeMillis, ((DMXFixture) fixtureRef).getChannel() + BLUE, color.getB()));

    return values;
  }

}
