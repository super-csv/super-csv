package org.supercsv.io;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * This writer class is capable of writing arrays and Lists to a CSV file.
 * 
 * @author Kasper B. Graversen
 */
public class CsvListWriter extends AbstractCsvWriter implements ICsvListWriter {
	
	/**
	 * Constructs a new <tt>CsvListWriter</tt> with the supplied Writer and CSV preferences. Note that the
	 * <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preferences
	 *            the CSV preferences
	 */
	public CsvListWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void write(final List<? extends Object> content, final CellProcessor[] processors) throws IOException {
		final List<? super Object> destination = new ArrayList<Object>();
		
		// convert source to List<String>
		final List<String> source = new ArrayList<String>();
		for( int i = 0; i < content.size(); i++ ) {
			Object value = content.get(i);
			source.add(value == null ? null : value.toString());
		}
		
		Util.processStringList(destination, source, processors, super.getLineNumber());
		write(destination);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final List<?> content) throws IOException {
		super.write(content);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final Object... content) throws IOException {
		super.write(content);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final String... content) throws IOException {
		super.write(content);
	}
}
