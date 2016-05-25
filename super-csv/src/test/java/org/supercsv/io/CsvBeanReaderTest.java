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
import static org.supercsv.SuperCsvTestUtils.ADA;
import static org.supercsv.SuperCsvTestUtils.ADA_STRING;
import static org.supercsv.SuperCsvTestUtils.ALICE;
import static org.supercsv.SuperCsvTestUtils.ALICE_STRING;
import static org.supercsv.SuperCsvTestUtils.BILL;
import static org.supercsv.SuperCsvTestUtils.BILL_STRING;
import static org.supercsv.SuperCsvTestUtils.BOB;
import static org.supercsv.SuperCsvTestUtils.BOB_STRING;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.GRACE;
import static org.supercsv.SuperCsvTestUtils.GRACE_STRING;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.JOHN;
import static org.supercsv.SuperCsvTestUtils.JOHN_STRING;
import static org.supercsv.SuperCsvTestUtils.LARRY;
import static org.supercsv.SuperCsvTestUtils.LARRY_STRING;
import static org.supercsv.SuperCsvTestUtils.MIRANDA;
import static org.supercsv.SuperCsvTestUtils.MIRANDA_STRING;
import static org.supercsv.SuperCsvTestUtils.PARTIAL_HEADER;
import static org.supercsv.SuperCsvTestUtils.READ_PROCESSORS;
import static org.supercsv.SuperCsvTestUtils.SERGEI;
import static org.supercsv.SuperCsvTestUtils.SERGEI_STRING;
import static org.supercsv.SuperCsvTestUtils.STEVE;
import static org.supercsv.SuperCsvTestUtils.STEVE_STRING;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.mock.Customer;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.mock.PersonBean;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvBeanReader class.
 * 
 * @author James Bassett
 * @author Pietro Aragona
 */
