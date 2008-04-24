package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
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
 * This class reads a line from a csv file, instantiates a bean and populate its fields.
 * 
 * @author Kasper B. Graversen
 */
public class CsvBeanReader extends AbstractCsvReader implements ICsvBeanReader {
/**
 * object used for storing intermediate result of a processing of cell processors and before put into maps/objects etc..
 * due to the typing, we cannot use super.line
 */
protected List<? super Object> lineResult = new ArrayList<Object>();
protected MethodCache cache = new MethodCache();

/**
 * Create a csv reader with a specific preference. Note that the <tt>reader</tt> provided in the argument will be
 * wrapped in a <tt>BufferedReader</tt> before accessed.
 */
public CsvBeanReader(final Reader reader, final CsvPreference preferences) {
	setPreferences(preferences);
	setInput(reader);
}

/**
 * Creates an object of the type or if it is an interface, create a proxy instance implementing the interface type.
 * 
 * @param clazz
 *            the type to instantiate. If the type is a class type, an instance can be created straight away. If the
 *            type is an interface type, a proxy is created on the fly which acts as an implementation.
 * @param nameMapping
 * @return A filled object
 * @throws InstantiationException
 * @throws IllegalAccessException
 * @throws InvocationTargetException
 */
<T> T fillObject(final Class<T> clazz, final String[] nameMapping) throws SuperCSVReflectionException {
	try {
		// create a proxy instance if an interface type is provided
		final T resultBean;
		if( clazz.isInterface() ) {
			resultBean = (T) new BeanInterfaceProxy().createProxy(clazz);
		} else {
			resultBean = clazz.newInstance();
		}
		// map results into an object by traversing the list of nameMapping and for each non-null,
		// map that name to an entry in the lineResult
		// map results to the setter methods
		for( int i = 0; i < nameMapping.length; i++ ) {
			// don't call a set-method in the bean, if there is no result to store
			if( nameMapping[i] == null ) {
				continue;
			}
			try {
				// System.out.println(String.format("mapping[i]= %s, lR[%d] = %s val '%s'", nameMapping[i], i,
				// lineResult
				// .get(i).getClass(), lineResult.get(i)));
				cache.getSetMethod(resultBean, nameMapping[i], lineResult.get(i).getClass())//
					.invoke(resultBean, lineResult.get(i));
			}
			catch(final IllegalArgumentException e) {
				throw new SuperCSVException("Method set" + nameMapping[i].substring(0, 1).toUpperCase()
					+ nameMapping[i].substring(1) + "() does not accept input \"" + lineResult.get(i) + "\" of type "
					+ lineResult.get(i).getClass().getName(), null, e);
			}
		}
		return resultBean;
	}
	catch(final InstantiationException e) {
		throw new SuperCSVReflectionException("Error while filling an object", e);
	}
	catch(final IllegalAccessException e) {
		throw new SuperCSVReflectionException("Error while filling an object", e);
	}
	catch(final InvocationTargetException e) {
		throw new SuperCSVReflectionException("Error while filling an object", e);
	}
}

/**
 * {@inheritDoc}
 */
public <T> T read(final Class<T> clazz, final String... nameMapping) throws IOException, SuperCSVReflectionException {
	if( tokenizer.readStringList(super.line) ) {
		lineResult.clear();
		lineResult.addAll(super.line);
		return fillObject(clazz, nameMapping);
	}
	return null; // EOF
}

/**
 * {@inheritDoc}
 */
public <T> T read(final Class<T> clazz, final String[] nameMapping, final CellProcessor[] processors)
	throws IOException, SuperCSVReflectionException, SuperCSVException {
	if( tokenizer.readStringList(super.line) ) {
		Util.processStringList(lineResult, super.line, processors, tokenizer.getLineNumber());
		return fillObject(clazz, nameMapping);
	}
	return null; // EOF
}
}
