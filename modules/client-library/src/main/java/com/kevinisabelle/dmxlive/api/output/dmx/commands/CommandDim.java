/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.api.output.dmx.commands;


import com.kevinisabelle.dmxlive.api.output.dmx.Color;

/**
 *
 * @author kisabelle
 */
public abstract class CommandDim extends AbstractDMXCommand {

  protected Color color;

  protected int dimmer;

}
