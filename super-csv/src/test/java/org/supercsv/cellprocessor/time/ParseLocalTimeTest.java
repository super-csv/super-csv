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
 * Tests the ParseLocalTime cell processor.
 */
@RunWith(Theories.class)
public class ParseLocalTimeTest {

	@DataPoints public static final LocalTime[] localTimes = { LocalTime.of(1, 2, 3, 0) };

	@DataPoints public static ParseLocalTime[] processors = { new ParseLocalTime(),
		new ParseLocalTime(DateTimeFormatter.ISO_LOCAL_TIME), new ParseLocalTime(new IdentityTransform()),
		new ParseLocalTime(DateTimeFormatter.ISO_LOCAL_TIME, new IdentityTransform()) };

	@DataPoints public static DateTimeFormatter[] formats = { DateTimeFormatter.ISO_LOCAL_TIME,
		DateTimeFormatter.ofPattern("mm HH ss") };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidLocalTime(final ParseLocalTime p, final LocalTime localTime) {
		assertEquals(localTime, p.execute(localTime.toString(), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final LocalTime localTime, final DateTimeFormatter formatter) {
		final ParseLocalTime p = new ParseLocalTime(formatter);
		assertEquals(localTime, p.execute(localTime.format(formatter), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParseLocalTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParseLocalTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnparsableString(final ParseLocalTime p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value");
		p.execute("not valid", ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseLocalTime((CellProcessor) null);
	}

	@Test
	public void testConstructor3WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new ParseLocalTime((DateTimeFormatter) null);
	}

	@Test
	public void testConstructor4WithNullFormatter() {
		exception.expect(NullPointerException.class);
		new ParseLocalTime(null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseLocalTime(DateTimeFormatter.ISO_LOCAL_TIME, null);
	}

}
