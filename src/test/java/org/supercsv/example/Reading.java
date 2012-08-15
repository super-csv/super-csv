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

import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.mock.CustomerBean;
import org.supercsv.prefs.CsvPreference;

/**
 * Examples of reading CSV files.
 */
public class Reading {
	
	private static final String CSV_FILENAME = "src/test/resources/customers.csv";
	
	public static void main(String[] args) throws Exception {
		readWithCsvBeanReader();
		readWithCsvListReader();
		readWithCsvMapReader();
		partialReadWithCsvBeanReader();
		partialReadWithCsvMapReader();
	}
	
	/**
	 * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
	 * columns are read as null (hence the NotNull() for mandatory columns).
	 * 
	 * @return the cell processors
	 */
	private static CellProcessor[] getProcessors() {
		
		final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
		StrRegEx.registerMessage(emailRegex, "must be a valid email address");
		
		final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), // customerNo (must be unique)
			new NotNull(), // firstName
			new NotNull(), // lastName
			new ParseDate("dd/MM/yyyy"), // birthDate
			new NotNull(), // mailingAddress
			new Optional(new ParseBool()), // married
			new Optional(new ParseInt()), // numberOfKids
			new NotNull(), // favouriteQuote
			new StrRegEx(emailRegex), // email
			new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
		};
		
		return processors;
	}
	
	/**
	 * An example of reading using CsvBeanReader.
	 */
	private static void readWithCsvBeanReader() throws Exception {
		
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			// the header elements are used to map the values to the bean (names must match)
			final String[] header = beanReader.getCsvHeader(true);
			final CellProcessor[] processors = getProcessors();
			
			CustomerBean customer;
			while( (customer = beanReader.read(CustomerBean.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
					beanReader.getRowNumber(), customer));
			}
			
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
	
	/**
	 * An example of reading using CsvListReader.
	 */
	private static void readWithCsvListReader() throws Exception {
		
		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			listReader.getCsvHeader(true); // skip the header (can't be used with CsvListReader)
			final CellProcessor[] processors = getProcessors();
			
			List<Object> customerList;
			while( (customerList = listReader.read(processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customerList=%s", listReader.getLineNumber(),
					listReader.getRowNumber(), customerList));
			}
			
		}
		finally {
			if( listReader != null ) {
				listReader.close();
			}
		}
	}
	
	/**
	 * An example of reading using CsvMapReader.
	 */
	private static void readWithCsvMapReader() throws Exception {
		
		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			// the header columns are used as the keys to the Map
			final String[] header = mapReader.getCsvHeader(true);
			final CellProcessor[] processors = getProcessors();
			
			Map<String, Object> customerMap;
			while( (customerMap = mapReader.read(header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customerMap=%s", mapReader.getLineNumber(),
					mapReader.getRowNumber(), customerMap));
			}
			
		}
		finally {
			if( mapReader != null ) {
				mapReader.close();
			}
		}
	}
	
	/**
	 * An example of partial reading using CsvBeanReader.
	 */
	private static void partialReadWithCsvBeanReader() throws Exception {
		
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			beanReader.getCsvHeader(true); // skip past the header (we're defining our own)
			
			// only map the first 3 columns - setting header elements to null means those columns are ignored
			final String[] header = new String[] { "customerNo", "firstName", "lastName", null, null, null, null, null,
				null, null };
			
			// no processing required for ignored columns
			final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), new NotNull(),
				new NotNull(), null, null, null, null, null, null, null };
			
			CustomerBean customer;
			while( (customer = beanReader.read(CustomerBean.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
					beanReader.getRowNumber(), customer));
			}
			
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
	
	/**
	 * An example of partial reading using CsvMapReader.
	 */
	private static void partialReadWithCsvMapReader() throws Exception {
		
		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			mapReader.getCsvHeader(true); // skip past the header (we're defining our own)
			
			// only map the first 3 columns - setting header elements to null means those columns are ignored
			final String[] header = new String[] { "customerNo", "firstName", "lastName", null, null, null, null, null,
				null, null };
			
			// apply some constraints to ignored columns (just because we can)
			final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), new NotNull(),
				new NotNull(), new NotNull(), new NotNull(), new Optional(), new Optional(), new NotNull(),
				new NotNull(), new LMinMax(0L, LMinMax.MAX_LONG) };
			
			Map<String, Object> customerMap;
			while( (customerMap = mapReader.read(header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customerMap=%s", mapReader.getLineNumber(),
					mapReader.getRowNumber(), customerMap));
			}
			
		}
		finally {
			if( mapReader != null ) {
				mapReader.close();
			}
		}
	}
	
}
