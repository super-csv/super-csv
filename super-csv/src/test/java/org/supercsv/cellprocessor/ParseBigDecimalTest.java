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

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the ParseBigDecimal processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseBigDecimalTest {
	
	private static final DecimalFormatSymbols FRENCH_SYMBOLS = new DecimalFormatSymbols(Locale.FRANCE);
	private static final DecimalFormatSymbols ENGLISH_SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processor3;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	private CellProcessor processorChain3;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseBigDecimal();
		processor2 = new ParseBigDecimal(ENGLISH_SYMBOLS);
		processor3 = new ParseBigDecimal(FRENCH_SYMBOLS);
		processorChain = new ParseBigDecimal(new IdentityTransform());
		processorChain2 = new ParseBigDecimal(ENGLISH_SYMBOLS, new IdentityTransform());
		processorChain3 = new ParseBigDecimal(FRENCH_SYMBOLS, new IdentityTransform());
	}
	
	/**
	 * Test unchained/chained execution with a valid positive input.
	 */
	@Test
	public void testValidInput() {
		
		String normalInput = "1357.459";
		String frenchInput = "1357,459";
		BigDecimal expectedOutput = new BigDecimal(normalInput);
		
		// normal input
		assertEquals(expectedOutput, processor.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processor2.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processorChain.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processorChain2.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		
		// french input ("," instead of "." as decimal symbol)
		assertEquals(expectedOutput, processor3.execute(frenchInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processorChain3.execute(frenchInput, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Test unchained/chained execution with a valid negative input.
	 */
	@Test
	public void testValidNegativeInput() {
		
		String normalInput = "-1357.459";
		String frenchInput = "-1357,459";
		BigDecimal expectedOutput = new BigDecimal(normalInput);
		
		// normal input
		assertEquals(expectedOutput, processor.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processor2.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processorChain.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processorChain2.execute(normalInput, ANONYMOUS_CSVCONTEXT));
		
		// french input ("," instead of "." as decimal symbol)
		assertEquals(expectedOutput, processor3.execute(frenchInput, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedOutput, processorChain3.execute(frenchInput, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a non-String input (should throw an exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNonString() {
		processor.execute(1234, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with an empty-String input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithEmptyString() {
		processor.execute("", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with null symbols (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullSymbols() {
		new ParseBigDecimal((DecimalFormatSymbols) null);
	}
	
}
