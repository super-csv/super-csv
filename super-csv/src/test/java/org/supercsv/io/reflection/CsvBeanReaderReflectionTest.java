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
package org.supercsv.io.reflection;

import org.supercsv.io.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.ADA_STRING;
import static org.supercsv.SuperCsvTestUtils.ALICE_STRING;
import static org.supercsv.SuperCsvTestUtils.BILL_STRING;
import static org.supercsv.SuperCsvTestUtils.BOB_STRING;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.GRACE_STRING;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.JOHN_STRING;
import static org.supercsv.SuperCsvTestUtils.LARRY_STRING;
import static org.supercsv.SuperCsvTestUtils.MIRANDA_STRING;
import static org.supercsv.SuperCsvTestUtils.SERGEI_STRING;
import static org.supercsv.SuperCsvTestUtils.STEVE_STRING;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvBeanReader with filling a non-public bean class.
 * 
 * @author Fabian Seifert
 */
public class CsvBeanReaderReflectionTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Reader reader;
	
	private CsvBeanReader beanReader;
	
	/**
	 * Sets up the reader for the tests.
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV_FILE);
		beanReader = new CsvBeanReader(reader, PREFS);
	}
	
	public void none() {
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		beanReader.close();
	}
	
	/**
	 * Tests the read() method with no processors, populating an existing non-public bean.
	 */
	@Test
	public void testReadIntoExistingProtectedBean() throws IOException {
		
		final String[] header = beanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(new CustomerStringProtectedBean(JOHN_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(BOB_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(ALICE_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(BILL_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(MIRANDA_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(STEVE_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(ADA_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(SERGEI_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(LARRY_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertEquals(new CustomerStringProtectedBean(GRACE_STRING),
			beanReader.read(new CustomerStringProtectedBean(), header));
		assertNull(beanReader.read(new CustomerStringProtectedBean(), header));
	}
	
	/**
	 * Tests the read() method with no processors for a non-public bean class.
	 */
	@Test
	public void testReadIntoProtectedBean() throws IOException {
		
		final String[] header = beanReader.getHeader(true);
		assertArrayEquals(HEADER, header);
		
		assertEquals(new CustomerStringProtectedBean(JOHN_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(BOB_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(ALICE_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(BILL_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(MIRANDA_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(STEVE_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(ADA_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(SERGEI_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(LARRY_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertEquals(new CustomerStringProtectedBean(GRACE_STRING),
			beanReader.read(CustomerStringProtectedBean.class, header));
		assertNull(beanReader.read(CustomerStringProtectedBean.class, header));
	}
}
