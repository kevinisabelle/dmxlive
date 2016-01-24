package com.kevinisabelle.dmxlive.music;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kevin
 */
public class TempoMap {

    private final List<Tempo> map = new LinkedList<>();

    public void Add(Tempo item) {
        map.add(item);
    }

    public void addAll(List<Tempo> list) {
        map.addAll(list);
    }

    public void clear() {
        map.clear();
    }

    public TimeInfo getTimeInfoAt(long targetTime) {

        long millis = 0;
        TimeInfo currentTime = new TimeInfo("1:0:0");

        for (int i = 0; i < map.size(); i++) {

            Tempo currentTempo = map.get(i);
            Tempo nextTempo = null;
            long nextTempoStart = -1;
            if (i + 1 <= map.size() - 1) {
                nextTempo = map.get(i + 1);
                nextTempoStart = millis + currentTempo.getTotalDuration(nextTempo.atTime.substract(currentTempo.atTime, currentTempo.signature));
            }

            // Loop and accumulate time and measures.            
            if (nextTempoStart > targetTime || nextTempoStart == -1) {

                //Add the remaining to current time
                currentTime = currentTime.add(new TimeInfo(currentTempo.signature, currentTempo.bpm, targetTime - millis), currentTempo.signature);
                break;
            }

            currentTime = currentTime.add(new TimeInfo(currentTempo.signature, currentTempo.bpm, nextTempoStart - millis), currentTempo.signature);
            millis = nextTempoStart;
        }

        return currentTime;
    }

    public long getAbsoluteTimeAt(TimeInfo targetTime) {

        long absTime = 0;

        for (int i = 0; i < map.size(); i++) {

            Tempo currentTempo = map.get(i);

            if (currentTempo.atTime.compareTo(targetTime, new TimeSignature("16/4")) >= 0) {
                break;
            }

            Tempo nextTempo = null;
            if (i + 1 <= map.size() - 1) {
                nextTempo = map.get(i + 1);
            }

            if (nextTempo != null && nextTempo.atTime.compareTo(targetTime, map.get(i).signature) < 0) {
                // Calculate the time of this full tempo block, next one is included
                TimeInfo timeNeeded = nextTempo.atTime.substract(map.get(i).atTime, map.get(i).signature);
                absTime += map.get(i).getTotalDuration(timeNeeded);

            } else {
                // Calculate the time in this block till destination
                TimeInfo timeNeeded = targetTime.substract(map.get(i).atTime, map.get(i).signature);
                absTime += map.get(i).getTotalDuration(timeNeeded);
            }
        }

        return absTime;
    }
}
