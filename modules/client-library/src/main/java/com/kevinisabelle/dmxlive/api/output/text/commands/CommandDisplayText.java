package com.kevinisabelle.dmxlive.api.output.text.commands;

import com.kevinisabelle.dmxlive.api.output.text.TextFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author kisabelle
 */
public class CommandDisplayText extends AbstractTextCommand {

    public CommandDisplayText(TimeInfo startOffset, List<TextFixture> fixtures, int scriptLineRef, String scriptItem) {
        super(startOffset, fixtures, scriptLineRef, scriptItem);
    }

}
