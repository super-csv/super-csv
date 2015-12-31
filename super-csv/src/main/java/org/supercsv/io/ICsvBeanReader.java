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
package org.supercsv.io;

import java.io.IOException;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.util.BeanField;

/**
 * Interface for CSV readers reading into objects/beans.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Vyacheslav Pushkin
 */
public interface ICsvBeanReader extends ICsvReader {
	
	/**
	 * Reads a row of a CSV file and populates an instance of the specified class, using the supplied name mapping to
	 * map column values to the appropriate fields.
	 * 
	 * @param clazz
	 *            the type to instantiate. If the type is a class then a new instance will be created using the default
	 *            no-args constructor. If the type is an interface, a proxy object which implements the interface will
	 *            be created instead.
	 * @param nameMapping
	 *            an array of Strings linking the CSV columns to their corresponding field in the bean (the array length
	 *            should match the number of columns). A <tt>null</tt> entry in the array indicates that the column
	 *            should be ignored (the field in the bean will be null - or its default value).
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws IllegalArgumentException
	 *             if nameMapping.length != number of columns read
	 * @throws NullPointerException
	 *             if clazz or nameMapping are null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @throws SuperCsvReflectionException
	 *             if there was an reflection exception while mapping the values to the bean
	 * @since 1.0
	 */
	<T> T read(Class<T> clazz, String... nameMapping) throws IOException;
	
	/**
	 * Reads a row of a CSV file and populates the bean, using the supplied name mapping to map column values to the
	 * appropriate fields.
	 * 
	 * @param bean
	 *            the bean to populate
	 * @param nameMapping
	 *            an array of Strings linking the CSV columns to their corresponding field in the bean (the array length
	 *            should match the number of columns). A <tt>null</tt> entry in the array indicates that the column
	 *            should be ignored (the field in the bean will be null - or its default value).
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws IllegalArgumentException
	 *             if nameMapping.length != number of columns read
	 * @throws NullPointerException
	 *             if bean or nameMapping are null
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @throws SuperCsvReflectionException
	 *             if there was an reflection exception while mapping the values to the bean
	 * @since 2.2.0
	 */
	<T> T read(T bean, String... nameMapping) throws IOException;
	
	/**
	 * Reads a row of a CSV file and populates an instance of the specified class, using the supplied name mapping to
	 * map column values to the appropriate fields. Before population the data can be further processed by cell
	 * processors (as with the nameMapping array, each element in the processors array corresponds with a CSV column). A
	 * <tt>null</tt> entry in the processors array indicates no further processing is required (the unprocessed String
	 * value will be set on the bean's field).
	 * 
	 * @param clazz
	 *            the type to instantiate. If the type is a class then a new instance will be created using the default
	 *            no-args constructor. If the type is an interface, a proxy object which implements the interface will
	 *            be created instead.
	 * @param nameMapping
	 *            an array of Strings linking the CSV columns to their corresponding field in the bean (the array length
	 *            should match the number of columns). A <tt>null</tt> entry in the array indicates that the column
	 *            should be ignored (the field in the bean will be null - or its default value).
	 * @param processors
	 *            an array of CellProcessors used to further process data before it is populated on the bean (each
	 *            element in the processors array corresponds with a CSV column - the number of processors should match
	 *            the number of columns). A <tt>null</tt> entry indicates no further processing is required (the
	 *            unprocessed String value will be set on the bean's field).
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws NullPointerException
	 *             if clazz, nameMapping, or processors are null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @throws SuperCsvReflectionException
	 *             if there was an reflection exception while mapping the values to the bean
	 * @since 1.0
	 */
	<T> T read(Class<T> clazz, String[] nameMapping, CellProcessor... processors) throws IOException;
	
	/**
	 * Reads a row of a CSV file and populates the bean, using the supplied name mapping to map column values to the
	 * appropriate fields. Before population the data can be further processed by cell processors (as with the
	 * nameMapping array, each element in the processors array corresponds with a CSV column). A <tt>null</tt> entry in
	 * the processors array indicates no further processing is required (the unprocessed String value will be set on the
	 * bean's field).
	 * 
	 * @param bean
	 *            the bean to populate
	 * @param nameMapping
	 *            an array of Strings linking the CSV columns to their corresponding field in the bean (the array length
	 *            should match the number of columns). A <tt>null</tt> entry in the array indicates that the column
	 *            should be ignored (the field in the bean will be null - or its default value).
	 * @param processors
	 *            an array of CellProcessors used to further process data before it is populated on the bean (each
	 *            element in the processors array corresponds with a CSV column - the number of processors should match
	 *            the number of columns). A <tt>null</tt> entry indicates no further processing is required (the
	 *            unprocessed String value will be set on the bean's field).
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws NullPointerException
	 *             if bean, nameMapping, or processors are null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @throws SuperCsvReflectionException
	 *             if there was an reflection exception while mapping the values to the bean
	 * @since 2.2.0
	 */
	<T> T read(T bean, String[] nameMapping, CellProcessor... processors) throws IOException;

