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
package org.supercsv.cellprocessor.time;

import static org.junit.Assert.assertEquals;
import static org.supercsv.cellprocessor.time.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.mock.IdentityTransform;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the ParseLocalDate cell processor.
 */
@RunWith(Theories.class)
public class ParseLocalDateTest {

	@DataPoints public static final LocalDate[] localDates = { LocalDate.of(2013, 10, 25) };
	@DataPoints public static final DateTimeFormatter[] formats = { DateTimeFormatter.ISO_LOCAL_DATE,
		DateTimeFormatter.ofPattern("eee MMM dd yyyy"), DateTimeFormatter.ofPattern("eee MMM dd yyyy", Locale.CHINA) };
	@DataPoints public static ParseLocalDate[] processors = { new ParseLocalDate(),
		new ParseLocalDate(DateTimeFormatter.ISO_LOCAL_DATE), new ParseLocalDate(new IdentityTransform()),
		new ParseLocalDate(DateTimeFormatter.ISO_LOCAL_DATE, new IdentityTransform()) };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidLocalDate(final ParseLocalDate p, final LocalDate localDate) {
		assertEquals(localDate, p.execute(localDate.toString(), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final LocalDate localDate, final DateTimeFormatter formatter) {
		final ParseLocalDate p = new ParseLocalDate(formatter);
		final ParseLocalDate pNext = new ParseLocalDate(formatter, new IdentityTransform());
		assertEquals(localDate, p.execute(localDate.format(formatter), SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
		assertEquals(localDate, pNext.execute(localDate.format(formatter), SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParseLocalDate p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParseLocalDate p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnparsableString(final ParseLocalDate p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value");
		p.execute("not valid", ANONYMOUS_CSVCONTEXT);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor2WithNullNext() {
		new ParseLocalDate((CellProcessor) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor3WithNullFormatter() {
		new ParseLocalDate((DateTimeFormatter) null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullFormatter() {
		new ParseLocalDate(null, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullNext() {
		new ParseLocalDate(DateTimeFormatter.ISO_LOCAL_DATE, null);
	}

}
