package com.juanjo.openDmx;

import org.apache.log4j.Logger;

public class OpenDmx {

  private static final Logger logger = Logger.getLogger(OpenDmx.class);

  static {
    try {

      logger.info("loading libraries...");

      System.loadLibrary("ftd2xx64");
      System.loadLibrary("libOpenDMX-FTDI64");

      logger.info("FTDI and OpenDMX dynamic libraries loaded.");

    }
    catch(UnsatisfiedLinkError e) {
      logger.fatal("Native code library failed to load.\n" + e);
      System.exit(1);
    }
  }

  public static final int OPENDMX_TX = 0;

  public static final int OPENDMX_RX = 1;

  protected OpenDmx() {

  }

  native public static boolean connect(int _mode);

  native public static boolean disconnect();

  native public static void setValue(int _channel, int _data);

  native public static int getValue(int _channel);
}
