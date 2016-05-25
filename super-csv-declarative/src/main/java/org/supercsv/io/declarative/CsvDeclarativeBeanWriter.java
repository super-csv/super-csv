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
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Form;
import org.supercsv.util.ReflectionUtils;
import org.supercsv.util.Util;

/**
 * CsvDeclarativeBeanWriter writes a CSV file via conventions and {@link org.supercsv.io.declarative.CellProcessor}
 * -annotations.
 * 
 * @author Dominik Schlosser
 */
public class CsvDeclarativeBeanWriter extends AbstractCsvWriter {
	private final List<Object> processedColumns = new ArrayList<Object>();
	
	private BeanCellProcessorExtractor cellProcessorExtractor = new BeanCellProcessorExtractor();
	
	/**
	 * Constructs a new <tt>CsvDeclarativeBeanWriter</tt> with the supplied Writer and CSV preferences. Note that the
	 * <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if writer or preference are null
	 */
	public CsvDeclarativeBeanWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
	}
	
	/**
	 * Writes a row of a CSV file, using the conventions and mappings provided
	 * {@link org.supercsv.io.declarative.CellProcessor}-annotations
	 * 
	 * @param source
	 *            The bean-instance to write
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws IllegalArgumentException
	 *             if source is null
	 * @throws SuperCsvException
	 *             if there was a general exception while writing/processing
	 * @throws SuperCsvReflectionException
	 *             if there was an reflection exception
	 * @since 2.5
	 */
	public void write(final Object source) throws IOException {
		if( source == null ) {
			throw new IllegalArgumentException("source must not be null");
		}
		
		incrementRowAndLineNo();
		
		List<Field> fields = ReflectionUtils.getFields(source.getClass());
		List<Object> beanValues = extractBeanValues(source, fields);
		
		List<CellProcessor> processors = cellProcessorExtractor.getCellProcessors(source.getClass());
		
		Util.executeCellProcessors(processedColumns, beanValues,
			processors.toArray(new CellProcessor[processors.size()]), getLineNumber(), getRowNumber());
		
		writeRow(processedColumns);
		flush();
	}
	
	private List<Object> extractBeanValues(final Object source, List<Field> fields) {
		
		if( source == null ) {
			throw new IllegalArgumentException("the bean to write should not be null");
		}
		
		List<Object> beanValues = new ArrayList<Object>();
		
		for( Field field : fields ) {
			field.setAccessible(true);
			try {
				beanValues.add(field.get(source));
			}
			catch(IllegalAccessException e) {
				throw new SuperCsvReflectionException(Form.at("error extracting bean value for field {}",
					field.getName()), e);
			}
		}
		
		return beanValues;
		
	}
}
