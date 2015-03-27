package com.kevinisabelle.dmxlive.driver.dmx;

import com.juanjo.openDmx.OpenDmx;
import com.kevinisabelle.dmxlive.api.driver.DmxDriver;
import org.apache.log4j.Logger;

/**
 * Wrapper class for the OpenDMX driver.
 * @author kevin
 */
public class EntecOpenDMX extends DmxDriver {

  private static Logger logger = Logger.getLogger(EntecOpenDMX.class);

  public EntecOpenDMX() {

  }

  @Override
  public String getName() {
    return "Enttec OpenDMX Blue Box";
  }

  public void sendSignal(int channel, int data) throws Exception {

    OpenDmx.setValue(channel, data);
  }

  @Override
  public void transmit(int channel, int value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean init() {
    try {

      boolean isOpen = OpenDmx.connect(OpenDmx.OPENDMX_TX);

      logger.info("OpenDMX box: " + (isOpen ? "* CONNECTED *" : "***** NOT CONNECTED *****"));

      return isOpen;

    }
    catch(Exception e) {
      logger.error("Cannot open OpenDMX driver: " + e.getMessage(), e);

      return false;
    }
  }

  @Override
  public boolean terminate() {
    return OpenDmx.disconnect();
  }
}
