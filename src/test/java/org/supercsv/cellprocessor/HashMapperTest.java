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
package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the HashMapper processor.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 */
public class HashMapperTest {
	
	private static final String DEFAULT_VALUE = "Default";
	
	private static final Map<Object, Object> VALUE_MAP = new HashMap<Object, Object>();
	static {
		VALUE_MAP.put(1, "1");
		VALUE_MAP.put(2, "2");
		VALUE_MAP.put(3, "3");
	}
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new HashMapper(VALUE_MAP);
		processor2 = new HashMapper(VALUE_MAP, DEFAULT_VALUE);
		processorChain = new HashMapper(VALUE_MAP, new IdentityTransform());
		processorChain2 = new HashMapper(VALUE_MAP, DEFAULT_VALUE, new IdentityTransform());
	}
	
	/**
	 * Tests chained/unchained execution with a valid key from the map.
	 */
	@Test
	public void testValidKey() {
		int validKey = 1;
		assertEquals("1", processor.execute(validKey, ANONYMOUS_CSVCONTEXT));
		assertEquals("1", processor2.execute(validKey, ANONYMOUS_CSVCONTEXT));
		assertEquals("1", processorChain.execute(validKey, ANONYMOUS_CSVCONTEXT));
		assertEquals("1", processorChain2.execute(validKey, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests chained/unchained execution with a key not in the map.
	 */
	@Test
	public void testInvalidKey() {
		int invalidKey = 4;
		assertFalse(VALUE_MAP.containsKey(invalidKey));
		
		// no default values
		assertNull(processor.execute(invalidKey, ANONYMOUS_CSVCONTEXT));
		assertNull(processorChain.execute(invalidKey, ANONYMOUS_CSVCONTEXT));
		
		// with default values
		assertEquals(DEFAULT_VALUE, processor2.execute(invalidKey, ANONYMOUS_CSVCONTEXT));
		assertEquals(DEFAULT_VALUE, processorChain2.execute(invalidKey, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null Map (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullMap() {
		new HashMapper(null, DEFAULT_VALUE);
	}
	
	/**
	 * Tests chained execution with a null Map (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testChainedConstructionWithNullMap() {
		new HashMapper(null, DEFAULT_VALUE, new IdentityTransform());
	}
	
	/**
	 * Tests construction with an empty Map (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyMap() {
		new HashMapper(new HashMap<Object, Object>(), DEFAULT_VALUE);
	}
	
	/**
	 * Tests chained execution with an empty Map (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testChainedConstructionWithEmptyMap() {
		new HashMapper(new HashMap<Object, Object>(), DEFAULT_VALUE, new IdentityTransform());
	}
	
}
