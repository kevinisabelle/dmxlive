package com.kevinisabelle.dmxlive.music;

import java.util.List;

/**
 *
 * @author Kevin
 */
public class TempoMap {
    
    
    private List<Tempo> map;
    
    public void Add(Tempo item){
        map.add(item);
    }
    
    public void fromScript(String scriptItem){
        
    }
    
    public String toScript(){
        return "";
    }
    
    public TimeInfo getTimeInfoAt(long absoluteTime){
        return new TimeInfo(1, 1, 0);
    } 
    
    public long getAbsoluteTimeAt(TimeInfo time){
        
        long absTime = 0;
        time.getMeasure();
        
        for (int i=0; i<map.size(); i++){
            absTime = map.get(i).getTotalDuration(time);
        
        }
        
        return 0l;
    }
   
    
}
