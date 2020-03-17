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
package org.supercsv.example;

import org.supercsv.SuperCsvTestUtils;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvDelayException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.mock.CustomerBean;
import org.supercsv.prefs.CallBackOnException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.DelayCellProcessorExceptions;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DelayExceptions {

	private static final String ROOT_PATH = DelayExceptions.class.getResource("/").getPath() + "/";

	private static final String CSV_FILENAME = ROOT_PATH + "customers_exceptions.csv";

	public static void main(String[] args) throws Exception {
		readWithDelayCellProcessorsExceptions();
		writeWithDelayCellProcessorExceptions();
	}

	private static void readWithDelayCellProcessorsExceptions() throws Exception {
		readNormal();
		readDelayCellProcessorExceptions();
	}

	private static void readNormal() throws Exception {
		CsvPreference preference = CsvPreference.STANDARD_PREFERENCE;
		System.out.println("readNormal Exceptions:");
		read(preference);
		System.out.println("==================================");
	}

	private static void readDelayCellProcessorExceptions() throws Exception {
		CsvPreference preference = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
			.delayCellProcessorExceptions(new DelayCellProcessorExceptions(false, new CallBackOnException() {
				public Object process(Object rawColumns) {
					return "###";
				}
			})).build();
		System.out.println("readDelayCellProcessorExceptions Exceptions:");
		read(preference);
		System.out.println("==================================");
	}

	private static void read(CsvPreference preference) throws Exception {
		ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(CSV_FILENAME), preference);

		// the header elements are used to map the values to the bean (names must match)
		final String[] header = beanReader.getHeader(true);
		final CellProcessor[] processors = getReadProcessors();

		CustomerBean customer = null;
		do {
			try {
				customer = beanReader.read(CustomerBean.class, header, processors);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		while( customer != null );
		beanReader.close();
	}

	private static void writeWithDelayCellProcessorExceptions() throws Exception {
		skipWriteExceptionsRow();
		writeExceptionsRow();
	}

	private static void skipWriteExceptionsRow() throws Exception {
		CsvPreference preference = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
			.delayCellProcessorExceptions(new DelayCellProcessorExceptions(true)).build();
		System.out.println("skipWriteExceptionsRow Exceptions:");
		write(preference, "skipWriteExceptionsRow");
		System.out.println("==================================");
	}

	private static void writeExceptionsRow() throws Exception {
		CsvPreference preference = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
			.delayCellProcessorExceptions(new DelayCellProcessorExceptions(false, new CallBackOnException() {
				public Object process(Object rawColumns) {
					return "###";
				}
			})).build();
		System.out.println("writeExceptionsRow Exceptions:");
		write(preference, "writeExceptionsRow");
		System.out.println("==================================");
	}

	private static void write(CsvPreference preference, String filename) throws Exception {
		List<CustomerBean> customers = getWriteData();

		ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter("target/" + filename + ".csv"), preference);

		// the header elements are used to map the bean values to each column (names must match)
		final String[] header = new String[] { "customerNo", "firstName", "lastName", "birthDate", "mailingAddress",
			"married", "numberOfKids", "favouriteQuote", "email", "loyaltyPoints" };
		final CellProcessor[] processors = getWriteProcessors();

		// write the header
		beanWriter.writeHeader(header);

		// write the beans
		for( final CustomerBean customer : customers ) {
			try {
				beanWriter.write(customer, header, processors);
			}
			catch(SuperCsvDelayException e) {
				System.out.println(e.toString());
			}
		}

		beanWriter.close();
	}

	private static List<CustomerBean> getWriteData() {
		// create the customer beans
		final CustomerBean john = new CustomerBean("1", "John", "Dunbar",
			new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(), SuperCsvTestUtils.time(10, 20, 0),
			"1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", null, null,
			"\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L);
		final CustomerBean david = new CustomerBean("2", "david", null,
			new GregorianCalendar(1949, Calendar.FEBRUARY, 10).getTime(), SuperCsvTestUtils.time(10, 20, 0), null, true,
			0, null, "david@hotmail.com", 111111L);
		final CustomerBean bob = new CustomerBean("3", "Bob", "Down",
			new GregorianCalendar(1919, Calendar.FEBRUARY, 25).getTime(), SuperCsvTestUtils.time(10, 20, 0),
			"1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States", true, 0,
			"\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind", "bobdown@hotmail.com", 123456L);
		final List<CustomerBean> customers = Arrays.asList(john, david, bob);
		return customers;
	}

	private static CellProcessor[] getReadProcessors() {

		final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
		StrRegEx.registerMessage(emailRegex, "must be a valid email address");

		final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), // customerNo (must be unique)
			new NotNull(), // firstName
			new NotNull(), // lastName
			new ParseDate("dd/MM/yyyy"), // birthDate
			new ParseSqlTime("HH:mm:ss"), //birthTime
			new NotNull(), // mailingAddress
			new Optional(new ParseBool()), // married
			new Optional(new ParseInt()), // numberOfKids
			new NotNull(), // favouriteQuote
			new StrRegEx(emailRegex), // email
			new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
		};

		return processors;
	}

	private static CellProcessor[] getWriteProcessors() {

		final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), // customerNo (must be unique)
			new NotNull(), // firstName
			new NotNull(), // lastName
			new FmtDate("dd/MM/yyyy"), // birthDate
			new NotNull(), // mailingAddress
			new Optional(new FmtBool("Y", "N")), // married
			new Optional(), // numberOfKids
			new NotNull(), // favouriteQuote
			new NotNull(), // email
			new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
		};

		return processors;
	}
}
