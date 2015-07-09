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
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * CsvResultSetWriter writes a CSV file by mapping each column of the {@code ResultSet}
 * to a column in CSV file using the column names stored in {@code ResultSetMetaData}
 * 
 * @author SingularityFX
 *
 */
public class CsvResultSetWriter extends AbstractCsvWriter implements ICsvResultSetWriter {
	
	/**
	 * Constructs a new {@code CsvResultSetWriter} with the supplied {@code Writer}
	 * and CSV preferences. Note that the {@code writer} will be wrapped in a 
	 * {@code BufferedWriter} before accessed.
	 * 
	 * @param 	writer
	 * 				the writer
	 * @param 	preference
	 * 				the CSV preferences
	 * @throws 	NullPointerException
	 *          	if writer or preference are null
	 */
	public CsvResultSetWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public void write(final ResultSet resultSet) throws SQLException, IOException {
		if (resultSet == null) {
			throw new NullPointerException("ResultSet cannot be null");
		}
		
		writeHeaders(resultSet); // increments row and line number
		writeContents(resultSet); // increments row and line number before writing of each row
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public void write(ResultSet resultSet, CellProcessor[] writeProcessors) throws SQLException, IOException {
		if (resultSet == null) {
			throw new NullPointerException("ResultSet cannot be null");
		}
		if (writeProcessors == null) {
			throw new NullPointerException("CellProcessor[] cannot be null");
		}
		
		writeHeaders(resultSet); // increments row and line number
		writeContents(resultSet, writeProcessors); // increments row and line number before writing of each row
	}

	private void writeHeaders(ResultSet resultSet) throws SQLException, IOException {
		super.incrementRowAndLineNo(); // This will allow the correct row/line numbers to be used in any exceptions thrown before writing occurs
		
		final ResultSetMetaData meta = resultSet.getMetaData();
		final int numberOfColumns = meta.getColumnCount();
		final List<Object> headers = new LinkedList<Object>();
		for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++) {
			headers.add(meta.getColumnName(columnIndex));
		}
		super.writeRow(headers);
	}
	
	private void writeContents(ResultSet resultSet) throws SQLException, IOException {
		final int numberOfColumns = resultSet.getMetaData().getColumnCount();
		final List<Object> objects = new LinkedList<Object>();
		while (resultSet.next()) {
			super.incrementRowAndLineNo(); // This will allow the correct row/line numbers to be used in any exceptions thrown before writing occurs
			objects.clear();
			for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++) {
				objects.add(resultSet.getObject(columnIndex));
			}
			super.writeRow(objects);
		}
	}
	
	private void writeContents(ResultSet resultSet, CellProcessor[] writeProcessors) throws SQLException, IOException {
		final int numberOfColumns = resultSet.getMetaData().getColumnCount();
		final List<Object> objects = new LinkedList<Object>();
		final List<Object> processedColumns = new LinkedList<Object>();
		while (resultSet.next()) {
			super.incrementRowAndLineNo(); // This will allow the correct row/line numbers to be used in any exceptions thrown before writing occurs
			objects.clear();
			for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++) {
				objects.add(resultSet.getObject(columnIndex));
			}
			Util.executeCellProcessors(processedColumns, objects, writeProcessors, getLineNumber(), getRowNumber());
			super.writeRow(processedColumns);
		}
	}
}
