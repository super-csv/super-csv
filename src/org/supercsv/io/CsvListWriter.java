package org.supercsv.io;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * The writer class capable of writing arrays and lists to a CSV file. Notice that the cell processors can also be
 * utilized when writing (using the <code>org.bestcsv.util</code>). They can help ensure that only numbers are written
 * in numeric columns, that numbers are unique or the output does not contain certain characters or exceed specified
 * string lengths.
 * 
 * @author Kasper B. Graversen
 */
public class CsvListWriter extends AbstractCsvWriter implements ICsvListWriter {

/**
 * Create a CSV writer. Note that the <tt>writer</tt> provided in the argument will be wrapped in a
 * <tt>BufferedWriter</tt> before accessed.
 * 
 * @param stream
 *            Stream to write to
 * @param preference
 *            defines separation character, end of line character, etc.
 */
public CsvListWriter(final Writer stream, final CsvPreference preference) {
	super(stream, preference);
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
