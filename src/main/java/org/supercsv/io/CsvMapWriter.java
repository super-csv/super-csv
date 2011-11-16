package org.supercsv.io;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * The writer class capable of writing maps of <b>different types</b> to a CSV file. Notice that the cell processors
 * can also be utilized when writing. E.g. they can help ensure that only numbers are written in numeric columns, that
 * numbers are unique or the output does not contain certain characters or exceed specified string lengths.
 * 
 * @author Kasper B. Graversen
 */
public class CsvMapWriter extends AbstractCsvWriter implements ICsvMapWriter {
List<? super Object> tmpDst = new ArrayList<Object>();

/**
 * Create a CSV writer. Note that the <tt>writer</tt> provided in the argument will be wrapped in a
 * <tt>BufferedWriter</tt> before accessed.
 * 
 * @param stream
 *            Stream to write to
 * @param preference
 *            defines separation character, end of line character, etc.
 * @since 1.0
 */
public CsvMapWriter(final Writer stream, final CsvPreference preference) {
	super(stream, preference);
}

/**
 * {@inheritDoc}
 */
public void write(final Map<String, ? extends Object> values, final String... nameMapping) throws IOException {
	super.write(Util.stringMap(values, nameMapping));
}

/**
 * {@inheritDoc}
 */
public void write(final Map<String, ? extends Object> source, final String[] nameMapping,
	final CellProcessor[] processor) throws IOException, SuperCSVException {
	tmpDst.clear();
	// only write if we are not failing
	Util.processStringList(tmpDst, Util.map2List(source, nameMapping), processor, getLineNumber());
	super.write(tmpDst.toArray());
}
}
