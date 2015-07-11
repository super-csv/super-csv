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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
 * Tests the ParseZonedDateTime cell processor.
 */
@RunWith(Theories.class)
public class ParseZonedDateTimeTest {
	@DataPoints public static final ZonedDateTime[] dateTimes = {
		ZonedDateTime.of(2013, 10, 25, 1, 2, 3, 0, ZoneOffset.ofHours(10)) };

	@DataPoints public static ParseZonedDateTime[] processors = { new ParseZonedDateTime(),
		new ParseZonedDateTime(DateTimeFormatter.ISO_ZONED_DATE_TIME), new ParseZonedDateTime(new IdentityTransform()),
		new ParseZonedDateTime(DateTimeFormatter.ISO_ZONED_DATE_TIME, new IdentityTransform()) };

	@DataPoints public static final DateTimeFormatter[] formats = { DateTimeFormatter.ISO_ZONED_DATE_TIME,
			DateTimeFormatter.ofPattern("eee MMM dd HH:mm:ss zzz yyyy") };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidDateTime(final ParseZonedDateTime p, final ZonedDateTime dateTime) {
		assertEquals(dateTime, p.execute(dateTime.toString(), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final ZonedDateTime dateTime, final DateTimeFormatter formatter) {
		final ParseZonedDateTime p = new ParseZonedDateTime(formatter);
		assertEquals(dateTime, p.execute(dateTime.format(formatter), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParseZonedDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParseZonedDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnparsableString(final ParseZonedDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value");
		p.execute("not valid", ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseZonedDateTime((CellProcessor) null);
	}

	@Test
	public void testConstructor3WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new ParseZonedDateTime((DateTimeFormatter) null);
	}

	@Test
	public void testConstructor4WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new ParseZonedDateTime(null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseZonedDateTime(DateTimeFormatter.ISO_ZONED_DATE_TIME, null);
	}

}