	/**
	 * <p>Reads a row of a CSV file and populates an instance of the specified class, using the supplied mapping to
	 * map column values to the appropriate fields. Before population the data can be further processed by cell
	 * processors. <tt>columnMapping</tt> parameter is a map where key is CSV column name and value is
	 * {@link org.supercsv.util.BeanField} object, which contains bean field name and optional cell processor instance.
	 * Columns which names are not included in the map are ignored (the field in the bean will be null - or its
	 * default value). A <tt>null</tt> value of cell processor indicates no further processing is required (the
	 * unprocessed String value will be set on the bean's field).</p>
	 *
	 * <p>This method is useful when:</p>
	 *
	 * <ul>
	 *   <li>CSV column names are different from bean field names</li>
	 *   <li>Not all columns in CSV file are needed to be added to the bean</li>
	 * </ul>
	 * 
	 * <p>This method is more convenient than {@code read(Class<T> clazz, String[] nameMapping)} when number of columns in CSV file is large.
	 * However, a header (column names) must be present in CSV file.</p>
	 *
	 * <p>Note that {@link org.supercsv.util.BeanField}'s <tt>equals()</tt> and <tt>hashCode()</tt> methods are based
	 * on field name value and cell processor <b>identity</b> (unless cell processor implementation overrides
	 * <tt>equals()</tt> and <tt>hashCode()</tt>, which is not the case for built-in cell processors).
	 * Therefore the following code will return false.</p>
	 *
	 * <pre>
	 * {@code map.put("columnName", BeanField.of("fieldName", new ParseDate("dd/MM/yyyy"));
	 *  return map.containsValue(BeanField.of("fieldName", new ParseDate("dd/MM/yyyy"));
	 * }
	 * </pre>
	 *
	 * @param clazz
	 *            the type to instantiate. If the type is a class then a new instance will be created using the default
	 *            no-args constructor. If the type is an interface, a proxy object which implements the interface will
	 *            be created instead.
	 * @param columnMapping
	 *            a map where key is CSV column name and value is {@link org.supercsv.util.BeanField} object, which contains bean field name and
	 *            optional cell processor instance. Columns which names are not included in the map are ignored
	 *            (the field in the bean will be null - or its default value).
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *            if an I/O error occurred
	 * @throws NullPointerException
	 *            if clazz or columnMapping are null
	 * @throws IllegalArgumentException
	 *            if columnMapping contains column name which is not actually present in CSV file
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *            if current row does not contain a column specified by columnMapping, or if there was a general exception
	 *            while reading/processing
	 * @throws SuperCsvReflectionException
	 *            if there was an reflection exception while mapping the values to the bean
	 * @since 2.5.0
	 */
	<T> T read(Class<T> clazz, Map<String, BeanField> columnMapping) throws IOException;

	/**
	 * <p>Reads a row of a CSV file and populates the bean, using the supplied mapping to
	 * map column values to the appropriate fields. Before population the data can be further processed by cell
	 * processors. <tt>columnMapping</tt> parameter is a map where key is CSV column name and value is
	 * {@link org.supercsv.util.BeanField} object, which contains bean field name and optional cell processor instance.
	 * Columns which names are not included in the map are ignored (the field in the bean will be null - or its
	 * default value). A <tt>null</tt> value of cell processor indicates no further processing is required (the
	 * unprocessed String value will be set on the bean's field).</p>
	 *
	 * <p>This method is useful when:</p>
	 *
	 * <ul>
	 *   <li>CSV column names are different from bean field names</li>
	 *   <li>Not all columns in CSV file are needed to be added to the bean</li>
	 * </ul>
	 *
	 * <p>This method is more convenient than <tt>read(T bean, String[] nameMapping)</tt> when number of columns in CSV file is large.
	 * However, a header (column names) must be present in CSV file.</p>
	 *
	 * <p>Note that {@link org.supercsv.util.BeanField}'s <tt>equals()</tt> and <tt>hashCode()</tt> methods are based
	 * on field name value and cell processor <b>identity</b> (unless cell processor implementation overrides
	 * <tt>equals</tt> and <tt>hashCode()</tt>, which is not the case for built-in cell processors).
	 * Therefore the following code will return false.</p>
	 *
	 * <pre>
	 * {@code map.put("columnName", BeanField.of("fieldName", new ParseDate("dd/MM/yyyy"));
	 *  return map.containsValue(BeanField.of("fieldName", new ParseDate("dd/MM/yyyy"));
	 * }
	 * </pre>
	 *
	 * @param bean
	 *            the bean to populate
	 * @param columnMapping
	 *            a map where key is CSV column name and value is {@link org.supercsv.util.BeanField} object. BeanField objects contain bean field
	 *            name and optional cell processor instance. Columns which names are not included in the map are ignored
	 *            (the field in the bean will be null - or its default value).
	 * @param <T>
	 *            the bean type
	 * @return a populated bean or null if EOF
	 * @throws IOException
	 *            if an I/O error occurred
	 * @throws NullPointerException
	 *            if bean or columnMapping are null
	 * @throws IllegalArgumentException
	 *            if columnMapping contains column name which is not actually present in CSV file
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *            if current row does not contain a column specified by columnMapping, or if there was a general exception
	 *            while reading/processing
	 * @throws SuperCsvReflectionException
	 *            if there was an reflection exception while mapping the values to the bean
	 * @since 2.5.0
	 */
	<T> T read(T bean, Map<String, BeanField> columnMapping) throws IOException;

}
