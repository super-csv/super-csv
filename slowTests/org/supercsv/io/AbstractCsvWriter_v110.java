/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.supercsv.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public abstract class AbstractCsvWriter_v110 implements ICsvWriter {
BufferedWriter outStream = null;
int lineNo;
CsvPreference preference;

protected AbstractCsvWriter_v110(final Writer stream, final CsvPreference preference) {
	setPreferences(preference);
	outStream = new BufferedWriter(stream);
	lineNo = 1;
}

/**
 * {@inheritDoc}
 */
public void close() throws IOException {
	outStream.flush();
	outStream.close();
}

/**
 * Make a string ready for writing by escaping various characters as specified by the CSV format
 * 
 * @param csvElem
 *            an elem of a csv file
 * @return an escaped version of the csv elem ready for persisting
 */
private String escapeString(final String csvElem) {
	final StringBuilder sb = new StringBuilder();
	final int delimiter = preference.getDelimiterChar();
	final char quote = (char) preference.getQuoteChar();
	
	// scan line, if element contains a new line
	boolean isEscaped = false;
	if( csvElem.indexOf('\n') > -1 || csvElem.indexOf('\r') > -1 ) {
		isEscaped = true;
		sb.append(quote);
	}
	
	char c;
	for( int i = 0; i < csvElem.length(); i++ ) {
		if( (c = csvElem.charAt(i)) == delimiter ) {
			sb.append(quote);
			sb.append(c); // if is delimiter found, escape it by quotes
			sb.append(quote);
		}
		else {
			sb.append(c);
		}
	}
	
	// if element contains a newline (mac,windows or linux), escape the
	// whole with a surrounding quotes
	if( isEscaped ) {
		sb.append(quote);
	}
	
	return sb.toString();
}

/**
 * {@inheritDoc}
 */
public int getLineNumber() {
	return lineNo;
}

/**
 * {@inheritDoc}
 */
public ICsvWriter setPreferences(final CsvPreference preference) {
	this.preference = preference;
	return this;
}

/**
 * The actual write to stream
 */
protected void write(final List<? extends Object> content) throws IOException {
	write(content.toArray());
}

protected void write(final Object[] content) throws IOException {
	// convert object array to strings and write them
	final String[] strarr = new String[content.length];
	int i = 0;
	for( final Object o : content ) {
		strarr[i++] = o.toString();
	}
	write(strarr);
}

protected void write(final String[] content) throws IOException {
	lineNo++;
	
	final int delimiter = preference.getDelimiterChar();
	int i = 0;
	switch( content.length ) {
	case 0:
		throw new SuperCSVException("There is no content to write for line " + getLineNumber(), new CSVContext(lineNo,
			0));
		
	case 1: // just write last element after switch
		break;
	
	default:
		// write first 0..N-1 elems
		for( ; i < content.length - 1; i++ ) {
			outStream.write(escapeString(content[i]));
			outStream.write(delimiter);
		}
		break;
	}
	
	// write last elem (without delimiter) and the EOL
	outStream.write(escapeString(content[i]));
	outStream.write(preference.getEndOfLineSymbols());
	return;
}

/**
 * {@inheritDoc}
 */
public void writeHeader(final String... header) throws IOException, SuperCSVException {
	this.write(header);
}

}
