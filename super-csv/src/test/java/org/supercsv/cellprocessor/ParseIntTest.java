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
 * Tests the ParseInt processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseIntTest {
	
	private static final int POSITIVE_VAL = 17;
	private static final String POSITIVE_STRING = "17";
	private static final int NEGATIVE_VAL = -43;
	private static final String NEGATIVE_STRING = "-43";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseInt();
		processorChain = new ParseInt(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid ints as input.
	 */
	@Test
	public void testValidInts() {
		// positive values
		assertEquals((Object) POSITIVE_VAL, processor.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals((Object) POSITIVE_VAL, processorChain.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		
		// negative values
		assertEquals((Object) NEGATIVE_VAL, processor.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals((Object) NEGATIVE_VAL, processorChain.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with valid int Strings as input.
	 */
	@Test
	public void testValidIntStrings() {
		// positive values
		assertEquals((Object) POSITIVE_VAL, processor.execute(POSITIVE_STRING, ANONYMOUS_CSVCONTEXT));
		assertEquals((Object) POSITIVE_VAL, processorChain.execute(POSITIVE_STRING, ANONYMOUS_CSVCONTEXT));
		
		// negative values
		assertEquals((Object) NEGATIVE_VAL, processor.execute(NEGATIVE_STRING, ANONYMOUS_CSVCONTEXT));
		assertEquals((Object) NEGATIVE_VAL, processorChain.execute(NEGATIVE_STRING, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with an badly formatted String input (should throw an exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithInvalidFormatString() {
		processor.execute("123.45", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non Integer input (should throw an exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNonIntInput() {
		processor.execute(1.23, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
