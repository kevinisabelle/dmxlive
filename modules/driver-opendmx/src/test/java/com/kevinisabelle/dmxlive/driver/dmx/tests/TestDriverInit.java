/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.driver.dmx.tests;

import com.kevinisabelle.dmxlive.driver.dmx.EntecOpenDMX;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
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

    ConsoleAppender ca = new ConsoleAppender();
    ca.setWriter(new OutputStreamWriter(System.out));
    ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
    Logger.getRootLogger().addAppender(ca);
    
    try {
      System.setProperty("java.library.path", "src/main/resources");
      Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
      fieldSysPath.setAccessible(true);
      try {
        fieldSysPath.set(null, null);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(TestDriverInit.class.getName()).log(Level.FATAL, null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(TestDriverInit.class.getName()).log(Level.FATAL, null, ex);
      }

      System.out.println(System.getProperty("user.dir"));
      System.out.println(System.getProperty("java.library.path"));
    } catch (NoSuchFieldException ex) {
      Logger.getLogger(TestDriverInit.class.getName()).log(Level.FATAL, null, ex);
    } catch (SecurityException ex) {
      Logger.getLogger(TestDriverInit.class.getName()).log(Level.FATAL, null, ex);
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
      //fail();
      
      return;
    }
    
    driver.transmit(6, 0);
    driver.transmit(7, 0);

    for (int i = 0; i < 255; i=i+10) {
      
      try {
        Thread.sleep(20);
      } catch (InterruptedException ex) {
        Logger.getLogger(TestDriverInit.class.getName()).log(Level.FATAL, null, ex);
      }
      
      
    
      
      for (int j = 3; j <= 255; j++) {

      driver.transmit(i, j);
      
      System.out.println("Sent: " + j + ":" + i);

      
      }
      
    }

    driver.terminate();
  }

}
