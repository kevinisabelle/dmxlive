package com.kevinisabelle.dmxlive.api.output.video;

import com.kevinisabelle.dmxlive.api.output.TimedEvent;
import com.kevinisabelle.dmxlive.api.output.TimedEvent;

public class TimedVideoEvent extends TimedEvent {

    private Object video;

    public TimedVideoEvent(long millis, Object video) {
        super(millis);
        this.video = video;
    }

    @Override
    public String toString() {
        return "Video: " + millis + ": " + getVideo().toString();
    }

    /**
     * @return the video
     */
    public Object getVideo() {
        return video;
    }

    /**
     * @param video the video to set
     */
    public void setVideo(Object video) {
        this.video = video;
    }

 

}
