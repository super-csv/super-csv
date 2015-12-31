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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE_CUSTOM_FIELD_MAPPING;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.PARTIAL_HEADER;
import static org.supercsv.SuperCsvTestUtils.READ_PROCESSORS;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.createColumnMapping;
import static org.supercsv.SuperCsvTestUtils.createColumnMappingWithProcessors;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvMapReader class.
 * 
 * @author James Bassett
 */
public class CsvMapReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Map<String, CellProcessor> columnMapping;
	private Map<String, CellProcessor> columnMappingProc;

	private Reader reader;
	
	private CsvMapReader mapReader;
	
	private CsvMapReader tokenizerMapReader;
	
	/**
	 * Sets up the reader for the tests.
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV_FILE);
		mapReader = new CsvMapReader(reader, PREFS);
		
		final Tokenizer tokenizer = new Tokenizer(reader, PREFS);
		tokenizerMapReader = new CsvMapReader(tokenizer, PREFS);

		columnMapping = createColumnMapping();
		columnMappingProc = createColumnMappingWithProcessors();
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		mapReader.close();
		tokenizerMapReader.close();
	}
	
	/**
	 * Tests the read() method.
	 */
	@Test
	public void testRead() throws IOException {
		
		final String[] header = mapReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		int i = 0;
		Map<String, String> customer;
		while( (customer = mapReader.read(header)) != null ) {
			assertEquals(STRING_CUSTOMERS.get(i).getCustomerNo(), customer.get("customerNo"));
			assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.get("firstName"));
			assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.get("lastName"));
			assertEquals(STRING_CUSTOMERS.get(i).getBirthDate(), customer.get("birthDate"));
			assertEquals(STRING_CUSTOMERS.get(i).getMailingAddress(), customer.get("mailingAddress"));
			assertEquals(STRING_CUSTOMERS.get(i).getMarried(), customer.get("married"));
			assertEquals(STRING_CUSTOMERS.get(i).getNumberOfKids(), customer.get("numberOfKids"));
			assertEquals(STRING_CUSTOMERS.get(i).getFavouriteQuote(), customer.get("favouriteQuote"));
			assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.get("email"));
			assertEquals(STRING_CUSTOMERS.get(i).getLoyaltyPoints(), customer.get("loyaltyPoints"));
			i++;
		}
		
		assertEquals(STRING_CUSTOMERS.size() + 1, mapReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method, but only mapping a few columns.
	 */
	@Test
	public void testPartialRead() throws IOException {
		
		assertArrayEquals(HEADER, mapReader.getHeader(true));
		
		int i = 0;
		Map<String, String> customer;
		while( (customer = mapReader.read(PARTIAL_HEADER)) != null ) {
			assertNull(customer.get("customerNo"));
			assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.get("firstName"));
			assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.get("lastName"));
			assertNull(customer.get("birthDate"));
			assertNull(customer.get("mailingAddress"));
			assertNull(customer.get("married"));
			assertNull(customer.get("numberOfKids"));
			assertNull(customer.get("favouriteQuote"));
			assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.get("email"));
			assertNull(customer.get("loyaltyPoints"));
			i++;
		}
		
		assertEquals(STRING_CUSTOMERS.size() + 1, mapReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method with processors.
	 */
	@Test
	public void testReadWithProcessors() throws IOException {
		
		final String[] header = mapReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		int i = 0;
		Map<String, Object> customer;
		while( (customer = mapReader.read(header, READ_PROCESSORS)) != null ) {
			assertEquals(CUSTOMERS.get(i).getCustomerNo(), customer.get("customerNo"));
			assertEquals(CUSTOMERS.get(i).getFirstName(), customer.get("firstName"));
			assertEquals(CUSTOMERS.get(i).getLastName(), customer.get("lastName"));
			assertEquals(CUSTOMERS.get(i).getBirthDate(), customer.get("birthDate"));
			assertEquals(CUSTOMERS.get(i).getMailingAddress(), customer.get("mailingAddress"));
			assertEquals(CUSTOMERS.get(i).getMarried(), customer.get("married"));
			assertEquals(CUSTOMERS.get(i).getNumberOfKids(), customer.get("numberOfKids"));
			assertEquals(CUSTOMERS.get(i).getFavouriteQuote(), customer.get("favouriteQuote"));
			assertEquals(CUSTOMERS.get(i).getEmail(), customer.get("email"));
			assertEquals(CUSTOMERS.get(i).getLoyaltyPoints(), customer.get("loyaltyPoints"));
			i++;
		}
		
		assertEquals(CUSTOMERS.size() + 1, mapReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method with processors, but only mapping a few columns.
	 */
	@Test
	public void testPartialReadWithProcessors() throws IOException {
		
		assertArrayEquals(HEADER, mapReader.getHeader(true));
		
		int i = 0;
		Map<String, Object> customer;
		while( (customer = mapReader.read(PARTIAL_HEADER, READ_PROCESSORS)) != null ) {
			assertNull(customer.get("customerNo"));
			assertEquals(CUSTOMERS.get(i).getFirstName(), customer.get("firstName"));
			assertEquals(CUSTOMERS.get(i).getLastName(), customer.get("lastName"));
			assertNull(customer.get("birthDate"));
			assertNull(customer.get("mailingAddress"));
			assertNull(customer.get("married"));
			assertNull(customer.get("numberOfKids"));
			assertNull(customer.get("favouriteQuote"));
			assertEquals(CUSTOMERS.get(i).getEmail(), customer.get("email"));
			assertNull(customer.get("loyaltyPoints"));
			i++;
		}
		
		assertEquals(CUSTOMERS.size() + 1, mapReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method using the tokenizer version of CsvMapReader (just to make sure it behaves exactly the
	 * same as the reader version).
	 */
	@Test
	public void testReadUsingTokenizerReader() throws IOException {
		
		final String[] header = tokenizerMapReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		int i = 0;
		Map<String, String> customer;
		while( (customer = tokenizerMapReader.read(HEADER)) != null ) {
			assertEquals(STRING_CUSTOMERS.get(i).getCustomerNo(), customer.get("customerNo"));
			assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.get("firstName"));
			assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.get("lastName"));
			assertEquals(STRING_CUSTOMERS.get(i).getBirthDate(), customer.get("birthDate"));
			assertEquals(STRING_CUSTOMERS.get(i).getMailingAddress(), customer.get("mailingAddress"));
			assertEquals(STRING_CUSTOMERS.get(i).getMarried(), customer.get("married"));
			assertEquals(STRING_CUSTOMERS.get(i).getNumberOfKids(), customer.get("numberOfKids"));
			assertEquals(STRING_CUSTOMERS.get(i).getFavouriteQuote(), customer.get("favouriteQuote"));
			assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.get("email"));
			assertEquals(STRING_CUSTOMERS.get(i).getLoyaltyPoints(), customer.get("loyaltyPoints"));
			i++;
		}
		
		assertEquals(STRING_CUSTOMERS.size() + 1, tokenizerMapReader.getRowNumber());
	}
	
	/**
	 * Tests the read(Map<String, CellProcessor> columnMapping) method - no cell processors
	 * @throws IOException
	 */
	@Test
	public void testReadMappedColumns() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		mapReader = new CsvMapReader(reader, PREFS);

		int i = 0;
		Map<String, Object> customer;
		List<Map<String, Object>> customers = new ArrayList<Map<String, Object>>();
		while( (customer = mapReader.read(columnMapping)) != null ) {
			assertEquals(10, customer.entrySet().size());
			assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.get("first name"));
			assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.get("last name"));
			assertEquals(STRING_CUSTOMERS.get(i).getBirthDate(), customer.get("date of birth"));
			assertEquals(STRING_CUSTOMERS.get(i).getMailingAddress(), customer.get("mailing address"));
			assertEquals(STRING_CUSTOMERS.get(i).getMarried(), customer.get("marital status"));
			assertEquals(STRING_CUSTOMERS.get(i).getNumberOfKids(), customer.get("number of kids"));
			assertEquals(STRING_CUSTOMERS.get(i).getFavouriteQuote(), customer.get("favourite quote"));
			assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.get("email address"));
			assertEquals(STRING_CUSTOMERS.get(i).getLoyaltyPoints(), customer.get("loyalty points"));
			assertEquals(STRING_CUSTOMERS.get(i).getCustomerNo(), customer.get("customer id"));
			i++;
			customers.add(customer);
		}
		assertEquals(5, customers.size());
	}

	/**
	 * Tests the read(Map<String, CellProcessor> columnMapping) method - with cell processors
	 * @throws IOException
	 */
	@Test
	public void testReadMappedColumnsWithProcessors() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		mapReader = new CsvMapReader(reader, PREFS);

		int i = 0;
		Map<String, Object> customer;
		List<Map<String, Object>> customers = new ArrayList<Map<String, Object>>();
		while( (customer = mapReader.read(columnMappingProc)) != null ) {
			assertEquals(10, customer.entrySet().size());
			assertEquals(CUSTOMERS.get(i).getFirstName(), customer.get("first name"));
			assertEquals(CUSTOMERS.get(i).getLastName(), customer.get("last name"));
			assertEquals(CUSTOMERS.get(i).getBirthDate(), customer.get("date of birth"));
			assertEquals(CUSTOMERS.get(i).getMailingAddress(), customer.get("mailing address"));
			assertEquals(CUSTOMERS.get(i).getMarried(), customer.get("marital status"));
			assertEquals(CUSTOMERS.get(i).getNumberOfKids(), customer.get("number of kids"));
			assertEquals(CUSTOMERS.get(i).getFavouriteQuote(), customer.get("favourite quote"));
			assertEquals(CUSTOMERS.get(i).getEmail(), customer.get("email address"));
			assertEquals(CUSTOMERS.get(i).getLoyaltyPoints(), customer.get("loyalty points"));
			assertEquals(CUSTOMERS.get(i).getCustomerNo(), customer.get("customer id"));
			i++;
			customers.add(customer);
		}
		assertEquals(5, customers.size());
	}

	/**
	 * Tests the read() method with column mapping that has column name that doesn't exist
	 * (no cell processor)
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadWithWrongColumnNamesNoProcessor() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		mapReader = new CsvMapReader(reader, PREFS);
		columnMappingProc.put("no such column", null);
		mapReader.read(columnMappingProc);
	}

	/**
	 * Tests the read() method with column mapping that has column name that doesn't exist
	 * (with cell processor)
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadWithWrongColumnNamesWithProcessor() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		mapReader = new CsvMapReader(reader, PREFS);
		columnMappingProc.put("no such column", new ParseDate("dd/MM/yyyy"));
		mapReader.read(columnMappingProc);
	}

	/**
	 * Tests the read() method, with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullNameMapping() throws IOException {
		mapReader.read((String[]) null);
	}
	
	/**
	 * Tests the read() method (with processors), with a null cell processor array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadProcessorsWithNullProcessors() throws IOException {
		mapReader.read(HEADER, null);
	}
	
	/**
	 * Tests the read() method (with processors), with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadProcessorsWithNullNameMapping() throws IOException {
		mapReader.read((String[]) null, READ_PROCESSORS);
	}
	
	/**
	 * Tests the read() method with a null column mapping
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullColumnMapping() throws IOException {
		mapReader.read((Map<String, CellProcessor>) null);
	}

	/**
	 * Test the read() method with empty column mapping
	 */
	@Test
	public void testReadEmptyMapping() throws IOException {
		Map<String, CellProcessor> columnMapping = new HashMap<String, CellProcessor>();
		Map<String, Object> map = mapReader.read(columnMapping);
		assertEquals(0, map.size());
	}

	/**
	 * Tests the Reader constructor with a null Reader.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testReaderConstructorWithNullReader() {
		new CsvMapReader((Reader) null, PREFS);
	}
	
	/**
	 * Tests the Reader constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testReaderConstructorWithNullPreferences() {
		new CsvMapReader(reader, null);
	}
	
	/**
	 * Tests the Tokenizer constructor with a null Reader.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testTokenizerConstructorWithNullReader() {
		new CsvMapReader((Tokenizer) null, PREFS);
	}
	
	/**
	 * Tests the Tokenizer constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testTokenizerConstructorWithNullPreferences() {
		new CsvMapReader(new Tokenizer(reader, PREFS), null);
	}
	
	/**
	 * Test that row/line numbers reported during exception are determined correctly
	 * when using read(Map<String, CellProcessor> columnMapping) method.
	 *
	 * @throws IOException
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testRowLineNumberCorrectness() throws IOException {
		final int lineNumber = 5;
		final int rowNumber = 4;
		final String CsvCausingException =
			"customer id,first name,date of birth\r\n"
			+ "1,\"Alexander\r\nGraham\",13/6/1945\r\n"
			+ "2,Bob,25/2/1919\r\n"
			+ "3,Alice,CAUSES EXCEPTION\r\n"
			+ "4,Bill,10/7/1973\r\n"
			+ "5,Miranda,3/1/1999\r\n";

		reader = new StringReader(CsvCausingException);
		mapReader = new CsvMapReader(reader, PREFS);

		columnMapping = new HashMap<String, CellProcessor>();
		columnMapping.put("customer id", null);
		columnMapping.put("first name", null);
		columnMapping.put("date of birth", new ParseDate("dd/MM/yyyy"));

		try {
			while( (mapReader.read(columnMapping)) != null ) {}
		} catch(SuperCsvCellProcessorException e) {
			final int actualLineNumber = e.getCsvContext().getLineNumber();
			final int actualRowNumber = e.getCsvContext().getRowNumber();
			assertEquals("line number not correct", lineNumber, actualLineNumber);
			assertEquals("row number not correct", rowNumber, actualRowNumber);
			throw e;
		}
	}

	/**
	 * Tests read(Map<String, CellProcessor> columnMapping) method in case when CSV file has different number of
	 * elements in different rows (all elements corresponding to columns specified by columnMapping are present)
	 *
	 * @throws IOException
	 */
	@Test
	public void testReadVariableRowLength() throws IOException {
		final String CsvVariableRowLength =
			"first name,last name,unrelated column 1,date of birth,unrelated column 2,mailing address,marital status,number of kids,favourite quote,email address,loyalty points,customer id\r\n"
				+ "John,Dunbar,unrelated data 1,13/06/1945,unrelated data 2,\"1600 Amphitheatre Parkway\r\nMountain View, CA 94043\r\nUnited States\",,,\"\"\"May the Force be with you.\"\" - Star Wars\"\r\n"
				+ "Bob,Down,unrelated data 1,25/02/1919,unrelated data 2,\"1601 Willow Rd.\r\nMenlo Park, CA 94025\r\nUnited States\",Y,0,\"\"\"Frankly, my dear, I don't give a damn.\"\" - Gone With The Wind\",bobdown@hotmail.com,123456,2\r\n"
				+ "Alice,Wunderland,unrelated data 1,08/08/1985,unrelated data 2,\"One Microsoft Way\r\nRedmond, WA 98052-6399\r\nUnited States\",Y,0,\"\"\"Play it, Sam. Play \"\"As Time Goes By.\"\"\"\" - Casablanca\",throughthelookingglass@yahoo.com,2255887799\r\n"
				+ "Bill,Jobs,unrelated data 1,10/07/1973,unrelated data 2,\"2701 San Tomas Expressway\r\nSanta Clara, CA 95050\r\nUnited States\",Y,3\r\n"
				+ "Miranda,Feist,unrelated data 1,03/01/1999";

		reader = new StringReader(CsvVariableRowLength);
		mapReader = new CsvMapReader(reader, PREFS);

		columnMapping = new LinkedHashMap<String, CellProcessor>();
		columnMapping.put("first name", null);
		columnMapping.put("last name", null);
		columnMapping.put("date of birth", new ParseDate("dd/MM/yyyy"));

		Map<String, Object> customer;

		int i = 0;
		while( (customer = mapReader.read(columnMapping)) != null ) {
			assertEquals(3, customer.size());
			assertEquals(CUSTOMERS.get(i).getFirstName(), customer.get("first name"));
			assertEquals(CUSTOMERS.get(i).getLastName(), customer.get("last name"));
			assertEquals(CUSTOMERS.get(i).getBirthDate(), customer.get("date of birth"));
			i++;
		}
	}

	/**
	 * Tests read(Map<String, CellProcessor> columnMapping) method in case when CSV file has different number of
	 * elements in different rows (<b>NOT</b> all elements corresponding to columns specified by columnMapping
	 * are present)
	 *
	 * @throws IOException
	 */
	@Test(expected = SuperCsvException.class)
	public void testReadVariableRowLengthMissingColumns() throws IOException {
		final String CsvVariableRowLength =
			"first name,last name,unrelated column 1,date of birth,unrelated column 2,mailing address,marital status,number of kids,favourite quote,email address,loyalty points,customer id\r\n"
				+ "John,Dunbar,unrelated data 1,13/06/1945,unrelated data 2,\"1600 Amphitheatre Parkway\r\nMountain View, CA 94043\r\nUnited States\",,,\"\"\"May the Force be with you.\"\" - Star Wars\"\r\n"
				+ "Bob,Down,unrelated data 1,25/02/1919,unrelated data 2,\"1601 Willow Rd.\r\nMenlo Park, CA 94025\r\nUnited States\",Y,0,\"\"\"Frankly, my dear, I don't give a damn.\"\" - Gone With The Wind\",bobdown@hotmail.com,123456,2\r\n"
				+ "Alice,Wunderland,unrelated data 1\r\n"
				+ "Bill,Jobs,unrelated data 1,10/07/1973,unrelated data 2,\"2701 San Tomas Expressway\r\nSanta Clara, CA 95050\r\nUnited States\",Y,3\r\n"
				+ "Miranda,Feist,unrelated data 1,03/01/1999";

		final int rowCausingException = 4;

		reader = new StringReader(CsvVariableRowLength);
		mapReader = new CsvMapReader(reader, PREFS);

		columnMapping = new LinkedHashMap<String, CellProcessor>();
		columnMapping.put("first name", null);
		columnMapping.put("last name", null);
		columnMapping.put("date of birth", new ParseDate("dd/MM/yyyy"));

		try {
			while( mapReader.read(columnMapping) != null ) {}
		} catch (SuperCsvException e) {
			assertEquals(rowCausingException, e.getCsvContext().getRowNumber());
			throw e;
		}
	}
}
