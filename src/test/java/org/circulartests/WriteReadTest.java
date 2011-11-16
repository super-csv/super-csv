/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.circulartests;

import static org.circulartests.CircularData.columnsToWrite;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Write text to a csv file, read it again and compare the two.
 * 
 * @author Kasper B. Graversen
 */
public class WriteReadTest {
@Test
public void should_handle_all_cases_of_writing_and_reading_special_chars() throws IOException {
	// write a bunch of data
	final StringWriter outFile = new StringWriter();
	final CsvListWriter csvWriter = new CsvListWriter(outFile, CsvPreference.EXCEL_PREFERENCE);
	csvWriter.write(columnsToWrite);
	csvWriter.close();
	final String writtenData = outFile.toString();
	
	// read data
	// System.out.println("read text:\n----------\n" + writtenData);
	final String[] readFileData = new CsvListReader(new StringReader(writtenData), CsvPreference.EXCEL_PREFERENCE)
		.read().toArray(new String[0]);
	
	// data must be the same
	Assert.assertEquals(columnsToWrite, readFileData);
}
}
