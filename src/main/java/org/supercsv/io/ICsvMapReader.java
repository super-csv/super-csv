package org.supercsv.io;

import java.io.IOException;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * The supertype for MapReaders. Map readers are capable of reading CSV files and populate map instances.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvMapReader extends ICsvReader {

/**
 * Read the line into a map using an array to define the keys which to query the data.
 * 
 * @param nameMapping
 *            defines the keys of the map, null entries denote columns to be skipped in the csv file
 * @return a map of [string,string] since no processing of the data is taking place *
 * @since 1.0
 */
public Map<String, String> read(String... nameMapping) throws IOException;

/**
 * Read a line into a map of any type converting the strings to types
 * 
 * @param nameMapping
 *            defines the keys of the map, null entries denote columns to be skipped in the csv file
 * @param processors
 *            an array of cell processors. Null values are accepted as not processing the corresponding position in the
 *            name mapper
 * @return null, or a map instance containing the values from the CSV file
 * @throws IOException
 *             in case of an I/O error
 * @throws SuperCSVException
 *             in case one of the cell processors fail processing *
 * @since 1.0
 */
public Map<String, ? super Object> read(String[] nameMapping, CellProcessor[] processors) throws IOException,
	SuperCSVException;

}
