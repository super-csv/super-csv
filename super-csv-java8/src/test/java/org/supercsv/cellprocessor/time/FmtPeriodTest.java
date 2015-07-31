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
 * Tests the FmtPeriod cell processor.
 */
@RunWith(Theories.class)
public class FmtPeriodTest {

	@DataPoints public static final FmtPeriod[] processors = { new FmtPeriod(),
		new FmtPeriod(new IdentityTransform()) };
	@DataPoints public static final Period[] periods = { Period.of(1, 2, 3), Period.of(0, 0, 0), Period.of(4, 0, 8) };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidPeriodString(final Period period, final FmtPeriod processor) {
		assertEquals(period.toString(), processor.execute(period, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final FmtPeriod p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonPeriodInput(final FmtPeriod p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.time.Period but is java.lang.Integer");
		p.execute(123, SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtPeriod(null);
	}
}
