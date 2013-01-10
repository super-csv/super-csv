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

import static org.dozer.loader.api.TypeMappingOptions.mapNull;
import static org.dozer.loader.api.TypeMappingOptions.oneWay;
import static org.dozer.loader.api.TypeMappingOptions.wildcard;

import java.io.IOException;
import java.io.Reader;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingBuilder;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ITokenizer;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

/**
 * CsvDozerBeanReader is a powerful replacement for {@link CsvBeanReader} that uses Dozer to map from CSV to a bean.
 * 
 * @author James Bassett
 * @since 2.0.0
 */
public class CsvDozerBeanReader extends AbstractCsvReader implements ICsvDozerBeanReader {
	
	private final DozerBeanMapper dozerBeanMapper;
	
	// source of dozer bean mapping
	private final CsvDozerBeanData beanData = new CsvDozerBeanData();
	
	/**
	 * Constructs a new <tt>CsvDozerBeanReader</tt> with the supplied Reader and CSV preferences and creates it's own
	 * DozerBeanMapper. Note that the <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if reader or preferences are null
	 */
	public CsvDozerBeanReader(final Reader reader, final CsvPreference preferences) {
		super(reader, preferences);
		this.dozerBeanMapper = new DozerBeanMapper();
	}
	
	/**
	 * Constructs a new <tt>CsvDozerBeanReader</tt> with the supplied (custom) Tokenizer and CSV preferences and creates
	 * it's own DozerBeanMapper. The tokenizer should be set up with the Reader (CSV input) and CsvPreference
	 * beforehand.
	 * 
	 * @param tokenizer
	 *            the tokenizer
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if tokenizer or preferences are null
	 */
	public CsvDozerBeanReader(final ITokenizer tokenizer, final CsvPreference preferences) {
		super(tokenizer, preferences);
		this.dozerBeanMapper = new DozerBeanMapper();
	}
	
	/**
	 * Constructs a new <tt>CsvDozerBeanReader</tt> with the supplied Reader, CSV preferences and DozerBeanMapper. Note
	 * that the <tt>reader</tt> will be wrapped in a <tt>BufferedReader</tt> before accessed.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 * @param dozerBeanMapper
	 *            the dozer bean mapper to use
	 * @throws NullPointerException
	 *             if reader, preferences or dozerBeanMapper are null
	 */
	public CsvDozerBeanReader(final Reader reader, final CsvPreference preferences,
		final DozerBeanMapper dozerBeanMapper) {
		super(reader, preferences);
		if( dozerBeanMapper == null ) {
			throw new NullPointerException("dozerBeanMapper should not be null");
		}
		this.dozerBeanMapper = dozerBeanMapper;
	}
	
	/**
	 * Constructs a new <tt>CsvDozerBeanReader</tt> with the supplied (custom) Tokenizer, CSV preferences and
	 * DozerBeanMapper. The tokenizer should be set up with the Reader (CSV input) and CsvPreference beforehand.
	 * 
	 * @param tokenizer
	 *            the tokenizer
	 * @param preferences
	 *            the CSV preferences
	 * @param dozerBeanMapper
	 *            the dozer bean mapper to use
	 * @throws NullPointerException
	 *             if tokenizer, preferences or dozerBeanMapper are null
	 */
	public CsvDozerBeanReader(final ITokenizer tokenizer, final CsvPreference preferences,
		final DozerBeanMapper dozerBeanMapper) {
		super(tokenizer, preferences);
		if( dozerBeanMapper == null ) {
			throw new NullPointerException("dozerBeanMapper should not be null");
		}
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
	public <T> T read(final Class<T> clazz) throws IOException {
		if( clazz == null ) {
			throw new NullPointerException("clazz should not be null");
		}
		
		if( readRow() ) {
			// call dozer to map the read columns to the bean
			beanData.getColumns().clear();
			beanData.getColumns().addAll(getColumns());
			return dozerBeanMapper.map(beanData, clazz);
		}
		
		return null; // EOF
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(final Class<T> clazz, final CellProcessor... processors) throws IOException {
		if( clazz == null ) {
			throw new NullPointerException("clazz should not be null");
		} else if( processors == null ) {
			throw new NullPointerException("processors should not be null");
		}
		
		if( readRow() ) {
			// execute the processors then call dozer to populate the bean
			Util.executeCellProcessors(beanData.getColumns(), getColumns(), processors, getLineNumber(), getRowNumber());
			return dozerBeanMapper.map(beanData, clazz);
		}
		
		return null; // EOF
	}
	
	/**
	 * Assembles the dozer bean mappings required by CsvDozerBeanReader programatically using the Dozer API.
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
		 *            the field mapping for for each column (may contain <tt>null</tt> elements to indicate ignored
		 *            columns)
		 * @throws NullPointerException
		 *             if clazz or fieldMapping is null
		 */
		public MappingBuilder(final Class<?> clazz, final String[] fieldMapping) {
			if( clazz == null ) {
				throw new NullPointerException("clazz should not be null");
			} else if( fieldMapping == null ) {
				throw new NullPointerException("fieldMapping should not be null");
			}
			this.clazz = clazz;
			this.fieldMapping = fieldMapping;
		}
		
		@Override
		protected void configure() {
			
			/*
			 * Add the required dozer mappings to map from each column (in the CsvDozerBeanData List) to its associated
			 * field in the supplied class. mapNull is disabled so that null field values are ignored. oneWay is enabled
			 * just in case a custom DozerBeanMapper is supplied (so the same DozerBeanMapper can be used by
			 * CsvDozerBeanWriter). wildcard is disabled to prevent Dozer from trying to map things automatically.
			 */
			final TypeMappingBuilder mappingBuilder = mapping(CsvDozerBeanData.class, clazz, oneWay(), wildcard(false),
				mapNull(false));
			
			for( int i = 0; i < fieldMapping.length; i++ ) {
				
				final String mapping = fieldMapping[i];
				
				if( mapping == null ) {
					continue; // no field mappings required (column will be ignored)
				}
				
				mappingBuilder.fields("columns[" + i + "]", mapping);
			}
		}
	}
	
}
