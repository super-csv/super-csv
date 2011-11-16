/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.circulartests;

/**
 * Data to be used for circular tests
 * 
 * @author Kasper B. Graversen
 */
public interface CircularData {
String[] columnsToWrite = { "comma, outside quote", "\"comma, inside quotes\"", // commas
	"\"quote\" outside quotes", "\"quote \"inside\" quotes\"", // quotes
	"newline\noutside quotes", "\"newline\ninside quotes\"", // newline
	"normal text", // normal
	" first char is a space", // spacing
	"17\" monitor", // single quote in string
	"" // empty
};
String[] expectedReadResultsFromColumnToWrite = { "\"comma, outside quote\"", "\"\"\"comma, inside quotes\"\"\"", // commas
	"\"\"\"quote\"\" outside quotes\"", "\"\"\"quote \"\"inside\"\" quotes\"\"\"", // quotes
	"\"newline\noutside quotes\"", "\"\"\"newline\ninside quotes\"\"\"", // newline
	"normal text", // normal
	"\" first char is a space\"", // spacing
	"\"17\"\" monitor\"", // single quote
	"" // empty
};

}
