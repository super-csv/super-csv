/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.test.supercsv.speedtests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.junit.BeforeClass;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class AbstractSpeedTest implements SpeedTestConstants {
	public static final int LINES_IN_TEST_FILE = 250000;
	static final String[] header = new String[] { "no", "name1", "name2", "phone", "date" };
	static final CellProcessor[] processors = new CellProcessor[] { new ParseLong(), null, null, new ParseLong(), new ParseDate("dd/MM/yy") };

	@BeforeClass
	public static void createTestFile() throws Exception {
		// only generate a test file if it doesn't exist
		if(new File(TEST_FILE).exists()) return;
		final Writer w = new BufferedWriter(new FileWriter(TEST_FILE));
		for(int i = 0; i < LINES_IN_TEST_FILE; i++)
			w.write(TestDataCreators.createAnonymousLine_num_str_str_num_date());
		w.close();

	}

	//
	// @AfterClass
	// public static void tearDown() throws Exception {
	// final File f = new File(TEST_FILE);
	// f.delete();
	// }
	protected String makeTableLine(final String msg, final long baseTime, final long runTime) {
		return String.format("<tr><td>%s</td><td>%4.2f%%</td><td>%4.2f</td></tr>", msg, (100.0 * runTime) / baseTime, runTime / (TEST_RUNS * 1000.0));
	}
}
