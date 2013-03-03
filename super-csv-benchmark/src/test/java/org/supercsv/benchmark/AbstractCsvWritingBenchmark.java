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

import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.io.dozer.ICsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;

/**
 * Provides methods to benchmark all of Super CSV's writers.
 * <p>
 * The benchmarks use JUnitBenchmarks
 * (http://labs.carrotsearch.com/junit-benchmarks-tutorial.html). As shown on
 * the website, you can generate a Google charts graph from the results.
 * <p>
 * No assertions were required to prevent code being optimized away, but they
 * were added to ensure the correct number of rows were written.
 * 
 * @author James Bassett
 */
public class AbstractCsvWritingBenchmark extends AbstractBenchmark {

	/**
	 * Times CsvListWriter.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param data
	 *            the data to write
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvListWriter(final Writer writer,
			final CsvPreference preference, final String[] header,
			final List<List<Object>> data, final int rows) throws Exception {

		ICsvListWriter listWriter = null;
		try {
			listWriter = new CsvListWriter(writer, preference);
			listWriter.writeHeader(header);

			for (List<?> row : data) {
				listWriter.write(row);
			}

			assertEquals(rows + 1, listWriter.getRowNumber());

		} finally {
			listWriter.close();
		}

	}

	/**
	 * Times CsvListWriter using processors.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param data
	 *            the data to write
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvListWriterUsingProcessors(final Writer writer,
			final CsvPreference preference, final String[] header,
			final List<List<Object>> data, final CellProcessor[] processors,
			final int rows) throws Exception {

		ICsvListWriter listWriter = null;
		try {
			listWriter = new CsvListWriter(writer, preference);
			listWriter.writeHeader(header);

			for (List<?> row : data) {
				listWriter.write(row, processors);
			}

			assertEquals(rows + 1, listWriter.getRowNumber());

		} finally {
			listWriter.close();
		}

	}

	/**
	 * Times CsvMapWriter.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param data
	 *            the data to write
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvMapWriter(final Writer writer,
			final CsvPreference preference, final String[] header,
			final List<Map<String, Object>> data, final int rows)
			throws Exception {

		ICsvMapWriter mapWriter = null;
		try {
			mapWriter = new CsvMapWriter(writer, preference);
			mapWriter.writeHeader(header);

			for (Map<String, ?> map : data) {
				mapWriter.write(map, header);
			}

			assertEquals(rows + 1, mapWriter.getRowNumber());

		} finally {
			mapWriter.close();
		}
	}

	/**
	 * Times CsvMapWriter using processors.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param data
	 *            the data to write
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvMapWriterUsingProcessors(final Writer writer,
			final CsvPreference preference, final String[] header,
			final List<Map<String, Object>> data,
			final CellProcessor[] processors, final int rows) throws Exception {

		ICsvMapWriter mapWriter = null;
		try {
			mapWriter = new CsvMapWriter(writer, preference);
			mapWriter.writeHeader(header);

			for (Map<String, ?> map : data) {
				mapWriter.write(map, header, processors);
			}

			assertEquals(rows + 1, mapWriter.getRowNumber());

		} finally {
			mapWriter.close();
		}
	}

	/**
	 * Times CsvBeanWriter.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param data
	 *            the data to write
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvBeanWriter(final Writer writer,
			final CsvPreference preference, final String[] header,
			final List<?> data, final int rows) throws Exception {
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(writer, preference);
			beanWriter.writeHeader(header);

			for (Object o : data) {
				beanWriter.write(o, header);
			}

			assertEquals(rows + 1, beanWriter.getRowNumber());

		} finally {
			beanWriter.close();
		}
	}

	/**
	 * Times CsvBeanWriter using processors.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param data
	 *            the data to write
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvBeanWriterUsingProcessors(final Writer writer,
			final CsvPreference preference, final String[] header,
			final List<?> data, final CellProcessor[] processors, final int rows)
			throws Exception {

		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(writer, preference);
			beanWriter.writeHeader(header);

			for (Object o : data) {
				beanWriter.write(o, header, processors);
			}

			assertEquals(rows + 1, beanWriter.getRowNumber());

		} finally {
			beanWriter.close();
		}
	}

	/**
	 * Times CsvDozerBeanWriter.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param beanClass
	 *            the type of the bean being written
	 * @param data
	 *            the data to write
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvDozerBeanWriter(final Writer writer,
			final CsvPreference preference, final String[] header,
			final Class<?> beanClass, final List<?> data, final int rows)
			throws Exception {

		ICsvDozerBeanWriter dozerBeanWriter = null;
		try {
			dozerBeanWriter = new CsvDozerBeanWriter(writer, preference);
			dozerBeanWriter.writeHeader(header);
			dozerBeanWriter.configureBeanMapping(beanClass, header);

			for (Object o : data) {
				dozerBeanWriter.write(o);
			}

			assertEquals(rows + 1, dozerBeanWriter.getRowNumber());

		} finally {
			dozerBeanWriter.close();
		}
	}

	/**
	 * Times CsvDozerBeanWriter using processors.
	 * 
	 * @param writer
	 *            the writer
	 * @param preference
	 *            the preferences
	 * @param header
	 *            the header
	 * @param beanClass
	 *            the type of the bean being written
	 * @param data
	 *            the data to write
	 * @param processors
	 *            the cell processors
	 * @param rows
	 *            the expected number of rows
	 * @throws Exception
	 */
	public void timeCsvDozerBeanWriterUsingProcessors(final Writer writer,
			final CsvPreference preference, final String[] header,
			final Class<?> beanClass, final List<?> data,
			final CellProcessor[] processors, final int rows) throws Exception {
		ICsvDozerBeanWriter dozerBeanWriter = null;
		try {
			dozerBeanWriter = new CsvDozerBeanWriter(writer, preference);
			dozerBeanWriter.writeHeader(header);
			dozerBeanWriter.configureBeanMapping(beanClass, header);

			for (Object o : data) {
				dozerBeanWriter.write(o, processors);
			}

			assertEquals(rows + 1, dozerBeanWriter.getRowNumber());

		} finally {
			dozerBeanWriter.close();
		}
	}

}
