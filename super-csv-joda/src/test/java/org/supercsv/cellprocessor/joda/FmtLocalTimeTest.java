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
package org.supercsv.cellprocessor.joda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.supercsv.cellprocessor.joda.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.joda.mock.IdentityTransform;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the FmtLocalTime cell processor.
 */
public class FmtLocalTimeTest {

	private static final String LOCAL_TIME_FORMAT = "hh:mm:ss.SSS";
	private static final String LOCAL_TIME_STRING = "01:02:03.000";
	private static final LocalTime LOCAL_TIME = new LocalTime(1, 2, 3, 0);

	private FmtLocalTime processor1;
	private FmtLocalTime processor2;
	private FmtLocalTime processor3;
	private FmtLocalTime processor4;
	private FmtLocalTime processorChain1;
	private FmtLocalTime processorChain2;
	private FmtLocalTime processorChain3;
	private FmtLocalTime processorChain4;
	private List<FmtLocalTime> processors;
	private DateTimeFormatter formatter;

	@Before
	public void setUp() {
		formatter = ISODateTimeFormat.hourMinuteSecondMillis();
		processor1 = new FmtLocalTime();
		processor2 = new FmtLocalTime(formatter);
		processor3 = new FmtLocalTime(LOCAL_TIME_FORMAT);
		processor4 = new FmtLocalTime(LOCAL_TIME_FORMAT, Locale.ENGLISH);
		processorChain1 = new FmtLocalTime(new IdentityTransform());
		processorChain2 = new FmtLocalTime(formatter, new IdentityTransform());
		processorChain3 = new FmtLocalTime(LOCAL_TIME_FORMAT,
				new IdentityTransform());
		processorChain4 = new FmtLocalTime(LOCAL_TIME_FORMAT, Locale.ENGLISH,
				new IdentityTransform());
		processors = Arrays.asList(processor1, processor2, processor3,
				processor4, processorChain1, processorChain2, processorChain3,
				processorChain4);
	}

	@Test
	public void testValidDateTimeString() {
		for (CellProcessor p : processors) {
			assertEquals(LOCAL_TIME_STRING,
					p.execute(LOCAL_TIME, ANONYMOUS_CSVCONTEXT));
		}
	}

	@Test
	public void testNullInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(null, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"this processor does not accept null input - "
								+ "if the column is optional then chain an Optional() processor before this one",
						e.getMessage());
			}
		}
	}

	@Test
	public void testNonLocalDateTimeInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(123, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"the input value should be of type org.joda.time.LocalTime but is java.lang.Integer",
						e.getMessage());
			}
		}
	}

	@Test
	public void testInvalidDateFormat() {
		final CellProcessor p = new FmtLocalTime("not valid");
		try {
			p.execute(LOCAL_TIME, ANONYMOUS_CSVCONTEXT);
			fail("expecting SuperCsvCellProcessorException");
		} catch (SuperCsvCellProcessorException e) {
			assertEquals("Failed to format value as a LocalTime",
					e.getMessage());
		}
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor2WithNullNext() {
		new FmtLocalTime((CellProcessor) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor3WithNullFormatter() {
		new FmtLocalTime((DateTimeFormatter) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullFormatter() {
		new FmtLocalTime((DateTimeFormatter) null, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullNext() {
		new FmtLocalTime(formatter, null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor5WithNullPattern() {
		new FmtLocalTime((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor6WithNullPattern() {
		new FmtLocalTime((String) null, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor6WithNullNext() {
		new FmtLocalTime(LOCAL_TIME_FORMAT, (CellProcessor) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor7WithNullPattern() {
		new FmtLocalTime((String) null, Locale.ENGLISH, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor7WithNullNext() {
		new FmtLocalTime(LOCAL_TIME_FORMAT, Locale.ENGLISH,
				(CellProcessor) null);
	}

}
