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
import static org.supercsv.SuperCsvTestUtils.WRITE_PROCESSORS;
import static org.supercsv.SuperCsvTestUtils.TEST_DATA_VARIOUS_TYPES;
import static org.supercsv.SuperCsvTestUtils.TEST_DATA_STRINGS;
import static org.supercsv.SuperCsvTestUtils.date;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.ResultSetMock;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvResultSetWriter class
 * 
 * @author SingularityFX
 * 
 */
public class CsvResultSetWriterTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	
	private Writer writer;
	private CsvResultSetWriter csvResultSetWriter;
		
	@Before
	public void setUp() {
		writer = new StringWriter();
		csvResultSetWriter = new CsvResultSetWriter(writer, PREFS);
	}
	
	/**
	 * Tests writing ResultSet to a CSV file (no CellProcessors)
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
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteNullResultSet() throws SQLException, IOException {
		csvResultSetWriter.write(null);
	}
	
	/**
	 * Test the write() method (with processors) with null ResultSet
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithProcessorsNullResultSet() throws SQLException, IOException {
		csvResultSetWriter.write(null, WRITE_PROCESSORS);
	}
	
	/**
	 * Tests the write() method (with processors) with a null cell processor array
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
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testRowLineNumberCorrectness() throws SQLException, IOException {
		final int LINE_NUMBER = 5;
		final int ROW_NUMBER = 4;
		final Object[][] causesException = {
			{"1", "Alexander\r\nGraham", date(1945, 6, 13), },
			{"2", "Bob", date(1919, 2, 25), }, 
			{"3", "Alice", "CAUSES EXCEPTION", },
			{"4", "Bill", date(1973, 7, 10), },
			{"5", "Miranda", date(1999, 1, 3), },
		};
		final String[] headers = {"customerNo", "firstName", "birthDate"};
		final ResultSet resultSet = new ResultSetMock(causesException, headers);
		final CellProcessor[] cellProcessors = {null, null, new FmtDate("dd/MM/yyyy")};
		try {
			csvResultSetWriter.write(resultSet, cellProcessors);
		} catch(SuperCsvCellProcessorException e) {
			final int actualLineNumber = e.getCsvContext().getLineNumber();
			final int actualRowNumber = e.getCsvContext().getRowNumber();
			assertEquals("line number not correct", LINE_NUMBER, actualLineNumber);
			assertEquals("row number not correct", ROW_NUMBER, actualRowNumber);
			throw e;
		}
	}
}
