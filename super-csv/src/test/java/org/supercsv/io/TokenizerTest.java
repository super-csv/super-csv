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
	
}
