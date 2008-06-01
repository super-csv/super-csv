package org.supercsv.io;

import java.io.IOException;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Supertype for readers reading into lists.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvListReader extends ICsvReader {
/**
 * Plain reading a line into a list of strings. This is the traditional and hence very low-level approach to CSV file
 * reading and consequently should be avoided.
 * 
 * @return null if end-of-file or a list representing the read line *
 * @since 1.0
 */
public List<String> read() throws IOException;

/**
 * Reading a line into a string array with the possibility to process the entries first (restricted by the fact that the
 * values must fit into a <code>List<String></code>!
 * 
 * @param processors
 *            An array of processors that processes each entry. <code>null</code> entries denotes no processing for
 *            that cell
 * @return null if end-of-file or a list representing the read line
 * @throws IOException
 * @since 1.0
 */
public List<String> read(CellProcessor... processors) throws IOException;
}
