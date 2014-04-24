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

import java.io.IOException;

import org.dozer.MappingException;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.ICsvReader;

/**
 * Interface for CSV readers reading into objects/beans using Dozer.
 * 
 * @author James Bassett
 * @since 2.0.0
 */
public interface ICsvDozerBeanReader extends ICsvReader {
	
	/**
	 * Configures the underlying DozerBeanMapper with the mappings required to map from the CSV file to the specified
	 * class (this method may only be called before reading, as it's not possible to configure a DozerBeanMapper that
	 * has already been initialized). Generally this method will only be called once, but it may called more times to
	 * add mappings for other classes (you can define mappings for two different subclasses for example, but if you
	 * define a mapping for the parent class then that will take precedence - inheritance mapping isn't supported).
	 * <p>
	 * Each element of the fieldMapping array represents a CSV column to be read and uses the standard Dozer field
	 * mapping syntax. For example, if you were configuring the mappings for Person class you might define
	 * <tt>firstName</tt> as the first element (just a simple field mapping), <tt>address.city</tt> as the second
	 * element (a nested - or deep - field mapping), and <tt>accounts[0].balance</tt> as the third element (index based
	 * mapping).
	 * <p>
	 * If you require access to the other features of Dozer in your mappings (customer getters/setters, bean factories,
	 * custom converters), then you should supply your own DozerBeanMapper to the Writer instead.
	 * 
	 * @param clazz
	 *            the class to add mapping configuration for (same as the type passed into write methods)
	 * @param fieldMapping
	 *            the field mapping for for each column (may contain <tt>null</tt> elements to indicate ignored columns)
	 * @throws NullPointerException
	 *             if clazz or fieldMapping is null
	 * @since 2.0.0
	 */
	void configureBeanMapping(Class<?> clazz, String[] fieldMapping);
	
	/**
	 * Configures the underlying DozerBeanMapper with the mappings required to map from the CSV file to the specified
	 * class (this method may only be called before reading, as it's not possible to configure a DozerBeanMapper that
	 * has already been initialized). Generally this method will only be called once, but it may called more times to
	 * add mappings for other classes (you can define mappings for two different subclasses for example, but if you
	 * define a mapping for the parent class then that will take precedence - inheritance mapping isn't supported).
	 * <p>
	 * Each element of the fieldMapping array represents a CSV column to be read and uses the standard Dozer field
	 * mapping syntax. For example, if you were configuring the mappings for Person class you might define
	 * <tt>firstName</tt> as the first element (just a simple field mapping), <tt>address.city</tt> as the second
	 * element (a nested - or deep - field mapping), and <tt>accounts[0].balance</tt> as the third element (index based
	 * mapping).
	 * <p>
	 * If you are mapping to an indexed list element (e.g. <tt>accounts[0]</tt>) and using a cell processor to return a
	 * custom bean type (e.g. a <tt>ParseAccount</tt> processor that creates an <tt>Account</tt> bean), you will need to
	 * specify a hint for that column so Dozer can map that column.
	 * <p>
	 * If you require access to the other features of Dozer in your mappings (customer getters/setters, bean factories,
	 * custom converters), then you should supply your own DozerBeanMapper to the Writer instead.
	 * 
	 * @param clazz
	 *            the class to add mapping configuration for (same as the type passed into write methods)
	 * @param fieldMapping
	 *            the field mapping for for each column (may contain <tt>null</tt> elements to indicate ignored columns)
	 * @param hintTypes
	 *            an array of types used as hints for Dozer when mapping to an indexed list element (e.g.
	 *            <tt>accounts[0]</tt>) - a null element indicates no hint is required for that column
	 * @throws NullPointerException
	 *             if clazz, fieldMapping, or hintTypes is null
	 * @throws IllegalArgumentException
	 *             if fieldMapping.length != hintTypes.length
	 * @since 2.1.0
	 */
	void configureBeanMapping(Class<?> clazz, String[] fieldMapping, Class<?>[] hintTypes);
	
	/**
	 * Reads a row of a CSV file and populates an instance of the specified class, using Dozer to map column values to
	 * the appropriate fields.
	 * 
	 * @param clazz
	 *            the type to instantiate
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws MappingException
	 *             if there was an exception during Dozer mapping
	 * @throws NullPointerException
	 *             if clazz is null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @since 2.0.0
	 */
	<T> T read(Class<T> clazz) throws IOException;
	
	/**
	 * Reads a row of a CSV file and populates the supplied bean, using Dozer to map column values to the appropriate
	 * fields.
	 * 
	 * @param bean
	 *            the bean to populate
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws MappingException
	 *             if there was an exception during Dozer mapping
	 * @throws NullPointerException
	 *             if bean is null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @since 2.2.0
	 */
	<T> T read(T bean) throws IOException;
	
	/**
	 * Reads a row of a CSV file and populates an instance of the specified class, using Dozer to map column values to
	 * the appropriate fields. Before population the data can be further processed by cell processors (each element in
	 * the processors array corresponds with a CSV column). A <tt>null</tt> entry in the processors array indicates no
	 * further processing is required (the unprocessed String value will be set on the bean's field) - though Dozer will
	 * attempt some conversions of it's own it the types don't match.
	 * 
	 * @param clazz
	 *            the type to instantiate
	 * @param processors
	 *            the cell processors
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws MappingException
	 *             if there was an exception during Dozer mapping
	 * @throws NullPointerException
	 *             if clazz is null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @since 2.0.0
	 */
	<T> T read(Class<T> clazz, CellProcessor... processors) throws IOException;
	
	/**
	 * Reads a row of a CSV file and populates the supplied bean, using Dozer to map column values to the appropriate
	 * fields. Before population the data can be further processed by cell processors (each element in the processors
	 * array corresponds with a CSV column). A <tt>null</tt> entry in the processors array indicates no further
	 * processing is required (the unprocessed String value will be set on the bean's field) - though Dozer will attempt
	 * some conversions of it's own it the types don't match.
	 * 
	 * @param bean
	 *            the bean to populate
	 * @param processors
	 *            the cell processors
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws MappingException
	 *             if there was an exception during Dozer mapping
	 * @throws NullPointerException
	 *             if bean is null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @since 2.2.0
	 */
	<T> T read(T bean, CellProcessor... processors) throws IOException;
	
}
