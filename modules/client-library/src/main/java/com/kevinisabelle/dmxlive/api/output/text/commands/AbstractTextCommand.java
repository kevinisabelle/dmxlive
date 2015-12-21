package com.kevinisabelle.dmxlive.api.output.text.commands;

import com.kevinisabelle.dmxlive.api.output.Command;
import com.kevinisabelle.dmxlive.api.output.text.TextFixture;

/**
 *
 * @author kisabelle
 */
public abstract class AbstractTextCommand extends Command<TextFixture> {

    public AbstractTextCommand(String scriptItem) {
        super(scriptItem);
    }


}
