package com.kevinisabelle.dmxlive.api.input;

import com.kevinisabelle.dmxlive.api.Driver;
import javax.sound.midi.MidiEvent;

/**
 *
 * @author Kevin
 * @param <T> The input driver
 */
public abstract class ControlFixture<T extends Driver> {

    protected T input;

    public ControlFixture(T input) {
        this.input = input;
    }

    public abstract String getName();

    public abstract void receiveMidiSignal(MidiEvent event);

    protected final void triggerScene(int scene) {

    }

    protected final void executeScript(String script) {

    }

    protected final void startSong() {

    }

    protected final void pauseSong() {

    }

    protected final void stopSong() {

    }

}
