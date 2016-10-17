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
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.HEADER_CSV;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests AbstractCsvWriter.
 *
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class AbstractCsvWriterTest {

	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	private static final CsvPreference SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS = new CsvPreference.Builder(
		CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build();

	private Writer writer;

	private AbstractCsvWriter abstractWriter;
	private AbstractCsvWriter surroundingSpacesNeedQuotesAbstractWriter;

	/**
	 * Implementation of AbstractCsvWriter for testing.
	 */
	static class MockCsvWriter extends AbstractCsvWriter {

		private CsvPreference preference;

		public MockCsvWriter(Writer writer, CsvPreference preference) {
			super(writer, preference);
			this.preference = preference;
		}
	}

	/**
	 * Sets up the writer for the tests.
	 */
	@Before
	public void setUp() {
		writer = new StringWriter();
		abstractWriter = new MockCsvWriter(writer, PREFS);
		surroundingSpacesNeedQuotesAbstractWriter = new MockCsvWriter(writer, SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS);
	}

	/**
	 * Closes the writer after the test.
	 */
	@After
	public void tearDown() throws IOException {
		abstractWriter.close();
		surroundingSpacesNeedQuotesAbstractWriter.close();
	}

	/**
	 * Tests the writeHeader() method.
	 */
	@Test
	public void testWriteHeader() throws IOException {
		assertEquals(0, abstractWriter.getLineNumber());
		assertEquals(0, abstractWriter.getRowNumber());

		abstractWriter.writeHeader(HEADER);

		assertEquals(1, abstractWriter.getLineNumber());
		assertEquals(1, abstractWriter.getRowNumber());

		abstractWriter.flush();
		assertEquals(HEADER_CSV + "\r\n", writer.toString());
	}

	/**
	 * Tests the writeComment() method.
	 */
	@Test
	public void testWriteComment() throws IOException {
		assertEquals(0, abstractWriter.getLineNumber());
		assertEquals(0, abstractWriter.getRowNumber());

		final String comment = "#this is a comment";
		abstractWriter.writeComment(comment);
		assertEquals(1, abstractWriter.getLineNumber());
		assertEquals(0, abstractWriter.getRowNumber());

		final String header = "this,is,the,header";
		abstractWriter.writeHeader(header.split(","));
		assertEquals(2, abstractWriter.getLineNumber());
		assertEquals(1, abstractWriter.getRowNumber());

		abstractWriter.writeComment(comment);
		assertEquals(3, abstractWriter.getLineNumber());
		assertEquals(1, abstractWriter.getRowNumber());

		abstractWriter.writeHeader(header.split(","));
		assertEquals(4, abstractWriter.getLineNumber());
		assertEquals(2, abstractWriter.getRowNumber());

		abstractWriter.flush();
		final String eol = PREFS.getEndOfLineSymbols();
		final String expected = comment + eol + header + eol + comment + eol + header + eol;
		assertEquals(expected, writer.toString());
	}

	/**
	 * Tests that all 3 variations of line terminators embedded in CSV are handled correctly (are replaced with the end
	 * of line symbols and line number is incremented correctly). writeHeader() is used because it increments the line
	 * number just like the write() methods exposed on concretes writers.
	 *
	 * @param csvWriter
	 *            the CSV writer
	 * @param prefs
	 *            the preferences
	 * @throws java.io.IOException
	 */
	private void writeHeaderWithEmbeddedEndOfLineSymbols(final MockCsvWriter csvWriter) throws IOException {

		final String eolSymbols = csvWriter.preference.getEndOfLineSymbols();

		final String textWithNewline = "text that\nspans\nthree lines";
		final String textWithCarriageReturn = "text that\rspans\rthree lines";
		final String textWithCarriageAndNewline = "text that\r\nspans\r\nthree lines";

		final String expected = "\"text that" + eolSymbols + "spans" + eolSymbols + "three lines\"" + eolSymbols;

		// \n
		csvWriter.writeHeader(textWithNewline);
		csvWriter.flush();
		assertEquals(expected, writer.toString());
		assertEquals(3, csvWriter.getLineNumber());
		assertEquals(1, csvWriter.getRowNumber());

		// \r
		csvWriter.writeHeader(textWithCarriageReturn);
		csvWriter.flush();
		assertEquals(expected + expected, writer.toString());
		assertEquals(6, csvWriter.getLineNumber());
		assertEquals(2, csvWriter.getRowNumber());

		// \r\n
		csvWriter.writeHeader(textWithCarriageAndNewline);
		csvWriter.flush();
		assertEquals(expected + expected + expected, writer.toString());
		assertEquals(9, csvWriter.getLineNumber());
		assertEquals(3, csvWriter.getRowNumber());

		// \r\n\n (checks that skipNewline only skips 1 newline)
		csvWriter.writeHeader("\r\n\n");
		csvWriter.flush();
		assertEquals(expected + expected + expected + "\"" + eolSymbols + eolSymbols + "\"" + eolSymbols,
			writer.toString());
		assertEquals(12, csvWriter.getLineNumber());
		assertEquals(4, csvWriter.getRowNumber());

		csvWriter.close();
	}

	/**
	 * Tests the writeHeader() method with an embedded carriage return and newline (i.e. Windows).
	 */
	@Test
	public void testWriteHeaderWithCarriageReturnNewline() throws IOException {
		writeHeaderWithEmbeddedEndOfLineSymbols(new MockCsvWriter(writer, CsvPreference.STANDARD_PREFERENCE));
	}

	/**
	 * Tests the writeHeader() method with an embedded newline (i.e. Linux).
	 */
	@Test
	public void testWriteHeaderWithNewline() throws IOException {
		writeHeaderWithEmbeddedEndOfLineSymbols(new MockCsvWriter(writer, CsvPreference.EXCEL_PREFERENCE));
	}

	/**
	 * Tests the writeHeader() method with an embedded carriage return (i.e. Mac).
	 */
	@Test
	public void testWriteHeaderWithCarriageReturn() throws IOException {
		writeHeaderWithEmbeddedEndOfLineSymbols(new MockCsvWriter(writer,
			new CsvPreference.Builder('"', ",", "\r").build()));
	}

	/**
	 * Tests the writeComment() method with a null String.
	 */
	@Test(expected = NullPointerException.class)
	public void writeCommentWithNull() throws IOException {
		abstractWriter.writeComment(null);
	}

	/**
	 * Tests the writeHeader() method with a null array.
	 */
	@Test(expected = NullPointerException.class)
	public void writeHeaderWithNull() throws IOException {
		abstractWriter.writeHeader((String[]) null);
	}

	/**
	 * Tests the writeHeader() method with an empty array.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void writeHeaderWithEmptyArray() throws IOException {
		abstractWriter.writeHeader(new String[] {});
	}

	/**
	 * Tests the constructor with a null writer.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullWriter() {
		new MockCsvWriter(null, PREFS);
	}

	/**
	 * Tests the constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullPreferences() {
		new MockCsvWriter(writer, null);
	}
}
