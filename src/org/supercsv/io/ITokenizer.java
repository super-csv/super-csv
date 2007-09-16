package org.supercsv.io;

import java.io.IOException;
import java.util.List;

import org.supercsv.exception.SuperCSVException;

/**
 * The tokenizer is an internal mechanism to the csv parser
 * 
 * @author Kasper B. Graversen
 */
interface ITokenizer {

	/**
	 * Close the underlying stream
	 * 
	 * @throws IOException
	 *             when raised by operating on the underlying stream
	 */
	public void close() throws IOException;

	public int getLineNumber();

	/**
	 * Read a csv line into the list result (can span multiple lines in the file) The result list is cleared as the
	 * first thing in the method
	 * 
	 * @param result
	 *            the result of the operation
	 * @return true if something was read. and false if EOF
	 * @throws IOException
	 * @since 1.0
	 */
	public boolean readStringList(List<String> result) throws IOException, SuperCSVException;
}