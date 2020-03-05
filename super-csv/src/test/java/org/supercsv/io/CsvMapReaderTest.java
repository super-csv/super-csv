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
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.PARTIAL_HEADER;
import static org.supercsv.SuperCsvTestUtils.READ_PROCESSORS;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvMapReader class.
 * 
 * @author James Bassett
 */
public class CsvMapReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Reader reader;

	private InputStream inputStream;
	
	private CsvMapReader mapReader;

	private CsvMapReader mapInputStream;
	
	private CsvMapReader tokenizerMapReader;
	
	/**
	 * Sets up the reader for the tests.
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV_FILE);
		mapReader = new CsvMapReader(reader, PREFS);

		inputStream = new ByteArrayInputStream(CSV_FILE.getBytes());
		mapInputStream = new CsvMapReader(inputStream, PREFS);
		
		final Tokenizer tokenizer = new Tokenizer(reader, PREFS);
		tokenizerMapReader = new CsvMapReader(tokenizer, PREFS);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		mapReader.close();
		mapInputStream.close();
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
	 * Tests the InputStream constructor with a null InputStream.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testInputStreamConstructorWithNullInputStream() {
		new CsvMapReader((InputStream) null, PREFS);
	}

	/**
	 * Tests the InputStream constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testInputStreamConstructorWithNullPreferences() {
		new CsvMapReader(inputStream, null);
	}
}
