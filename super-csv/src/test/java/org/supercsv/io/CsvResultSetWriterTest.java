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

import static org.junit.Assert.*;
import static org.supercsv.SuperCsvTestUtils.CSV_FILE;
import static org.supercsv.SuperCsvTestUtils.HEADER;
import static org.supercsv.SuperCsvTestUtils.CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.WRITE_PROCESSORS;
import static org.supercsv.SuperCsvTestUtils.date;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvDelayException;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.mock.ResultSetMock;
import org.supercsv.prefs.CallBackOnException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.DelayCellProcessorExceptions;

/**
 * Tests the CsvResultSetWriter class
 * 
 * @author SingularityFX
 * @author Pietro Aragona
 */
public class CsvResultSetWriterTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	public static Object[][] TEST_DATA_VARIOUS_TYPES;
	public static Object[][] TEST_DATA_STRINGS;
	
	private Writer writer;
	private CsvResultSetWriter csvResultSetWriter;
	
	@BeforeClass
	public static void beforeClass() {
		TEST_DATA_VARIOUS_TYPES = setUpTestDataCustomerBeans();
		TEST_DATA_STRINGS = setUpTestDataCustomerStringBeans();
	}
	
	private static Object[][] setUpTestDataCustomerBeans() {
		final Object[][] testData = new Object[10][11];
		for( int i = 0; i < 10; i++ ) {
			CustomerBean bean = CUSTOMERS.get(i);
			testData[i][0] = bean.getCustomerNo();
			testData[i][1] = bean.getFirstName();
			testData[i][2] = bean.getLastName();
			testData[i][3] = bean.getBirthDate();
			testData[i][4] = bean.getBirthTime();
			testData[i][5] = bean.getMailingAddress();
			testData[i][6] = bean.getMarried();
			testData[i][7] = bean.getNumberOfKids();
			testData[i][8] = bean.getFavouriteQuote();
			testData[i][9] = bean.getEmail();
			testData[i][10] = bean.getLoyaltyPoints();
		}
		return testData;
	}
	
	private static Object[][] setUpTestDataCustomerStringBeans() {
		final Object[][] testData = new Object[10][11];
		for( int i = 0; i < 10; i++ ) {
			CustomerStringBean bean = STRING_CUSTOMERS.get(i);
			testData[i][0] = bean.getCustomerNo();
			testData[i][1] = bean.getFirstName();
			testData[i][2] = bean.getLastName();
			testData[i][3] = bean.getBirthDate();
			testData[i][4] = bean.getBirthTime();
			testData[i][5] = bean.getMailingAddress();
			testData[i][6] = bean.getMarried();
			testData[i][7] = bean.getNumberOfKids();
			testData[i][8] = bean.getFavouriteQuote();
			testData[i][9] = bean.getEmail();
			testData[i][10] = bean.getLoyaltyPoints();
		}
		return testData;
	}
	
	@Before
	public void setUp() {
		writer = new StringWriter();
		csvResultSetWriter = new CsvResultSetWriter(writer, PREFS);
	}
	
	/**
	 * Tests writing ResultSet to a CSV file (no CellProcessors)
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testWrite() throws IOException, SQLException {
		final ResultSet resultSetMock = new ResultSetMock(TEST_DATA_STRINGS, HEADER);
		csvResultSetWriter.write(resultSetMock);
		csvResultSetWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	/**
	 * Test writing ResultSet to a CSV file with CellProcessors
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testWriteWithProcessors() throws SQLException, IOException {
		final ResultSet resultSetMock = new ResultSetMock(TEST_DATA_VARIOUS_TYPES, HEADER);
		csvResultSetWriter.write(resultSetMock, WRITE_PROCESSORS);
		csvResultSetWriter.flush();
		assertEquals(CSV_FILE, writer.toString());
	}
	
	// Tests for NullPointerException follow
	
	/**
	 * Tests the constructor with a null writer
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullWriter() {
		new CsvResultSetWriter(null, PREFS);
	}
	
	/**
	 * Tests the constructor with a null CsvPreference
	 */
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullCsvPreference() {
		new CsvResultSetWriter(writer, null);
	}
	
	/**
	 * Tests the write() method with null ResultSet
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteNullResultSet() throws SQLException, IOException {
		csvResultSetWriter.write(null);
	}
	
	/**
	 * Test the write() method (with processors) with null ResultSet
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithProcessorsNullResultSet() throws SQLException, IOException {
		csvResultSetWriter.write(null, WRITE_PROCESSORS);
	}
	
	/**
	 * Tests the write() method (with processors) with a null cell processor array
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteNullProcessors() throws SQLException, IOException {
		
		final ResultSet resultSet = new ResultSetMock(TEST_DATA_VARIOUS_TYPES, HEADER);
		csvResultSetWriter.write(resultSet, null);
	}
	
	/**
	 * Test that row/line numbers reported during exception are determined correctly
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testRowLineNumberCorrectness() throws SQLException, IOException {
		final int LINE_NUMBER = 5;
		final int ROW_NUMBER = 4;
		final Object[][] causesException = { { "1", "Alexander\r\nGraham", date(1945, 6, 13), },
			{ "2", "Bob", date(1919, 2, 25), }, { "3", "Alice", "CAUSES EXCEPTION", },
			{ "4", "Bill", date(1973, 7, 10), }, { "5", "Miranda", date(1999, 1, 3), }, };
		final String[] headers = { "customerNo", "firstName", "birthDate" };
		final ResultSet resultSet = new ResultSetMock(causesException, headers);
		final CellProcessor[] cellProcessors = { null, null, new FmtDate("dd/MM/yyyy") };
		try {
			csvResultSetWriter.write(resultSet, cellProcessors);
		}
		catch(SuperCsvCellProcessorException e) {
			final int actualLineNumber = e.getCsvContext().getLineNumber();
			final int actualRowNumber = e.getCsvContext().getRowNumber();
			assertEquals("line number not correct", LINE_NUMBER, actualLineNumber);
			assertEquals("row number not correct", ROW_NUMBER, actualRowNumber);
			throw e;
		}
	}

	/**
	 * Tests the write() method with DelayCellProcessorExceptions
	 */
	@Test
	public void testtestWriteWithDelayCellProcessorExceptions() throws SQLException, IOException {
		final Object[][] causesException = { { "1", "Alexander\r\nGraham", date(1945, 6, 13), },
			{ "2", "Bob", date(1919, 2, 25), }, { "3", "Alice", "CAUSES EXCEPTION", },
			{ "4", "Bill", date(1973, 7, 10), }, { "5", "Miranda", null, }, };
		final String[] headers = { "customerNo", "firstName", "birthDate" };
		final CellProcessor[] cellProcessors = { null, null, new FmtDate("dd/MM/yyyy") };
		String message = "Suppressed Exceptions for row 4:\n"
			+ "org.supercsv.exception.SuperCsvCellProcessorException: the input value should be of type java.util.Date but is java.lang.String\r\n"
			+ "processor=org.supercsv.cellprocessor.FmtDate\r\n"
			+ "context={lineNo=5, rowNo=4, columnNo=3, rowSource=[3, Alice, CAUSES EXCEPTION]}\n" + "\n"
			+ "Suppressed Exceptions for row 6:\n"
			+ "org.supercsv.exception.SuperCsvCellProcessorException: this processor does not accept null input - if the column is optional then chain an Optional() processor before this one\r\n"
			+ "processor=org.supercsv.cellprocessor.FmtDate\r\n"
			+ "context={lineNo=7, rowNo=6, columnNo=3, rowSource=[5, Miranda, null]}\n" + "\n";

		// skip write exceptions row;
		CsvPreference preference = new CsvPreference.Builder(PREFS).delayCellProcessorExceptions(
			new DelayCellProcessorExceptions(true)
		).build();
		Writer writer = new StringWriter();
		CsvResultSetWriter testWriter = new CsvResultSetWriter(writer, preference);
		ResultSet resultSet = new ResultSetMock(causesException, headers);
		try {
			testWriter.write(resultSet, cellProcessors);
			fail("should thrown SuperCsvDelayException");
		}
		catch(SuperCsvDelayException e) {
			assertEquals(message, e.toString());
		}
		testWriter.flush();
		assertEquals("customerNo,firstName,birthDate\r\n"
			+ "1,\"Alexander\r\n"
			+ "Graham\",13/06/1945\r\n"
			+ "2,Bob,25/02/1919\r\n"
			+ "4,Bill,10/07/1973\r\n", writer.toString());

		//no skip write exceptions row
		preference = new CsvPreference.Builder(PREFS).delayCellProcessorExceptions(
			new DelayCellProcessorExceptions(false, new CallBackOnException() {
				public Object process(Object rawColumns) {
					return "EMPTY COLUMNS";
				}
			})
		).build();
		writer = new StringWriter();
		testWriter = new CsvResultSetWriter(writer, preference);
		resultSet = new ResultSetMock(causesException, headers);
		try {
			testWriter.write(resultSet, cellProcessors);
			fail("should thrown SuperCsvDelayException");
		}
		catch(SuperCsvDelayException e) {
			assertEquals(message, e.toString());
		}
		testWriter.flush();
		assertEquals("customerNo,firstName,birthDate\r\n"
			+ "1,\"Alexander\r\n"
			+ "Graham\",13/06/1945\r\n"
			+ "2,Bob,25/02/1919\r\n"
			+ "3,Alice,EMPTY COLUMNS\r\n"
			+ "4,Bill,10/07/1973\r\n"
			+ "5,Miranda,EMPTY COLUMNS\r\n", writer.toString());
	}
}
