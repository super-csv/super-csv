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
package org.supercsv.cellprocessor.joda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.supercsv.cellprocessor.joda.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.joda.mock.IdentityTransform;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the ParseInterval cell processor.
 */
public class ParseIntervalTest {

	private static final String INTERVAL_STRING = "2013-10-25T01:02:03.000Z/2014-11-26T02:03:04.000Z";
	private static final DateTime START = new DateTime(2013, 10, 25, 1, 2, 3,
			0, DateTimeZone.UTC);
	private static final DateTime END = new DateTime(2014, 11, 26, 2, 3, 4, 0,
			DateTimeZone.UTC);

	private ParseInterval processor1;
	private ParseInterval processorChain1;
	private List<ParseInterval> processors;

	@Before
	public void setUp() {
		processor1 = new ParseInterval();
		processorChain1 = new ParseInterval(new IdentityTransform());
		processors = Arrays.asList(processor1, processorChain1);
	}

	@Test
	public void testValidInterval() {
		for (CellProcessor p : processors) {
			Interval result = (Interval) p.execute(INTERVAL_STRING,
					ANONYMOUS_CSVCONTEXT);
			assertEquals(START, result.getStart().withZone(DateTimeZone.UTC));
			assertEquals(END, result.getEnd().withZone(DateTimeZone.UTC));
		}
	}

	@Test
	public void testNullInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(null, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"this processor does not accept null input - "
								+ "if the column is optional then chain an Optional() processor before this one",
						e.getMessage());
			}
		}
	}

	@Test
	public void testNonStringInput() {
		for (CellProcessor p : processors) {
			try {
				p.execute(123, ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals(
						"the input value should be of type java.lang.String but is java.lang.Integer",
						e.getMessage());
			}
		}
	}

	@Test
	public void testUnparsableString() {
		for (CellProcessor p : processors) {
			try {
				p.execute("not valid", ANONYMOUS_CSVCONTEXT);
				fail("expecting SuperCsvCellProcessorException");
			} catch (SuperCsvCellProcessorException e) {
				assertEquals("Failed to parse value as an Interval",
						e.getMessage());
			}
		}
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor2WithNullNext() {
		new ParseInterval((CellProcessor) null);
	}

}
