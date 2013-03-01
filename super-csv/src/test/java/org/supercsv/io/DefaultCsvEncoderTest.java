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
package org.supercsv.io;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;
import org.supercsv.util.CsvContext;

/**
 * Tests the DefaultCsvEncoder class.
 * 
 * @author James Bassett
 */
public class DefaultCsvEncoderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	private static final CsvPreference SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS = new CsvPreference.Builder(
		CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build();
	private static final CsvPreference ALWAYS_QUOTE_PREFS = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
		.useQuoteMode(new AlwaysQuoteMode()).build();
	
	private DefaultCsvEncoder csvEncoder;
	
	/**
	 * Sets up the DefaultCsvEncoder.
	 */
	@Before
	public void setUp() {
		this.csvEncoder = new DefaultCsvEncoder();
	}
	
	/**
	 * Tests the encode() method with various preferences.
	 */
	@Test
	public void testEncode() {
		
		final CsvContext context = new CsvContext(1, 1, 1);
		
		final String empty = "";
		assertEquals("", csvEncoder.encode(empty, context, PREFS));
		assertEquals("", csvEncoder.encode(empty, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\"\"", csvEncoder.encode(empty, context, ALWAYS_QUOTE_PREFS));
		
		final String space = " ";
		assertEquals(" ", csvEncoder.encode(space, context, PREFS));
		assertEquals("\" \"", csvEncoder.encode(space, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\" \"", csvEncoder.encode(space, context, ALWAYS_QUOTE_PREFS));
		
		final String leadingSpace = " leading space";
		assertEquals(leadingSpace, csvEncoder.encode(leadingSpace, context, PREFS));
		assertEquals("\" leading space\"",
			csvEncoder.encode(leadingSpace, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\" leading space\"", csvEncoder.encode(leadingSpace, context, ALWAYS_QUOTE_PREFS));
		
		final String trailingSpace = "trailing space ";
		assertEquals(trailingSpace, csvEncoder.encode(trailingSpace, context, PREFS));
		assertEquals("\"trailing space \"",
			csvEncoder.encode(trailingSpace, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\"trailing space \"", csvEncoder.encode(trailingSpace, context, ALWAYS_QUOTE_PREFS));
		
		final String normal = "just a normal phrase";
		assertEquals(normal, csvEncoder.encode(normal, context, PREFS));
		assertEquals(normal, csvEncoder.encode(normal, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\"just a normal phrase\"", csvEncoder.encode(normal, context, ALWAYS_QUOTE_PREFS));
		
		final String comma = "oh look, a comma";
		assertEquals("\"oh look, a comma\"", csvEncoder.encode(comma, context, PREFS));
		assertEquals("\"oh look, a comma\"", csvEncoder.encode(comma, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\"oh look, a comma\"", csvEncoder.encode(comma, context, ALWAYS_QUOTE_PREFS));
		
		final String quoted = "\"Watch out for quotes\", he said";
		assertEquals("\"\"\"Watch out for quotes\"\", he said\"", csvEncoder.encode(quoted, context, PREFS));
		assertEquals("\"\"\"Watch out for quotes\"\", he said\"",
			csvEncoder.encode(quoted, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals("\"\"\"Watch out for quotes\"\", he said\"",
			csvEncoder.encode(quoted, context, ALWAYS_QUOTE_PREFS));
		
		final String twoLines = "text that spans\ntwo lines";
		int lineNumber = context.getLineNumber();
		assertEquals("\"text that spans\r\ntwo lines\"", csvEncoder.encode(twoLines, context, PREFS));
		assertEquals(++lineNumber, context.getLineNumber());
		assertEquals("\"text that spans\r\ntwo lines\"",
			csvEncoder.encode(twoLines, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals(++lineNumber, context.getLineNumber());
		assertEquals("\"text that spans\r\ntwo lines\"", csvEncoder.encode(twoLines, context, ALWAYS_QUOTE_PREFS));
		assertEquals(++lineNumber, context.getLineNumber());
		
		final String quotesTwoLines = "text \"with quotes\" that spans\ntwo lines";
		assertEquals("\"text \"\"with quotes\"\" that spans\r\ntwo lines\"",
			csvEncoder.encode(quotesTwoLines, context, PREFS));
		assertEquals(++lineNumber, context.getLineNumber());
		assertEquals("\"text \"\"with quotes\"\" that spans\r\ntwo lines\"",
			csvEncoder.encode(quotesTwoLines, context, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS));
		assertEquals(++lineNumber, context.getLineNumber());
		assertEquals("\"text \"\"with quotes\"\" that spans\r\ntwo lines\"",
			csvEncoder.encode(quotesTwoLines, context, ALWAYS_QUOTE_PREFS));
		assertEquals(++lineNumber, context.getLineNumber());
	}
	
}
