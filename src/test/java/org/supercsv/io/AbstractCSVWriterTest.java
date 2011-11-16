/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.supercsv.io;

import static org.circulartests.CircularData.columnsToWrite;
import static org.circulartests.CircularData.expectedReadResultsFromColumnToWrite;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * @author Kasper B. Graversen
 */
public class AbstractCSVWriterTest {

/**
 * implement abstract class in order to test it
 * 
 * @author Kasper B. Graversen
 */
static class TestClass extends AbstractCsvWriter {
TestClass(final Writer stream, final CsvPreference preference) {
	super(stream, preference);
}
}

@Test
public void should_escape() {
	final TestClass absWriter = new TestClass(new StringWriter(), CsvPreference.EXCEL_PREFERENCE);
	
	assertThat(columnsToWrite.length, is(expectedReadResultsFromColumnToWrite.length));
	for( int i = 0; i < columnsToWrite.length; i++ ) {
		Assert.assertEquals(expectedReadResultsFromColumnToWrite[i], absWriter.escapeString(columnsToWrite[i]));
		// assertThat(absWriter.escapeString(columnsToWrite[i]), is(expectedOutput[i]));
	}
	
}
// @Test
// public void should_escape_comma_outside_quote() throws IOException {
// StringWriter out = new StringWriter();
// TestClass writer = new TestClass(out, CsvPreference.EXCEL_PREFERENCE);
// String[] columnsToWrite = { "comma, outside quote", "\"comma, inside quotes\"", // commas
// "\"quote\" outside quotes", "\"quote \"inside\" quotes\"", // quotes
// "newline\noutside quotes", "\"newline\ninside quotes\"", // newline
// "normal text" // normal
// };
//
// // exercise
// writer.write(columnsToWrite);
// writer.close();
//
// // test
// Assert.assertEquals("comma \",\" outside quote, \"comma, inside quotes\", " + // commas
// "" + // quotes
// "" + // newline
// "", // normal
// out.getBuffer().toString());
// }
}
