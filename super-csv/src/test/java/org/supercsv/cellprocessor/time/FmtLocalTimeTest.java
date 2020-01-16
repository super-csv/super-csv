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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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
 * Tests the FmtLocalTime cell processor.
 */
@RunWith(Theories.class)
public class FmtLocalTimeTest {

	@DataPoints public static final LocalTime[] localTimes = { LocalTime.of(1, 2, 3, 0) };

	@DataPoints public static FmtLocalTime[] processors = { new FmtLocalTime(),
		new FmtLocalTime(DateTimeFormatter.ISO_LOCAL_TIME), new FmtLocalTime(new IdentityTransform()),
		new FmtLocalTime(DateTimeFormatter.ISO_LOCAL_TIME, new IdentityTransform()) };

	@DataPoints public static final DateTimeFormatter[] formats = {
		DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM), DateTimeFormatter.ofPattern("mm:ss") };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidDateTimeString(final FmtLocalTime p, final LocalTime localTime) {
		assertEquals(localTime.toString(), p.execute(localTime, ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final DateTimeFormatter formatter, final LocalTime localTime) {
		final FmtLocalTime p = new FmtLocalTime(formatter);
		assertEquals(localTime.format(formatter), p.execute(localTime, ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final FmtLocalTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonLocalDateTimeInput(final FmtLocalTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.time.LocalTime but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtLocalTime((CellProcessor) null);
	}

	@Test
	public void testConstructor3WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new FmtLocalTime((DateTimeFormatter) null);
	}

	@Test
	public void testConstructor4WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new FmtLocalTime(null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtLocalTime(DateTimeFormatter.ISO_LOCAL_TIME, null);
	}
}
