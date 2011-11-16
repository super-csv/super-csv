package org.supercsv.io;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVReflectionException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.MethodCache;
import org.supercsv.util.Util;

/**
 * This class capable of writing beans to a CSV file. Notice that the cell processors can also be useful when writing
 * data. For example, they can help ensure that only numbers are written in numeric columns, that numbers are unique or
 * the output does not contain certain characters or exceed specified string lengths.
 * 
 * @author Kasper B. Graversen
 */
public class CsvBeanWriter extends AbstractCsvWriter implements ICsvBeanWriter {
/**
 * object used for storing intermediate result of a processing of cell processors and before put into maps/objects etc..
 */
protected List<? super Object> result;
protected MethodCache cache = new MethodCache();

/**
 * Create a CSV writer. Note that the <tt>writer</tt> provided in the argument will be wrapped in a
 * <tt>BufferedWriter</tt> before accessed.
 * 
 * @param writer
 *            Stream to write to
 * @param preference
 *            defines separation character, end of line character, etc.
 */
public CsvBeanWriter(final Writer writer, final CsvPreference preference) {
	super(writer, preference);
	result = new ArrayList<Object>();
}

/**
 * populate <tt>result</tt> based on the source
 * 
 * @param source
 * @param nameMapping
 * @throws IllegalAccessException
 * @throws InvocationTargetException
 */
protected void fillListFromObject(final Object source, final String[] nameMapping) throws SuperCSVReflectionException {
	try {
		result.clear(); // object re-use
		
		// map results from an object by traversing the list of nameMapping and
		// for
		for( final String methodName : nameMapping ) {
			result.add(cache.getGetMethod(source, methodName).invoke(source));
		}
	}
	catch(final IllegalAccessException e) {
		throw new SuperCSVReflectionException("Error accessing object " + source, e);
	}
	catch(final InvocationTargetException e) {
		throw new SuperCSVReflectionException("Error accessing object " + source, e);
	}
}

/**
 * {@inheritDoc}
 */
public void write(final Object source, final String... nameMapping) throws IOException, SuperCSVReflectionException {
	fillListFromObject(source, nameMapping);
	super.write(result);
}

/**
 * {@inheritDoc}
 */
public void write(final Object source, final String[] nameMapping, final CellProcessor[] processor) throws IOException,
	SuperCSVReflectionException {
	fillListFromObject(source, nameMapping);
	final List<? super Object> processedRes = new ArrayList<Object>();
	
	Util.processStringList(processedRes, result, processor, super.getLineNumber());
	super.write(processedRes);
}
}
