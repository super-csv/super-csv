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
package org.supercsv.io.dozer;

import static org.dozer.loader.api.TypeMappingOptions.oneWay;
import static org.dozer.loader.api.TypeMappingOptions.wildcard;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingBuilder;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * CsvDozerBeanWriter is a powerful replacement for {@link CsvBeanWriter} that uses Dozer to map from a bean to CSV.
 * 
 * @author James Bassett
 * @since 2.0.0
 */
public class CsvDozerBeanWriter extends AbstractCsvWriter implements ICsvDozerBeanWriter {
	
	private final DozerBeanMapper dozerBeanMapper;
	
	// target of dozer bean mapping
	private final CsvDozerBeanData beanData = new CsvDozerBeanData();
	
	// temporary storage of processed columns to be written
	private final List<Object> processedColumns = new ArrayList<Object>();
	
	/**
	 * Constructs a new <tt>CsvDozerBeanWriter</tt> with the supplied Writer and CSV preferences and and creates it's
	 * own DozerBeanMapper. Note that the <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if writer or preference are null
	 */
	public CsvDozerBeanWriter(final Writer writer, final CsvPreference preference) {
		super(writer, preference);
		this.dozerBeanMapper = new DozerBeanMapper();
	}
	
	/**
	 * Constructs a new <tt>CsvDozerBeanWriter</tt> with the supplied Writer, CSV preferences and DozerBeanMapper. Note
	 * that the <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the CSV preferences
	 * @param dozerBeanMapper
	 *            the pre-configured DozerBeanMapper
	 * @throws NullPointerException
	 *             if writer, preference or dozerBeanMapper are null
	 */
	public CsvDozerBeanWriter(final Writer writer, final CsvPreference preference, final DozerBeanMapper dozerBeanMapper) {
		super(writer, preference);
		Util.requireNotNull(dozerBeanMapper, "dozerBeanMapper");
		this.dozerBeanMapper = dozerBeanMapper;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void configureBeanMapping(final Class<?> clazz, final String[] fieldMapping) {
		dozerBeanMapper.addMapping(new MappingBuilder(clazz, fieldMapping));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void write(final Object source) throws IOException {
		
		Util.requireNotNull(source, "source");
		
		// update the current row/line numbers
		super.incrementRowAndLineNo();
		
		// extract the bean values into the List using dozer
		beanData.getColumns().clear();
		dozerBeanMapper.map(source, beanData);
		
		// write the list
		super.writeRow(beanData.getColumns());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void write(final Object source, final CellProcessor[] processors) throws IOException {
		
		Util.requireNotNull(source, "source");
		Util.requireNotNull(processors, "processors");
		
		// update the current row/line numbers
		super.incrementRowAndLineNo();
		
		// extract the bean values into the List using dozer
		beanData.getColumns().clear();
		dozerBeanMapper.map(source, beanData);
		
		// execute the cell processors
		Util.executeCellProcessors(processedColumns, beanData.getColumns(), processors, getLineNumber(), getRowNumber());
		
		// write the list
		super.writeRow(processedColumns);
	}
	
	/**
	 * Assembles the dozer bean mappings required by CsvDozerBeanWriter programatically using the Dozer API.
	 */
	private static class MappingBuilder extends BeanMappingBuilder {
		
		private final Class<?> clazz;
		private final String[] fieldMapping;
		
		/**
		 * Constructs a new MappingBuilder.
		 * 
		 * @param clazz
		 *            the class to add mapping configuration for (same as the type passed into write methods)
		 * @param fieldMapping
		 *            the field mapping for for each column (cannot contain <tt>null</tt> elements)
		 * @throws NullPointerException
		 *             if clazz or fieldMapping (or one of its elements) is null
		 */
		public MappingBuilder(final Class<?> clazz, final String[] fieldMapping) {
			Util.requireNotNull(clazz, "clazz");
			Util.requireNotNull(fieldMapping, "fieldMapping");
			this.clazz = clazz;
			this.fieldMapping = fieldMapping;
		}
		
		@Override
		protected void configure() {
			
			/*
			 * Add the required dozer mappings to map from each field in the supplied class to its associated column (in
			 * the CsvDozerBeanData List). mapNull is enabled so that null field values are added to the List (otherwise
			 * the List would be too short!). oneWay is enabled just in case a custom DozerBeanMapper is supplied (so
			 * the same DozerBeanMapper can be used by CsvDozerBeanReader). wildcard is disabled to prevent Dozer from
			 * trying to map every field in the bean automatically. copyByReference is enabled on the field mapping to
			 * ensure no conversions are performed (bean values are just copied to the List).
			 */
			final TypeMappingBuilder mappingBuilder = mapping(clazz, type(CsvDozerBeanData.class).mapNull(true),
				oneWay(), wildcard(false));
			
			for( int i = 0; i < fieldMapping.length; i++ ) {
				
				final String mapping = fieldMapping[i];
				
				if( mapping == null ) {
					// a null field mapping at end of array results in the List being too short, so don't allow
					throw new NullPointerException(String.format("fieldMapping at index %d should not be null", i));
				}
				
				// add a field mapping from the field to the appropriate column in the beanData List
				mappingBuilder.fields(mapping, "columns[" + i + "]", FieldsMappingOptions.copyByReference());
			}
		}
	}
}
