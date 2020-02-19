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

import java.time.Period;

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
 * Tests the ParsePeriod cell processor.
 */
@RunWith(Theories.class)
public class ParsePeriodTest {

	@DataPoints public static ParsePeriod[] processors = { new ParsePeriod(),
		new ParsePeriod(new IdentityTransform()) };

	@DataPoints public static Period[] periods = { Period.of(1, 2, 3), Period.of(3, 0, 23) };
	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidPeriod(final ParsePeriod processor, final Period period) {
		assertEquals(period, processor.execute(period.toString(), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParsePeriod p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParsePeriod p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnparsableString(final ParsePeriod p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value as a Period");
		p.execute("not valid", ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParsePeriod(null);
	}

}
