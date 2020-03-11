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

import java.time.ZoneId;
import java.util.Map;
import java.util.TreeMap;

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
 * Tests the ParseZoneId cell processor.
 */
@RunWith(Theories.class)
public class ParseZoneIdTest {

	@DataPoints public static final ZoneId[] zoneIds = { ZoneId.of("Europe/Vienna"), ZoneId.of("Asia/Shanghai"),
		ZoneId.of("UTC") };

	@DataPoints public static ParseZoneId[] processors = { new ParseZoneId(), new ParseZoneId(new IdentityTransform()),
		new ParseZoneId(ZoneId.SHORT_IDS), new ParseZoneId(ZoneId.SHORT_IDS, new IdentityTransform()) };

	@DataPoints public static final String[] shortIds = ZoneId.SHORT_IDS.keySet()
		.toArray(new String[ZoneId.SHORT_IDS.size()]);

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidDateTimeZone(final ParseZoneId processor, final ZoneId zoneId) {
		assertEquals(zoneId, processor.execute(zoneId.toString(), ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testShortIds(final String shortId) {
		final ParseZoneId processor = new ParseZoneId(ZoneId.SHORT_IDS);
		assertEquals(ZoneId.of(shortId, ZoneId.SHORT_IDS), processor.execute(shortId, ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final ParseZoneId p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonStringInput(final ParseZoneId p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.lang.String but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testBadStringFormat(final ParseZoneId p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value as a ZoneId");
		p.execute("not valid", ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testUnknownZone(final ParseZoneId p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("Failed to parse value as a ZoneId");
		p.execute("Europe/Atlantis", ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseZoneId((CellProcessor) null);
	}

	@Test
	public void testConstructor3WithNullMapping() {
		exception.expect(NullPointerException.class);
		new ParseZoneId((Map<String, String>) null);
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new ParseZoneId(new TreeMap<>(), null);
	}

	@Test
	public void testConstructor4WithNullMapping() {
		exception.expect(NullPointerException.class);
		new ParseZoneId(null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNull() {
		exception.expect(NullPointerException.class);
		new ParseZoneId(null, null);
	}
}
