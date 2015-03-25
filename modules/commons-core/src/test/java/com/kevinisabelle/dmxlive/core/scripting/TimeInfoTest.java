package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.music.TimeSignature;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class TimeInfoTest {

  public TimeInfoTest() {

  }

  @Test
  public void testTimeInfoFromMillis() {

    long millis = 5750l;
    TimeSignature signature = new TimeSignature("4/4");
    int bpm = 120;

    TimeInfo timeInfo = new TimeInfo(signature, bpm, millis);
    System.out.println("Parsed time from millis: " + timeInfo);

    TimeInfo timeInfo3 = new TimeInfo("2:1:3/4");
    assertEquals(0.75, timeInfo3.getSubBeat());

    TimeInfo timeInfo2 = new TimeInfo("2:1:0.5");
    assertEquals(0.5, timeInfo2.getSubBeat());

  }

  @Test
  public void testArithmerics() {

    TimeInfo timeInfo = new TimeInfo("2:1:3/4");
    TimeInfo timeInfo2 = new TimeInfo("1:0:0");

    TimeInfo result = timeInfo.add(timeInfo2, new TimeSignature(4, 4));

    assertEquals(3, result.getMeasure());
    assertEquals(1, result.getBeat());
    assertEquals(0.75, result.getSubBeat());
  }

}
