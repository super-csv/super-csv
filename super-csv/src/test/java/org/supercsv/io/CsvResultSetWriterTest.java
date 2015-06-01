/*
 * Copyright 2015 Vyacheslav Pushkin (https://github.com/singularityfx)
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
import static org.supercsv.SuperCsvTestUtils.STRING_CUSTOMERS;
import static org.supercsv.SuperCsvTestUtils.WRITE_PROCESSORS;
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
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
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
	
	private static final String[] HEADERS = {
		"customerNo", "firstName", "lastName", "birthDate",
		"mailingAddress", "married", "numberOfKids", "favouriteQuote", 
		"email", "loyaltyPoints" };
	
	public static final Object[][] TEST_DATA_VARIOUS_TYPES = {
		{"1", "John", "Dunbar", date(1945, 6, 13),
			"1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", null, null,
			"\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L},
		{"2", "Bob", "Down", date(1919, 2, 25),
			"1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States", true, 0,
			"\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind", "bobdown@hotmail.com", 123456L},
		{"3", "Alice", "Wunderland", date(1985, 8, 8),
			"One Microsoft Way\nRedmond, WA 98052-6399\nUnited States", true, 0,
			"\"Play it, Sam. Play \"As Time Goes By.\"\" - Casablanca", "throughthelookingglass@yahoo.com", 2255887799L},
		{"4", "Bill", "Jobs", date(1973, 7, 10),
			"2701 San Tomas Expressway\nSanta Clara, CA 95050\nUnited States", true, 3,
			"\"You've got to ask yourself one question: \"Do I feel lucky?\" Well, do ya, punk?\" - Dirty Harry",
			"billy34@hotmail.com", 36L},
		{"5", "Miranda", "Feist", date(1999, 1, 3),
			"2-4 Rue du Sablon\nMorges, 1110\nSwitzerland", null, null, "\"You had me at \"hello.\"\" - Jerry Maguire",
			"miranda_feist@gmail.com", 54623L},
		{"6", "Steve", "Gates", date(2000, 12, 31),
			"701 First Avenue\nSunnyvale, CA 94089\nUnited States", false, 0,
			"\"Gentlemen, you can't fight in here! This is the War Room!\" - Dr Strangelove", "stevengates@yahoo.com",
			1341512L},
		{"7", "Ada", "Von Trappe", date(1956, 11, 18),
			"One Dell Way\nRound Rock, TX 78682\nUnited States", null, 2,
			"\"Hasta la vista, baby.\" - Terminator 2: Judgement Day", "vonada@gmail.com", 0L},
		{"8", "Sergei", "Denisovich", date(1944, 6, 22),
			"1 Infinite Loop\nCupertino, CA 95014\nUnited States", true, null,
			"\"Open the pod bay doors, HAL.\" - 2001: A Space Odyssey", "sergei@denisovich.com", 229431L},
		{"9", "Larry", "Ballmer", date(1901, 1, 1),
			"1-7-1 Konan\nMinato-ku\nTokyo, 108-0075\nJapan", false, 0, "\"A martini. Shaken, not stirred.\" - Goldfinger",
			"lazza99@gmail.com", 164L},
		{"10", "Grace", "Fowler", date(2003, 11, 28),
			"11-1, Kamitoba Hokotate-cho\nMinami-ku\nKyoto, 601-8501\nJapan", false, 0,
			"\"Carpe diem. Seize the day, boys. Make your lives extraordinary.\" - Dead Poets Society",
			"gracie@hotmail.com", 168841L},
	};
		
	public static final Object[][] TEST_DATA_STRINGS = {
		{"1", "John", "Dunbar", "13/06/1945",
			"1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", "", "",
			"\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", "0"},
		{"2", "Bob", "Down", "25/02/1919",
			"1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States", "Y", "0",
			"\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind", "bobdown@hotmail.com", "123456"},
		{"3", "Alice", "Wunderland", "08/08/1985",
			"One Microsoft Way\nRedmond, WA 98052-6399\nUnited States", "Y", "0",
			"\"Play it, Sam. Play \"As Time Goes By.\"\" - Casablanca", "throughthelookingglass@yahoo.com", "2255887799"},
		{"4", "Bill", "Jobs", "10/07/1973",
			"2701 San Tomas Expressway\nSanta Clara, CA 95050\nUnited States", "Y", "3",
			"\"You've got to ask yourself one question: \"Do I feel lucky?\" Well, do ya, punk?\" - Dirty Harry",
			"billy34@hotmail.com", "36"},
		{"5", "Miranda", "Feist", "03/01/1999",
			"2-4 Rue du Sablon\nMorges, 1110\nSwitzerland", "", "", "\"You had me at \"hello.\"\" - Jerry Maguire",
			"miranda_feist@gmail.com", "54623"},
		{"6", "Steve", "Gates", "31/12/2000",
			"701 First Avenue\nSunnyvale, CA 94089\nUnited States", "N", "0",
			"\"Gentlemen, you can't fight in here! This is the War Room!\" - Dr Strangelove", "stevengates@yahoo.com",
			"1341512"},
		{"7", "Ada", "Von Trappe", "18/11/1956",
			"One Dell Way\nRound Rock, TX 78682\nUnited States", "", "2",
			"\"Hasta la vista, baby.\" - Terminator 2: Judgement Day", "vonada@gmail.com", "0"},
		{"8", "Sergei", "Denisovich", "22/06/1944",
			"1 Infinite Loop\nCupertino, CA 95014\nUnited States", "Y", "",
			"\"Open the pod bay doors, HAL.\" - 2001: A Space Odyssey", "sergei@denisovich.com", "229431"},
		{"9", "Larry", "Ballmer", "01/01/1901",
			"1-7-1 Konan\nMinato-ku\nTokyo, 108-0075\nJapan", "N", "0", "\"A martini. Shaken, not stirred.\" - Goldfinger",
			"lazza99@gmail.com", "164"},
		{"10", "Grace", "Fowler", "28/11/2003",
			"11-1, Kamitoba Hokotate-cho\nMinami-ku\nKyoto, 601-8501\nJapan", "N", "0",
			"\"Carpe diem. Seize the day, boys. Make your lives extraordinary.\" - Dead Poets Society",
			"gracie@hotmail.com", "168841"},
	};
	
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
		ResultSet resultSetMock = new ResultSetMock(TEST_DATA_STRINGS, HEADERS);
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
		ResultSet resultSetMock = new ResultSetMock(TEST_DATA_VARIOUS_TYPES, HEADERS);
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
		ResultSet resultSet = new ResultSetMock(TEST_DATA_VARIOUS_TYPES, HEADERS);
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
		Object[][] causesException = {
			{"1", "Alexander\r\nGraham", date(1945, 6, 13), },
			{"2", "Bob", date(1919, 2, 25), }, 
			{"3", "Alice", "CAUSES EXCEPTION", },
			{"4", "Bill", date(1973, 7, 10), },
			{"5", "Miranda", date(1999, 1, 3), },
		};
		String[] headers = {"customerNo", "firstName", "birthDate"};
		ResultSet resultSet = new ResultSetMock(causesException, headers);
		CellProcessor[] cellProcessors = {null, null, new FmtDate("dd/MM/yyyy")};
		try {
			csvResultSetWriter.write(resultSet, cellProcessors);
		} catch(SuperCsvCellProcessorException e) {
			int actualLineNumber = e.getCsvContext().getLineNumber();
			int actualRowNumber = e.getCsvContext().getRowNumber();
			assertEquals("line number not correct", LINE_NUMBER, actualLineNumber);
			assertEquals("row number not correct", ROW_NUMBER, actualRowNumber);
			throw e;
		}
	}
}
