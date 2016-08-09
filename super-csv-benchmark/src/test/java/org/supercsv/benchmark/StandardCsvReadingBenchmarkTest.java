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

import java.io.FileReader;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.supercsv.benchmark.cellprocessor.ParseIconColour;
import org.supercsv.benchmark.cellprocessor.ParseStopType;
import org.supercsv.benchmark.cellprocessor.ParseStopTypeAndName;
import org.supercsv.benchmark.model.TransportLocation;
import org.supercsv.benchmark.model.TransportLocationStrings;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;

import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;

/**
 * Benchmarks each of the Super CSV readers. The CSV file is the first 50,000
 * rows of a file free for download from the Guardian newspaper website
 * (http://www.guardian.co.uk/news/datablog/2010/sep/27/uk-transport-national
 * -public-data-repository#data). The existing header was replaced with a
 * CsvBeanReader-compatible header for convenience. The file contains no
 * embedded newlines.
 *
 * @author James Bassett
 */
@BenchmarkMethodChart(filePrefix = "StandardCsvReadingBenchmark")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StandardCsvReadingBenchmarkTest extends AbstractCsvReadingBenchmark {

	private static final CsvPreference PREFS = new CsvPreference.Builder(
			CsvPreference.STANDARD_PREFERENCE).build();
	private static final CsvPreference TRIPLE_PIPE_DELIMITER_PREFS =new CsvPreference.Builder('"', "|||", "\r\n").build();

	// CSV file with 50,001 lines (including header)
	private static final String CSV_FILE = "Britain's transport infrastructure.csv";
	private static final String TRIPLE_PIPE_DELIMITER_CSV_FILE = "Britain's transport infrastructure multiple symbols delimiter.csv";

	public static final CellProcessor[] PROCESSORS = { new NotNull(), // atcoCode
			new NotNull(new ParseDouble()), // easting
			new NotNull(new ParseDouble()), // northing
			new NotNull(new ParseDouble()), // longitude
			new NotNull(new ParseDouble()), // latitude
			new NotNull(), // commonName
			new Optional(), // identifier
			new NotNull(), // direction
			new NotNull(), // street
			new Optional(), // landmark
			new NotNull(), // natGazId
			new Optional(), // natGazLocality
			new NotNull(new ParseStopType()), // stopType
			new NotNull(new ParseStopTypeAndName()), // stopTypeAndName
			new NotNull(new ParseIconColour()), // iconColour
	};

	// the number of data rows to read (max possible is 50,000)
	private static final int ROWS = 50000;

	/**
	 * Times CsvListReader.
	 */
	@Test
	public void testCsvListReader() throws Exception {
		timeCsvListReader(new FileReader(CSV_FILE), PREFS, ROWS);
	}

	/**
	 * Times CsvListReader using processors.
	 */
	@Test
	public void testCsvListReaderUsingProcessors() throws Exception {
		timeCsvListReaderUsingProcessors(new FileReader(CSV_FILE), PREFS,
				PROCESSORS, ROWS);
	}

	/**
	 * Times CsvMapReader.
	 */
	@Test
	public void testCsvMapReader() throws Exception {
		timeCsvMapReader(new FileReader(CSV_FILE), PREFS, ROWS);
	}

	/**
	 * Times CsvMapReader using processors.
	 */
	@Test
	public void testCsvMapReaderUsingProcessors() throws Exception {
		timeCsvMapReaderUsingProcessors(new FileReader(CSV_FILE), PREFS,
				PROCESSORS, ROWS);
	}

	/**
	 * Times CsvBeanReader.
	 */
	@Test
	public void testCsvBeanReader() throws Exception {
		timeCsvBeanReader(new FileReader(CSV_FILE), PREFS,
				TransportLocationStrings.class, ROWS);
	}

	/**
	 * Times CsvBeanReader using processors.
	 */
	@Test
	public void testCsvBeanReaderUsingProcessors() throws Exception {
		timeCsvBeanReaderUsingProcessors(new FileReader(CSV_FILE), PREFS,
				TransportLocation.class, PROCESSORS, ROWS);
	}

	/**
	 * Times CsvDozerBeanReader.
	 */
	@Test
	public void testCsvDozerBeanReader() throws Exception {
		timeCsvDozerBeanReader(new FileReader(CSV_FILE), PREFS,
				TransportLocationStrings.class, ROWS);
	}

	/**
	 * Times CsvDozerBeanReader using processors.
	 */
	@Test
	public void testCsvDozerBeanReaderUsingProcessors() throws Exception {
		timeCsvDozerBeanReaderUsingProcessors(new FileReader(CSV_FILE), PREFS,
				TransportLocation.class, PROCESSORS, ROWS);
	}

}
