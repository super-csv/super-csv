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

import java.time.LocalDateTime;
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
 * Tests the ParseLocalDateTime cell processor.
 */
@RunWith(Theories.class)
public class ParseLocalDateTimeTest {

	@Rule public ExpectedException exception = ExpectedException.none();

	@DataPoints public static final LocalDateTime[] localDateTimes = { LocalDateTime.of(2013, 10, 25, 1, 2, 3) };

	@DataPoints public static final DateTimeFormatter[] formats = { DateTimeFormatter.ISO_LOCAL_DATE_TIME,
		DateTimeFormatter.ofPattern("eee MMM dd yyyy HH:mm:ss"),
		DateTimeFormatter.ofPattern("eee MMM dd yyyy HH:mm:ss", Locale.CHINA) };

	@DataPoints public static ParseLocalDateTime[] processors = { new ParseLocalDateTime(),
		new ParseLocalDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME, new IdentityTransform()),
		new ParseLocalDateTime(new IdentityTransform()),
		new ParseLocalDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME) };

	@Theory
	public void testValidLocalDateTime(final ParseLocalDateTime p, final LocalDateTime localDateTime) {
		assertEquals(localDateTime, p.execute(localDateTime.toString(), SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final LocalDateTime localDateTime, final DateTimeFormatter formatter) {
		final ParseLocalDateTime p = new ParseLocalDateTime(formatter);
		assertEquals(localDateTime, p.execute(localDateTime.format(formatter), SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParseLocalDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParseLocalDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnparsableString(final ParseLocalDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value");
		p.execute("not valid", SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseLocalDateTime((CellProcessor) null);
	}

	@Test
	public void testConstructor3WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new ParseLocalDateTime((DateTimeFormatter) null);
	}

	@Test
	public void testConstructor4WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new ParseLocalDateTime(null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseLocalDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME, null);
	}

}
