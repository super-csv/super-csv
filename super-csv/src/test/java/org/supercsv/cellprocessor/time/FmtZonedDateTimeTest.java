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
 * Tests the FmtZonedDateTime cell processor.
 */
@RunWith(Theories.class)
public class FmtZonedDateTimeTest {

	@DataPoints public static final ZonedDateTime[] dateTimes = {
		ZonedDateTime.of(2013, 10, 25, 1, 2, 3, 0, ZoneOffset.ofHours(10)) };

	@DataPoints public static FmtZonedDateTime[] processor = { new FmtZonedDateTime(),
		new FmtZonedDateTime(DateTimeFormatter.ISO_DATE_TIME), new FmtZonedDateTime(new IdentityTransform()),
		new FmtZonedDateTime(DateTimeFormatter.ISO_DATE_TIME, new IdentityTransform()) };

	@DataPoints public static final DateTimeFormatter[] formats = { DateTimeFormatter.ISO_ZONED_DATE_TIME,
		DateTimeFormatter.ofPattern("eee MMM dd HH:mm:ss zzz yyyy") };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidDateTimeString(final FmtZonedDateTime p, final ZonedDateTime dateTime) {
		assertEquals(dateTime.toString(), p.execute(dateTime, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final DateTimeFormatter formatter, final ZonedDateTime dateTime) {
		final FmtZonedDateTime p = new FmtZonedDateTime(formatter);
		assertEquals(dateTime.format(formatter), p.execute(dateTime, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final FmtZonedDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonDateTimeInput(final FmtZonedDateTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.time.ZonedDateTime but is java.lang.Integer");
		p.execute(123, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtZonedDateTime((CellProcessor) null);
	}

	@Test
	public void testConstructor3WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new FmtZonedDateTime((DateTimeFormatter) null);
	}

	@Test
	public void testConstructor4WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new FmtZonedDateTime(null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtZonedDateTime(DateTimeFormatter.ISO_DATE_TIME, null);
	}

}
