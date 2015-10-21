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
import static org.supercsv.SuperCsvTestUtils.CSV_FILE_CUSTOM_FIELD_MAPPING;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.mock.Customer;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.mock.PersonBean;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.BeanField;

/**
 * Tests the CsvBeanReader class.
 * 
 * @author James Bassett
 * @author Vyacheslav Pushkin
 */
public class CsvBeanReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Map<String, BeanField> columnMapping;
	private Map<String, BeanField> columnMappingProc;

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

		columnMapping = new HashMap<String, BeanField>();
		columnMapping.put("first name", BeanField.of("firstName"));
		columnMapping.put("last name", BeanField.of("lastName"));
		columnMapping.put("date of birth", BeanField.of("birthDate"));
		columnMapping.put("mailing address", BeanField.of("mailingAddress"));
		columnMapping.put("marital status", BeanField.of("married"));
		columnMapping.put("number of kids", BeanField.of("numberOfKids"));
		columnMapping.put("favourite quote", BeanField.of("favouriteQuote"));
		columnMapping.put("email address", BeanField.of("email"));
		columnMapping.put("loyalty points", BeanField.of("loyaltyPoints"));
		columnMapping.put("customer id", BeanField.of("customerNo"));

		columnMappingProc = new HashMap<String, BeanField>(columnMapping);
		columnMappingProc.put("date of birth", BeanField.of("birthDate", new ParseDate("dd/MM/yyyy")));
		columnMappingProc.put("marital status", BeanField.of("married", new Optional(new ParseBool())));
		columnMappingProc.put("number of kids", BeanField.of("numberOfKids", new Optional(new ParseInt())));
		columnMappingProc.put("loyalty points", BeanField.of("loyaltyPoints", new ParseLong()));
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
		final String[] header = new String[] { "customerNo", null, null, null, "mailingAddress", null, null, null,
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
	 * Test the read() method using custom field mapping and processors
	 * @throws IOException
	 */
	@Test
	public void testReadWithCustomFieldMappingWithProcessors() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		beanReader = new CsvBeanReader(reader, PREFS);

		int i = 0;
		CustomerBean customer;
		while( (customer = beanReader.read(CustomerBean.class, columnMappingProc)) != null ) {
			compareCustomers(i++, customer);
		}
	}

	/**
	 * Test the read() method using custom field mapping and processors, populating an existing bean
	 * @throws IOException
	 */
	@Test
	public void testReadWithCustomFieldMappingWithProcessorsExistingBean() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		beanReader = new CsvBeanReader(reader, PREFS);

		int i = 0;
		CustomerBean customer;
		while( (customer = beanReader.read(new CustomerBean(), columnMappingProc)) != null ) {
			compareCustomers(i++, customer);
		}
	}

	/**
	 * Test the read() method using custom field mapping and no processors
	 * @throws IOException
	 */
	@Test
	public void testReadWithCustomFieldMappingNoProcessors() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		beanReader = new CsvBeanReader(reader, PREFS);

		int i = 0;
		CustomerStringBean customer;
		while( (customer = beanReader.read(CustomerStringBean.class, columnMapping)) != null ) {
			compareCustomers(i++, customer);
		}
	}

	/**
	 * Test the read() method using custom field mapping and no processors, populating an existing bean
	 * @throws IOException
	 */
	@Test
	public void testReadWithCustomFieldMappingNoProcessorsExistingBean() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		beanReader = new CsvBeanReader(reader, PREFS);

		int i = 0;
		CustomerStringBean customer;
		while( (customer = beanReader.read(new CustomerStringBean(), columnMapping)) != null ) {
			compareCustomers(i++, customer);
		}
	}

	private void compareCustomers(int i, CustomerBean customer) {
		assertEquals(CUSTOMERS.get(i), customer);
		assertEquals(CUSTOMERS.get(i).getBirthDate(), customer.getBirthDate());
		assertEquals(CUSTOMERS.get(i).getCustomerNo(), customer.getCustomerNo());
		assertEquals(CUSTOMERS.get(i).getEmail(), customer.getEmail());
		assertEquals(CUSTOMERS.get(i).getFavouriteQuote(), customer.getFavouriteQuote());
		assertEquals(CUSTOMERS.get(i).getFirstName(), customer.getFirstName());
		assertEquals(CUSTOMERS.get(i).getLastName(), customer.getLastName());
		assertEquals(CUSTOMERS.get(i).getLoyaltyPoints(), customer.getLoyaltyPoints());
		assertEquals(CUSTOMERS.get(i).getMailingAddress(), customer.getMailingAddress());
		assertEquals(CUSTOMERS.get(i).getMarried(), customer.getMarried());
		assertEquals(CUSTOMERS.get(i).getNumberOfKids(), customer.getNumberOfKids());
	}

	private void compareCustomers(int i, CustomerStringBean customer) {
		assertEquals(STRING_CUSTOMERS.get(i), customer);
		assertEquals(STRING_CUSTOMERS.get(i).getBirthDate(), customer.getBirthDate());
		assertEquals(STRING_CUSTOMERS.get(i).getCustomerNo(), customer.getCustomerNo());
		assertEquals(STRING_CUSTOMERS.get(i).getEmail(), customer.getEmail());
		assertEquals(STRING_CUSTOMERS.get(i).getFavouriteQuote(), customer.getFavouriteQuote());
		assertEquals(STRING_CUSTOMERS.get(i).getFirstName(), customer.getFirstName());
		assertEquals(STRING_CUSTOMERS.get(i).getLastName(), customer.getLastName());
		assertEquals(STRING_CUSTOMERS.get(i).getLoyaltyPoints(), customer.getLoyaltyPoints());
		assertEquals(STRING_CUSTOMERS.get(i).getMailingAddress(), customer.getMailingAddress());
		assertEquals(STRING_CUSTOMERS.get(i).getMarried(), customer.getMarried());
		assertEquals(STRING_CUSTOMERS.get(i).getNumberOfKids(), customer.getNumberOfKids());
	}

	/**
	 * Tests the read() method with an class that has no default no-arg constructor.
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testReadWithNonJavabean() throws IOException {
		beanReader.read(Integer.class, HEADER);
	}
	
	/**
	 * Tests the read() method with column mapping that has column name that doesn't exist
	 * (no cell processor)
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadWithWrongColumnNamesNoProcessor() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		beanReader = new CsvBeanReader(reader, PREFS);
		columnMappingProc.put("no such column", BeanField.of("birthDate"));
		beanReader.read(CustomerBean.class, columnMappingProc);
	}

	/**
	 * Tests the read() method with column mapping that has column name that doesn't exist
	 * (with cell processor)
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadWithWrongColumnNamesWithProcessor() throws IOException {
		reader = new StringReader(CSV_FILE_CUSTOM_FIELD_MAPPING);
		beanReader = new CsvBeanReader(reader, PREFS);
		columnMappingProc.put("no such column", BeanField.of("birthDate", new ParseDate("dd/MM/yyyy")));
		beanReader.read(CustomerBean.class, columnMappingProc);
	}

	/**
	 * Tests the read() method, with a null bean class.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBeanClass() throws IOException {
		beanReader.read(null, HEADER);
	}
	
	/**
	 * Tests the read() method, with a null bean class (custom field mapping)
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBeanClassCustomMapping() throws IOException {
		beanReader.read(null, columnMapping);
	}

	/**
	 * Tests the read() method, with a null bean.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBean() throws IOException {
		beanReader.read((Object) null, HEADER);
	}
	
	/**
	 * Tests the read() method, with a null bean (custom field mapping)
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBeanCustomMapping() throws IOException {
		beanReader.read((Object) null, columnMapping);
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
	 * Tests the read() method, with a null column name / BeanField map.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullCustomNameMapping() throws IOException {
		beanReader.read(PersonBean.class, (Map<String, BeanField>) null);
	}

	/**
	 * Tests the read() method, with a null column name / BeanField map.
	 */
	@Test(expected = NullPointerException.class)
	public void testReadIntoBeanWithNullCustomNameMapping() throws IOException {
		beanReader.read(new PersonBean(), (Map<String, BeanField>) null);
	}

	/**
	 * Tests the read() method with null fieldName inside BeanField
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullFieldName() throws IOException {
		Map<String, BeanField> map = new HashMap<String, BeanField>();
		map.put("firstName", BeanField.of(null));
		beanReader.read(PersonBean.class, map);
	}

	/**
	 * Tests the read() method with null fieldName inside BeanField
	 */
	@Test(expected = NullPointerException.class)
	public void testReadIntoBeanWithNullFieldName() throws IOException {
		Map<String, BeanField> map = new HashMap<String, BeanField>();
		map.put("firstName", BeanField.of(null));
		beanReader.read(new PersonBean(), map);
	}

	/**
	 * Test the read() method with empty column mapping
	 */
	@Test
	public void testReadEmptyMapping() throws IOException {
		Map<String, BeanField> map = new HashMap<String, BeanField>();
		PersonBean personBean = beanReader.read(PersonBean.class, map);
		assertNull(personBean.getFirstName());
	}

	/**
	 * Test the read() method with empty column mapping
	 */
	@Test
	public void testReadIntoBeanEmptyMapping() throws IOException {
		Map<String, BeanField> map = new HashMap<String, BeanField>();
		PersonBean personBean = new PersonBean();
		personBean.setLastName("Ivanov");
		personBean = beanReader.read(personBean, map);
		assertNull(personBean.getFirstName());
		assertEquals("Ivanov", personBean.getLastName());
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

	/**
	 * Tests that row/line numbers reported during exception are determined correctly
	 * when using custom field mapping.
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
		beanReader = new CsvBeanReader(reader, PREFS);

		columnMapping = new HashMap<String, BeanField>();
		columnMapping.put("customer id", BeanField.of("customerNo"));
		columnMapping.put("first name", BeanField.of("firstName"));
		columnMapping.put("date of birth", BeanField.of("birthDate", new ParseDate("dd/MM/yyyy")));

		try {
			while( (beanReader.read(new CustomerBean(), columnMapping)) != null ) {}
		} catch(SuperCsvCellProcessorException e) {
			final int actualLineNumber = e.getCsvContext().getLineNumber();
			final int actualRowNumber = e.getCsvContext().getRowNumber();
			assertEquals("line number not correct", lineNumber, actualLineNumber);
			assertEquals("row number not correct", rowNumber, actualRowNumber);
			throw e;
		}
	}

	/**
	 * Tests read method with custom field mapping in case when CSV file has different number of
	 * elements in different rows (all elements corresponding to columns specified by columnMapping
	 * are present)
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
		beanReader = new CsvBeanReader(reader, PREFS);

		columnMapping = new LinkedHashMap<String, BeanField>();
		columnMapping.put("first name", BeanField.of("firstName"));
		columnMapping.put("last name", BeanField.of("lastName"));
		columnMapping.put("date of birth", BeanField.of("birthDate", new ParseDate("dd/MM/yyyy")));

		CustomerBean customer;

		int i = 0;
		while( (customer = beanReader.read(CustomerBean.class, columnMapping)) != null ) {
			assertEquals(CUSTOMERS.get(i).getFirstName(), customer.getFirstName());
			assertEquals(CUSTOMERS.get(i).getLastName(), customer.getLastName());
			assertEquals(CUSTOMERS.get(i).getBirthDate(), customer.getBirthDate());
			i++;
		}
	}

	/**
	 * Tests read method with custom field mapping in case when CSV file has different number of
	 * elements in different rows (<b>NOT</b> all elements corresponding to columns specified by columnMapping
	 * are present).
	 *
	 * Shall throw SuperCsvException.
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
		beanReader = new CsvBeanReader(reader, PREFS);

		columnMapping = new LinkedHashMap<String, BeanField>();
		columnMapping.put("first name", BeanField.of("firstName"));
		columnMapping.put("last name", BeanField.of("lastName"));
		columnMapping.put("date of birth", BeanField.of("birthDate", new ParseDate("dd/MM/yyyy")));

		try {
			while( beanReader.read(CustomerBean.class, columnMapping) != null ) {}
		} catch (SuperCsvException e) {
			assertEquals(rowCausingException, e.getCsvContext().getRowNumber());
			throw e;
		}
	}
}
