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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.supercsv.prefs.CsvPreference.EXCEL_PREFERENCE;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.comment.CommentMatches;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

public class TokenizerTest {
	
	private static final CsvPreference NORMAL_PREFERENCE = EXCEL_PREFERENCE;
	private static final CsvPreference SPACES_NEED_QUOTES_PREFERENCE = new CsvPreference.Builder(EXCEL_PREFERENCE)
		.surroundingSpacesNeedQuotes(true).build();
	private static final CsvPreference DONT_IGNORE_EMPTY_LINES_PREFERENCE = new CsvPreference.Builder(EXCEL_PREFERENCE)
		.ignoreEmptyLines(false).build();
	private static final CsvPreference PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE = new CsvPreference.Builder(EXCEL_PREFERENCE)
		.setEmptyColumnParsing(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString).build();
		
	private Tokenizer tokenizer;
	private List<String> columns;
	
	/**
	 * Sets up the columns List for the test.
	 * 
	 * @throws Exception
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
	 * Tests the constructor with a null Reader (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullReader() throws Exception {
		createTokenizer(null, NORMAL_PREFERENCE);
	}
	
	/**
	 * Tests the constructor with a null CsvPreference (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullPreferences() throws Exception {
		createTokenizer("", null);
	}
	
	/**
	 * Tests the readColumns() method with null List (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadColumnsWithNullList() throws Exception {
		tokenizer = createTokenizer("", NORMAL_PREFERENCE);
		tokenizer.readColumns(null);
	}
	
	/**
	 * Tests the getPreferences() method.
	 */
	@Test()
	public void testGetPreferences() throws Exception {
		tokenizer = createTokenizer("", NORMAL_PREFERENCE);
		CsvPreference prefs = tokenizer.getPreferences();
		assertEquals(NORMAL_PREFERENCE.getDelimiterChar(), prefs.getDelimiterChar());
		assertEquals(NORMAL_PREFERENCE.getEndOfLineSymbols(), prefs.getEndOfLineSymbols());
		assertEquals(NORMAL_PREFERENCE.getQuoteChar(), prefs.getQuoteChar());
		assertEquals(NORMAL_PREFERENCE.isSurroundingSpacesNeedQuotes(), prefs.isSurroundingSpacesNeedQuotes());
	}
	
