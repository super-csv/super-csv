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
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;

/**
 * Interface for CSV writers writing objects/beans to CSV using Dozer.
 * 
 * @author James Bassett
 * @since 2.0.0
 */
public interface ICsvDozerBeanWriter {
	
	/**
	 * Configures the underlying DozerBeanMapper with the mappings required to map from the specified class to the CSV
	 * file (this method may only be called before writing, as it's not possible to configure a DozerBeanMapper that has
	 * already been initialized). Generally this method will only be called once, but it may called more times to add
	 * mappings for other classes (you can define mappings for two different subclasses for example, but if you define a
	 * mapping for the parent class then that will take precedence - inheritance mapping isn't supported).
	 * <p>
	 * Each element of the fieldMapping array represents a CSV column to be written and uses the standard Dozer field
	 * mapping syntax. For example, if you were configuring the mappings for Person class you might define
	 * <tt>firstName</tt> as the first element (just a simple field mapping), <tt>address.city</tt> as the second
	 * element (a nested - or deep - field mapping), and
	 * <tt>accounts[0].balance</tt> as the third element (index based mapping).
	 * <p>
	 * If you require access to the other features of Dozer in your mappings (customer getters/setters, bean factories, custom converters), 
	 * then you should supply your own DozerBeanMapper to the Writer instead.
	 * 
	 * @param clazz
	 *            the class to add mapping configuration for (same as the type passed into write methods)
	 * @param fieldMapping
	 *            the field mapping for for each column (cannot contain <tt>null</tt> elements)
	 * @throws NullPointerException
	 *             if clazz or fieldMapping (or one of its elements) is null
	 * @since 2.0.0
	 */
	void configureBeanMapping(Class<?> clazz, String[] fieldMapping);
	
	/**
	 * Writes the fields of the object as columns of a CSV file, using the pre-configured DozerBeanMapper to map fields
	 * to the appropriate columns. <tt>toString()</tt> will be called on each element prior to writing.
	 * 
	 * @param source
	 *            the object (bean instance) containing the values to write
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws MappingException
	 *             if there was an exception during Dozer mapping
	 * @throws NullPointerException
	 *             if source is null
	 * @throws SuperCsvException
	 *             if there was a general exception while writing
	 * @since 2.0.0
	 */
	void write(Object source) throws IOException;
	
	/**
	 * Writes the fields of the object as columns of a CSV file, using the pre-configured DozerBeanMapper to map fields
	 * to the appropriate columns.
	 * <p>
	 * Before writing, the data can be further processed by cell processors (each element in the processors array
	 * corresponds with a CSV column). A <tt>null</tt> entry in the processors array indicates no further processing is
	 * required (the value returned by toString() will be written as the column value). <tt>toString()</tt> will be
	 * called on each (processed) element prior to writing.
	 * 
	 * @param source
	 *            the object (bean instance) containing the values to write
	 * @param processors
	 *            an array of CellProcessors used to further process data before it is written (each element in the
	 *            processors array corresponds with a CSV column - the number of processors should match the number of
	 *            columns). A <tt>null</tt> entry indicates no further processing is required (the value returned by
	 *            toString() will be written as the column value).
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws MappingException
	 *             if there was an exception during Dozer mapping
	 * @throws NullPointerException
	 *             if source, nameMapping or processors are null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if there was a general exception while writing/processing
	 * @since 2.0.0
	 */
	void write(Object source, CellProcessor[] processors) throws IOException;
	
}
