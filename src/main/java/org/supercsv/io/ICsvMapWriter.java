package org.supercsv.io;

import java.io.IOException;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This is a supertype for writers writing maps
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvMapWriter extends ICsvWriter {

/**
 * Write the map into a line of the csv file. Note that the <tt>writer</tt> provided in the argument will be wrapped
 * in a <tt>BufferedWriter</tt> before accessed.
 * 
 * @param values
 *            denotes map of values must be saved
 * @param nameMapping
 *            defines order of the map's elements that are to be written. You need only to include the names you want to
 *            write.
 * @since 1.0
 */
void write(Map<String, ? extends Object> values, String... nameMapping) throws IOException;

/**
 * Write a line from a map converting processing the values before writing
 * 
 * @param source
 *            denotes map of values must be saved
 * @param nameMapping
 *            defines order of the map's elements that are to be written. You need only to include the names you want to
 *            write.
 * @param processor
 *            An array of processors to process the map's values before writing. <code>null</code> denotes no
 *            processing is to be taken place for that element. The size of the array must match the
 *            <code>nameMapping</code> array.
 * @throws IOException
 *             in case of an I/O error
 * @throws Exception
 *             in case one of the cell processors fail processing
 * @since 1.20
 */
void write(Map<String, ? extends Object> source, String[] nameMapping, CellProcessor[] processor) throws IOException;

}
