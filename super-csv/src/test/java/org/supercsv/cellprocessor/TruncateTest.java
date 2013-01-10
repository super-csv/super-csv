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
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Truncate processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class TruncateTest {
	
	private static final int MAX_SIZE = 3;
	private static final String SUFFIX = "...";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new Truncate(MAX_SIZE);
		processor2 = new Truncate(MAX_SIZE, SUFFIX);
		processorChain = new Truncate(MAX_SIZE, new IdentityTransform());
		processorChain2 = new Truncate(MAX_SIZE, SUFFIX, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with input the same as the max size (no truncating!).
	 */
	@Test
	public void testInputSameAsMax() {
		String input = "abc";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processor2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with input longer than the max size (truncating!).
	 */
	@Test
	public void testInputLongerThanMax() {
		String input = "abcd";
		String expected = "abc";
		String expectedSuffix = expected + "...";
		
		// no suffix
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		
		// suffix
		assertEquals(expectedSuffix, processor2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedSuffix, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with an invalid max value (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidMax() {
		processor = new Truncate(0);
	}
	
	/**
	 * Tests execution with an invalid max value and chaining (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidMaxChained() {
		processorChain = new Truncate(0, new IdentityTransform());
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null suffix String (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullSuffix() {
		new Truncate(MAX_SIZE, (String) null);
	}
	
}
