package com.kevinisabelle.dmxlive.api;

/**
 *
 * @author Kevin
 */
public interface Driver {

  public String getName();

  public boolean init();

  public boolean terminate();

}
