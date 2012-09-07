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
package org.supercsv.prefs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.supercsv.prefs.CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;
import static org.supercsv.prefs.CsvPreference.EXCEL_PREFERENCE;
import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;
import static org.supercsv.prefs.CsvPreference.TAB_PREFERENCE;

import org.junit.Test;

/**
 * Tests the CsvPreference class.
 * 
 * @author James Bassett
 */
public class CsvPreferenceTest {
	
	/**
	 * Tests the constant values.
	 */
	@Test
	public void testConstants() {
		assertEquals('"', STANDARD_PREFERENCE.getQuoteChar());
		assertEquals(',', STANDARD_PREFERENCE.getDelimiterChar());
		assertEquals("\r\n", STANDARD_PREFERENCE.getEndOfLineSymbols());
		
		assertEquals('"', EXCEL_PREFERENCE.getQuoteChar());
		assertEquals(',', EXCEL_PREFERENCE.getDelimiterChar());
		assertEquals("\n", EXCEL_PREFERENCE.getEndOfLineSymbols());
		
		assertEquals('"', EXCEL_NORTH_EUROPE_PREFERENCE.getQuoteChar());
		assertEquals(';', EXCEL_NORTH_EUROPE_PREFERENCE.getDelimiterChar());
		assertEquals("\n", EXCEL_NORTH_EUROPE_PREFERENCE.getEndOfLineSymbols());
		
		assertEquals('"', TAB_PREFERENCE.getQuoteChar());
		assertEquals('\t', TAB_PREFERENCE.getDelimiterChar());
		assertEquals("\n", TAB_PREFERENCE.getEndOfLineSymbols());
	}
	
	/**
	 * Tests a custom CsvPreference with default optional values.
	 */
	@Test
	public void testCustomPreferenceWithDefaults() {
		final CsvPreference custom = new CsvPreference.Builder('"', ',', "\n").build();
		assertEquals('"', custom.getQuoteChar());
		assertEquals(',', custom.getDelimiterChar());
		assertEquals("\n", custom.getEndOfLineSymbols());
		assertFalse(custom.isSurroundingSpacesNeedQuotes());
	}
	
	/**
	 * Tests a custom CsvPreference with supplied optional values.
	 */
	@Test
	public void testCustomPreference() {
		final CsvPreference custom = new CsvPreference.Builder('"', ',', "\n").surroundingSpacesNeedQuotes(true).build();
		assertEquals('"', custom.getQuoteChar());
		assertEquals(',', custom.getDelimiterChar());
		assertEquals("\n", custom.getEndOfLineSymbols());
		assertTrue(custom.isSurroundingSpacesNeedQuotes());
	}
	
	/**
	 * Tests a custom CsvPreference based on an existing constant with default optional values.
	 */
	@Test
	public void testCustomPreferenceBasedOnExistingWithDefaults() {
		final CsvPreference custom = new CsvPreference.Builder(EXCEL_PREFERENCE).build();
		assertEquals(EXCEL_PREFERENCE.getQuoteChar(), custom.getQuoteChar());
		assertEquals(EXCEL_PREFERENCE.getDelimiterChar(), custom.getDelimiterChar());
		assertEquals(EXCEL_PREFERENCE.getEndOfLineSymbols(), custom.getEndOfLineSymbols());
		assertEquals(EXCEL_PREFERENCE.isSurroundingSpacesNeedQuotes(), custom.isSurroundingSpacesNeedQuotes());
	}
	
	/**
	 * Tests a custom CsvPreference based on an existing constant with supplied optional values.
	 */
	@Test
	public void testCustomPreferenceBasedOnExisting() {
		final CsvPreference custom = new CsvPreference.Builder(EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true).build();
		assertEquals(EXCEL_PREFERENCE.getQuoteChar(), custom.getQuoteChar());
		assertEquals(EXCEL_PREFERENCE.getDelimiterChar(), custom.getDelimiterChar());
		assertEquals(EXCEL_PREFERENCE.getEndOfLineSymbols(), custom.getEndOfLineSymbols());
		assertTrue(custom.isSurroundingSpacesNeedQuotes());
	}
	
	/**
	 * Tests a custom CsvPreference with identical quote and delimiter chars (should throw an exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCustomPreferenceWithInvalidQuoteAndDelimeterChars() {
		new CsvPreference.Builder('|', '|', "\n").build();
	}
	
	/**
	 * Tests construction with null end of line symbols (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullEolSymbols() {
		new CsvPreference.Builder('"', ',', null).build();
	}
}
