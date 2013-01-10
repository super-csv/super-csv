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
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.READ_PROCESSORS;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvListReader class.
 * 
 * @author James Bassett
 */
public class CsvListReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Reader reader;
	
	private CsvListReader listReader;
	
	private CsvListReader tokenizerListReader;
	
	/**
	 * Sets up the reader for the tests.
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV_FILE);
		listReader = new CsvListReader(reader, PREFS);
		
		final Tokenizer tokenizer = new Tokenizer(reader, PREFS);
		tokenizerListReader = new CsvListReader(tokenizer, PREFS);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		listReader.close();
		tokenizerListReader.close();
	}
	
	/**
	 * Tests the read() method.
	 */
	@Test
	public void testRead() throws IOException {
		
		final String[] header = listReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		// read all of the customers in
		final List<List<String>> customers = new ArrayList<List<String>>();
		List<String> customer;
		while( (customer = listReader.read()) != null ) {
			customers.add(customer);
		}
		
		// assert that the List for each customer is correct (ensures Lists haven't been modified)
		for( int i = 0; i < customers.size(); i++ ) {
			customer = customers.get(i);
			assertEquals(STRING_CUSTOMERS.get(i).getCustomerNo(), customer.get(0));
			assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.get(1));
			assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.get(2));
			assertEquals(STRING_CUSTOMERS.get(i).getBirthDate(), customer.get(3));
			assertEquals(STRING_CUSTOMERS.get(i).getMailingAddress(), customer.get(4));
			assertEquals(STRING_CUSTOMERS.get(i).getMarried(), customer.get(5));
			assertEquals(STRING_CUSTOMERS.get(i).getNumberOfKids(), customer.get(6));
			assertEquals(STRING_CUSTOMERS.get(i).getFavouriteQuote(), customer.get(7));
			assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.get(8));
			assertEquals(STRING_CUSTOMERS.get(i).getLoyaltyPoints(), customer.get(9));
		}
		
		assertEquals(STRING_CUSTOMERS.size() + 1, listReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method with processors.
	 */
	@Test
	public void testReadWithProcessors() throws IOException {
		
		final String[] header = listReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		// read all of the customers in
		final List<List<Object>> customers = new ArrayList<List<Object>>();
		List<Object> customer;
		while( (customer = listReader.read(READ_PROCESSORS)) != null ) {
			customers.add(customer);
		}
		
		// assert that the List for each customer is correct (ensures Lists haven't been modified)
		for( int i = 0; i < customers.size(); i++ ) {
			customer = customers.get(i);
			assertEquals(CUSTOMERS.get(i).getCustomerNo(), customer.get(0));
			assertEquals(CUSTOMERS.get(i).getFirstName(), customer.get(1));
			assertEquals(CUSTOMERS.get(i).getLastName(), customer.get(2));
			assertEquals(CUSTOMERS.get(i).getBirthDate(), customer.get(3));
			assertEquals(CUSTOMERS.get(i).getMailingAddress(), customer.get(4));
			assertEquals(CUSTOMERS.get(i).getMarried(), customer.get(5));
			assertEquals(CUSTOMERS.get(i).getNumberOfKids(), customer.get(6));
			assertEquals(CUSTOMERS.get(i).getFavouriteQuote(), customer.get(7));
			assertEquals(CUSTOMERS.get(i).getEmail(), customer.get(8));
			assertEquals(CUSTOMERS.get(i).getLoyaltyPoints(), customer.get(9));
		}
		
		assertEquals(CUSTOMERS.size() + 1, listReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method using the tokenizer version of CsvListReader (just to make sure it behaves exactly the
	 * same as the reader version).
	 */
	@Test
	public void testReadUsingTokenizerReader() throws IOException {
		
		final String[] header = tokenizerListReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		// read all of the customers in
		final List<List<String>> customers = new ArrayList<List<String>>();
		List<String> customer;
		while( (customer = tokenizerListReader.read()) != null ) {
			customers.add(customer);
		}
		
		// assert that the List for each customer is correct (ensures Lists haven't been modified)
		for( int i = 0; i < customers.size(); i++ ) {
			customer = customers.get(i);
			assertEquals(STRING_CUSTOMERS.get(i).getCustomerNo(), customer.get(0));
			assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.get(1));
			assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.get(2));
			assertEquals(STRING_CUSTOMERS.get(i).getBirthDate(), customer.get(3));
			assertEquals(STRING_CUSTOMERS.get(i).getMailingAddress(), customer.get(4));
			assertEquals(STRING_CUSTOMERS.get(i).getMarried(), customer.get(5));
			assertEquals(STRING_CUSTOMERS.get(i).getNumberOfKids(), customer.get(6));
			assertEquals(STRING_CUSTOMERS.get(i).getFavouriteQuote(), customer.get(7));
			assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.get(8));
			assertEquals(STRING_CUSTOMERS.get(i).getLoyaltyPoints(), customer.get(9));
		}
		
		assertEquals(STRING_CUSTOMERS.size() + 1, tokenizerListReader.getRowNumber());
	}
	
	/**
	 * Tests the read() method (with processors), with a null cell processor array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadProcessorsWithNullProcessors() throws IOException {
		listReader.read((CellProcessor[]) null);
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
		new CsvListReader(new Tokenizer(reader, PREFS), null);
	}
	
}
