/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.api.output.audio.commands;

import com.kevinisabelle.dmxlive.api.output.audio.AudioFixture;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import java.util.List;

/**
 *
 * @author Kevin
 */
public class StartAudioClipCommand extends AbstractAudioCommand {

    public StartAudioClipCommand(TimeInfo startOffset, List<AudioFixture> fixtures, int absoluteScriptLineNumber, String scriptLine) {
        super(startOffset, fixtures, absoluteScriptLineNumber, scriptLine);
    }

    
}
