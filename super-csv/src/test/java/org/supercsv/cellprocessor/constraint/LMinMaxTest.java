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
import static org.supercsv.cellprocessor.constraint.LMinMax.MAX_INTEGER;
import static org.supercsv.cellprocessor.constraint.LMinMax.MIN_INTEGER;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.StrReplace;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the LMinMax constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class LMinMaxTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new LMinMax(MIN_INTEGER, MAX_INTEGER);
		processorChain = new LMinMax(MIN_INTEGER, MAX_INTEGER, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a long in the range.
	 */
	@Test
	public void testValidLong() {
		Long input = 123L;
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a long String in the range (should be converted to a Long).
	 */
	@Test
	public void testValidLongString() {
		String input = "123";
		Long expected = 123L;
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with the minimum allowed value.
	 */
	@Test
	public void testMinBoundary() {
		Long input = (long) MIN_INTEGER;
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with the maximum allowed value.
	 */
	@Test
	public void testMaxBoundary() {
		Long input = (long) MAX_INTEGER;
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests that this processor can be chained after a StringCellProcessor.
	 */
	@Test
	public void testChainedAfterStringCellProcessor() {
		final CellProcessor chain = new StrReplace("zero", "0", new LMinMax(MIN_INTEGER, MAX_INTEGER));
		assertEquals(Long.valueOf(0L), chain.execute("zero", ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a value less than the minimum (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testLessThanMin() {
		long lessThanMin = MIN_INTEGER - 1L;
		processor.execute(lessThanMin, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a value greater than the maximum (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testGreaterThanMax() {
		long greaterThanMax = MAX_INTEGER + 1L;
		processor.execute(greaterThanMax, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a String that can't be parsed to a Long (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNonLongString() {
		processor.execute("not long!", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a max < min (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidMaxMin() {
		new LMinMax(MAX_INTEGER, MIN_INTEGER);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
}
