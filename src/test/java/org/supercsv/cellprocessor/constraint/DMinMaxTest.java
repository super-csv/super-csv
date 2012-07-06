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
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the DMinMax constaint.
 * 
 * @author James Bassett
 */
public class DMinMaxTest {
	
	private static final double MIN = -5;
	private static final double MAX = 10;
	private static final double IN_RANGE = 0;
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new DMinMax(MIN, MAX);
		processorChain = new DMinMax(MIN, MAX, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a valid Double.
	 */
	@Test
	public void testValidDouble() {
		assertEquals(IN_RANGE, processor.execute(IN_RANGE, ANONYMOUS_CSVCONTEXT));
		assertEquals(IN_RANGE, processorChain.execute(IN_RANGE, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a valid String.
	 */
	@Test
	public void testValidString() {
		assertEquals(IN_RANGE, processor.execute(String.valueOf(IN_RANGE), ANONYMOUS_CSVCONTEXT));
		assertEquals(IN_RANGE, processorChain.execute(String.valueOf(IN_RANGE), ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests an input above the max.
	 */
	@Test(expected = SuperCSVException.class)
	public void testAboveMax() {
		processor.execute(11, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests an input below the min.
	 */
	@Test(expected = SuperCSVException.class)
	public void testBelowMin() {
		processor.execute(-6, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with invalid min/max values (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithMaxLessThanMin() {
		new DMinMax(2, 1);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-Number input (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithNonNumber() {
		processor.execute("abc", ANONYMOUS_CSVCONTEXT);
	}
}
