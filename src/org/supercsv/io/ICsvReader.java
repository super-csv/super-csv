package org.supercsv.io;

import java.io.IOException;

import org.supercsv.prefs.CsvPreference;

/**
 * The parent type for csv-readers. It defines the basic functionality of any readers.
 * 
 * @author Kasper B. Graversen
 */
interface ICsvReader {

/** close the stream */
public void close() throws IOException;

/**
 * Get column N of the current line This is useful for parsing e.g. first column and react by reading the line on the
 * basis of that first argument
 * 
 * @since 1.0
 */
public String get(int N) throws IOException, IndexOutOfBoundsException;

/**
 * This method is used to get an optional header of the csv file and move the file curser to the first row containing
 * data (the second row from the top) The header can subsequently be used as the <code>nameMapper</code> for read
 * operations.
 * 
 * @param firstLineCheck
 *            denotes whether a check should be made to ensure only the first line of the file can represent a header
 * @throws IOException
 *             if an I/O error occurs or if the method is not called as the first read operation on a source. *
 * @since 1.0
 */
public String[] getCSVHeader(boolean firstLineCheck) throws IOException;

/**
 * gets the current position in the file. The first line of the file is line number 1
 * 
 * @since 1.0
 */
public int getLineNumber();

/**
 * returns the length of the current line
 * 
 * @since 1.0
 */
public int length() throws IOException;

/**
 * Determine how the reader reads the input source.
 * 
 * @since 1.0
 */
public ICsvReader setPreferences(CsvPreference preference);

/**
 * Determine how the reader reads the csv file.
 * 
 * @since 1.10
 */
public ICsvReader setTokenizer(ITokenizer tokenizer);

}
