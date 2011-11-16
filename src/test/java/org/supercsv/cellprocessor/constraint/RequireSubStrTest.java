/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.supercsv.cellprocessor.constraint;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class RequireSubStrTest {
private static final CSVContext CSVCONTEXT = TestConstants.ANONYMOUS_CSVCONTEXT;

@Test
public void constructors_should_hande_valid_input() {
	// constructor 1
	Assert.assertEquals("abba", new RequireSubStr("a", "b").execute("abba", CSVCONTEXT));
	
	// constructor 2
	{
		final java.util.List<String> strl = new ArrayList<String>();
		strl.add("a");
		strl.add("b");
		Assert.assertEquals(true, new RequireSubStr(strl, new ComparerCellProcessor("abba"))
			.execute("abba", CSVCONTEXT));
	}
	// constructor 3
	Assert.assertEquals(true, new RequireSubStr("abba", new ComparerCellProcessor("abba")).execute("abba", CSVCONTEXT));
	
}

@Test
public void should_do_string_containing_substring() {
	Assert.assertEquals("abba", new RequireSubStr("a", "b").execute("abba", CSVCONTEXT));
	Assert.assertEquals("acc", new RequireSubStr("a", "b").execute("acc", CSVCONTEXT));
	Assert.assertEquals("cbc", new RequireSubStr("a", "b").execute("cbc", CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void should_fail_on_missing_string_missing_substring() {
	Assert.assertEquals("abba", new RequireSubStr("a", "b").execute("ccc", CSVCONTEXT));
}
}
