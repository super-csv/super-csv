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
import static org.supercsv.SuperCsvTestUtils.date;

import java.sql.Time;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the FmtTime processor.
 * 
 * @author Pietro Aragona
 */
public class FmtSqlTimeTest {
	
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final Date DATE = date(2015, 8, 30, 23, 45, 59);
	private static final Time TIME = new Time(DATE.getTime());
	private static final String FORMATTED_TIME = "23:45:59";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new FmtSqlTime(TIME_FORMAT);
		processorChain = new FmtSqlTime(TIME_FORMAT, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a valid time.
	 */
	@Test
	public void testWithValidDate() {
		assertEquals(FORMATTED_TIME, processor.execute(TIME, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_TIME, processorChain.execute(TIME, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-Time input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNonDate() {
		processor.execute(123, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid date format (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithInvalidDateFormat() {
		CellProcessor invalidDateFormatProcessor = new FmtSqlTime("abcd");
		invalidDateFormatProcessor.execute(TIME, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction of the processor with a null date format (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullDateFormat() {
		new FmtSqlTime(null);
	}
}
