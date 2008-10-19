package org.supercsv.io;

import java.io.IOException;

import org.supercsv.prefs.CsvPreference;
import org.supercsv.exception.SuperCSVException;

/**
 * Super Type for all csv writers.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvWriter {
/**
 * close the stream *
 * 
 * @since 1.0
 */
void close() throws IOException;

/**
 * Flush the CSV lines to their intended destination,
 * while flushing the underlined stream.
 * @since 1.53
 */
public void flush() throws IOException, SuperCSVException;

/**
 * return the number of lines written so far. The first line is 1 *
 * 
 * @since 1.0
 */
int getLineNumber();

/**
 * Determine how the writer writes the destination. *
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
 * @since 1.0
 */
void writeHeader(String... header) throws IOException;

}
