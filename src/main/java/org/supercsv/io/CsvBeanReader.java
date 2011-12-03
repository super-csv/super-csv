package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.exception.SuperCSVReflectionException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.BeanInterfaceProxy;
import org.supercsv.util.MethodCache;
import org.supercsv.util.Util;

/**
 * This class reads a CSV file by instantiating a bean for every row and mapping each column to a field on the bean
 * (using the supplied name mapping).
 * 
 * @author Kasper B. Graversen
 */
public class CsvBeanReader extends AbstractCsvReader implements ICsvBeanReader {
	
	/** temporary storage of processed columns to be mapped to the bean */
	protected List<? super Object> lineResult = new ArrayList<Object>();
	
	/** cache of methods for mapping from columns to fields */
	protected MethodCache cache = new MethodCache();
	
	/**
	 * Constructs a new <tt>CsvBeanReader</tt> with the supplied Reader and CSV preferences. Note that the
	 * <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 */
	public CsvBeanReader(final Reader reader, final CsvPreference preferences) {
		setPreferences(preferences);
		setInput(reader);
	}
	
	/**
	 * Instantiates the bean (or creates a proxy if it's an interface), and maps the processed columns to the fields of
	 * the bean.
	 * 
	 * @param clazz
	 *            the bean class to instantiate (a proxy will be created if an interface is supplied), using the default
	 *            (no argument) constructor
	 * @param nameMapping
	 *            the name mappings
	 * @return A filled object
	 * @throws SuperCSVReflectionException
	 */
	private <T> T populateBean(final Class<T> clazz, final String[] nameMapping) throws SuperCSVReflectionException {
		try {
			// instantiate the bean or proxy
			final T resultBean;
			if( clazz.isInterface() ) {
				resultBean = new BeanInterfaceProxy().createProxy(clazz);
			} else {
				resultBean = clazz.newInstance();
			}
			
			// map each column to its associated field on the bean
			for( int i = 0; i < nameMapping.length; i++ ) {
				
				// don't call a set-method in the bean if there is no name mapping for the column or no result to store
				if( nameMapping[i] == null || lineResult.get(i) == null ) {
					continue;
				}
				
				try {
					Method setMethod = cache.getSetMethod(resultBean, nameMapping[i], lineResult.get(i).getClass());
					setMethod.invoke(resultBean, lineResult.get(i));
				}
				catch(final IllegalArgumentException e) {
					throw new SuperCSVException("Method set" + nameMapping[i].substring(0, 1).toUpperCase()
						+ nameMapping[i].substring(1) + "() does not accept input \"" + lineResult.get(i)
						+ "\" of type " + lineResult.get(i).getClass().getName(), null, e);
				}
			}
			return resultBean;
		}
		catch(final InstantiationException e) {
			throw new SuperCSVReflectionException("Error while populating bean", e);
		}
		catch(final IllegalAccessException e) {
			throw new SuperCSVReflectionException("Error while populating bean", e);
		}
		catch(final InvocationTargetException e) {
			throw new SuperCSVReflectionException("Error while populating bean", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(final Class<T> clazz, final String... nameMapping) throws IOException,
		SuperCSVReflectionException {
		if( tokenizer.readStringList(super.line) ) {
			lineResult.clear();
			lineResult.addAll(super.line);
			return populateBean(clazz, nameMapping);
		}
		return null; // EOF
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(final Class<T> clazz, final String[] nameMapping, final CellProcessor... processors)
		throws IOException, SuperCSVReflectionException, SuperCSVException {
		if( tokenizer.readStringList(super.line) ) {
			// execute the processors then populate the bean
			Util.processStringList(lineResult, super.line, processors, tokenizer.getLineNumber());
			return populateBean(clazz, nameMapping);
		}
		return null; // EOF
	}
}
