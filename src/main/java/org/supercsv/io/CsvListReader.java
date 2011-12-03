package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * A simple reader, reading a line from a CSV file into a <tt>List</tt>. This low-level approach to CSV-reading
 * should be considered a last resort when the more elaborate schemes do not fit your purpose.
 * 
 * @author Kasper B. Graversen
 */
public class CsvListReader extends AbstractCsvReader implements ICsvListReader {
	
	/**
	 * Constructs a new <tt>CsvListReader</tt> with the supplied Reader and CSV preferences. Note that the
	 * <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 */
	public CsvListReader(final Reader reader, final CsvPreference preferences) {
		setPreferences(preferences);
		setInput(reader);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> read() throws IOException {
		if( tokenizer.readStringList(super.line) ) {
			return super.line;
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> read(final CellProcessor... processors) throws IOException {
		if( tokenizer.readStringList(super.line) ) {
			// execute the processors
			final List<? super Object> processedColumns = new ArrayList<Object>();
			Util.processStringList(processedColumns, super.line, processors, getLineNumber());
			
			// call toString() on each object
			final List<String> result = new ArrayList<String>();
			for( final Object i : processedColumns ) {
				result.add(i.toString());
			}
			return result;
			
		}
		return null;
	}
}
