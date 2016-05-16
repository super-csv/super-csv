/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.io.declarative;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseChar;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.io.ITokenizer;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Form;
import org.supercsv.util.ReflectionUtils;

/**
 * This reader maps csv files to beans via conventions and {@link org.supercsv.io.declarative.CellProcessor}
 * -annotations. The fields in the bean must match the csv's fields in type and order. {@link CellProcessor}s are
 * created automatically for all known types. Additional processors can be added by annotating fields with their
 * respective annotations. Annotation-order defines processor call-order.
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class CsvDeclarativeBeanReader extends AbstractCsvReader {
	private static final Map<Class<?>, CellProcessor> DEFAULT_PROCESSORS = new HashMap<Class<?>, CellProcessor>();
	static {
		DEFAULT_PROCESSORS.put(BigDecimal.class, new ParseBigDecimal());
		DEFAULT_PROCESSORS.put(Boolean.class, new ParseBool());
		DEFAULT_PROCESSORS.put(boolean.class, new ParseBool());
		DEFAULT_PROCESSORS.put(Character.class, new ParseChar());
		DEFAULT_PROCESSORS.put(char.class, new ParseChar());
		DEFAULT_PROCESSORS.put(Double.class, new ParseDouble());
		DEFAULT_PROCESSORS.put(double.class, new ParseDouble());
		DEFAULT_PROCESSORS.put(Integer.class, new ParseInt());
		DEFAULT_PROCESSORS.put(int.class, new ParseInt());
		DEFAULT_PROCESSORS.put(Long.class, new ParseLong());
		DEFAULT_PROCESSORS.put(long.class, new ParseLong());
	}
	
	private final List<Object> processedColumns = new ArrayList<Object>();
	private BeanCellProcessorExtractor cellProcessorExtractor = new BeanCellProcessorExtractor(DEFAULT_PROCESSORS);
	
	/**
	 * Constructs a new <tt>CsvBeanReader</tt> with the supplied Reader and CSV preferences. Note that the
	 * <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if reader or preferences are null
	 */
	public CsvDeclarativeBeanReader(final Reader reader, final CsvPreference preferences) {
		super(reader, preferences);
	}
	
	/**
	 * Constructs a new <tt>CsvBeanReader</tt> with the supplied (custom) Tokenizer and CSV preferences. The tokenizer
	 * should be set up with the Reader (CSV input) and CsvPreference beforehand.
	 * 
	 * @param tokenizer
	 *            the tokenizer
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if tokenizer or preferences are null
	 */
	public CsvDeclarativeBeanReader(final ITokenizer tokenizer, final CsvPreference preferences) {
		super(tokenizer, preferences);
	}
	
	/**
	 * Reads a row of a CSV file and populates an instance of the specified class, using the conventional mappings and
	 * provided {@link org.supercsv.io.declarative.CellProcessor}-annotations
	 * 
	 * @param clazz
	 *            the type to instantiate. If the type is a class then a new instance will be created using the default
	 *            no-args constructor. If the type is an interface, a proxy object which implements the interface will
	 *            be created instead.
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws IllegalArgumentException
	 *             if nameMapping.length != number of columns read or clazz is null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @throws SuperCsvReflectionException
	 *             if there was an reflection exception while mapping the values to the bean
	 * @since 2.5
	 */
	public <T> T read(final Class<T> clazz) throws IOException {
		if( clazz == null ) {
			throw new IllegalArgumentException("clazz should not be null");
		}
		
		List<Field> fields = ReflectionUtils.getFields(clazz);
		
		return readIntoBean(ReflectionUtils.instantiateBean(clazz), fields,
			cellProcessorExtractor.getCellProcessors(clazz));
	}
	
	private <T> T populateBean(final T resultBean, List<Field> fields) {
		for( int i = 0; i < fields.size(); i++ ) {
			final Object fieldValue = processedColumns.get(i);
			
			Field field = fields.get(i);
			if( field == null || fieldValue == null ) {
				continue;
			}
			
			try {
				field.setAccessible(true);
				field.set(resultBean, fieldValue);
			}
			catch(IllegalAccessException e) {
				throw new SuperCsvReflectionException(Form.at("Cannot set value on field '{}'", field.getName()), e);
			}
			
		}
		
		return resultBean;
	}
	
	private <T> T readIntoBean(final T bean, List<Field> fields, final List<CellProcessor> processors)
		throws IOException {
		
		if( readRow() ) {
			if( fields.size() != length() ) {
				throw new IllegalArgumentException(Form.at(
					"the number of fields in the bean and the number of columns read "
						+ "should be the same size (nameMapping length = {}, columns = {})", fields.size(), length()));
			}
			
			executeProcessors(processedColumns, processors.toArray(new CellProcessor[processors.size()]));
			
			return populateBean(bean, fields);
		}
		
		return null; // EOF
	}
	
}
