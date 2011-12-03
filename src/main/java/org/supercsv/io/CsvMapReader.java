package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * CsvMapReaders read each CSV line into a Map with the column name as the key, and the input value as the value.
 * 
 * @author Kasper B. Graversen
 */
public class CsvMapReader extends AbstractCsvReader implements ICsvMapReader {
	
	/**
	 * Constructs a new <tt>CsvMapReader</tt> with the supplied Reader and CSV preferences. Note that the
	 * <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 */
	public CsvMapReader(final Reader reader, final CsvPreference preferences) {
		setPreferences(preferences);
		setInput(reader);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> read(final String... nameMapping) throws IOException {
		final Map<String, String> destination = new HashMap<String, String>();
		super.line.clear();
		// read the line, if result, convert it to a map
		if( tokenizer.readStringList(super.line) ) {
			Util.mapStringList(destination, nameMapping, super.line);
			return destination;
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, ? super Object> read(final String[] nameMapping, final CellProcessor[] processors)
		throws IOException {
		final Map<String, Object> destination = new HashMap<String, Object>();
		
		// temporary storage of processed column before writing to the map
		final List<Object> lineResult = new ArrayList<Object>();
		
		if( !tokenizer.readStringList(super.line) ) {
			return null;
		}
		
		// set to null all read columns we want to ignore
		for( int i = 0; i < line.size(); i++ ) {
			if( nameMapping[i] == null ) {
				super.line.set(i, null);
			}
		}
		Util.processStringList(lineResult, super.line, processors, getLineNumber());
		Util.mapStringList((Map<String, Object>) destination, nameMapping, (List<Object>) lineResult);
		
		return destination;
	}
}
