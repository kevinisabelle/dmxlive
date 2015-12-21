package com.kevinisabelle.dmxlive.api.output.text;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;

public class TimedTextEvent extends TimedEvent {

    private String text;

    public TimedTextEvent(long millis, String text) {
        super(millis);
        this.text = text;
    }

    @Override
    public String toString() {
        return "Text: " + millis + ": " + getText();
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

}
