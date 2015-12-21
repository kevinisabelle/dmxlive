package com.kevinisabelle.dmxlive.api.output.text;

import com.kevinisabelle.dmxlive.api.driver.TextDriver;
import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.api.output.text.commands.AbstractTextCommand;

/**
 *
 * @author kevin
 */
public abstract class TextFixture  extends Fixture<TextDriver, AbstractTextCommand> {

    public TextFixture(TextDriver output) {
        super(output);
    }

   

}
