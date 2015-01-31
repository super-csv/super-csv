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

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.joda.mock.IdentityTransform;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the FmtLocalDate cell processor.
 */
public class FmtLocalDateTest {

	private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
	private static final String LOCAL_DATE_STRING = "2013-10-25";
	private static final LocalDate LOCAL_DATE = new LocalDate(2013, 10, 25);

	private FmtLocalDate processor1;
	private FmtLocalDate processor2;
	private FmtLocalDate processor3;
	private FmtLocalDate processor4;
	private FmtLocalDate processorChain1;
	private FmtLocalDate processorChain2;
	private FmtLocalDate processorChain3;
	private FmtLocalDate processorChain4;
	private List<FmtLocalDate> processors;
	private DateTimeFormatter formatter;

	@Before
	public void setUp() {
		formatter = ISODateTimeFormat.yearMonthDay();
		processor1 = new FmtLocalDate();
		processor2 = new FmtLocalDate(formatter);
		processor3 = new FmtLocalDate(LOCAL_DATE_FORMAT);
		processor4 = new FmtLocalDate(LOCAL_DATE_FORMAT, Locale.ENGLISH);
		processorChain1 = new FmtLocalDate(new IdentityTransform());
		processorChain2 = new FmtLocalDate(formatter, new IdentityTransform());
		processorChain3 = new FmtLocalDate(LOCAL_DATE_FORMAT,
				new IdentityTransform());
		processorChain4 = new FmtLocalDate(LOCAL_DATE_FORMAT, Locale.ENGLISH,
				new IdentityTransform());
		processors = Arrays.asList(processor1, processor2, processor3,
				processor4, processorChain1, processorChain2, processorChain3,
				processorChain4);
	}

	@Test
	public void testValidDateTimeString() {
		for (CellProcessor p : processors) {
			assertEquals(LOCAL_DATE_STRING,
					p.execute(LOCAL_DATE, ANONYMOUS_CSVCONTEXT));
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
	public void testNonLocalDateInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(123, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"the input value should be of type org.joda.time.LocalDate but is java.lang.Integer",
						e.getMessage());
			}
		}
	}

	@Test
	public void testInvalidDateFormat() {
		final CellProcessor p = new FmtLocalDate("not valid");
		try {
			p.execute(LOCAL_DATE, ANONYMOUS_CSVCONTEXT);
			fail("expecting SuperCsvCellProcessorException");
		} catch (SuperCsvCellProcessorException e) {
			assertEquals("Failed to format value as a LocalDate", e.getMessage());
		}
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor2WithNullNext() {
		new FmtLocalDate((CellProcessor) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor3WithNullFormatter() {
		new FmtLocalDate((DateTimeFormatter) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullFormatter() {
		new FmtLocalDate((DateTimeFormatter) null, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullNext() {
		new FmtLocalDate(formatter, null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor5WithNullPattern() {
		new FmtLocalDate((String) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor6WithNullPattern() {
		new FmtLocalDate((String) null, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor6WithNullNext() {
		new FmtLocalDate(LOCAL_DATE_FORMAT, (CellProcessor) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor7WithNullPattern() {
		new FmtLocalDate((String) null, Locale.ENGLISH, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor7WithNullNext() {
		new FmtLocalDate(LOCAL_DATE_FORMAT, Locale.ENGLISH, (CellProcessor) null);
	}

}
