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
import java.time.format.TextStyle;
import java.util.Locale;

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
 * Tests the FmtZoneId cell processor.
 */
@RunWith(Theories.class)
public class FmtZoneIdTest {

	@DataPoints public static final ZoneId[] zoneIds = { ZoneId.of("Europe/Vienna"), ZoneId.of("UTC") };

	@DataPoints public static FmtZoneId[] processors = { new FmtZoneId(), new FmtZoneId(new IdentityTransform()) };

	@DataPoints public static final Locale[] locales = { Locale.CHINA, Locale.ENGLISH, Locale.GERMANY };

	@DataPoints public static final TextStyle[] textStyles = { TextStyle.FULL, TextStyle.SHORT };

	@Rule public ExpectedException exception = ExpectedException.none();

	@Theory
	public void testValidZoneIdString(final FmtZoneId p, final ZoneId zoneId) {
		assertEquals(zoneId.toString(), p.execute(zoneId, ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testFormats(final TextStyle textStyle, final Locale locale, final ZoneId zoneId) {
		final FmtZoneId p = new FmtZoneId(textStyle, locale);
		assertEquals(zoneId.getDisplayName(textStyle, locale), p.execute(zoneId, ANONYMOUS_CSVCONTEXT));
	}

	@Theory
	public void testNullInput(final FmtZoneId p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("this processor does not accept null input - "
			+ "if the column is optional then chain an Optional() processor before this one");
		p.execute(null, ANONYMOUS_CSVCONTEXT);
	}

	@Theory
	public void testNonZoneIdInput(final FmtZoneId p) {
		exception.expect(SuperCsvCellProcessorException.class);
		exception.expectMessage("the input value should be of type java.time.ZoneId but is java.lang.Integer");
		p.execute(123, ANONYMOUS_CSVCONTEXT);
	}

	@Test
	public void testConstructor2WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtZoneId(null);
	}

	@Test
	public void testConstructor3WithNullTestStyle() {
		exception.expect(NullPointerException.class);
		new FmtZoneId(null, Locale.ENGLISH);
	}

	@Test
	public void testConstructor3WithNullLocale() {
		exception.expect(NullPointerException.class);
		new FmtZoneId(TextStyle.FULL, null);
	}

	@Test
	public void testConstructor4WithNullLocale() {
		exception.expect(NullPointerException.class);
		new FmtZoneId(TextStyle.FULL, null, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullTestStyle() {
		exception.expect(NullPointerException.class);
		new FmtZoneId(null, Locale.ENGLISH, new IdentityTransform());
	}

	@Test
	public void testConstructor4WithNullNext() {
		exception.expect(NullPointerException.class);
		new FmtZoneId(TextStyle.FULL, Locale.ENGLISH, null);
	}
}
