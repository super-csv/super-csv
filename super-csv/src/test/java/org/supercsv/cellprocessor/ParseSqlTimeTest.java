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
import static org.supercsv.SuperCsvTestUtils.date;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the ParseTime processor.
 * 
 * @author Pietro Aragona
 */
public class ParseSqlTimeTest {
	
	private static final Date DATE = date(2015, 8, 30, 23, 45, 59);
	private static final Time TIME = new Time(DATE.getTime());
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final String FORMATTED_TIME = "23:45:59";
	
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
		processor = new ParseSqlTime(TIME_FORMAT);
		processor2 = new ParseSqlTime(TIME_FORMAT, true);
		processor3 = new ParseSqlTime(TIME_FORMAT, false);
		processorChain = new ParseSqlTime(TIME_FORMAT, new IdentityTransform());
		processorChain2 = new ParseSqlTime(TIME_FORMAT, true, new IdentityTransform());
		processorChain3 = new ParseSqlTime(TIME_FORMAT, false, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with various date formats.
	 */
	@Test
	public void testValidTime() {
		// first date format
		assertEquals(TIME.toString(), processor.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
		assertEquals(TIME.toString(), processor2.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
		assertEquals(TIME.toString(), processor3.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
		assertEquals(TIME.toString(), processorChain.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
		assertEquals(TIME.toString(), processorChain2.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
		assertEquals(TIME.toString(), processorChain3.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
		
	}
	
	/**
	 * Tests execution with an invalid time (doesn't match format), should throw an exception.
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testBadlyFormattedDate() {
		processor.execute("22-20-59", ANONYMOUS_CSVCONTEXT).toString();
	}
	
	/**
	 * Tests execution with an invalid time (matches format, but data invalid) and non-lenient processors (should throw
	 * an exception).
	 */
	@Test
	public void testInvalidTimeWithNonLenient() {
		final String dodgyDate = "22:20:61";
		for( final CellProcessor cp : Arrays.asList(processor, processor3, processorChain, processorChain3) ) {
			try {
				cp.execute(dodgyDate, ANONYMOUS_CSVCONTEXT).toString();
				fail("should have thrown a SuperCsvCellProcessorException");
			}
			catch(SuperCsvCellProcessorException e) {}
		}
	}
	
	/**
	 * Tests execution with a non String input (should throw an exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNonCharInput() {
		processor.execute(1, ANONYMOUS_CSVCONTEXT).toString();
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null date format (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullDateFormat() {
		new ParseSqlTime(null);
	}
	
	/**
	 * Tests construction (using the Locale constructor) with a null date format (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testLocaleConstructorWithNullDateFormat() {
		new ParseSqlTime(null, false, Locale.GERMAN);
	}
	
	/**
	 * Tests construction (using the Locale constructor) with a null locale (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testLocaleConstructorWithNullLocale() {
		new ParseSqlTime(TIME_FORMAT, false, (Locale) null);
	}
	
}
