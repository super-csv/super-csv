/*
 * Copyright 2007-2015 Kasper B. Graversen
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

package org.supercsv.datacreators;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/** Create data for performance tests */
public class SparseDataCreator {

	@Ignore("This is a test only for convenience")
	@Test
	public void CreateSparseNumericData() throws IOException
	{
		FileWriter f = new FileWriter("SparseNumbersOnly.csv"); 
		CsvListWriter ff = new CsvListWriter(f, CsvPreference.STANDARD_PREFERENCE);
		
		final int rowsToProduce = 1000000;
		int j = 33;
		for(int i = 0; i < rowsToProduce; i++)
		{
			if(j == 0) j = 2;
			ff.write(i, i*j, i/(double)j);
			j = i*j % 1843;
		}
		ff.close();
	}
}
