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
package org.supercsv.cellprocessor.constraint;

import static org.junit.Assert.assertEquals;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the StrMinMax constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class StrMinMaxTest {
	
	private static final int MIN = 3;
	private static final int MAX = 6;
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new StrMinMax(MIN, MAX);
		processorChain = new StrMinMax(MIN, MAX, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a String of valid length.
	 */
	@Test
	public void testValidInput() {
		String input = "valid";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a Integer String of valid length (should be converted to a String).
	 */
	@Test
	public void testValidIntegerString() {
		Integer input = 1234;
		String expected = "1234";
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with the minimum allowed length.
	 */
	@Test
	public void testMinBoundary() {
		String input = "123";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with the maximum allowed length.
	 */
	@Test
	public void testMaxBoundary() {
		String input = "123456";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a length less than the minimum (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testLessThanMin() {
		String input = "12";
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a length greater than the maximum (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testGreaterThanMax() {
		String input = "1234567";
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a min < 0(should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithNegativeMin() {
		new StrMinMax(-1, MAX);
	}
	
	/**
	 * Tests execution with a max < min (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidMaxMin() {
		new StrMinMax(MAX, MIN);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
