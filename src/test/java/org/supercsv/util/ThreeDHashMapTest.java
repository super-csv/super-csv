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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the ThreeDHashMap class.
 * 
 * @author James Bassett
 */
public class ThreeDHashMapTest {
	
	// 3D map of people's ages by country, city and name
	private ThreeDHashMap<String, String, String, Integer> personMap;
	
	/**
	 * Sets up the map for the test.
	 */
	@Before
	public void setUp() {
		personMap = new ThreeDHashMap<String, String, String, Integer>();
		personMap.set("Australia", "Brisbane", "Jim", 26);
		personMap.set("Australia", "Brisbane", "Jane", 57);
		personMap.set("Australia", "Sydney", "Harry", 19);
		personMap.set("New Zealand", "Auckland", "Sally", 34);
		personMap.set("New Zealand", "Wellington", "John", 88);
		personMap.set("United Kingdom", "London", "Elizabeth", 34);
		personMap.set("India", "Mumbai", "Vernon", 27);
	}
	
	/**
	 * Tests the containsKey() method (with 2 parameters).
	 */
	@Test
	public void testContainsKeyWithTwoParams() {
		
		final String firstKey = "Australia";
		final String secondKey = "Sydney";
		
		// both keys exist
		assertTrue(personMap.containsKey(firstKey, secondKey));
		
		// first key doesn't exist
		assertFalse(personMap.containsKey("invalid", secondKey));
		
		// second key doesn't exist
		assertFalse(personMap.containsKey(firstKey, "invalid"));
		
	}
	
	/**
	 * Tests the containsKey() method (with 3 parameters).
	 */
	@Test
	public void testContainsKeyWithThreeParams() {
		
		final String firstKey = "United Kingdom";
		final String secondKey = "London";
		final String thirdKey = "Elizabeth";
		
		// both keys exist
		assertTrue(personMap.containsKey(firstKey, secondKey, thirdKey));
		
		// first key doesn't exist
		assertFalse(personMap.containsKey("invalid", secondKey, thirdKey));
		
		// second key doesn't exist
		assertFalse(personMap.containsKey(firstKey, "invalid", thirdKey));
		
		// third key doesn't exist
		assertFalse(personMap.containsKey(firstKey, secondKey, "invalid"));
		
	}
	
	/**
	 * Tests the get() method (with 1 parameter).
	 */
	@Test
	public void testGet() {
		
		// invalid key
		assertNull(personMap.get("invalid"));
		
		assertEquals(2, personMap.get("Australia").size());
		assertEquals(2, personMap.get("New Zealand").size());
		assertEquals(1, personMap.get("United Kingdom").size());
		assertEquals(1, personMap.get("India").size());
	}
	
	/**
	 * Tests the getAs2d() method.
	 */
	@Test
	public void testGetAs2d() {
		
		// invalid key (should be empty)
		assertEquals(0, personMap.getAs2d("invalid").size());
		
		assertEquals(2, personMap.getAs2d("Australia").size());
		assertEquals(2, personMap.getAs2d("New Zealand").size());
		assertEquals(1, personMap.getAs2d("United Kingdom").size());
		assertEquals(1, personMap.getAs2d("India").size());
	}
	
	/**
	 * Tests the get() method (with 2 parameters).
	 */
	@Test
	public void testGetWithTwoParams() {
		
		// invalid first key
		assertNull(personMap.get("United States", "New York"));
		
		// invalid second key
		assertNull(personMap.get("India", "Chennai"));
		
		assertEquals(2, personMap.get("Australia", "Brisbane").size());
		assertEquals(1, personMap.get("Australia", "Sydney").size());
		assertEquals(1, personMap.get("New Zealand", "Auckland").size());
		assertEquals(1, personMap.get("New Zealand", "Wellington").size());
		assertEquals(1, personMap.get("United Kingdom", "London").size());
		assertEquals(1, personMap.get("India", "Mumbai").size());
	}
	
	/**
	 * Tests the get() method (with 3 parameters).
	 */
	@Test
	public void testGetWithThreeParams() {
		personMap.get(null, null, null);
		
		// invalid first key
		assertNull(personMap.get("United States", "New York", "George"));
		
		// invalid second key
		assertNull(personMap.get("India", "Chennai", "Raj"));
		
		// invalid third key
		assertNull(personMap.get("India", "Mumbai", "Raj"));
		
		assertEquals(26, personMap.get("Australia", "Brisbane", "Jim").intValue());
		assertEquals(57, personMap.get("Australia", "Brisbane", "Jane").intValue());
		assertEquals(19, personMap.get("Australia", "Sydney", "Harry").intValue());
		assertEquals(34, personMap.get("New Zealand", "Auckland", "Sally").intValue());
		assertEquals(88, personMap.get("New Zealand", "Wellington", "John").intValue());
		assertEquals(34, personMap.get("United Kingdom", "London", "Elizabeth").intValue());
		assertEquals(27, personMap.get("India", "Mumbai", "Vernon").intValue());
	}
	
	/**
	 * Tests the size() method (with no parameters).
	 */
	@Test
	public void testSize() {
		assertEquals(4, personMap.size());
	}
	
	/**
	 * Tests the size() method (with 1 parameter).
	 */
	@Test
	public void testSizeWithOneParam() {
		
		// invalid key
		assertEquals(0, personMap.size("United States"));
		
		assertEquals(2, personMap.size("Australia"));
		assertEquals(2, personMap.size("New Zealand"));
		assertEquals(1, personMap.size("United Kingdom"));
		assertEquals(1, personMap.size("India"));
	}
	
	/**
	 * Tests the size() method (with 2 parameters).
	 */
	@Test
	public void testSizeWithTwoParams() {
		
		// invalid first key
		assertEquals(0, personMap.size("United States", "New York"));
		
		// invalid second key
		assertEquals(0, personMap.size("India", "Chennai"));
		
		assertEquals(2, personMap.size("Australia", "Brisbane"));
		assertEquals(1, personMap.size("Australia", "Sydney"));
		assertEquals(1, personMap.size("New Zealand", "Auckland"));
		assertEquals(1, personMap.size("New Zealand", "Wellington"));
		assertEquals(1, personMap.size("United Kingdom", "London"));
		assertEquals(1, personMap.size("India", "Mumbai"));
	}
	
	/**
	 * Tests the keySet() method.
	 */
	@Test
	public void keySet() {
		final Set<String> expectedKeys = new HashSet<String>(Arrays.asList("Australia", "New Zealand",
			"United Kingdom", "India"));
		assertEquals(expectedKeys.size(), personMap.keySet().size());
		assertTrue(personMap.keySet().containsAll(expectedKeys));
	}
	
}
