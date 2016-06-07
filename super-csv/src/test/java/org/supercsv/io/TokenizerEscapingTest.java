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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Section of TokenizerTest that deals solely with escaping.
 */
public class TokenizerEscapingTest {

	private Tokenizer tokenizer;
	private List<String> columns;

	/**
	 * Sets up the columns List for the test.
	 */
	@Before
	public void setUp() {
		columns = new ArrayList<String>();
	}

	/**
	 * Tidies up after the test.
	 */
	@After
	public void tearDown() throws IOException {
		if( tokenizer != null ) {
			tokenizer.close();
		}
	}

	/**
	 * Creates a Tokenizer with the input and preferences.
	 *
	 * @param input
	 *            the input String
	 * @param preference
	 *            the preferences
	 * @return the Tokenizer
	 */
	private static Tokenizer createTokenizer(String input, CsvPreference preference) {
		final Reader r = input != null ? new StringReader(input) : null;
		return new Tokenizer(r, preference);
	}

	/**
	 * Tests the readColumns() method with a quote character escaped with another quote (RFC style)
	 */
	@Test
	public void testQuotedQuoteCharWithDoubleQuoteEscape() throws Exception {
		final String input = "one,\"two\",\"field with \"\" quote char\",four";
		tokenizer = createTokenizer(input, CsvPreference.STANDARD_PREFERENCE);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 4);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("field with \" quote char", columns.get(2));
		assertEquals("four", columns.get(3));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with a quote character escaped with a backslash (non-RFC
	 * style)
	 */
	@Test
	public void testQuotedQuoteCharWithBackslashEscape() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "one,two,\"field with \\\" quote char\",four";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 4);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("field with \" quote char", columns.get(2));
		assertEquals("four", columns.get(3));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with an escape character following another escape char.
	 */
	@Test
	public void testEscapedEscapeChar() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"field with \\\\ escape char\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 1);
		assertEquals("field with \\ escape char", columns.get(0));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with an even number of escape characters
	 * char.
	 */
	@Test
	public void testEvenSeriesOfEscapeChars() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"\\\\\\\\\\\\\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 1);
		assertEquals("\\\\\\", columns.get(0));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with an odd number of escape characters
	 * char.
	 */
	@Test(expected = SuperCsvException.class)
	public void testOddSeriesOfEscapeChars() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"\\\\\\\\\\\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);
	}

	/**
	 * Tests the readColumns() method with a doubled escape char in an unquoted field, which
	 * should show as two literal escape chars.
	 *
	 * Doubled escape chars only evaluate to a single escape char when they are in a quoted field.
	 */
	@Test
	public void testDoubledEscapeCharInUnquotedFieldIsLiteral() throws Exception {
		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\\\\";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 1);
		assertEquals("\\\\", columns.get(0));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}


	/**
	 * Tests the readColumns() method with a lone escape char outside of quoted fields
	 */
	@Test
	public void testLoneEscapeCharInUnquotedField() throws Exception {
		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "one\\,\\two,thr\\ee";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 3);
		assertEquals("one\\", columns.get(0));
		assertEquals("\\two", columns.get(1));
		assertEquals("thr\\ee", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with a doubled escape char outside of quoted fields
	 */
	@Test
	public void testDoubledEscapeCharInUnquotedField() throws Exception {
		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "one\\\\,\\\\two,thr\\\\ee";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 3);
		assertEquals("one\\\\", columns.get(0));
		assertEquals("\\\\two", columns.get(1));
		assertEquals("thr\\\\ee", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with an escape character preceding neither another escape
	 * char nor a quote char.  In this situation, just pass the data through rather than
	 * attempting to interpret the quote char.
	 */
	@Test
	public void testEscapedNonEscapeChar() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"field with \\an escape char on neither escape nor quote\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 1);
		assertEquals("field with \\an escape char on neither escape nor quote", columns.get(0));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Test double-quote char when in backslash-escape mode should throw exception
	 */
	@Test(expected = SuperCsvException.class)
	public void testDoubleQuoteBackslashEscapeChar() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"field with an escaped quote \\\" and a \"\" double quote\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		Assert.fail();
	}


}
