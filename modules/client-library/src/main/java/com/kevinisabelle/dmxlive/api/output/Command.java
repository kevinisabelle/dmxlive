package com.kevinisabelle.dmxlive.api.output;

import com.kevinisabelle.dmxlive.music.TempoMap;
import com.kevinisabelle.dmxlive.music.TimeHelper;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Kevin
 * @param <T>
 */
public abstract class Command<T extends Fixture> {

    protected List<T> fixturesRef;
    protected TimeInfo startOffset;
    protected int scriptLineRef;
    protected String scriptLineText;

    protected Map<Fixture, List<TimedEvent>> computedTimedEvent = null;

    public Command(TimeInfo startOffset, List<T> fixtures, int absoluteScriptLineNumber, String scriptLine) {
        this.fixturesRef = fixtures;
        this.startOffset = startOffset;
        this.scriptLineRef = absoluteScriptLineNumber;
        this.scriptLineText = scriptLine;

        this.compute();
    }

    /*
    Convert the parameters instructions and ask the fixtures to product time event which are stored in computedTimeEvent
     */
    private void compute() {

        computedTimedEvent = new HashMap<>();

        for (Fixture fixture : fixturesRef) {
            computedTimedEvent.put(fixture, fixture.processCommandToTimedEvents(this));
        }
    }

    public final Map<Fixture, List<TimedEvent>> getTimedEventAt(TimeInfo from, TimeInfo to, TempoMap tempoMap) {

        long startMillis = tempoMap.getAbsoluteTimeAt(from);
        long endMillis = tempoMap.getAbsoluteTimeAt(to);

        Map<Fixture, List<TimedEvent>> result = new HashMap<>();

        for (Fixture fixture : fixturesRef) {
            
            result.put(fixture, 
                    computedTimedEvent.get(fixture)
                    .stream()
                    .filter(te -> te.getMillis() > startMillis && te.getMillis() < endMillis)
                    .collect(Collectors.toList()));
        }
        
        return result;
    }

    /**
     * @return the fixtureRef
     */
    public List<T> getFixturesRef() {
        return fixturesRef;
    }

    /**
     * @return the startOffset
     */
    public TimeInfo getStartOffset() {
        return startOffset;
    }

    public TimeInfo getEndTime() {
        return startOffset.add(new TimeInfo("0:1:0"), new TimeSignature("4/4"));
    }

    /**
     * @return the scriptLineRef
     */
    public int getScriptLineRef() {
        return scriptLineRef;
    }

    /**
     * @return the scriptLineRef
     */
    public String getScriptLineText() {
        return scriptLineText;
    }
}
