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
import static org.junit.Assert.fail;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.JOHN;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.WRITE_PROCESSORS;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCsvDelayException;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.prefs.CallBackOnException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.DelayCellProcessorExceptions;

/**
 * Tests the CsvBeanWriter class.
 * 
 * @author James Bassett
 */
public class CsvBeanWriterTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Writer writer;
	
	private CsvBeanWriter beanWriter;
	
	private CustomerBean customer;
	
	/**
	 * Sets up the writer for the tests.
	 */
	@Before
	public void setUp() {
		writer = new StringWriter();
		beanWriter = new CsvBeanWriter(writer, PREFS);
		customer = new CustomerBean();
	}
	
	/**
	 * Closes the bean writer after the test.
	 */
	@After
	public void tearDown() throws IOException {
		beanWriter.close();
	}
	
	/**
	 * Tests the constructor with a null writer.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWillNullWriter() {
		new CsvBeanWriter(null, PREFS);
	}
	
	/**
	 * Tests the constructor with a null CsvPreference.
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWillNullPreference() {
		new CsvBeanWriter(writer, null);
	}
	
	/**
	 * Tests the write() method.
	 */
	@Test
	public void testWrite() throws IOException {
		beanWriter.writeHeader(HEADER);
		for( CustomerStringBean customer : STRING_CUSTOMERS ) {
			beanWriter.write(customer, HEADER);
		}
		beanWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Tests the write() method with processors.
	 */
	@Test
	public void testWriteProcessors() throws IOException {
		beanWriter.writeHeader(HEADER);
		for( CustomerBean customer : CUSTOMERS ) {
			beanWriter.write(customer, HEADER, WRITE_PROCESSORS);
		}
		beanWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}

	/**
	 * Tests the write() method with DelayCellProcessorExceptions
	 */
	@Test
	public void testWriteWithDelayCellProcessorExceptions() throws IOException {
		CustomerBean customer = new CustomerBean("1", "John", "Dunbar", null, null,
			null, null, null, null, "jdunbar@gmail.com", 0L);
		String message = "Suppressed Exceptions for row 1:\n"
			+ "org.supercsv.exception.SuperCsvCellProcessorException: this processor does not accept null input - if the column is optional then chain an Optional() processor before this one\n"
			+ "processor=org.supercsv.cellprocessor.FmtDate\n"
			+ "context={lineNo=1, rowNo=1, columnNo=4, rowSource=[1, John, Dunbar, null, null, null, null, null, null, jdunbar@gmail.com, 0]}\n"
			+ "org.supercsv.exception.SuperCsvCellProcessorException: this processor does not accept null input - if the column is optional then chain an Optional() processor before this one\n"
			+ "processor=org.supercsv.cellprocessor.FmtSqlTime\n"
			+ "context={lineNo=1, rowNo=1, columnNo=5, rowSource=[1, John, Dunbar, null, null, null, null, null, null, jdunbar@gmail.com, 0]}";

		// skip write exceptions row;
		CsvPreference preference = new CsvPreference.Builder(PREFS).delayCellProcessorExceptions(
			new DelayCellProcessorExceptions(true)
		).build();
		Writer writer = new StringWriter();
		CsvBeanWriter testWriter = new CsvBeanWriter(writer, preference);
		try {
			testWriter.write(customer, HEADER, WRITE_PROCESSORS);
			fail("should thrown SuperCsvDelayException");
		}
		catch( SuperCsvDelayException e ){
			assertEquals(message, e.toString().trim().replace("\r", ""));
		}
		testWriter.flush();
		assertEquals("", writer.toString());

		//no skip write exceptions row
		preference = new CsvPreference.Builder(PREFS).delayCellProcessorExceptions(
			new DelayCellProcessorExceptions(false, new CallBackOnException() {
				public Object process(Object rawColumns) {
					return "EMPTY COLUMNS";
				}
			})
		).build();
		writer = new StringWriter();
		testWriter = new CsvBeanWriter(writer, preference);
		try {
			testWriter.write(customer, HEADER, WRITE_PROCESSORS);
			fail("should thrown SuperCsvDelayException");
		}
		catch( SuperCsvDelayException e ){
			assertEquals(message, e.toString().trim().replace("\r", ""));
		}
		testWriter.flush();
		assertEquals("1,John,Dunbar,EMPTY COLUMNS,EMPTY COLUMNS,,,,,jdunbar@gmail.com,0\r\n", writer.toString());
	}
	
	/**
	 * Tests the write() method with a null bean.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithNullSource() throws IOException {
		beanWriter.write(null, HEADER);
	}
	
	/**
	 * Tests the write() method with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithNullNameMappingArray() throws IOException {
		beanWriter.write(customer, (String[]) null);
	}
	
	/**
	 * Tests the write() method with a a name mapping containing nulls (should be empty columns).
	 */
	@Test
	public void testWriteWithNullNameMapping() throws IOException {
		
		final String[] headerWithNulls = new String[] { "customerNo", null, "firstName", null, "lastName" };
		final String expectedCsv = JOHN.getCustomerNo() + ",," + JOHN.getFirstName() + ",," + JOHN.getLastName()
			+ "\r\n";
		
		beanWriter.write(JOHN, headerWithNulls);
		beanWriter.flush();
		assertEquals(expectedCsv, writer.toString());
	}
	
	/**
	 * Tests the write() method (with processors) with a null bean.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteProcessorsWithNullSource() throws IOException {
		beanWriter.write(null, HEADER, WRITE_PROCESSORS);
	}
	
	/**
	 * Tests the write() method (with processors) with a null name mapping array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteProcessorsWithNullNameMapping() throws IOException {
		beanWriter.write(customer, null, WRITE_PROCESSORS);
	}
	
	/**
	 * Tests the write() method (with processors) with a null cell processor array.
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteProcessorsWithNullProcessors() throws IOException {
		beanWriter.write(customer, HEADER, null);
		
	}
	
	/**
	 * Tests the write() method when a getter throws an Exception.
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testGetterThrowingException() throws IOException {
		beanWriter.write(new ExceptionBean(), "exception");
	}
	
	/**
	 * Bean to test exceptions when invoking getters using CsvBeanWriter.
	 */
	public static class ExceptionBean extends CustomerBean {
		
		public String getException() {
			throw new RuntimeException("oops!");
		}
		
	}
}
