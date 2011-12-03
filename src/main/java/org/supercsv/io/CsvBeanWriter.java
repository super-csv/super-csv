package org.supercsv.io;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVReflectionException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.MethodCache;
import org.supercsv.util.Util;

/**
 * This class writes a CSV file by mapping each field on the bean to a column in the CSV file (using the supplied name
 * mapping).
 * 
 * @author Kasper B. Graversen
 */
public class CsvBeanWriter extends AbstractCsvWriter implements ICsvBeanWriter {
	
	/** temporary storage of processed fields to be written */
	protected List<? super Object> result;
	
	/** cache of methods for mapping from fields to columns */
	protected MethodCache cache = new MethodCache();
	
	/**
	 * Constructs a new <tt>CsvBeanWriter</tt> with the supplied Writer and CSV preferences. Note that the
	 * <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 */
	public CsvBeanWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
		result = new ArrayList<Object>();
	}
	
	/**
	 * Creates a List of Strings from the bean representing the columns to write.
	 * 
	 * @param source
	 *            the bean
	 * @param nameMapping
	 *            the name mapping
	 * @throws SuperCSVReflectionException
	 */
	protected void fillListFromObject(final Object source, final String[] nameMapping)
		throws SuperCSVReflectionException {
		
		// name mapping is mandatory for bean writing
		if( nameMapping == null ) {
			throw new NullInputException(
				"the nameMapping array can't be null as it's used to map from fields to columns");
		}
		
		try {
			result.clear(); // object re-use
			
			for( final String methodName : nameMapping ) {
				Method getMethod = cache.getGetMethod(source, methodName);
				result.add(getMethod.invoke(source));
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
		// fill the list with the values in the bean's fields
		fillListFromObject(source, nameMapping);
		
		// write the list
		super.write(result);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void write(final Object source, final String[] nameMapping, final CellProcessor[] processors)
		throws IOException, SuperCSVReflectionException {
		// fill the list with the values in the bean's fields
		fillListFromObject(source, nameMapping);
		
		// execute the processors for each column
		final List<? super Object> processedColumns = new ArrayList<Object>();
		Util.processStringList(processedColumns, result, processors, super.getLineNumber());
		
		// write the list
		super.write(processedColumns);
	}
}
