package org.supercsv.io;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;

import org.supercsv.prefs.CsvPreference;

/**
 * Super CSV v1.02 tokenizer
 * 
 * @author Kasper B. Graversen
 */
public class Tokenizer_v102 implements ITokenizer {
CsvPreference preferences;
LineNumberReader lnr;

StringBuilder sb = null;

public Tokenizer_v102(final Reader stream, final CsvPreference preference) {
	this.preferences = preference;
	lnr = new LineNumberReader(stream);
	sb = new StringBuilder(500);
}

private void addSpaces(final StringBuilder sb, final int spaces) {
	for( int i = 0; i < spaces; i++ ) {
		sb.append(" ");
	}
}

/**
 * {@inheritDoc}
 */
public void close() throws IOException {
	lnr.close();
}

/**
 * {@inheritDoc}
 */
public int getLineNumber() {
	return lnr.getLineNumber();
}

/**
 * {@inheritDoc}
 */
public boolean readStringList(final List<String> result) throws IOException {
	result.clear();
	PARSERSTATE state = PARSERSTATE.NORMAL; // start out in normal mode
	
	// fast access to preferences
	final int quote = preferences.getQuoteChar();
	final int delim = preferences.getDelimiterChar();
	
	String line;
	// read non-empty lines only
	do {
		line = lnr.readLine();
		if( line == null ) { return false; // EOF
		}
	}
	while( line.length() == 0 ); // skip zero len lines
	
	// start parsing
	line = line + "\n"; // add a newline to determine end of line (making
	// parsing easier)
	sb = new StringBuilder();
	
	// proccess the line (and maybe more lines of the file)
	int p = 0; // the pos of the cursor on the line
	int linenoQuoteState = -1; // the line number of the file where the
	
	int potentialSpaces = 0; // only add spaces if in quote mode. if in non-quote mode, count the spaces and only
	// add them if a non-delimiter or non-end-of-line is met
	// in other cases, we are ending the cell, and we do not want to do that with spaces..
	
	while( true ) {
		// relies on p being incremented at least at the end of the while
		final char c = line.charAt(p);
		
		// react to char c depending on the state we are in
		switch( state ) {
		
		case NORMAL:
			// if(log.isDebugEnabled()) log.debug("normal " + p);
			
			if( c == delim ) {
				result.add(sb.toString()); // save token
				sb.delete(0, sb.length()); // reset the stringbuilder
				potentialSpaces = 0;
				break; // read more
			} else if( c == ' ' ) { // trim starting spaces (trailing spaces
				// are removed using the String.trim()
				if( sb.length() > 0 ) {
					// first on the line
					potentialSpaces++;
				}
				break; // read more
			} else if( c == '\n' ) {
				// save token
				result.add(sb.toString());
				sb = null;
				potentialSpaces = 0;
				return true; // we've read a line
			} else if( c == quote && sb.length() == 0 ) { // quote first on line cannot be escaped
				state = PARSERSTATE.QUOTESCOPE;
				// update variable in order to do debug statements
				linenoQuoteState = getLineNumber();
				break; // read more
			} else if( c == quote && line.charAt(p + 1) == quote && sb.length() > 0 ) {
				// an escaped quote - can not happen as first character
				addSpaces(sb, potentialSpaces);
				potentialSpaces = 0;
				sb.append(c); // add and skip the first quote
				// (end of switch will skip the next quote)
				p++;
				break; // read more
			} else if( c == quote && line.charAt(p + 1) != quote ) { // a single quote, change state and don't
				// append
				state = PARSERSTATE.QUOTESCOPE;
				// update variable in order to do debug statements
				linenoQuoteState = getLineNumber();
				addSpaces(sb, potentialSpaces);
				potentialSpaces = 0;
				break; // read more
			}

			else { // if just a normal character
				addSpaces(sb, potentialSpaces);
				potentialSpaces = 0;
				sb.append(c); // add the char
			}
			break;
		
		// for each situation above, repeat now in the quote scope
		case QUOTESCOPE:
			// System.out.println("quote: '" + p + "'");
			
			if( c == delim ) {
				// delimiter does not count as delimiter in quote scope
				sb.append(c);
				break; // read more
			} else if( c == '\n' ) { // newline does not count as newline in
				// quote scope
				sb.append('\n');
				// parse the next line of the file
				p = -1; // reset delta to point to start of new line (set to
				// -1 as it will be incremented to 0 at the end of
				// the switch)
				line = lnr.readLine();
				if( line == null ) { throw new IOException(
					"File ended unexpectedly while reading a quoted cell starting on line: " + linenoQuoteState); }
				line = line + '\n'; // add \n to make parsing easy
				break; // read more
			} else if( c == quote && line.charAt(p + 1) == quote ) {
				// an escaped quote,
				sb.append(c); // add and skip the first quote (end of
				// switch will skip the next quote
				p++;
				break; // read more
			} else if( line.charAt(p) == quote && line.charAt(p + 1) != quote ) {
				// a single quote, only change state
				state = PARSERSTATE.NORMAL;
				break; // read more
			} else { // if just a normal character
				sb.append(c); // add the char
				// System.out.println("Adding char '" + c + "'");
			}
			break;
		} // switch
		
		p++; // read next char of the line
	}
}
}
