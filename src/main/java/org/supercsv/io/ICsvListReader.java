package org.supercsv.io;

import java.io.IOException;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Interface for readers that read into Lists.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvListReader extends ICsvReader {
	/**
	 * Reads a line into a List of Strings. This is the traditional and hence very low-level approach to CSV file
	 * reading and consequently should be avoided.
	 * 
	 * @return null if end-of-file or a list representing the read line
	 * @since 1.0
	 */
	List<String> read() throws IOException;
	
	/**
	 * Reads a line into a List of Strings with the possibility to process the entries first (restricted by the fact
	 * that the values must fit into a List of Strings).
	 * 
	 * @param processors
	 *            An array of processors that processes each entry. <code>null</code> entries denote no processing for
	 *            that cell
	 * @return null if end-of-file or a list representing the read line
	 * @throws IOException
	 * @since 1.0
	 */
	List<String> read(CellProcessor... processors) throws IOException;
}
