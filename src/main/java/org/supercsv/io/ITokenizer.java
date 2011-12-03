package org.supercsv.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.supercsv.exception.SuperCSVException;

/**
 * The interface for tokenizers, which are responsible for reading the CSV file, line by line.
 * 
 * @author Kasper B. Graversen
 */
public interface ITokenizer extends Closeable {
	
	/**
	 * Gets the line number. Line numbering begins at 0. This number increments at every line terminator as the data is
	 * read.
	 * 
	 * @return the line number
	 */
	int getLineNumber();
	
	/**
	 * Read a CSV line into the supplied List (which can potentially span multiple lines in the file). The result list
	 * is cleared as the first operation in the method.
	 * 
	 * @param result
	 *            the List to read into
	 * @return true if something was read, and false if EOF
	 * @throws IOException
	 *             when an IOException occurs
	 * @throws SuperCSVException
	 *             on errors in parsing the input
	 * @since 1.0
	 */
	boolean readStringList(List<String> result) throws IOException, SuperCSVException;
}
