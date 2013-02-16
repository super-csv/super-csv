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

import static org.junit.Assert.*;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the NormalQuoteMode class.
 * 
 * @author James Bassett
 */
public class NormalQuoteModeTest {
	
	/**
	 * Tests that the quotesRequired() method always returns false.
	 */
	@Test
	public void testQuotesRequired() {
		final QuoteMode quoteMode = new NormalQuoteMode();
		assertFalse(quoteMode.quotesRequired("input", ANONYMOUS_CSVCONTEXT, CsvPreference.STANDARD_PREFERENCE));
		assertFalse(quoteMode.quotesRequired(null, null, null));
	}
	
}