	/**
	 * Tests the readColumns() method with no data.
	 */
	@Test
	public void testReadColumnsWithNoData() throws Exception {
		final String input = "";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.isEmpty());
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests that the readColumns() method skips over empty lines.
	 */
	@Test
	public void testEmptyLines() throws Exception {
		
		final String input = "\n\nthis is the third line\n";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertEquals("this is the third line", columns.get(0));
		assertEquals(3, tokenizer.getLineNumber());
		assertEquals("this is the third line", tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests that the readColumns() method doesn't skip over empty lines if the ignoreEmptyLines
	 * preference is disabled.
	 */
	@Test
	public void testEmptyLinesWithIgnoreEmptyLines() throws Exception {
		
		final String input = "\nthis is the second line\n\n";
		tokenizer = createTokenizer(input, DONT_IGNORE_EMPTY_LINES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertNull(columns.get(0));
		assertEquals(1, tokenizer.getLineNumber());
		assertEquals("", tokenizer.getUntokenizedRow());
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertEquals("this is the second line", columns.get(0));
		assertEquals(2, tokenizer.getLineNumber());
		assertEquals("this is the second line", tokenizer.getUntokenizedRow());
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertNull(columns.get(0));
		assertEquals(3, tokenizer.getLineNumber());
		assertEquals("", tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method a quoted section has text surrounding it. This is not technically valid CSV, but
	 * the tokenizer is lenient enough to allow it (it will just unescape the quoted section).
	 */
	@Test
	public void testQuotedFieldWithSurroundingText() throws Exception {
		
		final String input = "surrounding \"quoted\" text";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertEquals("surrounding quoted text", columns.get(0));
		assertEquals(1, tokenizer.getLineNumber());
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same result when surrounding spaces require quotes
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertEquals("surrounding quoted text", columns.get(0));
		assertEquals(1, tokenizer.getLineNumber());
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method when a quoted section with text after it. This is not technically valid CSV, but
	 * the tokenizer is lenient enough to allow it (it will just unescape the quoted section).
	 */
	@Test
	public void testQuotedFieldWithTextAfter() throws Exception {
		
		// illegal char after quoted section
		final String input = "\"quoted on 2 lines\nand afterward some\" text";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertEquals(1, columns.size());
		assertEquals("quoted on 2 lines\nand afterward some text", columns.get(0));
		assertEquals(2, tokenizer.getLineNumber());
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// should have exactly the same result when surrounding spaces need quotes
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertEquals(1, columns.size());
		assertEquals("quoted on 2 lines\nand afterward some text", columns.get(0));
		assertEquals(2, tokenizer.getLineNumber());
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a single quoted newline.
	 */
	@Test
	public void testQuotedNewline() throws Exception {
		
		final String input = "\"\n\"";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertEquals("\n", columns.get(0));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes (results should be identical)
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 1);
		assertEquals("\n", columns.get(0));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a variety of quoted newlines.
	 */
	@Test
	public void testQuotedNewlines() throws Exception {
		
		final String input = "\"one line\",\"two\nlines\",\"three\nlines\n!\"";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one line", columns.get(0));
		assertEquals("two\nlines", columns.get(1));
		assertEquals("three\nlines\n!", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes (results should be identical)
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one line", columns.get(0));
		assertEquals("two\nlines", columns.get(1));
		assertEquals("three\nlines\n!", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	
	/**
	 * Tests the readColumns() method when a quoted field has consecutive newlines.
	 */
	@Test
	public void testQuotedTextWithConsecutiveNewLines() throws Exception {
		
		// second field has consecutive newlines
		final String input = "one, \"multiline\n\n\ntext\"";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertEquals(2, columns.size());
		assertEquals("one", columns.get(0));
		assertEquals(" multiline\n\n\ntext", columns.get(1));
		assertEquals(4, tokenizer.getLineNumber());
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// should have exactly the same result when surrounding spaces need quotes
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("one", columns.get(0));
		assertEquals("multiline\n\n\ntext", columns.get(1));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method when EOF is reached within quote scope.
	 */
	@Test
	public void testQuotedFieldWithUnexpectedEOF() throws Exception {
		
		// EOF reached within quote scope
		final String input = "\"quoted spanning\ntwo lines with EOF reached before another quote";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		try {
			tokenizer.readColumns(columns);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("unexpected end of file while reading quoted column beginning on line 1 and ending on line 2",
				e.getMessage());
		}
	}

	/**
	 * Tests the readColumns() method when a newline is reached in quote
	 * scoped when a single line is only supposed to be read
	 */
	@Test
	public void testQuotedFieldWithUnexpectedNewline() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo\",\"bar\n" +
				"\"baz\",\"zoo\"\n" +
				"\"aaa\",\"bbb\"";
		CsvPreference pref = new CsvPreference.Builder(NORMAL_PREFERENCE)
				.maxLinesPerRow(1).build();

		tokenizer = createTokenizer(input, pref);
		try {
			boolean first = tokenizer.readColumns(columns);
			assertEquals(true , first);

			tokenizer.readColumns(columns);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("unexpected end of line while reading quoted column on line 2",
					e.getMessage());
		}
	}

	/**
	 * Tests the readColumns() method when a newline is reached in quote
	 * scoped when two lines are only supposed to be read
	 */
	@Test
	public void testQuotedFieldWithTwoMaxLines() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo\",\"bar\n" +
				"baz,zoo\n" +
				"aaa,bbb";
		CsvPreference pref = new CsvPreference.Builder(NORMAL_PREFERENCE)
				.maxLinesPerRow(2).build();

		tokenizer = createTokenizer(input, pref);
		try {
			boolean first = tokenizer.readColumns(columns);
			assertEquals(true , first);

			boolean second = tokenizer.readColumns(columns);
			assertEquals(true , second);

			tokenizer.readColumns(columns);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("max number of lines to read exceeded while reading quoted column beginning on line 2 and ending on line 3",
					e.getMessage());
		}
	}

	@Test
	public void testQuotedFieldWithUnexpectedNewlineNoNextLineRead() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo\",\"bar\n" +
				"\"baz\",\"zoo\"\n" +
				"\"aaa\",\"bbb\"";
		CsvPreference pref = new CsvPreference.Builder(NORMAL_PREFERENCE)
				.maxLinesPerRow(1).build();

		tokenizer = createTokenizer(input, pref);
		try {
			final boolean first = tokenizer.readColumns(columns);
			assertEquals(true , first);
			assertEquals("[col1, col2]" , columns.toString());

			tokenizer.readColumns(columns);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("unexpected end of line while reading quoted column on line 2",
					e.getMessage());
		}
		final boolean third = tokenizer.readColumns(columns);
		assertEquals(true , third);
		assertEquals("[baz, zoo]" , columns.toString());

		final boolean fourth = tokenizer.readColumns(columns);
		assertEquals(true , fourth);
		assertEquals("[aaa, bbb]" , columns.toString());

		//line 4 was the last 
		final boolean fifth = tokenizer.readColumns(columns);
		assertEquals(false , fifth);
	}

	/**
	 * Tests the readColumns() method when a newline is reached in quote
	 * scoped when two lines are only supposed to be read
	 */
	@Test
	public void testQuotedFieldWithTwoMaxLinesNoMoreLinesRead() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo,bar\n" +
				"baz,zoo\n" +
				"aaa,bbb";
		CsvPreference pref = new CsvPreference.Builder(NORMAL_PREFERENCE)
				.maxLinesPerRow(2).build();

		tokenizer = createTokenizer(input, pref);
		try {
			boolean first = tokenizer.readColumns(columns);
			assertEquals(true , first);
			assertEquals("[col1, col2]" , columns.toString());
			

			boolean second = tokenizer.readColumns(columns);
			assertEquals(true , second);
			assertEquals("[\"foo,bar]" , columns.toString());


			tokenizer.readColumns(columns);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("max number of lines to read exceeded while reading quoted column beginning on line 2 and ending on line 3",
					e.getMessage());
		}
		boolean fourth = tokenizer.readColumns(columns);
		assertEquals(true , fourth);
		assertEquals("[aaa, bbb]" , columns.toString());
		
	}

	/**
	 * Tests the readColumns() method with a leading space before the first quoted field. This is not technically valid
	 * CSV, but the tokenizer is lenient enough to allow it. The leading spaces will be trimmed off when surrounding
	 * spaces require quotes, otherwise they will be part of the field.
	 */
	@Test
	public void testQuotedFirstFieldWithLeadingSpace() throws Exception {
		
		// leading spaces should be preserved
		final String input = "  \"quoted with leading spaces\",two";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("  quoted with leading spaces", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes (leading spaces trimmed)
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("quoted with leading spaces", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a leading space before the last quoted field. This is not technically valid
	 * CSV, but the tokenizer is lenient enough to allow it. The leading spaces will be trimmed off when surrounding
	 * spaces require quotes, otherwise they will be part of the field.
	 */
	@Test
	public void testQuotedLastFieldWithLeadingSpace() throws Exception {
		
		// last field has a leading space before quote (should be preserved)
		final String input = "one,two,  \"quoted with leading spaces\"";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("  quoted with leading spaces", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// leading space should be trimmed off
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("quoted with leading spaces", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a trailing space after the first quoted field. This is not technically valid
	 * CSV, but the tokenizer is lenient enough to allow it. The trailing spaces will be trimmed off when surrounding
	 * spaces require quotes, otherwise they will be part of the field.
	 */
	@Test
	public void testQuotedFirstFieldWithTrailingSpace() throws Exception {
		
		// first field has a leading space before quote (should be preserved)
		final String input = "\"quoted with trailing spaces\"  ,two";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("quoted with trailing spaces  ", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// trailing spaces should be trimmed
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("quoted with trailing spaces", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a trailing space after the last quoted field. This is not technically valid
	 * CSV, but the tokenizer is lenient enough to allow it. The trailing spaces will be trimmed off when surrounding
	 * spaces require quotes, otherwise they will be part of the field.
	 */
	@Test
	public void testQuotedLastFieldWithTrailingSpace() throws Exception {
		
		// last field has a leading space before quote (should be preserved)
		final String input = "one,two,\"quoted with trailing spaces\"  ";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("quoted with trailing spaces  ", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// trailing spaces should be trimmed off
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("quoted with trailing spaces", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a variety of quoted spaces.
	 */
	@Test
	public void testQuotedSpaces() throws Exception {
		
		final String input = "\" one \",\"  two  \",\"   three   \"";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals(" one ", columns.get(0));
		assertEquals("  two  ", columns.get(1));
		assertEquals("   three   ", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes (results should be identical)
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals(" one ", columns.get(0));
		assertEquals("  two  ", columns.get(1));
		assertEquals("   three   ", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with a quote character escaped with another quote (RFC style)
	 */
	@Test
	public void testQuotedQuoteCharWithDoubleQuoteEscape() throws Exception {
		final String input = "one,\"two\",\"field with \"\" quote char\",four";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);

		assertTrue(columns.size() == 4);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("field with \" quote char", columns.get(2));
		assertEquals("four", columns.get(3));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}

	/**
	 * Tests the readColumns() method with a quote character escaped with another quote (RFC style)
	 */
	@Test
	public void testQuotedQuoteCharWithBackslashEscape() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(NORMAL_PREFERENCE)
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

		final CsvPreference csvPref = new CsvPreference.Builder(NORMAL_PREFERENCE)
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

		final CsvPreference csvPref = new CsvPreference.Builder(NORMAL_PREFERENCE)
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

		final CsvPreference csvPref = new CsvPreference.Builder(NORMAL_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"\\\\\\\\\\\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);
	}

	/**
	 * Tests the readColumns() method with an escape character preceding neither another escape
	 * char nor a quote char.  In this situation, just pass the data through rather than
	 * attempting to interpret the quote char.
	 */
	@Test
	public void testEscapedNonEscapeChar() throws Exception {

		final CsvPreference csvPref = new CsvPreference.Builder(NORMAL_PREFERENCE)
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

		final CsvPreference csvPref = new CsvPreference.Builder(NORMAL_PREFERENCE)
				.setQuoteEscapeChar('\\')
				.build();

		final String input = "\"field with an escaped quote \\\" and a \"\" double quote\"";
		tokenizer = createTokenizer(input, csvPref);
		tokenizer.readColumns(columns);

		Assert.fail();
	}
	
	/**
	 * Tests the readColumns() method with a variety of unquoted spaces.
	 */
	@Test
	public void testSpaces() throws Exception {
		
		final String input = " one ,  two  ,   three   ";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals(" one ", columns.get(0));
		assertEquals("  two  ", columns.get(1));
		assertEquals("   three   ", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one", columns.get(0));
		assertEquals("two", columns.get(1));
		assertEquals("three", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with a variety of spaces and tabs.
	 */
	@Test
	public void testSpacesAndTabs() throws Exception {
		
		// tabs should never be trimmed
		final String input = "\t, \tone\t ,  \ttwo\t  ,   \tthree\t   ";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 4);
		assertEquals("\t", columns.get(0));
		assertEquals(" \tone\t ", columns.get(1));
		assertEquals("  \ttwo\t  ", columns.get(2));
		assertEquals("   \tthree\t   ", columns.get(3));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 4);
		assertEquals("\t", columns.get(0));
		assertEquals("\tone\t", columns.get(1));
		assertEquals("\ttwo\t", columns.get(2));
		assertEquals("\tthree\t", columns.get(3));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests the readColumns() method with spaces between words.
	 */
	@Test
	public void testSpacesBetweenWords() throws Exception {
		
		final String input = " one partridge ,  two turtle doves  ,   three french hens   ";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals(" one partridge ", columns.get(0));
		assertEquals("  two turtle doves  ", columns.get(1));
		assertEquals("   three french hens   ", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
		
		// same input when surrounding spaces require quotes
		tokenizer = createTokenizer(input, SPACES_NEED_QUOTES_PREFERENCE);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("one partridge", columns.get(0));
		assertEquals("two turtle doves", columns.get(1));
		assertEquals("three french hens", columns.get(2));
		assertEquals(input, tokenizer.getUntokenizedRow());
	}
	
	/**
	 * Tests that the CommentStartsWith comment matcher works (comments are skipped).
	 */
	@Test
	public void testSkipCommentsStartsWith() throws IOException {
		
		final CsvPreference commentsStartWithPrefs = new CsvPreference.Builder(EXCEL_PREFERENCE).skipComments(
			new CommentStartsWith("#")).build();
		
		final String input = "#comment\nnot,a,comment\n# another comment\nalso,not,comment";
		final Tokenizer tokenizer = createTokenizer(input, commentsStartWithPrefs);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("not", columns.get(0));
		assertEquals("a", columns.get(1));
		assertEquals("comment", columns.get(2));
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("also", columns.get(0));
		assertEquals("not", columns.get(1));
		assertEquals("comment", columns.get(2));
		
		assertFalse(tokenizer.readColumns(columns));
	}
	
	/**
	 * Tests that the CommentMatches comment matcher works (comments are skipped).
	 */
	@Test
	public void testSkipCommentsMatches() throws IOException {
		
		final CsvPreference commentsMatchesPrefs = new CsvPreference.Builder(EXCEL_PREFERENCE).skipComments(
			new CommentMatches("<!--.*-->")).build();
		
		final String input = "<!--comment-->\nnot,a,comment\n<!-- another comment-->\nalso,not,comment";
		final Tokenizer tokenizer = createTokenizer(input, commentsMatchesPrefs);
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("not", columns.get(0));
		assertEquals("a", columns.get(1));
		assertEquals("comment", columns.get(2));
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 3);
		assertEquals("also", columns.get(0));
		assertEquals("not", columns.get(1));
		assertEquals("comment", columns.get(2));
		
		assertFalse(tokenizer.readColumns(columns));
		
	}
	
	/**
	 * Tests that the readColumns() method reads a null column as null when preferences is ParseEmptyColumnsAsNull.
	 */
	@Test
	public void testReadNullWithParseEmptyColumnsAsNull() throws Exception {
		
		final String input = ",";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		assertTrue(NORMAL_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsNull));
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals(",", tokenizer.getUntokenizedRow());
		assertEquals(null, columns.get(0));
		assertEquals(null, columns.get(1));
	}
	

	/**
	 * Tests that the readColumns() method reads an empty string column as null when preferences is ParseEmptyColumnsAsNull.
	 */
	@Test
	public void testReadEmptyStringsWithParseEmptyColumnsAsNull() throws Exception {
		
		final String input = "\"\",\"\"";
		tokenizer = createTokenizer(input, NORMAL_PREFERENCE);
		assertTrue(NORMAL_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsNull));
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("\"\",\"\"", tokenizer.getUntokenizedRow());
		assertEquals(null, columns.get(0));
		assertEquals(null, columns.get(1));
	}
	
	/**
	 * Tests that the readColumns() method reads a null column as null when preferences is ParseEmptyColumnsAsEmptyString.
	 */
	@Test
	public void testReadNullWithParseEmptyColumnsAsEmptyString() throws Exception {
		
		final String input = ",";
		tokenizer = createTokenizer(input, PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE);
		assertTrue(PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString));
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals(",", tokenizer.getUntokenizedRow());
		assertEquals(null, columns.get(0));
		assertEquals(null, columns.get(1));
	}
	
	/**
	 * Tests that the readColumns() method reads an empty string column as empty string when preferences is ParseEmptyColumnsAsEmptyString.
	 */
	@Test
	public void testReadEmptyStringsWithParseEmptyColumnsAsEmptyString() throws Exception {
		
		final String input = "\"\",\"\"";
		tokenizer = createTokenizer(input, PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE);
		assertTrue(PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString));
		
		tokenizer.readColumns(columns);
		assertTrue(columns.size() == 2);
		assertEquals("\"\",\"\"", tokenizer.getUntokenizedRow());
		assertEquals("", columns.get(0));
		assertEquals("", columns.get(1));
	}	
	
}
