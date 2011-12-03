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
 * Defines the standard behaviour of a CSV writer.
 * 
 * @author Kasper B. Graversen
 */
public abstract class AbstractCsvWriter implements ICsvWriter {
	final StringBuilder sb = new StringBuilder();
	BufferedWriter writer;
	int lineNo;
	CsvPreference preference;
	
	/**
	 * Constructs a new <tt>AbstractCsvWriter</tt> with the supplied writer and preferences.
	 * 
	 * @param writer
	 *            the stream to write to
	 * @param preference
	 *            the CSV preferences
	 */
	protected AbstractCsvWriter(final Writer writer, final CsvPreference preference) {
		setPreferences(preference);
		this.writer = new BufferedWriter(writer);
		lineNo = 1;
	}
	
	/**
	 * Closes the underlying writer, flushing it first.
	 */
	public void close() throws IOException {
		writer.close();
	}
	
	/**
	 * Flushes the underlying writer.
	 */
	public void flush() throws IOException {
		writer.flush();
	}
	
	/**
	 * Make a string ready for writing by escaping various characters as specified by the CSV format
	 * 
	 * @param csvElement
	 *            an element of a CSV file
	 * @return an escaped version of the element ready for persisting
	 */
	protected String escapeString(final String csvElement) {
		if( csvElement.length() == 0 ) {
			return "";
		}
		
		sb.delete(0, sb.length()); // reusing builder object
		
		final int delimiter = preference.getDelimiterChar();
		final char quote = (char) preference.getQuoteChar();
		final char whiteSpace = ' ';
		final String eolSymbols = preference.getEndOfLineSymbols();
		
		boolean needForEscape = false; // if newline or start with space
		if( csvElement.charAt(0) == whiteSpace ) {
			needForEscape = true;
		}
		
		char c;
		final int lastPos = csvElement.length() - 1;
		for( int i = 0; i <= lastPos; i++ ) {
			
			c = csvElement.charAt(i);
			
			if( c == delimiter ) {
				needForEscape = true;
				sb.append(c);
			} else if( c == quote ) {
				// if its the first character, escape it and set need for space
				if( i == 0 ) {
					sb.append(quote);
					sb.append(quote);
					needForEscape = true;
				} else {
					sb.append(quote);
					sb.append(quote);
					needForEscape = true; // TODO review comments above
				}
			} else if( c == '\n' ) {
				needForEscape = true;
				sb.append(eolSymbols);
			} else {
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
	 * Writes the List of content.
	 * 
	 * @param content
	 *            the content to write
	 * @throws IOException
	 */
	protected void write(final List<? extends Object> content) throws IOException {
		// convert object array to strings and write them
		final String[] strarr = new String[content.size()];
		int i = 0;
		for( final Object o : content ) {
			if( o == null ) {
				throw new NullInputException("Object at position " + i + " is null",
					new CSVContext(getLineNumber(), i), (Throwable) null);
			}
			strarr[i++] = o.toString();
		}
		write(strarr);
	}
	
	/**
	 * Writes one or more Objects.
	 * 
	 * @param content
	 *            the content to write
	 * @throws IOException
	 */
	protected void write(final Object... content) throws IOException {
		// convert object array to strings and write them
		final String[] strarr = new String[content.length];
		int i = 0;
		for( final Object o : content ) {
			if( o == null ) {
				throw new NullInputException("Object at position " + i + " is null",
					new CSVContext(getLineNumber(), i), (Throwable) null);
			}
			strarr[i++] = o.toString();
		}
		write(strarr);
	}
	
	/**
	 * Writes one or more Strings.
	 * 
	 * @param content
	 *            the content to write
	 * @throws IOException
	 */
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
					writer.write(escapeString(content[i]));
					writer.write(delimiter);
				}
				break;
		}
		
		// write last elem (without delimiter) and the EOL
		writer.write(escapeString(content[i]));
		writer.write(preference.getEndOfLineSymbols());
		return;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void writeHeader(final String... header) throws IOException, SuperCSVException {
		this.write(header);
	}
	
}
