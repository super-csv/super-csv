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
import static org.junit.Assert.fail;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;
import static org.supercsv.SuperCsvTestUtils.assertExecution;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
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
	
	private static final DecimalFormatSymbols ENGLISH_SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);
	private static final DecimalFormatSymbols GERMAN_SYMBOLS = new DecimalFormatSymbols(Locale.GERMANY);
	
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
		processor3 = new ParseBigDecimal(GERMAN_SYMBOLS);
		processorChain = new ParseBigDecimal(new IdentityTransform());
		processorChain2 = new ParseBigDecimal(ENGLISH_SYMBOLS, new IdentityTransform());
		processorChain3 = new ParseBigDecimal(GERMAN_SYMBOLS, new IdentityTransform());
	}

	@Test
	public void testValidBigDecimals() {
		final BigDecimal testValue = new BigDecimal("999.00");

		for( final CellProcessor p : Arrays.asList(processor, processor2, processor3,
							   processorChain, processorChain2, processorChain3) ) {
			assertEquals(testValue, p.execute(testValue, ANONYMOUS_CSVCONTEXT));
		}
	}

	/**
	 * Test unchained/chained execution with a valid positive input.
	 */
	@Test
	public void testValidInput() {
		
		String normalInput = "1357.459";
		String germanInput = "1357,459";
		BigDecimal expectedOutput = new BigDecimal(normalInput);
		
		// normal input
		for( final CellProcessor p : Arrays.asList(processor, processor2, processorChain, processorChain2) ) {
			assertExecution(p, normalInput, expectedOutput);
		}
		
		// german input ("," instead of "." as decimal symbol)
		for( final CellProcessor p : Arrays.asList(processor3, processorChain3) ) {
			assertExecution(p, germanInput, expectedOutput);
		}
	}
	
	/**
	 * Test unchained/chained execution with a valid negative input.
	 */
	@Test
	public void testValidNegativeInput() {
		
		String normalInput = "-1357.459";
		String germanInput = "-1357,459";
		BigDecimal expectedOutput = new BigDecimal(normalInput);
		
		// normal input
		for( final CellProcessor p : Arrays.asList(processor, processor2, processorChain, processorChain2) ) {
			assertExecution(p, normalInput, expectedOutput);
		}
		
		// german input ("," instead of "." as decimal symbol)
		for( final CellProcessor p : Arrays.asList(processor3, processorChain3) ) {
			assertExecution(p, germanInput, expectedOutput);
		}
	}
	
	/**
	 * Tests that grouping separators are handled correctly for positive numbers.
	 */
	@Test
	public void testValidInputWithGroupingSeparator() {
		String normalInput = "1,357.459";
		String germanInput = "1.357,459";
		BigDecimal expectedOutput = new BigDecimal("1357.459");
		
		// 'no symbols' processors should choke on grouping separators
		for( CellProcessor p : Arrays.asList(processor, processorChain) ) {
			try {
				p.execute(normalInput, ANONYMOUS_CSVCONTEXT);
				fail("should have thrown SuperCsvCellProcessorException");
			}
			catch(SuperCsvCellProcessorException e) {}
		}
		
		// normal input
		for( final CellProcessor p : Arrays.asList(processor2, processorChain2) ) {
			assertExecution(p, normalInput, expectedOutput);
		}
		
		// german input (opposite - "," is decimal symbol and "." is grouping)
		for( final CellProcessor p : Arrays.asList(processor3, processorChain3) ) {
			assertExecution(p, germanInput, expectedOutput);
		}
	}
	
	/**
	 * Tests that grouping separators are handled correctly for negative numbers.
	 */
	@Test
	public void testValidNegativeInputWithGroupingSeparator() {
		String normalInput = "-1,357.459";
		String germanInput = "-1.357,459";
		BigDecimal expectedOutput = new BigDecimal("-1357.459");
		
		// 'no symbols' processors should choke on grouping separators
		for( CellProcessor p : Arrays.asList(processor, processorChain) ) {
			try {
				p.execute(normalInput, ANONYMOUS_CSVCONTEXT);
				fail("should have thrown SuperCsvCellProcessorException");
			}
			catch(SuperCsvCellProcessorException e) {}
		}
		
		// normal input
		for( final CellProcessor p : Arrays.asList(processor2, processorChain2) ) {
			assertExecution(p, normalInput, expectedOutput);
		}
		
		// german input (opposite - "," is decimal symbol and "." is grouping)
		for( final CellProcessor p : Arrays.asList(processor3, processorChain3) ) {
			assertExecution(p, germanInput, expectedOutput);
		}
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
