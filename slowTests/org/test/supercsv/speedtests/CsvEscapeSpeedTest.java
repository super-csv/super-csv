/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.test.supercsv.speedtests;

import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.AbstractCsvWriter_v110;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.NullWriter;
import org.timespread.TimeSpread;
import org.timespread.TimedCodeBlock;
import org.timespread.reporter.Reporter;
import org.timespread.reporter.ReporterConfiguration;

/**
 * Measure the escaping of strings before they are written to the file
 * 
 * @author Kasper B. Graversen
 */
public class CsvEscapeSpeedTest {
	/**
	 * implementation of the v1.10 writer
	 * 
	 * @author Kasper B. Graversen
	 */
	class ListWriter_v110 extends AbstractCsvWriter_v110 implements ICsvListWriter {
		public ListWriter_v110(final Writer writer, final CsvPreference prefs) {
			super(writer, prefs);
		}

		@Override
		public void write(final List<? extends Object> content) throws IOException {
			super.write(content);
		}

		public void write(final List<? extends Object> content, final CellProcessor... processors) throws IOException {
			throw new RuntimeException("not yet implemented");
		}

		@Override
		public void write(final Object... content) throws IOException {
			super.write(content);
		}

		@Override
		public void write(final String... content) throws IOException {
			super.write(content);
		}
	}

	/** data to be written */
	static List<String[]>	testData;

	@BeforeClass
	public static void fillTestDataArray() throws IOException {
		final long MAX_LINES_TO_READ = 200000;

		testData = new ArrayList<String[]>();
		final CsvListReader reader = new CsvListReader(new FileReader(SpeedTestConstants.TEST_FILE),
				CsvPreference.EXCEL_PREFERENCE);
		List<String> line;
		long lineReadCounter = 0;
		while((line = reader.read()) != null && lineReadCounter++ < MAX_LINES_TO_READ) {
			// if(lineReadCounter % 50000 == 0) System.out.print(lineReadCounter + " ");
			testData.add(line.toArray(new String[0]));
		}
		System.out.println();
	}

	@Test
	public void test_encoding_for_Write_() throws IOException {
		final int TEST_RUNS = 5;

		TimeSpread spread = new TimeSpread(2, 3);
		spread.setZeroZeroTitle("Version");
		spread.setColumnTitles("Average running time", "Standard error of mean", "% difference");
		spread.setRowTitles("v1.10", "v1.14");

		// write v1.10
		spread.add(0, 0, TEST_RUNS, new TimedCodeBlock() {
			public void execute() {
				try {
					writeFile(new ListWriter_v110(new NullWriter(), CsvPreference.EXCEL_PREFERENCE));
				}
				catch(IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});

		// write v1.14
		spread.add(1, 0, TEST_RUNS, new TimedCodeBlock() {
			public void execute() {
				try {
					writeFile(new CsvListWriter(new NullWriter(), CsvPreference.EXCEL_PREFERENCE));
				}
				catch(IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});

		// set stat columns
		spread.set(0, 1, spread.stdErrorOfMean(0, 0));
		spread.set(1, 1, spread.stdErrorOfMean(1, 0));
		spread.set(1, 2, spread.percentageDifference(1, 0, 0, 0));
		System.out.println(spread.toString());
		System.out.println(new Reporter().report(spread, ReporterConfiguration.makeHTMLConfig()));
		Assert.assertTrue("v1.14 must be faster than v1.10", spread.get(0, 0) > spread.get(1, 0));
	}

	/**
	 * helper method called with different kinds of implementations
	 * 
	 * @param writer
	 * @throws IOException
	 */
	private void writeFile(final ICsvListWriter writer) throws IOException {
		for(final String[] line : testData) {
			writer.write(line);
		}
	}
}
