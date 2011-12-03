package org.supercsv.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

import org.supercsv.prefs.CsvPreference;

/**
 * The interface for CSV writers.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvWriter extends Closeable, Flushable {
	
	/**
	 * Return the number of lines written so far. The first line is 1.
	 * 
	 * @since 1.0
	 */
	int getLineNumber();
	
	/**
	 * Sets the CSV preferences.
	 * 
	 * @since 1.0
	 */
	ICsvWriter setPreferences(CsvPreference preference);
	
	/**
	 * Write a string array as a header. The elements of the array cannot be null.
	 * 
	 * @throws IOException
	 *             When in IO exception occur
	 * @param header
	 *            one or more header Strings
	 * @since 1.0
	 */
	void writeHeader(String... header) throws IOException;
	
}
