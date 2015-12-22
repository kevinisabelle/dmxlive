/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxlive.api;

import com.kevinisabelle.dmxlive.music.TempoMap;
import com.kevinisabelle.dmxlive.music.TimeInfo;
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
public class TempoMapTest {
	
	public TempoMapTest() {
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
	public void TestCreate(){
		
		TempoMap map = new TempoMap();
		
		map.Add(new TempoMap.Item(new TimeInfo, null, bpm));
	}
	
}
