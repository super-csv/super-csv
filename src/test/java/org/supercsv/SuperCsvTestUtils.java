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
package org.supercsv;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.CustomerBean;
import org.supercsv.mock.CustomerStringBean;
import org.supercsv.util.CsvContext;

/**
 * Utility methods and constants for tests.
 * 
 * @author James Bassett
 */
public class SuperCsvTestUtils {
	
	public static final CsvContext ANONYMOUS_CSVCONTEXT = new CsvContext(1, 2, 3);
	
	/** the complete header for testing */
	public static final String[] HEADER = new String[] { "customerNo", "firstName", "lastName", "birthDate",
		"mailingAddress", "married", "numberOfKids", "favouriteQuote", "email", "loyaltyPoints" };
	public static final String HEADER_CSV = "customerNo,firstName,lastName,birthDate,mailingAddress,married,"
		+ "numberOfKids,favouriteQuote,email,loyaltyPoints";
	
	/** partial header for testing partial reading (only a few columns are mapped) */
	public static final String[] PARTIAL_HEADER = new String[] { null, "firstName", "lastName", null, null, null, null,
		null, "email", null };
	
	/** the processors to use for CSV reading tests */
	public static final CellProcessor[] READ_PROCESSORS = new CellProcessor[] { null, null, null,
		new ParseDate("dd/MM/yyyy"), null, new Optional(new ParseBool()), new Optional(new ParseInt()), null, null,
		new ParseLong() };
	
	/** the processors to use for CSV writing tests */
	public static final CellProcessor[] WRITE_PROCESSORS = new CellProcessor[] { null, null, null,
		new FmtDate("dd/MM/yyyy"), null, new ConvertNullTo("", new FmtBool("Y", "N")), null, null, null, null };
	
