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
package org.supercsv.quote;

import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * The interface for quoting modes.
 * 
 * @author James Bassett
 * @since 2.1.0
 */
public interface QuoteMode {
	
	/**
	 * Determines whether surrounding quotes are required if the CSV column to be written doesn't contain special
	 * characters (if it does then it will have surrounding quotes added to conform to RFC4180).
	 * 
	 * @param csvColumn
	 *            an element of a CSV file
	 * @param context
	 *            the context
	 * @param preference
	 *            the CSV preferences
	 * @return true if surrounding quotes are required, otherwise false
	 */
	boolean quotesRequired(String csvColumn, CsvContext context, CsvPreference preference);
	
}
