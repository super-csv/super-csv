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
package org.supercsv.webtests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

public class WriteWebExampleTest {
	StringWriter outFile;
	ICsvMapWriter writer;
	
	@Before
	public void setUp() {
		outFile = new StringWriter();
		writer = new CsvMapWriter(outFile, CsvPreference.EXCEL_PREFERENCE);
	}
	
	@After
	public void tearDown() throws IOException {
		outFile.close();
	}
	
	@Test
	public void testWriting() throws IOException {
		final String[] header = new String[] { "name", "city", "zip" };
		final HashMap<String, ? super Object> data1 = new HashMap<String, Object>();
		data1.put(header[0], "Karl");
		data1.put(header[1], "Tent city");
		data1.put(header[2], 5565);
		final HashMap<String, ? super Object> data2 = new HashMap<String, Object>();
		data2.put(header[0], "Banjo");
		data2.put(header[1], "River side");
		data2.put(header[2], 5551);
		writer.writeHeader(header);
		writer.write(data1, header);
		writer.write(data2, header);
		writer.close();
		Assert.assertEquals("name,city,zip\nKarl,Tent city,5565\nBanjo,River side,5551\n", outFile.toString());
	}
}
