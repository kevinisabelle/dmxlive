/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.driver.dmx.tests;

import com.kevinisabelle.dmxlive.driver.dmx.EntecOpenDMX;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Kevin
 */
public class TestDriverInit {

  public TestDriverInit() {
  }

  @BeforeClass
  public static void setUpClass() {

    try {
      System.setProperty("java.library.path", "dll64");
      Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
      fieldSysPath.setAccessible(true);
      try {
        fieldSysPath.set(null, null);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(TestDriverInit.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(TestDriverInit.class.getName()).log(Level.SEVERE, null, ex);
      }

      System.out.println(System.getProperty("user.dir"));
      System.out.println(System.getProperty("java.library.path"));
    } catch (NoSuchFieldException ex) {
      Logger.getLogger(TestDriverInit.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SecurityException ex) {
      Logger.getLogger(TestDriverInit.class.getName()).log(Level.SEVERE, null, ex);
    }

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
  //@Ignore
  public void testDriverInit() {

    EntecOpenDMX driver = new EntecOpenDMX();
    boolean started = driver.init();

    if (!started) {
      fail();
    }

    for (int i = 0; i < 253; i++) {
      
      for (int j = 0; j < 10; j++) {

      driver.transmit(j, i);
      
      System.out.println("Sent: " + j + ":" + i);

      try {
        Thread.sleep(2);
      } catch (InterruptedException ex) {
        Logger.getLogger(TestDriverInit.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      }

      
    }

    driver.terminate();
  }

}
