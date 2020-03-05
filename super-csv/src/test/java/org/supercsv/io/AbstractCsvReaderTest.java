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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.comment.CommentMatches;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests AbstractCsvReader.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class AbstractCsvReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private static final CsvPreference SURROUNDING_SPACES_NEED_QUOTES_PREFS = new CsvPreference.Builder(
		CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build();
	
	private Reader reader;
	
	private Reader surroundingSpacesNeedQuotesReader;
	
	private AbstractCsvReader abstractReader;
	
	private AbstractCsvReader surroundingSpacesNeedQuotesAbstractReader;
	
	/**
	 * Implementation of AbstractCsvReader for testing.
	 */
	private static class MockCsvReader extends AbstractCsvReader {
		
		public MockCsvReader(Reader reader, CsvPreference preferences) {
			super(reader, preferences);
		}
		
	}
	
	/**
	 * Sets up the reader for the tests.
	 */
	@Before
	public void setUp() {
		reader = new StringReader("firstName,lastName,age,address\n" + "John,Smith,23,\n"
			+ "Harry,Potter,,\"Gryffindor\nHogwarts Castle\nUK\"");
		abstractReader = new MockCsvReader(reader, PREFS);
		
		surroundingSpacesNeedQuotesReader = new StringReader("firstName, lastName, age, address\n"
			+ " John , Smith, 23 , \n" + "Harry, Potter, , \"Gryffindor\nHogwarts Castle\nUK\" ");
		surroundingSpacesNeedQuotesAbstractReader = new MockCsvReader(surroundingSpacesNeedQuotesReader,
			SURROUNDING_SPACES_NEED_QUOTES_PREFS);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		abstractReader.close();
		surroundingSpacesNeedQuotesAbstractReader.close();
	}
	
	/**
	 * Tests a normal reading scenario, asserting all of the properties available each time.
	 */
	@Test
	public void testReadingWithNormalReader() throws IOException {
		assertReading(abstractReader);
	}
	
	/**
	 * Reusable method to test a normal reading scenario, asserting all of the properties are available each time.
	 */
	private void assertReading(final AbstractCsvReader csvReader) throws IOException {
		
		assertEquals(PREFS, csvReader.getPreferences());
		
		assertEquals(0, csvReader.getLineNumber());
		assertEquals(0, csvReader.getRowNumber());
		assertEquals("", csvReader.getUndecodedRow());
		assertEquals(0, csvReader.length());
		
		// read the header
		final String[] header = csvReader.getHeader(true);
		assertEquals(4, header.length);
		assertEquals("firstName", header[0]);
		assertEquals("lastName", header[1]);
		assertEquals("age", header[2]);
		assertEquals("address", header[3]);
		
		assertEquals(1, csvReader.getLineNumber());
		assertEquals(1, csvReader.getRowNumber());
		assertEquals("firstName,lastName,age,address", csvReader.getUndecodedRow());
		assertEquals(4, csvReader.length());
		
		// read the first data row
		assertTrue(csvReader.readRow());
		List<String> line = csvReader.getColumns(); // John,Smith,23,\"1 Sesame St\nNew York\"
		assertEquals(4, csvReader.length());
		assertEquals("John", line.get(0));
		assertEquals("Smith", line.get(1));
		assertEquals("23", line.get(2));
		assertNull(line.get(3));
		
		// get() should return the same values as the List from read()
		assertTrue(Arrays.equals(line.toArray(), new Object[] { csvReader.get(1), csvReader.get(2), csvReader.get(3),
			csvReader.get(4) }));
		
		assertEquals(2, csvReader.getLineNumber());
		assertEquals(2, csvReader.getRowNumber());
		assertEquals("John,Smith,23,", csvReader.getUndecodedRow());
		assertEquals(4, csvReader.length());
		
		// read the second data row
		assertTrue(csvReader.readRow());
		line = csvReader.getColumns(); // Harry,Potter,13,\"Gryffindor\nHogwarts Castle\nUK\"
		assertEquals(4, csvReader.length());
		assertEquals("Harry", line.get(0));
		assertEquals("Potter", line.get(1));
		assertNull(line.get(2));
		assertEquals("Gryffindor\nHogwarts Castle\nUK", line.get(3));
		
		// get() should return the same values as the List from read()
		assertTrue(Arrays.equals(line.toArray(), new Object[] { csvReader.get(1), csvReader.get(2), csvReader.get(3),
			csvReader.get(4) }));
		
		assertEquals(5, csvReader.getLineNumber()); // 2 newlines in harry's address
		assertEquals(3, csvReader.getRowNumber());
		assertEquals("Harry,Potter,,\"Gryffindor\nHogwarts Castle\nUK\"", csvReader.getUndecodedRow());
		assertEquals(4, csvReader.length());
		
		// read again (should be EOF)
		assertFalse(csvReader.readRow());
		assertEquals(5, csvReader.getLineNumber());
		assertEquals(3, csvReader.getRowNumber());
		assertEquals("", csvReader.getUndecodedRow());
		assertEquals(0, csvReader.length());
		
	}
	
	/**
	 * Tests a normal reading scenario (with surroundingSpacesNeedQuotes enabled), asserting all of the properties available
	 * each time.
	 */
	@Test
	public void testReadingWithSurroundingSpacesNeedQuotesReader() throws IOException {
		assertReadingWithSurroundingSpacesNeedQuotesEnabled(surroundingSpacesNeedQuotesAbstractReader);
	}
	
	/**
	 * Reusable method to test a reading scenario (with surroundingSpacesNeedQuotes enabled), asserting all of the
	 * properties are available each time.
	 */
	private void assertReadingWithSurroundingSpacesNeedQuotesEnabled(final AbstractCsvReader csvReader) throws IOException {
		
		assertEquals(SURROUNDING_SPACES_NEED_QUOTES_PREFS, csvReader.getPreferences());
		
		assertEquals(0, csvReader.getLineNumber());
		assertEquals(0, csvReader.getRowNumber());
		assertEquals("", csvReader.getUndecodedRow());
		assertEquals(0, csvReader.length());
		
		// read the header
		final String[] header = csvReader.getHeader(true);
		assertEquals(4, header.length);
		assertEquals("firstName", header[0]);
		assertEquals("lastName", header[1]);
		assertEquals("age", header[2]);
		assertEquals("address", header[3]);
		
		assertEquals(1, csvReader.getLineNumber());
		assertEquals(1, csvReader.getRowNumber());
		assertEquals("firstName, lastName, age, address", csvReader.getUndecodedRow());
		assertEquals(4, csvReader.length());
		
		// read the first data row
		assertTrue(csvReader.readRow());
		List<String> line = csvReader.getColumns(); // John , Smith, 23 , \"1 Sesame St\nNew York\"
		assertEquals(4, csvReader.length());
		assertEquals("John", line.get(0));
		assertEquals("Smith", line.get(1));
		assertEquals("23", line.get(2));
		assertNull(line.get(3));
		
		// get() should return the same values as the List from read()
		assertTrue(Arrays.equals(line.toArray(), new Object[] { csvReader.get(1), csvReader.get(2), csvReader.get(3),
			csvReader.get(4) }));
		
		assertEquals(2, csvReader.getLineNumber());
		assertEquals(2, csvReader.getRowNumber());
		assertEquals(" John , Smith, 23 , ", csvReader.getUndecodedRow());
		assertEquals(4, csvReader.length());
		
		// read the second data row
		assertTrue(csvReader.readRow());
		line = csvReader.getColumns(); // Harry, Potter, 13, \"Gryffindor\nHogwarts Castle\nUK\"
		assertEquals(4, csvReader.length());
		assertEquals("Harry", line.get(0));
		assertEquals("Potter", line.get(1));
		assertNull(line.get(2));
		assertEquals("Gryffindor\nHogwarts Castle\nUK", line.get(3));
		
		// get() should return the same values as the List from read()
		assertTrue(Arrays.equals(line.toArray(), new Object[] { csvReader.get(1), csvReader.get(2), csvReader.get(3),
			csvReader.get(4) }));
		
		assertEquals(5, csvReader.getLineNumber()); // 2 newlines in harry's address
		assertEquals(3, csvReader.getRowNumber());
		assertEquals("Harry, Potter, , \"Gryffindor\nHogwarts Castle\nUK\" ", csvReader.getUndecodedRow());
		assertEquals(4, csvReader.length());
		
		// read again (should be EOF)
		assertFalse(csvReader.readRow());
		assertEquals(5, csvReader.getLineNumber());
		assertEquals(3, csvReader.getRowNumber());
		assertEquals("", csvReader.getUndecodedRow());
		assertEquals(0, csvReader.length());
		
	}
	
	/**
	 * Tests the getCsvHeader(true) can't be called when a line has already been read.
	 */
	@Test
	public void testIllegalGetHeader() throws IOException {
		
		abstractReader.getHeader(true);
		
		try {
			abstractReader.getHeader(true);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("CSV header must be fetched as the first read operation, but 1 lines have already been read",
				e.getMessage());
		}
		
	}
	
	/**
	 * Tests getCsvHeader(false).
	 */
	@Test
	public void testGetHeaderNoCheck() throws IOException {
		assertEquals(4, abstractReader.getHeader(false).length);
		assertEquals(4, abstractReader.getHeader(false).length);
		assertEquals(4, abstractReader.getHeader(false).length);
		assertNull(abstractReader.getHeader(false)); // should be EOF
	}
	
	/**
	 * Tests the Reader constructor with a null Reader.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testReaderConstructorWithNullReader() {
		new CsvListReader((Reader) null, PREFS);
	}
	
	/**
	 * Tests the Reader constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testReaderConstructorWithNullPreferences() {
		new CsvListReader(reader, null);
	}

	/**
	 * Tests the readRow() method when a newline is reached in quote
	 * scoped when a single line is only supposed to be read
	 */
	@Test
	public void testQuotedFieldWithUnexpectedNewline() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo\",\"bar\n" +
				"\"baz\",\"zoo\"\n" +
				"\"aaa\",\"bbb\"";
		CsvPreference pref = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
				.maxLinesPerRow(1).build();

		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), pref);
		try {
			boolean first = reader.readRow();
			assertEquals(true , first);

			reader.readRow();
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("unexpected end of line while reading quoted column on line 2",
					e.getMessage());
		}
		reader.close();
	}

	@Test
	public void testQuotedFieldWithUnexpectedNewlineNoNextLineRead() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo\",\"bar\n" +
				"\"baz\",\"zoo\"\n" +
				"\"aaa\",\"bbb\"";
		CsvPreference pref = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
				.maxLinesPerRow(1).build();

		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), pref);

		try {
			final boolean first = reader.readRow();
			assertEquals(true , first);
			assertEquals("[col1, col2]" , reader.getColumns().toString());

			reader.readRow();
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("unexpected end of line while reading quoted column on line 2",
					e.getMessage());
		}
		final boolean third = reader.readRow();
		assertEquals(true , third);
		assertEquals("[baz, zoo]" , reader.getColumns().toString());

		final boolean fourth = reader.readRow();
		assertEquals(true , fourth);
		assertEquals("[aaa, bbb]" , reader.getColumns().toString());

		//line 4 was the last
		final boolean fifth = reader.readRow();
		assertEquals(false , fifth);

		reader.close();
	}

	/**
	 * Tests the readRow() method when a newline is reached in quote
	 * scoped when two lines are only supposed to be read
	 */
	@Test
	public void testQuotedFieldWithTwoMaxLines() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo\",\"bar\n" +
				"baz,zoo\n" +
				"aaa,bbb";
		CsvPreference pref = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
				.maxLinesPerRow(2).build();

		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), pref);
		try {
			boolean first = reader.readRow();
			assertEquals(true , first);

			boolean second = reader.readRow();
			assertEquals(true , second);

			reader.readRow();
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("max number of lines to read exceeded while reading quoted column beginning on line 2 and ending on line 3",
					e.getMessage());
		}
		reader.close();
	}

	/**
	 * Tests the readRow() method when a newline is reached in quote
	 * scoped when two lines are only supposed to be read
	 */
	@Test
	public void testQuotedFieldWithTwoMaxLinesNoMoreLinesRead() throws Exception {

		// Row 2 has a missing trailing quote
		final String input = "col1,col2\n" +
				"\"foo,bar\n" +
				"baz,zoo\n" +
				"aaa,bbb";
		CsvPreference pref = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
				.maxLinesPerRow(2).build();

		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), pref);
		try {
			boolean first = reader.readRow();
			assertEquals(true , first);
			assertEquals("[col1, col2]" , reader.getColumns().toString());


			boolean second = reader.readRow();
			assertEquals(true , second);
			assertEquals("[\"foo,bar]" , reader.getColumns().toString());


			reader.readRow();
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("max number of lines to read exceeded while reading quoted column beginning on line 2 and ending on line 3",
					e.getMessage());
		}
		boolean fourth = reader.readRow();
		assertEquals(true , fourth);
		assertEquals("[aaa, bbb]" , reader.getColumns().toString());

		reader.close();
	}

	/**
	 * Tests that the CommentMatches comment matcher works (comments are skipped).
	 */
	@Test
	public void testSkipCommentsMatches() throws IOException {

		final CsvPreference commentsMatchesPrefs = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE
		).skipComments(new CommentMatches("<!--.*-->")).build();

		final String input = "<!--comment-->\nnot,a,comment\n<!-- another comment-->\nalso,not,comment";
		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), commentsMatchesPrefs);

		reader.readRow();
		assertTrue(reader.getColumns().size() == 3);
		assertEquals("not", reader.getColumns().get(0));
		assertEquals("a", reader.getColumns().get(1));
		assertEquals("comment", reader.getColumns().get(2));

		reader.readRow();
		assertTrue(reader.getColumns().size() == 3);
		assertEquals("also", reader.getColumns().get(0));
		assertEquals("not", reader.getColumns().get(1));
		assertEquals("comment", reader.getColumns().get(2));

		assertFalse(reader.readRow());

		reader.close();
	}


	/**
	 * Tests that the CommentStartsWith comment matcher works (comments are skipped).
	 */
	@Test
	public void testSkipCommentsStartsWith() throws IOException {

		final CsvPreference commentsStartWithPrefs = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).skipComments(
				new CommentStartsWith("#")).build();

		final String input = "#comment\nnot,a,comment\n# another comment\nalso,not,comment";
		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), commentsStartWithPrefs);
		reader.readRow();
		assertTrue(reader.getColumns().size() == 3);
		assertEquals("not", reader.getColumns().get(0));
		assertEquals("a", reader.getColumns().get(1));
		assertEquals("comment", reader.getColumns().get(2));

		reader.readRow();
		assertTrue(reader.getColumns().size() == 3);
		assertEquals("also", reader.getColumns().get(0));
		assertEquals("not", reader.getColumns().get(1));
		assertEquals("comment", reader.getColumns().get(2));

		assertFalse(reader.readRow());

		reader.close();
	}

	/**
	 * Tests that the readRow() method doesn't skip over empty lines if the ignoreEmptyLines
	 * preference is disabled.
	 */
	@Test
	public void testEmptyLinesWithIgnoreEmptyLines() throws Exception {

		final CsvPreference notIgnoreEmptyLinesPrefs = new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
				.ignoreEmptyLines(false).build();

		final String input = "\nthis is the second line\n\n";
		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), notIgnoreEmptyLinesPrefs);

		reader.readRow();
		assertTrue(reader.getColumns().size() == 1);
		assertNull(reader.getColumns().get(0));
		assertEquals(1, reader.getLineNumber());
		assertEquals("", reader.getUndecodedRow());

		reader.readRow();
		assertTrue(reader.getColumns().size() == 1);
		assertEquals("this is the second line", reader.getColumns().get(0));
		assertEquals(2, reader.getLineNumber());
		assertEquals("this is the second line", reader.getUndecodedRow());

		reader.readRow();
		assertTrue(reader.getColumns().size() == 1);
		assertNull(reader.getColumns().get(0));
		assertEquals(3, reader.getLineNumber());
		assertEquals("", reader.getUndecodedRow());

		reader.close();
	}

	/**
	 * Tests the readRow() method when EOF is reached within quote scope.
	 */
	@Test
	public void testQuotedFieldWithUnexpectedEOF() throws Exception {

		// EOF reached within quote scope
		final String input = "\"quoted spanning\ntwo lines with EOF reached before another quote";
		AbstractCsvReader reader = new MockCsvReader(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		try {
			reader.readRow();
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCsvException e) {
			assertEquals("unexpected end of file while reading quoted column beginning on line 1 and ending on line 2",
					e.getMessage());
		}
		reader.close();
	}

	/**
     * Tests that the readRow() method skips over empty lines.
     */
    @Test
    public void testEmptyLines() throws Exception {
        final String input = "\n\nthis is the third line\n";
        AbstractCsvReader reader = new MockCsvReader(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
        reader.readRow();
        assertTrue(reader.getColumns().size() == 1);
        assertEquals("this is the third line", reader.getColumns().get(0));
        assertEquals(3, reader.getLineNumber());
        assertEquals("this is the third line", reader.getUndecodedRow());
        reader.close();
    }

    /**
     * Tests the readRow() method with an odd number of escape characters
     * char.
     */
    @Test(expected = SuperCsvException.class)
    public void testOddSeriesOfEscapeChars() throws Exception {

        final CsvPreference csvPref = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
                .setQuoteEscapeChar('#')
                .build();

        final String input = "\"#####\"";
        abstractReader = new MockCsvReader(new StringReader(input), csvPref);
        abstractReader.readRow();
    }

    /**
     * Test double-quote char when in backslash-escape mode should throw exception
     */
    @Test(expected = SuperCsvException.class)
    public void testDoubleQuoteBackslashEscapeChar() throws Exception {

        // quote char is ' and escape char is $
        final CsvPreference csvPref = new CsvPreference.Builder('\'', ',', "\n")
                .setQuoteEscapeChar('$')
                .build();

        final String input = "'field with an escaped quote #' and a '' double quote'";
        abstractReader = new MockCsvReader(new StringReader(input), csvPref);
        abstractReader.readRow();

        Assert.fail();
    }
}
