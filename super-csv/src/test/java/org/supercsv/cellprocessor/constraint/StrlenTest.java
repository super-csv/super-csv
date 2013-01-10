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
import org.supercsv.SuperCsvTestUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Strlen constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class StrlenTest {
	
	private static final int LENGTH1 = 2;
	private static final int LENGTH2 = 3;
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new Strlen(LENGTH1, LENGTH2);
		processorChain = new Strlen(LENGTH1, new IdentityTransform()); // only allows 1 length
		processorChain2 = new Strlen(new int[] { LENGTH1, LENGTH2 }, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with inputs of valid lengths.
	 */
	@Test
	public void testValidInput() {
		String input = "OK";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
		
		input = "yep";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		// skip 'processorChain' as it only has 1 valid length
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
		
	}
	
	/**
	 * Tests execution with an input with an unexpected length (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testInvalidInput() {
		processor.execute("four", SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction of the processor with a negative length (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeLength() {
		new Strlen(-1);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullArray() {
		new Strlen((int[]) null);
	}
	
	/**
	 * Tests construction with an empty array (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyArray() {
		new Strlen(new int[] {});
	}
}
