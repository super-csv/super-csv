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
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.decoder.CsvDecoder;
import org.supercsv.decoder.DefaultCsvDecoder;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * Defines the standard behaviour of a CSV reader.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public abstract class AbstractCsvReader implements ICsvReader {

	private final LineNumberReader reader;
	
	private final CsvDecoder decoder;
	
	private final CsvPreference preferences;
	
	// the current decoded columns
	private final List<String> columns = new ArrayList<String>();

	// the raw, undecoded CSV row (may span multiple lines)
	private final StringBuilder undecodedRow = new StringBuilder();

	private final boolean ignoreEmptyLines;

	private final CommentMatcher commentMatcher;

	private final  int maxLinesPerRow;
	
	// the number of CSV records read
	private int rowNumber = 0;
	
	/**
	 * Constructs a new <tt>AbstractCsvReader</tt>, using the default {@link DefaultCsvDecoder}.
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

		this.reader = new LineNumberReader(reader);
		this.preferences = preferences;
		this.decoder = preferences.getDecoder();
		this.decoder.initDecoder(preferences);
		this.ignoreEmptyLines = preferences.isIgnoreEmptyLines();
		this.commentMatcher = preferences.getCommentMatcher();
		this.maxLinesPerRow = preferences.getMaxLinesPerRow();
	}
	
	/**
	 * Closes the Decoder and its associated Reader.
	 */
	public void close() throws IOException {
		reader.close();
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
		
		if( firstLineCheck && reader.getLineNumber() != 0 ) {
			throw new SuperCsvException(String.format(
				"CSV header must be fetched as the first read operation, but %d lines have already been read",
				reader.getLineNumber()));
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
		return reader.getLineNumber();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUndecodedRow() {
		return undecodedRow.toString();
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
	 * Gets the decoded columns.
	 * 
	 * @return the decoded columns
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
	 * Calls the decoder to read a CSV row. The columns can then be retrieved using {@link #getColumns()}.
	 * 
	 * @return true if something was read, and false if EOF
	 * @throws IOException
	 *             when an IOException occurs
	 * @throws SuperCsvException
	 *             on errors in parsing the input
	 */
	protected boolean readRow() throws IOException {
		columns.clear();
		undecodedRow.setLength(0);
		String line;
		int quoteScopeStartingLine = 1;
		// read a line (ignoring empty lines/comments if necessary)
		do {
			line = getLineNumber() == 0 ? Util.subtractBom(reader.readLine()) : reader.readLine();
			if(line == null){
				return false;
			}
		}
		while( ignoreEmptyLines && line.length() == 0 || (commentMatcher != null && commentMatcher.isComment(line)) );
		undecodedRow.append(line);
		columns.addAll(decoder.decode(line, false));
		// the current row parsing is not completed, read the next row to continue parsing (the row may span multiple lines)
		while( decoder.isPending() ) {
			quoteScopeStartingLine++;
			if( maxLinesPerRow > 0 && quoteScopeStartingLine > maxLinesPerRow ) {
				String msg = maxLinesPerRow == 1 ?
						String.format("unexpected end of line while reading quoted column on line %d",
								getLineNumber()) :
						String.format("max number of lines to read exceeded while reading quoted column" +
										" beginning on line %d and ending on line %d",
								quoteScopeStartingLine - ( maxLinesPerRow -1 ), getLineNumber());
				throw new SuperCsvException(msg);
			}
			line = reader.readLine();
			if( line == null ) {
				throw new SuperCsvException(String.format(
						"unexpected end of file while reading quoted column beginning on line %d and ending on line %d",
						getLineNumber() - (quoteScopeStartingLine -1 ) + 1, getLineNumber()));
			}
			undecodedRow.append('\n');
			undecodedRow.append(line);
			columns.addAll(decoder.decode(line, true));
		}
		rowNumber++;
		return true;
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
	 *             if processedColumns or processors are null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if the wrong number of processors are supplied, or CellProcessor execution failed
	 */
	protected List<Object> executeProcessors(final List<Object> processedColumns, final CellProcessor[] processors) {
		Util.executeCellProcessors(processedColumns, getColumns(), processors, getLineNumber(), getRowNumber());
		return processedColumns;
	}
	
}
