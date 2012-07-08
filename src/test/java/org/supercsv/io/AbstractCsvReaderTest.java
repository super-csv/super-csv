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
import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests AbstractCsvReader.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class AbstractCsvReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private static final CsvPreference TRIM_PREFS = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
		.trimMode(true).build();
	
	private Reader reader;
	
	private Reader trimModeReader;
	
	private AbstractCsvReader abstractReader;
	
	private AbstractCsvReader tokenizerAbstractReader;
	
	private AbstractCsvReader trimModeAbstractReader;
	
	private ITokenizer tokenizer;
	
	/**
	 * Implementation of AbstractCsvReader for testing.
	 */
	private static class MockCsvReader extends AbstractCsvReader {
		
		public MockCsvReader(ITokenizer tokenizer, CsvPreference preferences) {
			super(tokenizer, preferences);
		}
		
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
		
		tokenizer = new Tokenizer(reader, PREFS);
		tokenizerAbstractReader = new MockCsvReader(tokenizer, PREFS);
		
		trimModeReader = new StringReader("firstName, lastName, age, address\n"
			+ " John , Smith, 23 , \n"
			+ "Harry, Potter, , \"Gryffindor\nHogwarts Castle\nUK\" ");
		trimModeAbstractReader = new MockCsvReader(trimModeReader, TRIM_PREFS);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		abstractReader.close();
		tokenizerAbstractReader.close();
		trimModeAbstractReader.close();
	}
	
	/**
	 * Tests a normal reading scenario, asserting all of the properties available each time.
	 */
	@Test
	public void testReadingWithNormalReader() throws IOException {
		assertReading(abstractReader);
	}
	
	/**
	 * Tests a normal reading scenario (with the custom tokenizer reader), asserting all of the properties available
	 * each time.
	 */
	@Test
	public void testReadingWithTokenizerReader() throws IOException {
		assertReading(tokenizerAbstractReader);
	}
	
	/**
	 * Reusable method to test a normal reading scenario, asserting all of the properties are available each time.
	 */
	private void assertReading(final AbstractCsvReader csvReader) throws IOException {
		
		assertEquals(PREFS, csvReader.getPreferences());
		
		assertEquals(0, csvReader.getLineNumber());
		assertEquals(0, csvReader.getRowNumber());
		assertEquals("", csvReader.getUntokenizedRow());
		assertEquals(0, csvReader.length());
		
		// read the header
		final String[] header = csvReader.getCsvHeader(true);
		assertEquals(4, header.length);
		assertEquals("firstName", header[0]);
		assertEquals("lastName", header[1]);
		assertEquals("age", header[2]);
		assertEquals("address", header[3]);
		
		assertEquals(1, csvReader.getLineNumber());
		assertEquals(1, csvReader.getRowNumber());
		assertEquals("firstName,lastName,age,address", csvReader.getUntokenizedRow());
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
		assertEquals("John,Smith,23,", csvReader.getUntokenizedRow());
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
		assertEquals("Harry,Potter,,\"Gryffindor\nHogwarts Castle\nUK\"", csvReader.getUntokenizedRow());
		assertEquals(4, csvReader.length());
		
		// read again (should be EOF)
		assertFalse(csvReader.readRow());
		assertEquals(5, csvReader.getLineNumber());
		assertEquals(3, csvReader.getRowNumber());
		assertEquals("", csvReader.getUntokenizedRow());
		assertEquals(0, csvReader.length());
		
	}
	
	/**
	 * Tests a normal reading scenario (with trim mode enabled), asserting all of the properties available each time.
	 */
	@Test
	public void testReadingWithTrimModeReader() throws IOException {
		assertTrimModeReading(trimModeAbstractReader);
	}
	
	/**
	 * Reusable method to test a trim mode reading scenario, asserting all of the properties are available each time.
	 */
	private void assertTrimModeReading(final AbstractCsvReader csvReader) throws IOException {
		
		assertEquals(TRIM_PREFS, csvReader.getPreferences());
		
		assertEquals(0, csvReader.getLineNumber());
		assertEquals(0, csvReader.getRowNumber());
		assertEquals("", csvReader.getUntokenizedRow());
		assertEquals(0, csvReader.length());
		
		// read the header
		final String[] header = csvReader.getCsvHeader(true);
		assertEquals(4, header.length);
		assertEquals("firstName", header[0]);
		assertEquals("lastName", header[1]);
		assertEquals("age", header[2]);
		assertEquals("address", header[3]);
		
		assertEquals(1, csvReader.getLineNumber());
		assertEquals(1, csvReader.getRowNumber());
		assertEquals("firstName, lastName, age, address", csvReader.getUntokenizedRow());
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
		assertEquals(" John , Smith, 23 , ", csvReader.getUntokenizedRow());
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
		assertEquals("Harry, Potter, , \"Gryffindor\nHogwarts Castle\nUK\" ", csvReader.getUntokenizedRow());
		assertEquals(4, csvReader.length());
		
		// read again (should be EOF)
		assertFalse(csvReader.readRow());
		assertEquals(5, csvReader.getLineNumber());
		assertEquals(3, csvReader.getRowNumber());
		assertEquals("", csvReader.getUntokenizedRow());
		assertEquals(0, csvReader.length());
		
	}
	
	/**
	 * Tests the getCsvHeader(true) can't be called when a line has already been read.
	 */
	@Test
	public void testIllegalGetHeader() throws IOException {
		
		abstractReader.getCsvHeader(true);
		
		try {
			abstractReader.getCsvHeader(true);
			fail("should have thrown SuperCsvException");
		}
		catch(SuperCSVException e) {
			assertEquals("CSV header must be fetched as the first read operation, but 1 lines have already been read",
				e.getMessage());
		}
		
	}
	
	/**
	 * Tests getCsvHeader(false).
	 */
	@Test
	public void testGetHeaderNoCheck() throws IOException {
		assertEquals(4, abstractReader.getCsvHeader(false).length);
		assertEquals(4, abstractReader.getCsvHeader(false).length);
		assertEquals(4, abstractReader.getCsvHeader(false).length);
		assertNull(abstractReader.getCsvHeader(false)); // should be EOF
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
	 * Tests the Tokenizer constructor with a null Reader.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testTokenizerConstructorWithNullReader() {
		new CsvListReader((Tokenizer) null, PREFS);
	}
	
	/**
	 * Tests the Tokenizer constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testTokenizerConstructorWithNullPreferences() {
		new CsvListReader(tokenizer, null);
	}
	
}
