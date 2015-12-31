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
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.supercsv.util.BeanField;

/**
 * Examples of reading CSV files.
 */
public class Reading {
	
	private static final String CSV_FILENAME = "src/test/resources/customers.csv";
	
	private static final String VARIABLE_CSV_FILENAME = "src/test/resources/customerswithvariablecolumns.csv";
	
	private static final String CSV_FILENAME_MAP_ARG = "src/test/resources/customers-different-column-names-and-unrelated-column.csv";
	
	private static final String EMAIL_REGEX = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
	
	public static void main(String[] args) throws Exception {
		readWithCsvBeanReader();
		readWithCsvListReader();
		readVariableColumnsWithCsvListReader();
		readWithCsvMapReader();
		partialReadWithCsvBeanReader();
		partialReadWithCsvMapReader();
		readWithCsvBeanReaderMapArg();
		readWithCsvListReaderMapArg();
		readWithCsvMapReaderMapArg();
	}
	
	/**
	 * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
	 * columns are read as null (hence the NotNull() for mandatory columns).
	 * 
	 * @return the cell processors
	 */
	private static CellProcessor[] getProcessors() {
		
		StrRegEx.registerMessage(EMAIL_REGEX, "must be a valid email address");
		
		final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), // customerNo (must be unique)
			new NotNull(), // firstName
			new NotNull(), // lastName
			new ParseDate("dd/MM/yyyy"), // birthDate
			new NotNull(), // mailingAddress
			new Optional(new ParseBool()), // married
			new Optional(new ParseInt()), // numberOfKids
			new NotNull(), // favouriteQuote
			new StrRegEx(EMAIL_REGEX), // email
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
			final String[] header = beanReader.getHeader(true);
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
			
			listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
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
	 * An example of reading a file with a variable number of columns using CsvListReader. It demonstrates that you can
	 * still use cell processors, but you must execute them by calling the executeProcessors() method on the reader,
	 * instead of supplying processors to the read() method. In this scenario, the last column (birthDate) is sometimes
	 * missing.
	 */
	private static void readVariableColumnsWithCsvListReader() throws Exception {
		
		final CellProcessor[] allProcessors = new CellProcessor[] { new UniqueHashCode(), // customerNo (must be unique)
			new NotNull(), // firstName
			new NotNull(), // lastName
			new ParseDate("dd/MM/yyyy") }; // birthDate
		
		final CellProcessor[] noBirthDateProcessors = new CellProcessor[] { allProcessors[0], // customerNo
			allProcessors[1], // firstName
			allProcessors[2] }; // lastName
		
		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(VARIABLE_CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
			
			while( (listReader.read()) != null ) {
				
				// use different processors depending on the number of columns
				final CellProcessor[] processors;
				if( listReader.length() == noBirthDateProcessors.length ) {
					processors = noBirthDateProcessors;
				} else {
					processors = allProcessors;
				}
				
				final List<Object> customerList = listReader.executeProcessors(processors);
				System.out.println(String.format("lineNo=%s, rowNo=%s, columns=%s, customerList=%s",
					listReader.getLineNumber(), listReader.getRowNumber(), customerList.size(), customerList));
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
			final String[] header = mapReader.getHeader(true);
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
			
			beanReader.getHeader(true); // skip past the header (we're defining our own)
			
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
			
			mapReader.getHeader(true); // skip past the header (we're defining our own)
			
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
	
	/**
	 * <p>Example of reading using CsvBeanReader's read(Map<String, BeanField> columnMapping) method</p>
	 * 
	 * <p>This method is useful when:</p>
	 *
	 * <ul>
	 *   <li>CSV column names are different from bean field names</li>
	 *   <li>Quantity of columns in CSV file is large and only small number of them is needed to instantiate the bean </li>
	 * </ul>
	 * 
	 */
	private static void readWithCsvBeanReaderMapArg() throws IOException {
		
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(CSV_FILENAME_MAP_ARG), CsvPreference.STANDARD_PREFERENCE);
			
			Map<String, BeanField> columnMapping = getColumnMappingBean();
			
			CustomerBean customer;
			while( (customer = beanReader.read(CustomerBean.class, columnMapping)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
					beanReader.getRowNumber(), customer));
			}
			
		} finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
	
	/**
	 * <p>Example of reading using CsvListReader's read(Map<String, BeanField> columnMapping) method</p>
	 * 
	 * <p>This method is useful when not all columns in CSV file are needed to be added to the List.</p>
	 * 
	 */
	private static void readWithCsvListReaderMapArg() throws IOException {
		
		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(new FileReader(CSV_FILENAME_MAP_ARG), CsvPreference.STANDARD_PREFERENCE);
			
			// Create map where key is CSV column name and value is optional cell processor instance
			Map<String, CellProcessor> columnMapping = getColumnMappingListMap();
			
			List<Object> customer;
			while( (customer = listReader.read(columnMapping)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", listReader.getLineNumber(),
					listReader.getRowNumber(), customer));
			}
			
		} finally {
			if( listReader != null ) {
				listReader.close();
			}
		}
	}
	
	
	/**
	 * <p>Example of reading using CsvMapReader's read(Map<String, BeanField> columnMapping) method</p>
	 * 
	 * <p>This method can be used when not all columns in CSV file are needed to be added to the resulting Map and is more
	 * convenient than <tt>read(String... nameMapping)</tt> when number of columns in CSV fle is large.</p>
	 * 
	 */
	private static void readWithCsvMapReaderMapArg() throws IOException {
		
		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(CSV_FILENAME_MAP_ARG), CsvPreference.STANDARD_PREFERENCE);
			
			// Create map where key is CSV column name and value is optional cell processor instance
			Map<String, CellProcessor> columnMapping = getColumnMappingListMap();
			
			Map<String, Object> customer;
			while( (customer = mapReader.read(columnMapping)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", mapReader.getLineNumber(),
					mapReader.getRowNumber(), customer));
			}
			
		} finally {
			if( mapReader != null ) {
				mapReader.close();
			}
		}
	}
	
	/**
	 * Sets up the processors used for the examples (Bean reader).
	 * 
	 * @return the cell processors
	 */
	private static Map<String, BeanField> getColumnMappingBean() {
		// Create map where key is CSV column name and value is BeanField object which contains bean field name 
		// and optional cell processor instance
		Map<String, BeanField> columnMapping = new HashMap<String, BeanField>();
		// CSV column name and bean field name doesn't have to be the same.
		// You don't have to include all CSV columns to the map (not included columns will be ignored).
		columnMapping.put("customer id", BeanField.of("customerNo", new UniqueHashCode()));
		columnMapping.put("first name", BeanField.of("firstName", new NotNull()));
		columnMapping.put("birth date", BeanField.of("birthDate", new ParseDate("dd/MM/yyyy")));
		columnMapping.put("mailing address", BeanField.of("mailingAddress", new NotNull()));
		columnMapping.put("married", BeanField.of("married", new Optional(new ParseBool())));
		columnMapping.put("number of kids", BeanField.of("numberOfKids", new Optional(new ParseInt())));
		columnMapping.put("favourite quote", BeanField.of("favouriteQuote", new NotNull()));
		columnMapping.put("E-Mail", BeanField.of("email", new StrRegEx(EMAIL_REGEX)));
		columnMapping.put("loyalty points", BeanField.of("loyaltyPoints", new LMinMax(0L, LMinMax.MAX_LONG)));
		return columnMapping;
	}
	
	/**
	 * Sets up the processors used for the examples (List and Map readers).
	 * 
	 * @return the cell processors
	 */
	private static Map<String, CellProcessor> getColumnMappingListMap() {
		// Create map where key is CSV column name and value is an optional cell processor instance.
		// You shall use an ordered map implementation in order to preserve the order of columns.
		// in resulting List when using CsvListReader.
		Map<String, CellProcessor> columnMapping = new LinkedHashMap<String, CellProcessor>();
		// CSV column name and bean field name doesn't have to be the same.
		// You don't have to include all CSV columns to the map (not included columns will be ignored).
		columnMapping.put("customer id", new UniqueHashCode());
		columnMapping.put("first name", new NotNull());
		columnMapping.put("birth date", new ParseDate("dd/MM/yyyy"));
		columnMapping.put("mailing address", new NotNull());
		columnMapping.put("married", new Optional(new ParseBool()));
		columnMapping.put("number of kids", new Optional(new ParseInt()));
		columnMapping.put("favourite quote", new NotNull());
		columnMapping.put("E-Mail", new StrRegEx(EMAIL_REGEX));
		columnMapping.put("loyalty points", new LMinMax(0L, LMinMax.MAX_LONG));
		return columnMapping;
	}
	
}
