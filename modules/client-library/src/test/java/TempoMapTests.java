import com.kevinisabelle.dmxlive.music.Tempo;
import com.kevinisabelle.dmxlive.music.TempoMap;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin
 */
public class TempoMapTests {
    
    public TempoMapTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testTempoMap() {
        
        // 4 secondes
        Tempo tempo1 = new Tempo(new TimeInfo("1:0:0"), new TimeSignature("4/4"), 120);
        Tempo tempo2 = new Tempo(new TimeInfo("3:0:0"), new TimeSignature("12/8"), 180);
        Tempo tempo3 = new Tempo(new TimeInfo("5:0:0"), new TimeSignature("4/4"), 120);
        
        TempoMap tempoMap = new TempoMap();
        tempoMap.addAll(Arrays.asList(tempo1, tempo2, tempo3));
        
        long ms = tempoMap.getAbsoluteTimeAt(new TimeInfo("3:0:0"));
        
        assertEquals(ms, 4000);
        assertEquals(tempoMap.getTimeInfoAt(ms), new TimeInfo("3:0:0"));
        
        ms = tempoMap.getAbsoluteTimeAt(new TimeInfo("4:0:0"));
        
        assertEquals(ms, 6000);
        assertEquals(tempoMap.getTimeInfoAt(ms), new TimeInfo("4:0:0"));
        
        ms = tempoMap.getAbsoluteTimeAt(new TimeInfo("4:6:0"));
        
        assertEquals(ms, 7000);
        assertEquals(tempoMap.getTimeInfoAt(ms), new TimeInfo("4:6:0"));
        
        ms = tempoMap.getAbsoluteTimeAt(new TimeInfo("4:8:0"));
        
        assertEquals(ms, 7333);
        assertEquals(tempoMap.getTimeInfoAt(ms), new TimeInfo("4:7:0.998"));
        
        ms = tempoMap.getAbsoluteTimeAt(new TimeInfo("5:0:0"));
        
        assertEquals(ms, 8000);
        assertEquals(tempoMap.getTimeInfoAt(ms), new TimeInfo("5:0:0"));
        
        ms = tempoMap.getAbsoluteTimeAt(new TimeInfo("6:0:1/4"));
        
        assertEquals(ms, 10125);        
        assertEquals(tempoMap.getTimeInfoAt(ms), new TimeInfo("6:0:1/4"));
        
    }
    
}
