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

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the FmtNumber processor. As FmtNumber uses the default locale, this test must be written in a locale
 * independent way (so the test will pass). The test can be run in a different local by specifying the
 * <tt>user.language</tt> and <tt>user.country</tt> JVM properties (i.e. <tt>-Duser.language=de -Duser.country=DE</tt>).
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class FmtNumberTest {
	
	private static final String DECIMAL_FORMAT = "00.00";
	
	// locale-independent
	private static final String FORMATTED_NUMBER = new DecimalFormat(DECIMAL_FORMAT).format(12.34);
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new FmtNumber(DECIMAL_FORMAT);
		processor2 = new FmtNumber(new DecimalFormat(DECIMAL_FORMAT));
		processorChain = new FmtNumber(DECIMAL_FORMAT, new IdentityTransform());
		processorChain2 = new FmtNumber(new DecimalFormat(DECIMAL_FORMAT), new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution.
	 */
	@Test
	public void testFormat() {
		final double number = 12.34;
		assertEquals(FORMATTED_NUMBER, processor.execute(number, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processor2.execute(number, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain.execute(number, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain2.execute(number, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with an input that should round up.
	 */
	@Test
	public void testRoundUp() {
		final double toRoundUp = 12.339;
		assertEquals(FORMATTED_NUMBER, processor.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processor2.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain2.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with an input that should round down.
	 */
	@Test
	public void testRoundDown() {
		final double toRoundDown = 12.341;
		assertEquals(FORMATTED_NUMBER, processor.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processor2.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain2.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-Number input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNonNumber() {
		processor.execute("abc", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid number format (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithInvalidNumberFormat() {
		final double number = 12.34;
		CellProcessor invalidNumberFormatProcessor = new FmtNumber("%%%");
		invalidNumberFormatProcessor.execute(number, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid number format String (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullNumberFormatString() {
		final double number = 12.34;
		CellProcessor invalidNumberFormatProcessor = new FmtNumber((String) null);
		invalidNumberFormatProcessor.execute(number, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid number format object (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullNumberFormat() {
		final double number = 12.34;
		CellProcessor invalidNumberFormatProcessor = new FmtNumber((DecimalFormat) null);
		invalidNumberFormatProcessor.execute(number, ANONYMOUS_CSVCONTEXT);
	}
	
}
