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
 * Tests the Token processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class TokenTest {
	
	private static final String TOKEN = "foo";
	private static final String REPLACEMENT = "bar";
	private static final int TOKEN2 = 2;
	private static final String REPLACEMENT2 = "two";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		// foo -> bar
		processor = new Token(TOKEN, REPLACEMENT);
		processorChain = new Token(TOKEN, REPLACEMENT, new IdentityTransform());
		
		// 2 -> two
		processor2 = new Token(TOKEN2, REPLACEMENT2);
		processorChain2 = new Token(TOKEN2, REPLACEMENT2, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid input.
	 */
	@Test
	public void testValidInput() {
		// string input
		assertEquals(REPLACEMENT, processor.execute(TOKEN, ANONYMOUS_CSVCONTEXT));
		assertEquals(REPLACEMENT, processorChain.execute(TOKEN, ANONYMOUS_CSVCONTEXT));
		
		// int input
		assertEquals(REPLACEMENT2, processor2.execute(TOKEN2, ANONYMOUS_CSVCONTEXT));
		assertEquals(REPLACEMENT2, processorChain2.execute(TOKEN2, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution when the token is not found (should return input unchanged).
	 */
	@Test
	public void testTokenNotFound() {
		// expecting 'foo', not 2
		assertEquals(TOKEN2, processor.execute(TOKEN2, ANONYMOUS_CSVCONTEXT));
		assertEquals(TOKEN2, processorChain.execute(TOKEN2, ANONYMOUS_CSVCONTEXT));
		
		// expecting 2, not 'foo'
		assertEquals(TOKEN, processor2.execute(TOKEN, ANONYMOUS_CSVCONTEXT));
		assertEquals(TOKEN, processorChain2.execute(TOKEN, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
}
