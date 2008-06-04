package org.supercsv.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CSVContext;

/**
 * The writer class capable of writing arrays, maps,... to a CSV file. Notice that the cell processors can also be
 * utilized when writing. E.g. they can help ensure that only numbers are written in numeric columns, that numbers are
 * unique or the output does not contain certain characters or exceed specified string lengths.
 * 
 * @author Kasper B. Graversen
 */
public abstract class AbstractCsvWriter implements ICsvWriter {
final StringBuilder sb = new StringBuilder();
BufferedWriter outStream;
int lineNo;
CsvPreference preference;

protected AbstractCsvWriter(final Writer stream, final CsvPreference preference) {
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
protected String escapeString(final String csvElem) {
	if( csvElem.length() == 0 ) {
		return "";
	}
	
	sb.delete(0, sb.length()); // reusing builder object
	
	final int delimiter = preference.getDelimiterChar();
	final char quote = (char) preference.getQuoteChar();
	final char whiteSpace = ' ';
	final String EOLSymbols = preference.getEndOfLineSymbols();
	
	boolean needForEscape = false; // if newline or start with space
	if( csvElem.charAt(0) == whiteSpace ) {
		needForEscape = true;
	}
	
	char c;
	final int lastPos = csvElem.length() - 1;
	for( int i = 0; i <= lastPos; i++ ) {
		
		c = csvElem.charAt(i);
		
		if( c == delimiter ) {
			needForEscape = true;
			sb.append(c);
		}
		else
			if( c == quote ) {
				// if its the first character, escape it and set need for space
				if( i == 0 ) {
					sb.append(quote);
					sb.append(quote);
					needForEscape = true;
				}
				else {
					sb.append(quote);
					sb.append(quote);
					needForEscape = true; // TODO review comments above
				}
			}
			else
				if( c == '\n' ) {
					needForEscape = true;
					sb.append(EOLSymbols);
				}
				else {
					sb.append(c);
				}
	}
	
	// if element contains a newline (mac,windows or linux), escape the
	// whole with a surrounding quotes
	if( needForEscape ) {
		return quote + sb.toString() + quote;
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

protected void write(final Object... content) throws IOException {
	// convert object array to strings and write them
	final String[] strarr = new String[content.length];
	int i = 0;
	for( final Object o : content ) {
		if( o == null ) {
			throw new NullInputException("Object at position " + i + " is null", new CSVContext(getLineNumber(), i),
				(Throwable) null);
		}
		strarr[i++] = o.toString();
	}
	write(strarr);
}

protected void write(final String... content) throws IOException {
	lineNo++;
	
	final int delimiter = preference.getDelimiterChar();
	int i = 0;
	switch( content.length ) {
	case 0:
		throw new SuperCSVException("There is no content to write for line " + getLineNumber(), new CSVContext(
			getLineNumber(), 0));
		
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