public class CsvBeanReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Reader reader;
	
	private CsvBeanReader beanReader;
	
	private CsvBeanReader tokenizerBeanReader;
	
	/**
	 * Sets up the reader for the tests.
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV_FILE);
		beanReader = new CsvBeanReader(reader, PREFS);
		
		final Tokenizer tokenizer = new Tokenizer(reader, PREFS);
		tokenizerBeanReader = new CsvBeanReader(tokenizer, PREFS);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		beanReader.close();
		tokenizerBeanReader.close();
	}
	
	/**
	 * Tests the read() method using processors.
	 */
	@Test
	public void testReadWithProcessors() throws IOException {
		
		final String[] header = beanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(JOHN, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(BOB, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(ALICE, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(BILL, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(MIRANDA, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(STEVE, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(ADA, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(SERGEI, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(LARRY, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertEquals(GRACE, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		assertNull(beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
	}
	
	/**
	 * Tests the read() method using processors, but only mapping a few columns.
	 */
	@Test
	public void testPartialReadWithProcessors() throws IOException {
		
		assertArrayEquals(HEADER, beanReader.getHeader(true));
		
		final String[] header = PARTIAL_HEADER;
		for( CustomerBean fullCustomer : CUSTOMERS ) {
			
			// create the expected customer (same as full but with only first/last name and email)
			CustomerBean expectedCustomer = new CustomerBean();
			expectedCustomer.setFirstName(fullCustomer.getFirstName());
			expectedCustomer.setLastName(fullCustomer.getLastName());
			expectedCustomer.setEmail(fullCustomer.getEmail());
			assertEquals(expectedCustomer, beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
		}
		
		assertNull(beanReader.read(CustomerBean.class, header, READ_PROCESSORS));
	}
	
	/**
	 * Tests the read() method with no processors.
	 */
	@Test
	public void testRead() throws IOException {
		
		final String[] header = beanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(JOHN_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(BOB_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(ALICE_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(BILL_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(MIRANDA_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(STEVE_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(ADA_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(SERGEI_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(LARRY_STRING, beanReader.read(CustomerStringBean.class, header));
		assertEquals(GRACE_STRING, beanReader.read(CustomerStringBean.class, header));
		assertNull(beanReader.read(CustomerStringBean.class, header));
	}
	
	/**
	 * Tests the read() method, but only mapping a few columns.
	 */
	@Test
	public void testPartialRead() throws IOException {
		
		assertArrayEquals(HEADER, beanReader.getHeader(true));
		
		final String[] header = PARTIAL_HEADER;
		for( CustomerStringBean fullCustomer : STRING_CUSTOMERS ) {
			
			// create the expected customer (same as full but with only first/last name and email)
			CustomerBean expectedCustomer = new CustomerBean();
			expectedCustomer.setFirstName(fullCustomer.getFirstName());
			expectedCustomer.setLastName(fullCustomer.getLastName());
			expectedCustomer.setEmail(fullCustomer.getEmail());
			assertEquals(expectedCustomer, beanReader.read(CustomerBean.class, header));
		}
		
		assertNull(beanReader.read(CustomerBean.class, header));
	}
	
	/**
	 * Tests the read() method with no processors, populating an existing bean.
	 */
	@Test
	public void testReadIntoExistingBean() throws IOException {
		
		final String[] header = beanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(JOHN_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(BOB_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(ALICE_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(BILL_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(MIRANDA_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(STEVE_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(ADA_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(SERGEI_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(LARRY_STRING, beanReader.read(new CustomerStringBean(), header));
		assertEquals(GRACE_STRING, beanReader.read(new CustomerStringBean(), header));
		assertNull(beanReader.read(new CustomerStringBean(), header));
	}
	
	/**
	 * Tests the read() method using processors, populating an existing bean.
	 */
	@Test
	public void testReadIntoExistingBeanWithProcessors() throws IOException {
		
		final String[] header = beanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(JOHN, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(BOB, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(ALICE, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(BILL, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(MIRANDA, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(STEVE, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(ADA, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(SERGEI, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(LARRY, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertEquals(GRACE, beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
		assertNull(beanReader.read(new CustomerBean(), header, READ_PROCESSORS));
	}
	
	/**
	 * Tests the read() method with no processors, using the tokenizer version of CsvBeanReader (just to make sure it
	 * behaves exactly the same as the reader version).
	 */
	@Test
	public void testReadUsingTokenizerReader() throws IOException {
		
		final String[] header = tokenizerBeanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(JOHN_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(BOB_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(ALICE_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(BILL_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(MIRANDA_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(STEVE_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(ADA_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(SERGEI_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(LARRY_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertEquals(GRACE_STRING, tokenizerBeanReader.read(CustomerStringBean.class, header));
		assertNull(tokenizerBeanReader.read(CustomerStringBean.class, header));
	}
	
	/**
	 * Tests the read() method with an interface and using processors.
	 */
	@Test
	public void testReadWithProcessorsUsingInterface() throws IOException {
		assertArrayEquals(HEADER, beanReader.getHeader(true));
		
		// only map the fields relevant to the interface
		final String[] header = new String[] { "customerNo", null, null, null, null, "mailingAddress", null, null, null,
			null, "loyaltyPoints" };
			
		int i = 0;
		Customer customer;
		while( (customer = beanReader.read(Customer.class, header, READ_PROCESSORS)) != null ) {
			assertEquals(CUSTOMERS.get(i).getCustomerNo(), customer.getCustomerNo());
			assertEquals(CUSTOMERS.get(i).getMailingAddress(), customer.getMailingAddress());
			assertEquals(CUSTOMERS.get(i).getLoyaltyPoints(), customer.getLoyaltyPoints());
			i++;
		}
		
		assertEquals(CUSTOMERS.size() + 1, beanReader.getRowNumber());
		
	}
	
	/**
	 * Tests the read() method with an class that has no default no-arg constructor.
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testReadWithNonJavabean() throws IOException {
		beanReader.read(Integer.class, HEADER);
	}
	
	/**
	 * Tests the read() method, with a null bean class.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBeanClass() throws IOException {
		beanReader.read(null, HEADER);
	}
	
	/**
	 * Tests the read() method, with a null bean.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBean() throws IOException {
		beanReader.read((Object) null, HEADER);
	}
	
	/**
	 * Tests the read() method, with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullNameMapping() throws IOException {
		beanReader.read(PersonBean.class, (String[]) null);
	}
	
	/**
	 * Tests the read() method, with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadIntoBeanWithNullNameMapping() throws IOException {
		beanReader.read(new PersonBean(), (String[]) null);
	}
	
	/**
	 * Tests the read() method, with a name mapping array that's not the right size.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadWithInvalidSizeNameMapping() throws IOException {
		beanReader.getHeader(true);
		beanReader.read(PersonBean.class, new String[] { null, "firstName" });
	}
	
	/**
	 * Tests the read() method, with a name mapping array that's not the right size.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadIntoBeanWithInvalidSizeNameMapping() throws IOException {
		beanReader.getHeader(true);
		beanReader.read(new PersonBean(), new String[] { null, "firstName" });
	}
	
	/**
	 * Tests the read() method (with processors), with a null bean class.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadProcessorsWithNullBeanClass() throws IOException {
		beanReader.read(null, HEADER, READ_PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors), with a null bean.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadUsingProcessorsWithNullBean() throws IOException {
		beanReader.read((Object) null, HEADER, READ_PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors), with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadProcessorsWithNullNameMapping() throws IOException {
		beanReader.read(PersonBean.class, (String[]) null, READ_PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors), with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadIntoBeanUsingProcessorsWithNullNameMapping() throws IOException {
		beanReader.read(new PersonBean(), (String[]) null, READ_PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors), with a null cell processor array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadProcessorsWithNullProcessors() throws IOException {
		beanReader.read(PersonBean.class, HEADER, (CellProcessor[]) null);
	}
	
	/**
	 * Tests the read() method (with processors), with a null cell processor array.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadIntoBeanUsingProcessorsWithNullProcessors() throws IOException {
		beanReader.read(new PersonBean(), HEADER, (CellProcessor[]) null);
	}
	
	/**
	 * Tests the Reader constructor with a null Reader.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testReaderConstructorWithNullReader() {
		new CsvBeanReader((Reader) null, PREFS);
	}
	
	/**
	 * Tests the Reader constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testReaderConstructorWithNullPreferences() {
		new CsvBeanReader(reader, null);
	}
	
	/**
	 * Tests the Tokenizer constructor with a null Reader.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testTokenizerConstructorWithNullReader() {
		new CsvBeanReader((Tokenizer) null, PREFS);
	}
	
	/**
	 * Tests the Tokenizer constructor with a null preference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testTokenizerConstructorWithNullPreferences() {
		new CsvBeanReader(new Tokenizer(reader, PREFS), null);
	}
	
	/**
	 * Tests the read() method when invoking the bean's constructor throws IllegalAccessException.
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testBeanInstantationThrowingIllegalAccessException() throws IOException {
		beanReader.read(IllegalAccessBean.class, HEADER);
	}
	
	/**
	 * Tests the read() method when invoking a setter throws an Exception.
	 */
	@SuppressWarnings("resource")
	@Test(expected = SuperCsvReflectionException.class)
	public void testSetterThrowingException() throws IOException {
		new CsvBeanReader(new StringReader("value"), PREFS).read(ExceptionBean.class, "illegalArgument");
	}
	
	/**
	 * Bean to test exceptions when invoking setters using CsvBeanReader.
	 */
	public static class ExceptionBean extends CustomerBean {
		
		public void setIllegalArgument(String s) {
			throw new IllegalArgumentException("i don't like it!");
		}
		
	}
	
	/**
	 * Bean to test exceptions when invoking the constructor using CsvBeanWriter.
	 */
	public static class IllegalAccessBean extends CustomerBean {
		
		public IllegalAccessBean() throws IllegalAccessException {
			throw new IllegalAccessException("naughty naughty!");
		}
		
	}
}