	// each line in the CSV file and it's corresponding CustomerBean/CustomerStringBeans for testing
	public static final String JOHN_CSV = "1,John,Dunbar,13/06/1945,\"1600 Amphitheatre Parkway\r\nMountain View, CA 94043\r\nUnited States\","
		+ ",,\"\"\"May the Force be with you.\"\" - Star Wars\",jdunbar@gmail.com,0";
	public static final CustomerBean JOHN = new CustomerBean("1", "John", "Dunbar", date(1945, 6, 13),
		"1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", null, null,
		"\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L);
	public static final CustomerStringBean JOHN_STRING = new CustomerStringBean(JOHN);
	
	public static final String BOB_CSV = "2,Bob,Down,25/02/1919,\"1601 Willow Rd.\r\nMenlo Park, CA 94025\r\nUnited States\","
		+ "Y,0,\"\"\"Frankly, my dear, I don't give a damn.\"\" - Gone With The Wind\",bobdown@hotmail.com,123456";
	public static final CustomerBean BOB = new CustomerBean("2", "Bob", "Down", date(1919, 2, 25),
		"1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States", true, 0,
		"\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind", "bobdown@hotmail.com", 123456L);
	public static final CustomerStringBean BOB_STRING = new CustomerStringBean(BOB);
	
	public static final String ALICE_CSV = "3,Alice,Wunderland,08/08/1985,\"One Microsoft Way\r\nRedmond, WA 98052-6399\r\nUnited States\","
		+ "Y,0,\"\"\"Play it, Sam. Play \"\"As Time Goes By.\"\"\"\" - Casablanca\",throughthelookingglass@yahoo.com,2255887799";
	public static final CustomerBean ALICE = new CustomerBean("3", "Alice", "Wunderland", date(1985, 8, 8),
		"One Microsoft Way\nRedmond, WA 98052-6399\nUnited States", true, 0,
		"\"Play it, Sam. Play \"As Time Goes By.\"\" - Casablanca", "throughthelookingglass@yahoo.com", 2255887799L);
	public static final CustomerStringBean ALICE_STRING = new CustomerStringBean(ALICE);
	
	public static final String BILL_CSV = "4,Bill,Jobs,10/07/1973,\"2701 San Tomas Expressway\r\nSanta Clara, CA 95050\r\nUnited States\","
		+ "Y,3,\"\"\"You've got to ask yourself one question: \"\"Do I feel lucky?\"\" Well, do ya, punk?\"\" - Dirty Harry\",billy34@hotmail.com,36";
	public static final CustomerBean BILL = new CustomerBean("4", "Bill", "Jobs", date(1973, 7, 10),
		"2701 San Tomas Expressway\nSanta Clara, CA 95050\nUnited States", true, 3,
		"\"You've got to ask yourself one question: \"Do I feel lucky?\" Well, do ya, punk?\" - Dirty Harry",
		"billy34@hotmail.com", 36L);
	public static final CustomerStringBean BILL_STRING = new CustomerStringBean(BILL);
	
	public static final String MIRANDA_CSV = "5,Miranda,Feist,03/01/1999,\"2-4 Rue du Sablon\r\nMorges, 1110\r\nSwitzerland\","
		+ ",,\"\"\"You had me at \"\"hello.\"\"\"\" - Jerry Maguire\",miranda_feist@gmail.com,54623";
	public static final CustomerBean MIRANDA = new CustomerBean("5", "Miranda", "Feist", date(1999, 1, 3),
		"2-4 Rue du Sablon\nMorges, 1110\nSwitzerland", null, null, "\"You had me at \"hello.\"\" - Jerry Maguire",
		"miranda_feist@gmail.com", 54623L);
	public static final CustomerStringBean MIRANDA_STRING = new CustomerStringBean(MIRANDA);
	
	public static final String STEVE_CSV = "6,Steve,Gates,31/12/2000,\"701 First Avenue\r\nSunnyvale, CA 94089\r\nUnited States\","
		+ "N,0,\"\"\"Gentlemen, you can't fight in here! This is the War Room!\"\" - Dr Strangelove\",stevengates@yahoo.com,1341512";
	public static final CustomerBean STEVE = new CustomerBean("6", "Steve", "Gates", date(2000, 12, 31),
		"701 First Avenue\nSunnyvale, CA 94089\nUnited States", false, 0,
		"\"Gentlemen, you can't fight in here! This is the War Room!\" - Dr Strangelove", "stevengates@yahoo.com",
		1341512L);
	public static final CustomerStringBean STEVE_STRING = new CustomerStringBean(STEVE);
	
	public static final String ADA_CSV = "7,Ada,Von Trappe,18/11/1956,\"One Dell Way\r\nRound Rock, TX 78682\r\nUnited States\","
		+ ",2,\"\"\"Hasta la vista, baby.\"\" - Terminator 2: Judgement Day\",vonada@gmail.com,0";
	public static final CustomerBean ADA = new CustomerBean("7", "Ada", "Von Trappe", date(1956, 11, 18),
		"One Dell Way\nRound Rock, TX 78682\nUnited States", null, 2,
		"\"Hasta la vista, baby.\" - Terminator 2: Judgement Day", "vonada@gmail.com", 0L);
	public static final CustomerStringBean ADA_STRING = new CustomerStringBean(ADA);
	
	public static final String SERGEI_CSV = "8,Sergei,Denisovich,22/06/1944,\"1 Infinite Loop\r\nCupertino, CA 95014\r\nUnited States\","
		+ "Y,,\"\"\"Open the pod bay doors, HAL.\"\" - 2001: A Space Odyssey\",sergei@denisovich.com,229431";
	public static final CustomerBean SERGEI = new CustomerBean("8", "Sergei", "Denisovich", date(1944, 6, 22),
		"1 Infinite Loop\nCupertino, CA 95014\nUnited States", true, null,
		"\"Open the pod bay doors, HAL.\" - 2001: A Space Odyssey", "sergei@denisovich.com", 229431L);
	public static final CustomerStringBean SERGEI_STRING = new CustomerStringBean(SERGEI);
	
	public static final String LARRY_CSV = "9,Larry,Ballmer,01/01/1901,\"1-7-1 Konan\r\nMinato-ku\r\nTokyo, 108-0075\r\nJapan\","
		+ "N,0,\"\"\"A martini. Shaken, not stirred.\"\" - Goldfinger\",lazza99@gmail.com,164";
	public static final CustomerBean LARRY = new CustomerBean("9", "Larry", "Ballmer", date(1901, 1, 1),
		"1-7-1 Konan\nMinato-ku\nTokyo, 108-0075\nJapan", false, 0, "\"A martini. Shaken, not stirred.\" - Goldfinger",
		"lazza99@gmail.com", 164L);
	public static final CustomerStringBean LARRY_STRING = new CustomerStringBean(LARRY);
	
	public static final String GRACE_CSV = "10,Grace,Fowler,28/11/2003,\"11-1, Kamitoba Hokotate-cho\r\nMinami-ku\r\nKyoto, 601-8501\r\nJapan\","
		+ "N,0,\"\"\"Carpe diem. Seize the day, boys. Make your lives extraordinary.\"\" - Dead Poets Society\",gracie@hotmail.com,168841";
	public static final CustomerBean GRACE = new CustomerBean("10", "Grace", "Fowler", date(2003, 11, 28),
		"11-1, Kamitoba Hokotate-cho\nMinami-ku\nKyoto, 601-8501\nJapan", false, 0,
		"\"Carpe diem. Seize the day, boys. Make your lives extraordinary.\" - Dead Poets Society",
		"gracie@hotmail.com", 168841L);
	public static final CustomerStringBean GRACE_STRING = new CustomerStringBean(GRACE);
	
	/**
	 * The CSV file to use for testing.
	 */
	public static final String CSV_FILE = new StringBuilder(HEADER_CSV).append("\r\n").append(JOHN_CSV).append("\r\n")
		.append(BOB_CSV).append("\r\n").append(ALICE_CSV).append("\r\n").append(BILL_CSV).append("\r\n")
		.append(MIRANDA_CSV).append("\r\n").append(STEVE_CSV).append("\r\n").append(ADA_CSV).append("\r\n")
		.append(SERGEI_CSV).append("\r\n").append(LARRY_CSV).append("\r\n").append(GRACE_CSV).append("\r\n").toString();
	
	/** List of populated customer beans to use for testing */
	public static final List<CustomerBean> CUSTOMERS = Arrays.asList(JOHN, BOB, ALICE, BILL, MIRANDA, STEVE, ADA,
		SERGEI, LARRY, GRACE);
	
	/** List of populated customer string beans to use for testing */
	public static final List<CustomerStringBean> STRING_CUSTOMERS = Arrays.asList(JOHN_STRING, BOB_STRING,
		ALICE_STRING, BILL_STRING, MIRANDA_STRING, STEVE_STRING, ADA_STRING, SERGEI_STRING, LARRY_STRING, GRACE_STRING);
	
	/**
	 * An easy and non-deprecated non-lenient way of creating Date objects.
	 * 
	 * @param year
	 *            the year, e.g. 2007 is year 2007
	 * @param month
	 *            the month, where 1 == January
	 * @param dayOfMonth
	 *            the day of the month, where 1 == first day of the month
	 * @return a Date object with time set to midnight, ie. hour = 00, minutes = 00, seconds = 00 and milliseconds = 000
	 */
	public static Date date(final int year, final int month, final int dayOfMonth) {
		final Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.set(year, month - 1, dayOfMonth, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * An easy and non-deprecated non-lenient way of creating Date objects.
	 * 
	 * @param year
	 *            the year, e.g. 2007 is year 2007
	 * @param month
	 *            the month, where 1 == January
	 * @param dayOfMonth
	 *            the day of the month, where 1 == first day of the month
	 * @param hour
	 *            the hour in 24 hour format where 0 == midnight
	 * @param minute
	 *            is the minute 0-59
	 * @param second
	 *            is the seconds 0-59
	 * @return a Date object with time set to midnight, ie. hour = 00, minutes = 00, seconds = 00 and milliseconds = 000
	 */
	public static Date date(final int year, final int month, final int dayOfMonth, final int hour, final int minute,
		final int second) {
		final Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.set(year, month - 1, dayOfMonth, hour, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
}
