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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.mock.IdentityTransform;

/**
 * Test the Util class.
 * 
 * @author James Bassett
 */
public class UtilTest {
	
	private static final String[] NAME_MAPPING = new String[] { "name", null, "city" };
	
	private static final List<String> LIST = Arrays.asList("Ezio", "25", "Venice");
	
	private static final CellProcessor[] PROCESSORS = new CellProcessor[] { new IdentityTransform(), new ParseInt(),
		null };
	
	private static final int LINE_NO = 23;
	
	private static final int ROW_NO = 12;
	
	private static final Map<String, Object> MAP = new HashMap<String, Object>();
	static {
		MAP.put("name", "Ezio");
		MAP.put("age", 25);
		MAP.put("city", "Venice");
	}
	
	/**
	 * Tests the filterMapToList() method (the age attribute is not used).
	 */
	@Test
	public void testFilterMapToList() {
		List<Object> list = Util.filterMapToList(MAP, NAME_MAPPING);
		assertTrue(list.size() == 3);
		assertEquals("Ezio", list.get(0));
		assertNull(list.get(1));
		assertEquals("Venice", list.get(2));
	}
	
	/**
	 * Tests the filterMapToList() method with a null map (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToListWithNullMap() {
		Util.filterMapToList(null, NAME_MAPPING);
	}
	
	/**
	 * Tests the filterMapToList() method with a null name mapping array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToListWithNullNameMapping() {
		Util.filterMapToList(MAP, null);
	}
	
	/**
	 * Tests the filterListToMap() method.
	 */
	@Test
	public void testFilterListToMap() {
		final Map<String, String> map = new HashMap<String, String>();
		Util.filterListToMap(map, NAME_MAPPING, LIST);
		assertTrue(map.size() == 2);
		assertEquals("Ezio", map.get("name"));
		assertEquals("Venice", map.get("city"));
	}
	
	/**
	 * Tests the filterListToMap() method with a null destination map (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterListToMapWithNullDestMap() {
		Util.filterListToMap(null, NAME_MAPPING, LIST);
	}
	
	/**
	 * Tests the filterListToMap() method with a null name mapping array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterListToMapWithNullNameMapping() {
		Util.filterListToMap(new HashMap<String, String>(), null, LIST);
	}
	
	/**
	 * Tests the filterListToMap() method with a null source list (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterListToMapWithNullSourceList() {
		Util.filterListToMap(new HashMap<String, String>(), NAME_MAPPING, null);
	}
	
	/**
	 * Tests the filterListToMap() method with a name mapping array with too few elements (should throw an exception).
	 */
	@Test(expected = SuperCsvException.class)
	public void testFilterListToMapWithSizeMismatch() {
		Util.filterListToMap(new HashMap<String, String>(), new String[] { "notEnoughColumns" }, LIST);
	}
	
	/**
	 * Tests the filterListToMap() method with a name mapping array with duplicate elements (should throw an exception).
	 */
	@Test(expected = SuperCsvException.class)
	public void testFilterListToMapWithDuplicateNameMapping() {
		Util.filterListToMap(new HashMap<String, String>(), new String[] { "name", "name", "city" }, LIST);
	}
	
	/**
	 * Tests the executeCellProcessors() method.
	 */
	@Test
	public void testExecuteCellProcessors() {
		List<Object> destinationList = new ArrayList<Object>();
		Util.executeCellProcessors(destinationList, LIST, PROCESSORS, LINE_NO, ROW_NO);
		assertTrue(destinationList.size() == 3);
		assertEquals("Ezio", destinationList.get(0));
		assertEquals(Integer.valueOf(25), destinationList.get(1));
		assertEquals("Venice", destinationList.get(2));
	}
	
	/**
	 * Tests the executeCellProcessors() method with a null destination List (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testExecuteCellProcessorsWithNullDestination() {
		Util.executeCellProcessors(null, LIST, PROCESSORS, LINE_NO, ROW_NO);
	}
	
	/**
	 * Tests the executeCellProcessors() method with a null source List (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testExecuteCellProcessorsWithNullSource() {
		Util.executeCellProcessors(new ArrayList<Object>(), null, PROCESSORS, LINE_NO, ROW_NO);
	}
	
	/**
	 * Tests the executeCellProcessors() method with a processors array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testExecuteCellProcessorsWithNullProcessors() {
		Util.executeCellProcessors(new ArrayList<Object>(), LIST, null, LINE_NO, ROW_NO);
	}
	
	/**
	 * Tests the executeCellProcessors() method with a source List whose size doesn't match the number of CellProcessors
	 * (should throw an Exception).
	 */
	@Test(expected = SuperCsvException.class)
	public void testExecuteCellProcessorsWithSizeMismatch() {
		final List<Object> invalidSizeList = new ArrayList<Object>();
		Util.executeCellProcessors(new ArrayList<Object>(), invalidSizeList, PROCESSORS, LINE_NO, ROW_NO);
	}
	
	/**
	 * Tests the filterMapToObjectArray() method.
	 */
	@Test
	public void testFilterMapToObjectArray() {
		final Object[] objectArray = Util.filterMapToObjectArray(MAP, NAME_MAPPING);
		assertTrue(objectArray.length == 3);
		assertEquals("Ezio", objectArray[0]);
		assertNull(objectArray[1]);
		assertEquals("Venice", objectArray[2]);
	}
	
	/**
	 * Tests the filterMapToObjectArray() method with a null values Map (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToObjectArrayWithNullValues() {
		Util.filterMapToObjectArray(null, NAME_MAPPING);
	}
	
	/**
	 * Tests the filterMapToObjectArray() method with a null nameMapping array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFilterMapToObjectArrayWithNullNameMapping() {
		Util.filterMapToObjectArray(MAP, null);
	}
	
	/**
	 * Tests the objectArrayToStringArray() method.
	 */
	@Test
	public void testObjectArrayToStringArray() {
		final Object[] input = new Object[] { 1, null, "three" };
		final String[] output = Util.objectArrayToStringArray(input);
		assertEquals(3, output.length);
		assertEquals("1", output[0]);
		assertNull(output[1]);
		assertEquals("three", output[2]);
	}
	
	/**
	 * Tests the objectArrayToStringArray() method with a null array.
	 */
	@Test
	public void testObjectArrayToStringArrayWithNullArray() {
		assertNull(Util.objectArrayToStringArray(null));
	}
	
	/**
	 * Tests the objectListToStringArray() method.
	 */
	@Test
	public void testObjectListToStringArray() {
		final List<Object> input = Arrays.asList(new Object[] { 1, null, "three" });
		final String[] output = Util.objectListToStringArray(input);
		assertEquals(3, output.length);
		assertEquals("1", output[0]);
		assertNull(output[1]);
		assertEquals("three", output[2]);
	}
	
	/**
	 * Tests the objectListToStringArray() method with a null List.
	 */
	@Test
	public void testObjectListToStringArrayWithNullList() {
		assertNull(Util.objectListToStringArray(null));
	}
	
	/**
	 * Tests the private constructor for test coverage (yes, this is stupid).
	 */
	@Test
	public void testPrivateConstructor() throws Exception {
		Constructor<?> c = Util.class.getDeclaredConstructors()[0];
		c.setAccessible(true);
		c.newInstance();
	}
	
}
