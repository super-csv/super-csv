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
 * The writer class capable of writing maps of <b>different types</b> to a CSV file. 
 * 
 * @author Kasper B. Graversen
 */
public class CsvMapWriter extends AbstractCsvWriter implements ICsvMapWriter {
	List<? super Object> tmpDst = new ArrayList<Object>();
	
	/**
	 * Constructs a new <tt>CsvMapWriter</tt> with the supplied Writer and CSV preferences. Note that the
	 * <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 * @since 1.0
	 */
	public CsvMapWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
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
		Util.processStringList(tmpDst, Util.map2List(source, nameMapping), processor, getLineNumber());
		super.write(tmpDst.toArray());
	}
}
