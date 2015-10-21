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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;
import org.supercsv.util.Util;

/**
 * Defines the standard behaviour of a CSV reader.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Vyacheslav Pushkin
 */
public abstract class AbstractCsvReader implements ICsvReader {
	
	private static final String COLUMN_NAME_NOT_FOUND =
		"Column name not found: columnMapping contains column name that is not actually present in CSV header";
	private static final String COLUMN_NOT_FOUND =
		"Column not found: current row does not contain a column specified by columnMapping";

	private final ITokenizer tokenizer;
	
	private final CsvPreference preferences;
	
	private List<String> csvHeader;

	// the current tokenized columns
	private final List<String> columns = new ArrayList<String>();
	
	// the number of CSV records read
	private int rowNumber = 0;
	
	/**
	 * Constructs a new <tt>AbstractCsvReader</tt>, using the default {@link Tokenizer}.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if reader or preferences are null
	 */
	public AbstractCsvReader(final Reader reader, final CsvPreference preferences) {
		if( reader == null ) {
			throw new NullPointerException("reader should not be null");
		} else if( preferences == null ) {
			throw new NullPointerException("preferences should not be null");
		}
		
		this.preferences = preferences;
		this.tokenizer = new Tokenizer(reader, preferences);
	}
	
	/**
	 * Constructs a new <tt>AbstractCsvReader</tt>, using a custom {@link Tokenizer} (which should have already been set
	 * up with the Reader, CsvPreference, and CsvContext). This constructor should only be used if the default Tokenizer
	 * doesn't provide the required functionality.
	 * 
	 * @param tokenizer
	 *            the tokenizer
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if tokenizer or preferences are null
	 */
	public AbstractCsvReader(final ITokenizer tokenizer, final CsvPreference preferences) {
		if( tokenizer == null ) {
			throw new NullPointerException("tokenizer should not be null");
		} else if( preferences == null ) {
			throw new NullPointerException("preferences should not be null");
		}
		
		this.preferences = preferences;
		this.tokenizer = tokenizer;
	}
	
