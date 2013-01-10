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
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.WRITE_PROCESSORS;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvListWriter class.
 * 
 * @author James Bassett
 */
public class CsvListWriterTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Writer writer;
	
	private CsvListWriter listWriter;
	
	/**
	 * Sets up the writer for the tests.
	 */
	@Before
	public void setUp() {
		writer = new StringWriter();
		listWriter = new CsvListWriter(writer, PREFS);
	}
	
	/**
	 * Closes the list writer after the test.
	 */
	@After
	public void tearDown() throws IOException {
		listWriter.close();
	}
	
	/**
	 * Tests the write() method with a List and array of CellProcessors.
	 */
	@Test
	public void testWriteListAndProcessors() throws IOException {
		listWriter.writeHeader(HEADER);
		for( CustomerBean customer : CUSTOMERS ) {
			final List<Object> customerList = Arrays.asList(new Object[] { customer.getCustomerNo(),
				customer.getFirstName(), customer.getLastName(), customer.getBirthDate(), customer.getMailingAddress(),
				customer.getMarried(), customer.getNumberOfKids(), customer.getFavouriteQuote(), customer.getEmail(),
				customer.getLoyaltyPoints() });
			listWriter.write(customerList, WRITE_PROCESSORS);
		}
		listWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with a null List and array of CellProcessors.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteListAndProcessorsWithNullList() throws IOException {
		listWriter.write(null, WRITE_PROCESSORS);
	}
	
	/**
	 * Tests the write() method with a List and null array of CellProcessors.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteListAndProcessorsWithNullProcessors() throws IOException {
		listWriter.write(Arrays.asList(""), null);
	}
	
	/**
	 * Tests the write() method with a List.
	 */
	@Test
	public void testWriteList() throws IOException {
		listWriter.writeHeader(HEADER);
		for( CustomerStringBean customer : STRING_CUSTOMERS ) {
			final List<Object> customerList = Arrays.asList(new Object[] { customer.getCustomerNo(),
				customer.getFirstName(), customer.getLastName(), customer.getBirthDate(), customer.getMailingAddress(),
				customer.getMarried(), customer.getNumberOfKids(), customer.getFavouriteQuote(), customer.getEmail(),
				customer.getLoyaltyPoints() });
			listWriter.write(customerList);
		}
		listWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with a null List.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteListWithNullList() throws IOException {
		listWriter.write((List<?>) null);
	}
	
	/**
	 * Tests the write() method with an Object array.
	 */
	@Test
	public void testWriteObjectArray() throws Exception {
		listWriter.writeHeader(HEADER);
		for( CustomerStringBean customer : STRING_CUSTOMERS ) {
			final Object[] customerArray = new Object[] { customer.getCustomerNo(), customer.getFirstName(),
				customer.getLastName(), customer.getBirthDate(), customer.getMailingAddress(), customer.getMarried(),
				customer.getNumberOfKids(), customer.getFavouriteQuote(), customer.getEmail(),
				customer.getLoyaltyPoints() };
			listWriter.write(customerArray);
		}
		listWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with a null Object array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteObjectArrayWithNullArray() throws IOException {
		listWriter.write((Object[]) null);
	}
	
	/**
	 * Tests the write() method with a String array.
	 */
	@Test
	public void testWriteStringArray() throws IOException {
		listWriter.writeHeader(HEADER);
		for( CustomerStringBean customer : STRING_CUSTOMERS ) {
			final String[] customerArray = new String[] { customer.getCustomerNo(), customer.getFirstName(),
				customer.getLastName(), customer.getBirthDate(), customer.getMailingAddress(), customer.getMarried(),
				customer.getNumberOfKids(), customer.getFavouriteQuote(), customer.getEmail(),
				customer.getLoyaltyPoints() };
			listWriter.write(customerArray);
		}
		listWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with a null String array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteStringArrayWithNullArray() throws IOException {
		listWriter.write((String[]) null);
	}
	
	/**
	 * Tests the constructor with a null writer.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWillNullWriter() {
		new CsvListWriter(null, PREFS);
	}
	
	/**
	 * Tests the constructor with a null CsvPreference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWillNullPreference() {
		new CsvListWriter(writer, null);
	}
	
}
