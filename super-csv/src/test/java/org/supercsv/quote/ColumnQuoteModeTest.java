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
package org.supercsv.quote;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Tests the ColumnQuoteMode class.
 * 
 * @author James Bassett
 */
public class ColumnQuoteModeTest {
	
	/**
	 * Tests the quotesRequired() method.
	 */
	@Test
	public void testQuotesRequired() {
		final String input = "input";
		final CsvPreference prefs = CsvPreference.STANDARD_PREFERENCE;
		
		final QuoteMode quoteMode = new ColumnQuoteMode(true, false, false, true);
		assertTrue(quoteMode.quotesRequired(input, new CsvContext(1, 1, 1), prefs));
		assertFalse(quoteMode.quotesRequired(input, new CsvContext(1, 1, 2), prefs));
		assertFalse(quoteMode.quotesRequired(input, new CsvContext(1, 1, 3), prefs));
		assertTrue(quoteMode.quotesRequired(input, new CsvContext(1, 1, 4), prefs));
	}
	
	/**
	 * Tests the quotesRequired() method when the boolean array isn't the right size.
	 */
	@Test(expected = SuperCsvException.class)
	public void testQuotesRequiredWithInvalidBooleanArray() {
		final String input = "input";
		final CsvPreference prefs = CsvPreference.STANDARD_PREFERENCE;
		
		final QuoteMode quoteMode = new ColumnQuoteMode(new boolean[] {});
		quoteMode.quotesRequired(input, new CsvContext(1, 1, 1), prefs);
	}
	
	/**
	 * Tests construction with a null array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullArray() {
		new ColumnQuoteMode(null);
	}
	
}
