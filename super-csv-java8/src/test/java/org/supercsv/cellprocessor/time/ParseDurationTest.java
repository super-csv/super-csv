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

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.supercsv.cellprocessor.time.mock.IdentityTransform;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the ParseDuration cell processor.
 */
@RunWith(Theories.class)
public class ParseDurationTest {

	@DataPoints public static ParseDuration[] processors = { new ParseDuration(),
		new ParseDuration(new IdentityTransform()) };

	@DataPoints public static ZonedDateTime[] times = { ZonedDateTime.of(2013, 10, 25, 1, 2, 3, 0, ZoneOffset.UTC),
		ZonedDateTime.of(2014, 11, 26, 2, 3, 4, 0, ZoneOffset.UTC) };
	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidDuration(final ParseDuration p, final ZonedDateTime start, final ZonedDateTime end) {
		final Duration duration = Duration.between(start, end);
		assertEquals(duration, p.execute(duration.toString(), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParseDuration p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParseDuration p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnparsableString(final ParseDuration p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value as a Duration");
		p.execute("not valid", ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseDuration(null);
	}

}
