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
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the StrNotNullOrEmpty constraint.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 */
public class StrNotNullOrEmptyTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new StrNotNullOrEmpty();
		processorChain = new StrNotNullOrEmpty(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a non-null/empty value.
	 */
	@Test
	public void testValidInput() {
		String input = "not null or empty!";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a empty String (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testEmptyString() {
		String input = "";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a non-String input (should throw an Exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testNonStringInput() {
		processor.execute(123, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
