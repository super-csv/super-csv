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
package org.supercsv.benchmark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;

/**
 * Provides methods to benchmark all of Super CSV's readers.
 * <p>
 * The benchmarks use JUnitBenchmarks
 * (http://labs.carrotsearch.com/junit-benchmarks-tutorial.html). As shown on
 * the website, you can generate a Google charts graph from the results.
 * <p>
 * The tests contain assertions on the values returned to ensure that the code
 * is not optimized away.
 * 
 * @author James Bassett
 */
public abstract class AbstractCsvReadingBenchmark extends AbstractBenchmark {

	/**
	 * Times CsvListReader.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvListReader(final Reader reader,
			final CsvPreference preference, final int rows) throws Exception {

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(reader, preference);

			final String[] header = listReader.getHeader(true);
			assertNotNull(header);

			for (int j = 0; j < rows; j++) {
				final List<String> row = listReader.read();
				assertNotNull(row);
			}

			assertEquals(rows + 1, listReader.getRowNumber());

		} finally {
			listReader.close();

		}

	}

	/**
	 * Times CsvListReader using processors.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvListReaderUsingProcessors(final Reader reader,
			final CsvPreference preference, final CellProcessor[] processors,
			final int rows) throws Exception {

		ICsvListReader listReader = null;
		try {
			listReader = new CsvListReader(reader, preference);

			final String[] header = listReader.getHeader(true);
			assertNotNull(header);

			for (int j = 0; j < rows; j++) {
				final List<Object> row = listReader.read(processors);
				assertNotNull(row);
			}

			assertEquals(rows + 1, listReader.getRowNumber());

		} finally {
			listReader.close();
		}
	}

	/**
	 * Times CsvMapReader.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvMapReader(final Reader reader,
			final CsvPreference preference, final int rows) throws Exception {

		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(reader, preference);

			final String[] header = mapReader.getHeader(true);
			assertNotNull(header);

			for (int j = 0; j < rows; j++) {
				final Map<String, String> row = mapReader.read(header);
				assertNotNull(row);
			}

			assertEquals(rows + 1, mapReader.getRowNumber());

		} finally {
			mapReader.close();
		}
	}

	/**
	 * Times CsvMapReader using processors.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvMapReaderUsingProcessors(final Reader reader,
			final CsvPreference preference, final CellProcessor[] processors,
			final int rows) throws Exception {

		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(reader, preference);

			final String[] header = mapReader.getHeader(true);
			assertNotNull(header);

			for (int j = 0; j < rows; j++) {
				final Map<String, Object> row = mapReader.read(header,
						processors);
				assertNotNull(row);
			}

			assertEquals(rows + 1, mapReader.getRowNumber());

		} finally {
			mapReader.close();
		}
	}

	/**
	 * Times CsvBeanReader.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param beanClass
	 *            the type of the bean class
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvBeanReader(final Reader reader,
			final CsvPreference preference, final Class<?> beanClass,
			final int rows) throws Exception {

		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(reader, preference);

			final String[] header = beanReader.getHeader(true);
			assertNotNull(header);

			for (int j = 0; j < rows; j++) {
				final Object bean = beanReader.read(beanClass, header);
				assertNotNull(bean);
			}

			assertEquals(rows + 1, beanReader.getRowNumber());

		} finally {
			beanReader.close();
		}
	}

	/**
	 * Times CsvBeanReader using processors.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param beanClass
	 *            the type of the bean class
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvBeanReaderUsingProcessors(final Reader reader,
			final CsvPreference preference, final Class<?> beanClass,
			final CellProcessor[] processors, final int rows) throws Exception {

		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(reader, preference);

			String[] header = beanReader.getHeader(true);
			assertNotNull(header);

			for (int j = 0; j < rows; j++) {
				final Object bean = beanReader.read(beanClass, header,
						processors);
				assertNotNull(bean);
			}

			assertEquals(rows + 1, beanReader.getRowNumber());

		} finally {
			beanReader.close();
		}
	}

	/**
	 * Times CsvDozerBeanReader.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param beanClass
	 *            the type of the bean class
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvDozerBeanReader(final Reader reader,
			final CsvPreference preference, final Class<?> beanClass,
			final int rows) throws Exception {

		ICsvDozerBeanReader dozerBeanReader = null;
		try {
			dozerBeanReader = new CsvDozerBeanReader(reader, preference);

			final String[] header = dozerBeanReader.getHeader(true);
			assertNotNull(header);

			dozerBeanReader.configureBeanMapping(beanClass, header);

			for (int j = 0; j < rows; j++) {
				final Object bean = dozerBeanReader.read(beanClass);
				assertNotNull(bean);
			}

			assertEquals(rows + 1, dozerBeanReader.getRowNumber());

		} finally {
			dozerBeanReader.close();
		}
	}

	/**
	 * Times CsvDozerBeanReader using processors.
	 * 
	 * @param reader
	 *            the CSV reader
	 * @param preference
	 *            the preferences
	 * @param beanClass
	 *            the type of the bean class
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the number of rows to read
	 * @throws Exception
	 */
	public void timeCsvDozerBeanReaderUsingProcessors(final Reader reader,
			final CsvPreference preference, final Class<?> beanClass,
			final CellProcessor[] processors, final int rows) throws Exception {

		ICsvDozerBeanReader dozerBeanReader = null;
		try {
			dozerBeanReader = new CsvDozerBeanReader(reader, preference);

			final String[] header = dozerBeanReader.getHeader(true);
			assertNotNull(header);

			dozerBeanReader.configureBeanMapping(beanClass, header);

			for (int j = 0; j < rows; j++) {
				final Object bean = dozerBeanReader.read(beanClass, processors);
				assertNotNull(bean);
			}

			assertEquals(rows + 1, dozerBeanReader.getRowNumber());

		} finally {
			dozerBeanReader.close();
		}
	}

}
