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

import java.sql.Time;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;
import static org.supercsv.SuperCsvTestUtils.date;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Tests the ParseTime processor.
 * 
 * @author Pietro Aragona
 */
public class ParseTimeTest {
	
	private static final Date DATE = date(2015, 8, 30, 23, 45, 59);
	private static final Time TIME = new Time(DATE.getTime());
	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final String FORMATTED_DATE = "30/08/2011 23:45:59";
	private static final String FORMATTED_TIME = "23:45:59";
	
	private CellProcessor processor;
	private CellProcessor processor1;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseDate(DATE_FORMAT, new ParseTime());
		processor1 = new ParseDate(TIME_FORMAT, new ParseTime());
	}
	
	@Test
	public void testValidTime() {
		assertEquals(TIME.toString(), processor1.execute(FORMATTED_TIME, ANONYMOUS_CSVCONTEXT).toString());
	}
	
	@Test
	public void testValidDate() {
		assertEquals(TIME.toString(), processor.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT).toString());
	}
	
}
