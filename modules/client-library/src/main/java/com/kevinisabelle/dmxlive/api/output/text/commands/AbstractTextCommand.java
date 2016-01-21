package com.kevinisabelle.dmxlive.api.output.text.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.midi.MidiFixture;
import com.kevinisabelle.dmxlive.api.output.text.TextFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractTextCommand extends Command<TextFixture> {

    public AbstractTextCommand(TimeInfo startOffset, List<TextFixture> fixtures, int scriptLineRef, String scriptItem) {
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }


}
