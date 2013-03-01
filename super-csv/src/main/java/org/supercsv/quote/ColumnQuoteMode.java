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

import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * When using ColumnQuoteMode surrounding quotes are only applied if the column should always be quoted, or if required
 * to escape special characters (per RFC4180).
 * 
 * @author James Bassett
 * @since 2.1.0
 */
public class ColumnQuoteMode implements QuoteMode {
	
	private final boolean[] columnsToQuote;
	
	/**
	 * Constructs a new <tt>ColumnQuoteMode</tt>.
	 * 
	 * @param columnsToQuote
	 *            array of booleans (one per CSV column) indicating whether each column should be quoted or not
	 * @throws NullPointerException
	 *             if columnsToQuote is null
	 */
	public ColumnQuoteMode(final boolean... columnsToQuote) {
		if( columnsToQuote == null ) {
			throw new NullPointerException("columnsToQuote should not be null");
		}
		this.columnsToQuote = columnsToQuote;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean quotesRequired(final String csvColumn, final CsvContext context, final CsvPreference preference) {
		if( context.getColumnNumber() > columnsToQuote.length ) {
			throw new SuperCsvException(String.format(
				"the number of elements in the columnsToQuote array (%d) doesn't match the number of columns",
				columnsToQuote.length), context);
		}
		return columnsToQuote[context.getColumnNumber() - 1];
	}
	
}
