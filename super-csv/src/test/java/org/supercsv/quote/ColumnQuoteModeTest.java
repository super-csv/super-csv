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
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Tests the ColumnQuoteMode class.
 * 
 * @author James Bassett
 */
public class ColumnQuoteModeTest {
	
	/**
	 * Tests the quotesRequired() method with a boolean array.
	 */
	@Test
	public void testQuotesRequiredWithBooleanArray() {
		testQuotesRequired(new ColumnQuoteMode(new boolean[] { true, false, false, true }));
	}
	
	/**
	 * Tests the quotesRequired() method with an int array.
	 */
	@Test
	public void testQuotesRequiredWithIntArray() {
		testQuotesRequired(new ColumnQuoteMode(1, 4));
	}
	
	/**
	 * Tests the quotesRequired() method with the supplied quote mode.
	 * 
	 * @param quoteMode
	 *            the quote mode to use
	 */
	private void testQuotesRequired(final QuoteMode quoteMode) {
		final String input = "input";
		final CsvPreference prefs = CsvPreference.STANDARD_PREFERENCE;
		
		assertTrue(quoteMode.quotesRequired(input, new CsvContext(1, 1, 1), prefs));
		assertFalse(quoteMode.quotesRequired(input, new CsvContext(1, 1, 2), prefs));
		assertFalse(quoteMode.quotesRequired(input, new CsvContext(1, 1, 3), prefs));
		assertTrue(quoteMode.quotesRequired(input, new CsvContext(1, 1, 4), prefs));
	}
	
	/**
	 * Tests construction with a null boolean array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullBooleanArray() {
		new ColumnQuoteMode((boolean[]) null);
	}
	
	/**
	 * Tests construction with a null int array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullIntArray() {
		new ColumnQuoteMode((int[]) null);
	}
	
}