	/**
	 * Closes the Tokenizer and its associated Reader.
	 */
	public void close() throws IOException {
		tokenizer.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String get(final int n) {
		return columns.get(n - 1); // column numbers start at 1
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getHeader(final boolean firstLineCheck) throws IOException {
		
		if( firstLineCheck && tokenizer.getLineNumber() != 0 ) {
			throw new SuperCsvException(String.format(
				"CSV header must be fetched as the first read operation, but %d lines have already been read",
				tokenizer.getLineNumber()));
		}
		
		if( readRow() ) {
			return columns.toArray(new String[columns.size()]);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getLineNumber() {
		return tokenizer.getLineNumber();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUntokenizedRow() {
		return tokenizer.getUntokenizedRow();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRowNumber() {
		return rowNumber;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int length() {
		return columns.size();
	}
	
	/**
	 * Gets the tokenized columns.
	 * 
	 * @return the tokenized columns
	 */
	protected List<String> getColumns() {
		return columns;
	}
	
	/**
	 * Gets the preferences.
	 * 
	 * @return the preferences
	 */
	protected CsvPreference getPreferences() {
		return preferences;
	}
	
	/**
	 * Calls the tokenizer to read a CSV row. The columns can then be retrieved using {@link #getColumns()}.
	 * 
	 * @return true if something was read, and false if EOF
	 * @throws IOException
	 *             when an IOException occurs
	 * @throws SuperCsvException
	 *             on errors in parsing the input
	 */
	protected boolean readRow() throws IOException {
		if( tokenizer.readColumns(columns) ) {
			rowNumber++;
			return true;
		}
		return false;
	}
	
	/**
	 * Executes the supplied cell processors on the last row of CSV that was read and populates the supplied List of
	 * processed columns.
	 * 
	 * @param processedColumns
	 *            the List to populate with processed columns
	 * @param processors
	 *            the cell processors
	 * @return the updated List
	 * @throws NullPointerException
	 *             if processedColumns or processors is null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if the wrong number of processors are supplied, or CellProcessor execution failed
	 */
	protected List<Object> executeProcessors(final List<Object> processedColumns, final CellProcessor[] processors) {
		Util.executeCellProcessors(processedColumns, getColumns(), processors, getLineNumber(), getRowNumber());
		return processedColumns;
	}
	
	/**
	 * 
	 * Template method used by concrete CsvReader classes as part of implementation of certain read() methods.
	 *
	 * Uses the following methods containing implementation-specific details:
	 * <ul>
	 *   <li>getCellProcessorFromMapEntryValue(M mapping)</li>
	 *   <li>{@literal addValueToDestination(T destination, Object cellValue, Entry<String, M> entry)}</li>
	 * </ul>
	 *
	 * @param columnMapping
	 *             map where key is CSV column name and value can be different depending on concrete CsvReader implementation
	 * @param <M>
	 *             type of columnMapping map value
	 * @param destination
	 *             object to be filled with data read from CSV row
	 * @param <T>
	 *             type of destination
	 * @return object filled with data read from CSV row or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws IllegalArgumentException
	 * 			   if columnMapping contains column name that is not actually present in CSV header
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if current row does not contain a column specified by columnMapping, or CellProcessor execution failed
	 */
	protected <T, M> T readWithColumnMapping(final Map<String, M> columnMapping, final T destination) throws IOException {

		// Obtain CSV header (actual column names from CSV file) if it hasn't been done already
		if( csvHeader == null ) {
			csvHeader = Arrays.asList(getHeader(true));
		}

		if( readRow() ) {
			for (Entry<String, M> entry : columnMapping.entrySet()) {
				// find column number by looking up the columnName in the CSV header
				int i = csvHeader.indexOf(entry.getKey());
				if( i == -1 ) {
					throw new IllegalArgumentException(COLUMN_NAME_NOT_FOUND);
				}

				Object cellValue;
				CellProcessor processor;

				if( (processor = getCellProcessorFromMapEntryValue(entry.getValue())) == null ) {
					cellValue = getColumns().get(i);
				} else {
					final CsvContext context = new CsvContext(getLineNumber(), getRowNumber(), i + 1);
					context.setRowSource(new ArrayList<Object>(getColumns()));
					try {
						cellValue = processor.execute((Object)getColumns().get(i), context);
					} catch (IndexOutOfBoundsException e) {
						throw new SuperCsvException(COLUMN_NOT_FOUND, context);
					}
				}

				addValueToDestination(destination, cellValue, entry);
			}
			return destination;
		}

		return null; // EOF
	}

	/**
	 * 
	 * Takes an object which either contains a CellProcessor or is a CellProcessor itself
	 * (depending on concrete CsvReader implementation) and returns a CellProcessor.
	 *
	 * Used by template method <tt>readWithColumnMapping()</tt>. Optional method (default implementation throws
	 * <tt>UnsupportedOperationException</tt>).
	 *
	 * @param cellProcessorOrItsContainer
	 *             object which either contains a CellProcessor or is a CellProcessor itself
	 *             (depending on CsvReader implementation) and returns a CellProcessor
	 * @param <M>
	 *             type of cellProcessorOrItsContainer
	 * @return CellProcessor instance
	 */
	protected <M> CellProcessor getCellProcessorFromMapEntryValue(M cellProcessorOrItsContainer) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * Adds a value read from a single cell in CSV file row to a destination object (which exact type is determined by concrete CsvReader implementation).
	 *
	 * Used by template method <tt>readWithColumnMapping()</tt>. Optional method (default implementation throws
	 * <tt>UnsupportedOperationException</tt>).
	 *
	 * @param destination
	 *             object to be filled with data read from CSV row
	 * @param <T>
	 *             type of destination
	 * @param cellValue
	 *             value from a single cell in CSV file row
	 * @param entry
	 *             Map entry where key is CSV column name and value can be different depending on concrete CsvReader implementation
	 * @param <M>
	 *             type of map the <tt>entry</tt> is coming from
	 */
	protected <T, M> void addValueToDestination(T destination, Object cellValue, Entry<String, M> entry) {
		throw new UnsupportedOperationException();
	}

}
