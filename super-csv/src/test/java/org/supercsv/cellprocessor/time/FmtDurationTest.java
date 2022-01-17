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
 * Tests the FmtDuration cell processor.
 */
@RunWith(Theories.class)
public class FmtDurationTest {

	@DataPoints public static final ZonedDateTime[] times = {
		ZonedDateTime.of(2013, 10, 25, 1, 2, 3, 0, ZoneOffset.UTC),
		ZonedDateTime.of(2014, 11, 26, 2, 3, 4, 0, ZoneOffset.UTC) };

	@DataPoints public static FmtDuration[] processors = { new FmtDuration(),
		new FmtDuration(new IdentityTransform()) };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidDurationString(final FmtDuration p, final ZonedDateTime start, final ZonedDateTime end) {
		final Duration duration = Duration.between(start, end);
		assertEquals(duration.toString(), p.execute(duration, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final FmtDuration p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonDurationInput(final FmtDuration p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.time.Duration but is java.lang.Integer");
		p.execute(123, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtDuration(null);
	}

}
