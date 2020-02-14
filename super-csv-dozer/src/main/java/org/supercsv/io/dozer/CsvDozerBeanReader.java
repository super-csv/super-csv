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

import static org.dozer.loader.api.FieldsMappingOptions.hintB;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;
import static org.dozer.loader.api.TypeMappingOptions.oneWay;
import static org.dozer.loader.api.TypeMappingOptions.wildcard;

import java.io.IOException;
import java.io.Reader;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingBuilder;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.decoder.CsvDecoder;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

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
	 * {@inheritDoc}
	 */
	public void configureBeanMapping(final Class<?> clazz, final String[] fieldMapping) {
		dozerBeanMapper.addMapping(new MappingBuilder(clazz, fieldMapping));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void configureBeanMapping(final Class<?> clazz, final String[] fieldMapping, final Class<?>[] hintTypes) {
		dozerBeanMapper.addMapping(new MappingBuilder(clazz, fieldMapping, hintTypes));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(final Class<T> clazz) throws IOException {
		if( clazz == null ) {
			throw new NullPointerException("clazz should not be null");
		}
		
		return readIntoBean(null, clazz, null);
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
		
		return readIntoBean(null, clazz, processors);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(final T bean) throws IOException {
		if( bean == null ) {
			throw new NullPointerException("bean should not be null");
		}
		
		return readIntoBean(bean, null, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T read(final T bean, final CellProcessor... processors) throws IOException {
		if( bean == null ) {
			throw new NullPointerException("bean should not be null");
		} else if( processors == null ) {
			throw new NullPointerException("processors should not be null");
		}
		
		return readIntoBean(bean, null, processors);
	}
	
	/**
	 * Reads a row of a CSV file and populates the a bean, using Dozer to map column values to the appropriate field. If
	 * an existing bean is supplied, Dozer will populate that, otherwise Dozer will create an instance of type clazz
	 * (only one of bean or clazz should be supplied). If processors are supplied then they are used, otherwise the raw
	 * String values will be used.
	 * 
	 * @param bean
	 *            the bean to populate (if null, then clazz will be used instead)
	 * @param clazz
	 *            the type to instantiate (only required if bean is null)
	 * @param processors
	 *            the (optional) cell processors
	 * @return the populated bean
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	private <T> T readIntoBean(final T bean, final Class<T> clazz, final CellProcessor[] processors) throws IOException {
		if( readRow() ) {
			if( processors == null ) {
				// populate bean data with the raw String values
				beanData.getColumns().clear();
				beanData.getColumns().addAll(getColumns());
			} else {
				// populate bean data with the processed values
				executeProcessors(beanData.getColumns(), processors);
			}
			
			if( bean != null ) {
				// populate existing bean
				dozerBeanMapper.map(beanData, bean);
				return bean;
			} else {
				// let Dozer create a new bean
				return dozerBeanMapper.map(beanData, clazz);
			}
			
		}
		
		return null; // EOF
	}
	
	/**
	 * Assembles the dozer bean mappings required by CsvDozerBeanReader programatically using the Dozer API.
	 */
	private static class MappingBuilder extends BeanMappingBuilder {
		
		private final Class<?> clazz;
		private final String[] fieldMapping;
		private final Class<?>[] hintTypes;
		
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
			this.hintTypes = null;
		}
		
		/**
		 * Constructs a new MappingBuilder.
		 * 
		 * @param clazz
		 *            the class to add mapping configuration for (same as the type passed into write methods)
		 * @param fieldMapping
		 *            the field mapping for for each column (may contain <tt>null</tt> elements to indicate ignored
		 *            columns)
		 * @throws NullPointerException
		 *             if clazz, fieldMapping or hintTypes is null
		 * @throws IllegalArgumentException
		 *             if fieldMapping.length != hintTypes.length
		 */
		public MappingBuilder(final Class<?> clazz, final String[] fieldMapping, final Class<?>[] hintTypes) {
			if( clazz == null ) {
				throw new NullPointerException("clazz should not be null");
			} else if( fieldMapping == null ) {
				throw new NullPointerException("fieldMapping should not be null");
			} else if( hintTypes == null ) {
				throw new NullPointerException("fieldMapping should not be null");
			} else if( fieldMapping.length != hintTypes.length ) {
				throw new IllegalArgumentException(String.format(
					"hintTypes length(%d) should match fieldMapping length(%d)", hintTypes.length, fieldMapping.length));
			}
			this.clazz = clazz;
			this.fieldMapping = fieldMapping;
			this.hintTypes = hintTypes;
		}
		
		@Override
		protected void configure() {
			
			/*
			 * Add the required dozer mappings to map from each column (in the CsvDozerBeanData List) to its associated
			 * field in the supplied class. mapNull is enabled so that null CSV values are added to Lists if indexed
			 * mapping is used. oneWay is enabled just in case a custom DozerBeanMapper is supplied (so the same
			 * DozerBeanMapper can be used by CsvDozerBeanWriter). wildcard is disabled to prevent Dozer from trying to
			 * map things automatically.
			 */
			final TypeMappingBuilder mappingBuilder = mapping(CsvDozerBeanData.class, clazz, oneWay(), wildcard(false),
				mapNull(true));
			
			for( int i = 0; i < fieldMapping.length; i++ ) {
				
				final String mapping = fieldMapping[i];
				
				if( mapping == null ) {
					continue; // no field mappings required (column will be ignored)
				}
				
				if( hintTypes != null && hintTypes[i] != null ) {
					// add a hint on the target field
					mappingBuilder.fields("columns[" + i + "]", mapping, hintB(hintTypes[i]));
				} else {
					mappingBuilder.fields("columns[" + i + "]", mapping);
				}
				
			}
		}
	}
	
}
