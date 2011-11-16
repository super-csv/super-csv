package org.supercsv.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.supercsv.exception.SuperCSVException;

/**
 * The tokenizer is an internal mechanism to the csv parser
 * 
 * @author Kasper B. Graversen
 */
public interface ITokenizer extends Closeable {
	
	int getLineNumber();
	
	/**
	 * Read a csv line into the list result (can span multiple lines in the file) The result list is cleared as the
	 * first thing in the method
	 * 
	 * @param result
	 *            the result of the operation
	 * @return true if something was read. and false if EOF
	 * @throws IOException
	 *             when an io-error occurs
	 * @throws SuperCSVException
	 *             on errors in parsing the input
	 * @since 1.0
	 */
	boolean readStringList(List<String> result) throws IOException, SuperCSVException;
}
