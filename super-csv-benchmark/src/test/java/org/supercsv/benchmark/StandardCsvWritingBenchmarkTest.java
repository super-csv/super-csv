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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.supercsv.benchmark.cellprocessor.FormatIconColour;
import org.supercsv.benchmark.cellprocessor.FormatStopTypeAndName;
import org.supercsv.benchmark.model.TransportLocation;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.Util;

import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

/**
 * Benchmarks each of the Super CSV writers. The CSV file used as the data for
 * writing is the first 50,000 rows of a file free for download from the
 * Guardian newspaper website
 * (http://www.guardian.co.uk/news/datablog/2010/sep/27/uk-transport-national
 * -public-data-repository#data). The file (and consquently the data being
 * written) contains no embedded newlines.
 * 
 * @author James Bassett
 */
@BenchmarkMethodChart(filePrefix = "StandardCsvWritingBenchmark")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StandardCsvWritingBenchmarkTest extends AbstractCsvWritingBenchmark {

	// CSV file with 50,001 lines (including header)
	private static final String CSV_FILE = "Britain's transport infrastructure.csv";

	private static final String OUTPUT_DIR = "target" + File.separator;

	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;

	private static String[] HEADER;

	private static final CellProcessor[] PROCESSORS = { new NotNull(), // atcoCode
			new NotNull(), // easting
			new NotNull(), // northing
			new NotNull(), // longitude
			new NotNull(), // latitude
			new NotNull(), // commonName
			new Optional(), // identifier
			new NotNull(), // direction
			new NotNull(), // street
			new Optional(), // landmark
			new NotNull(), // natGazId
			new Optional(), // natGazLocality
			new NotNull(), // stopType
			new NotNull(new FormatStopTypeAndName()), // stopTypeAndName
			new NotNull(new FormatIconColour()), // iconColour
	};

	// the number of data rows to read (max possible is 50,000)
	private static final int ROWS = 50000;

	private static List<Object> BEAN_DATA;
	private static List<List<Object>> LIST_DATA;
	private static List<Map<String, Object>> MAP_DATA;

	/**
	 * Set up the data for writing by reading the required number of records
	 * from the reading benchmark CSV file.
	 */
	@BeforeClass
	public static void setUpData() throws Exception {
		BEAN_DATA = new ArrayList<Object>();
		LIST_DATA = new ArrayList<List<Object>>();
		MAP_DATA = new ArrayList<Map<String, Object>>();

		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(CSV_FILE),
					CsvPreference.STANDARD_PREFERENCE);
			HEADER = beanReader.getHeader(true);

			for (int j = 0; j < ROWS; j++) {
				TransportLocation location = beanReader.read(
						TransportLocation.class, HEADER,
						StandardCsvReadingBenchmarkTest.PROCESSORS);

				// bean data
				BEAN_DATA.add(location);

				// list data
				List<Object> list = Arrays.asList(location.toObjectArray());
				LIST_DATA.add(list);

				// map data
				Map<String, Object> map = new HashMap<String, Object>();
				Util.filterListToMap(map, HEADER, list);
				MAP_DATA.add(map);
			}

		} finally {
			beanReader.close();
		}

	}

	/**
	 * Times CsvListWriter.
	 */
	@Test
	public void testCsvListWriter() throws Exception {
		timeCsvListWriter(new FileWriter(OUTPUT_DIR + "CsvListWriter" + ROWS
				+ ".csv"), PREFS, HEADER, LIST_DATA, ROWS);
	}

	/**
	 * Times CsvListWriter using processors.
	 */
	@Test
	public void testCsvListWriterUsingProcessors() throws Exception {
		timeCsvListWriterUsingProcessors(new FileWriter(OUTPUT_DIR
				+ "CsvListWriterUsingProcessors" + ROWS + ".csv"), PREFS,
				HEADER, LIST_DATA, PROCESSORS, ROWS);

	}

	/**
	 * Times CsvMapWriter.
	 */
	@Test
	public void testCsvMapWriter() throws Exception {
		timeCsvMapWriter(new FileWriter(OUTPUT_DIR + "CsvMapWriter" + ROWS
				+ ".csv"), PREFS, HEADER, MAP_DATA, ROWS);
	}

	/**
	 * Times CsvMapWriter using processors.
	 */
	@Test
	public void testCsvMapWriterUsingProcessors() throws Exception {
		timeCsvMapWriterUsingProcessors(new FileWriter(OUTPUT_DIR
				+ "CsvMapWriterUsingProcessors" + ROWS + ".csv"), PREFS,
				HEADER, MAP_DATA, PROCESSORS, ROWS);
	}

	/**
	 * Times CsvBeanWriter.
	 */
	@Test
	public void testCsvBeanWriter() throws Exception {
		timeCsvBeanWriter(new FileWriter(OUTPUT_DIR + "CsvBeanWriter" + ROWS
				+ ".csv"), PREFS, HEADER, BEAN_DATA, ROWS);
	}

	/**
	 * Times CsvBeanWriter using processors.
	 */
	@Test
	public void testCsvBeanWriterUsingProcessors() throws Exception {
		timeCsvBeanWriterUsingProcessors(new FileWriter(OUTPUT_DIR
				+ "CsvBeanWriterUsingProcessors" + ROWS + ".csv"), PREFS,
				HEADER, BEAN_DATA, PROCESSORS, ROWS);
	}

	/**
	 * Times CsvDozerBeanWriter.
	 */
	@Test
	public void testCsvDozerBeanWriter() throws Exception {
		timeCsvDozerBeanWriter(new FileWriter(OUTPUT_DIR + "CsvDozerBeanWriter"
				+ ROWS + ".csv"), PREFS, HEADER, TransportLocation.class,
				BEAN_DATA, ROWS);
	}

	/**
	 * Times CsvDozerBeanWriter using processors.
	 */
	@Test
	public void testCsvDozerBeanWriterUsingProcessors() throws Exception {
		timeCsvDozerBeanWriterUsingProcessors(new FileWriter(OUTPUT_DIR
				+ "CsvDozerBeanWriterUsingProcessors" + ROWS + ".csv"), PREFS,
				HEADER, TransportLocation.class, BEAN_DATA, PROCESSORS, ROWS);
	}

}
