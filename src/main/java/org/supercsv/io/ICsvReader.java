package org.supercsv.io;

import java.io.Closeable;
import java.io.IOException;

import org.supercsv.prefs.CsvPreference;

/**
 * The interface for CSV readers.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvReader extends Closeable {
	
	/**
	 * Get column N of the current line.
	 * 
	 * @param n
	 *            the column to get
	 * @return the n'th column
	 * @throws IOException
	 * @throws IndexOutOfBoundsException
	 * @since 1.0
	 */
	String get(int n) throws IOException, IndexOutOfBoundsException;
	
	/**
	 * This method is used to get an optional header of the CSV file and move the file cursor to the first row
	 * containing data (the second row from the top) The header can subsequently be used as the <code>nameMapper</code>
	 * array for read operations.
	 * 
	 * @param firstLineCheck
	 *            denotes whether a check should be made to ensure only the first line of the file can represent a
	 *            header
	 * @throws IOException
	 *             if an I/O error occurs or if the method is not called as the first read operation on a source.
	 * @since 1.0
	 */
	String[] getCSVHeader(boolean firstLineCheck) throws IOException;
	
	/**
	 * Gets the current position in the file. The first line of the file is line number 1.
	 * 
	 * @since 1.0
	 */
	int getLineNumber();
	
	/**
	 * Returns the length of the current line.
	 * 
	 * @since 1.0
	 */
	int length() throws IOException;
	
	/**
	 * Sets the CSV preferences.
	 * 
	 * @since 1.0
	 */
	ICsvReader setPreferences(CsvPreference preference);
	
	/**
	 * Sets the tokenizer, responsible for reading the input.
	 * 
	 * @since 1.10
	 */
	ICsvReader setTokenizer(ITokenizer tokenizer);
	
}
