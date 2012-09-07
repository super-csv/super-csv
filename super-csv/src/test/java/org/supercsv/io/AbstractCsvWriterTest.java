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
	private static final CsvPreference SURROUNDING_SPACES_REQUIRE_QUOTES_PREFS = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build();
	
	private Writer writer;
	
	private AbstractCsvWriter abstractWriter;
	private AbstractCsvWriter surroundingSpacesNeedQuotesAbstractWriter;
	
	/**
	 * Implementation of AbstractCsvWriter for testing.
	 */
	static class MockCsvWriter extends AbstractCsvWriter {
		
		public MockCsvWriter(Writer writer, CsvPreference preference) {
			super(writer, preference);
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
	 * Tests the escapeString() method with and without surroundingSpacesNeedQuotes enabled.
	 */
	@Test
	public void testEscapeString(){
		
		assertEquals("", abstractWriter.escapeString(""));
		assertEquals("", surroundingSpacesNeedQuotesAbstractWriter.escapeString(""));
		
		assertEquals(" ", abstractWriter.escapeString(" "));
		assertEquals("\" \"", surroundingSpacesNeedQuotesAbstractWriter.escapeString(" "));
		
		assertEquals(" leading space", abstractWriter.escapeString(" leading space"));
		assertEquals("\" leading space\"", surroundingSpacesNeedQuotesAbstractWriter.escapeString(" leading space"));
		
		assertEquals("trailing space ", abstractWriter.escapeString("trailing space "));
		assertEquals("\"trailing space \"", surroundingSpacesNeedQuotesAbstractWriter.escapeString("trailing space "));
		
		assertEquals("just a normal phrase", abstractWriter.escapeString("just a normal phrase"));
		assertEquals("just a normal phrase", surroundingSpacesNeedQuotesAbstractWriter.escapeString("just a normal phrase"));
		
		assertEquals("\"oh look, a comma\"", abstractWriter.escapeString("oh look, a comma"));
		assertEquals("\"oh look, a comma\"", surroundingSpacesNeedQuotesAbstractWriter.escapeString("oh look, a comma"));
		
		assertEquals("\"\"\"Watch out for quotes\"\", he said\"", abstractWriter.escapeString("\"Watch out for quotes\", he said"));
		assertEquals("\"\"\"Watch out for quotes\"\", he said\"", surroundingSpacesNeedQuotesAbstractWriter.escapeString("\"Watch out for quotes\", he said"));
		
		assertEquals("\"text that spans\r\ntwo lines\"", abstractWriter.escapeString("text that spans\ntwo lines"));
		assertEquals("\"text that spans\r\ntwo lines\"", surroundingSpacesNeedQuotesAbstractWriter.escapeString("text that spans\ntwo lines"));
		
		assertEquals("\"text \"\"with quotes\"\" that spans\r\ntwo lines\"", abstractWriter.escapeString("text \"with quotes\" that spans\ntwo lines"));
		assertEquals("\"text \"\"with quotes\"\" that spans\r\ntwo lines\"", surroundingSpacesNeedQuotesAbstractWriter.escapeString("text \"with quotes\" that spans\ntwo lines"));
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
