/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the TwoDHashMap class.
 * 
 * @author James Bassett
 */
public class TwoDHashMapTest {
	
	// 2D maps of typical orchestral instrument numbers by section and instrument name
	private TwoDHashMap<String, String, Integer> orchestraMap;
	private TwoDHashMap<String, String, Integer> orchestraMap2;
	
	/**
	 * Sets up the maps TwoDHashMaps for the test, using both constructors.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() {
		orchestraMap = new TwoDHashMap<String, String, Integer>();
		populateTwoDHashMap(orchestraMap);
		
		orchestraMap2 = new TwoDHashMap<String, String, Integer>(new HashMap<String, HashMap<String, Integer>>());
		populateTwoDHashMap(orchestraMap2);
	}
	
	/**
	 * Populates a TwoDHashMap with test data.
	 * 
	 * @param map
	 *            the map to populate
	 */
	private static void populateTwoDHashMap(TwoDHashMap<String, String, Integer> map) {
		map.set("Woodwind", "Piccolo", 1);
		map.set("Woodwind", "Flute", 3);
		map.set("Woodwind", "Oboe", 2);
		map.set("Woodwind", "Cor Anglais", 1);
		map.set("Woodwind", "Clarinet", 3);
		map.set("Woodwind", "Bass Clarinet", 1);
		map.set("Woodwind", "Soprano Saxophone", 1);
		map.set("Woodwind", "Alto Saxophone", 1);
		map.set("Woodwind", "Tenor Saxophone", 1);
		map.set("Woodwind", "Baritone Saxophone", 1);
		map.set("Woodwind", "Bassoon", 2);
		map.set("Woodwind", "Contrabassoon", 1);
		map.set("Brass", "Horn", 4);
		map.set("Brass", "Trumpet", 3);
		map.set("Brass", "Trombone", 4);
		map.set("Brass", "Tuba", 1);
		map.set("Percussion", "Timpani", 2);
		map.set("Percussion", "Snare Drum", 1);
		map.set("Percussion", "Tenor Drum", 1);
		map.set("Percussion", "Bass Drum", 1);
		map.set("Percussion", "Cymbals", 1);
		map.set("Strings", "Harp", 1);
		map.set("Strings", "Violin (I)", 16);
		map.set("Strings", "Violin (II)", 16);
		map.set("Strings", "Viola", 12);
		map.set("Strings", "Cello", 10);
		map.set("Strings", "Double Bass", 8);
	}
	
	/**
	 * Tests the containsKey() method on both maps.
	 */
	@Test
	public void testContainsKey() {
		
		final String key1 = "Woodwind";
		final String key2 = "Bassoon";
		
		// both keys exist
		assertTrue(orchestraMap.containsKey(key1, key2));
		assertTrue(orchestraMap2.containsKey(key1, key2));
		
		// first key doesn't exist
		assertFalse(orchestraMap.containsKey("invalid", key2));
		assertFalse(orchestraMap2.containsKey("invalid", key2));
		
		// second key doesn't exist
		assertFalse(orchestraMap.containsKey(key1, "invalid"));
		assertFalse(orchestraMap2.containsKey(key1, "invalid"));
		
	}
	
	/**
	 * Tests the get() method on both maps.
	 */
	@Test
	public void testGet() {
		
		final String key1 = "Percussion";
		final String key2 = "Timpani";
		final Integer expectedNo = 2;
		
		// both keys exist
		assertEquals(expectedNo, orchestraMap.get(key1, key2));
		assertEquals(expectedNo, orchestraMap2.get(key1, key2));
		
		// first key doesn't exist
		assertNull(orchestraMap.get("invalid", key2));
		assertNull(orchestraMap2.get("invalid", key2));
		
		// second key doesn't exist
		assertNull(orchestraMap.get(key1, "invalid"));
		assertNull(orchestraMap2.get(key1, "invalid"));
		
	}
	
	/**
	 * Tests the size() method on both maps.
	 */
	@Test
	public void testSize() {
		final int expectedSize = 4;
		assertEquals(expectedSize, orchestraMap.size());
		assertEquals(expectedSize, orchestraMap2.size());
	}
	
	/**
	 * Tests the size() method (for the inner map) on both maps.
	 */
	@Test
	public void testSizeWithParam() {
		final int expectedWoodwinds = 12;
		final int expectedBrass = 4;
		final int expectedPercussion = 5;
		final int expectedStrings = 6;
		
		// first key doesn't exist
		assertEquals(0, orchestraMap.size("invalid"));
		assertEquals(0, orchestraMap2.size("invalid"));
		
		assertEquals(expectedWoodwinds, orchestraMap.size("Woodwind"));
		assertEquals(expectedWoodwinds, orchestraMap2.size("Woodwind"));
		
		assertEquals(expectedBrass, orchestraMap.size("Brass"));
		assertEquals(expectedBrass, orchestraMap2.size("Brass"));
		
		assertEquals(expectedPercussion, orchestraMap.size("Percussion"));
		assertEquals(expectedPercussion, orchestraMap2.size("Percussion"));
		
		assertEquals(expectedStrings, orchestraMap.size("Strings"));
		assertEquals(expectedStrings, orchestraMap2.size("Strings"));
	}
	
	/**
	 * Tests the keySet() method on both maps.
	 */
	@Test
	public void keySet() {
		final Set<String> expectedKeys = new HashSet<String>(
			Arrays.asList("Woodwind", "Brass", "Percussion", "Strings"));
		assertEquals(expectedKeys.size(), orchestraMap.keySet().size());
		assertEquals(expectedKeys.size(), orchestraMap2.keySet().size());
		assertTrue(orchestraMap.keySet().containsAll(expectedKeys));
		assertTrue(orchestraMap2.keySet().containsAll(expectedKeys));
	}
	
	/**
	 * Tests the constructor with a null argument (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNull() {
		new TwoDHashMap<String, String, Integer>(null);
		
	}
}
