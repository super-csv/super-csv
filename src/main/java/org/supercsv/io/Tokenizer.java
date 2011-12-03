package org.supercsv.io;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;

import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CSVContext;

/**
 * Reads the CSV file, line by line.
 * 
 * @author Kasper B. Graversen
 */
public class Tokenizer implements ITokenizer {
	
	private static final int STR_BUILDER_INIT_CAPACITY = 500;

	/** the CSV preferences */
	CsvPreference preferences;
	
	/** the line number reader */
	LineNumberReader lnr;
	
	StringBuilder sb = null;
	
	/**
	 * Constructs a new <tt>Tokenizer</tt>, which reads the CSV file, line by line.
	 * 
	 * @param reader
	 *            the reader
	 * @param preference
	 *            the CSV preferences
	 */
	public Tokenizer(final Reader reader, final CsvPreference preference) {
		this.preferences = preference;
		lnr = new LineNumberReader(reader);
		sb = new StringBuilder(STR_BUILDER_INIT_CAPACITY);
	}
	
	/**
	 * Adds the required number of spaces to the StringBuilder.
	 * 
	 * @param sb
	 *            the StringBuilder
	 * @param spaces
	 *            the number of spaces to add
	 */
	private static void addSpaces(final StringBuilder sb, final int spaces) {
		for( int i = 0; i < spaces; i++ ) {
			sb.append(" ");
		}
	}
	
	/**
	 * Closes the underlying reader.
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
		
		// clear the result List and StringBuilder
		result.clear();
		sb.setLength(0);
		
		// keep reading lines until data is found
		String line;
		do {
			line = lnr.readLine();
			if( line == null ) {
				return false; // EOF
			}
		}
		while( line.length() == 0 );
		
		// add a newline to determine end of line (making parsing easier)
		line += "\n";
		
		// fast access to preferences
		final int quote = preferences.getQuoteChar();
		final int delim = preferences.getDelimiterChar();
		
		int charIndex = 0; // the index of the current character being processed
		int linenoQuoteState = -1; // the line number of the file where a potential multi-line cell starts
		
		/*
		 * spaces between words or after the last word. This is used to remove trailing spaces in non-quote mode, e.g.
		 * 'foo ,' is read as 'foo' whereas 'foo bar' is read as 'foo bar'.
		 */
		int potentialSpaces = 0;
		
		// parsing begins in normal mode
		PARSERSTATE state = PARSERSTATE.NORMAL;
		
		// process each character in the line, catering for surrounding quotes (QUOTESCOPE)
		while( true ) {
			
			final char c = line.charAt(charIndex);
			
			switch( state ) {
				
				case NORMAL:

					if( c == delim ) {
						/*
						 * Delimiter. Save the token then continue to next character.
						 */
						result.add(sb.toString());
						sb.setLength(0);
						potentialSpaces = 0;
						break;
						
					} else if( c == ' ' ) {
						/*
						 * Space. Remove leading spaces, keep track of all other spaces, then continue to next
						 * character.
						 */
						if( sb.length() > 0 ) {
							potentialSpaces++;
						}
						break;
						
					} else if( c == '\n' ) {
						/*
						 * Newline. Save token and return (we've read a line).
						 */
						result.add(sb.toString());
						return true;
						
					} else if( c == quote ) {
						
						if( sb.length() == 0 ) {
							/*
							 * Quote at start of line (can't be escaped). Update to QUOTESCOPE, then continue to next
							 * character.
							 */
							state = PARSERSTATE.QUOTESCOPE;
							linenoQuoteState = getLineNumber();
							break;
							
						} else if( line.charAt(charIndex + 1) == quote ) {
							/*
							 * An escaped quote (""). Add the saved spaces, add a single quote, then move the cursor so
							 * the next iteration of the loop will read the character following the escaped quote.
							 */
							addSpaces(sb, potentialSpaces);
							potentialSpaces = 0;
							sb.append(c);
							charIndex++;
							break;
							
						} else {
							/*
							 * A single quote ("). Update to QUOTESCOPE (but don't save quote), add the saved spaces,
							 * then continue to next character.
							 */
							state = PARSERSTATE.QUOTESCOPE;
							linenoQuoteState = getLineNumber();
							addSpaces(sb, potentialSpaces);
							potentialSpaces = 0;
							break;
						}
					} else {
						/*
						 * Just a normal character. Add saved spaces, add the character, then continue to next
						 * character.
						 */
						addSpaces(sb, potentialSpaces);
						potentialSpaces = 0;
						sb.append(c);
						break;
					}
					
				case QUOTESCOPE:

					if( c == '\n' ) {
						
						/*
						 * Newline. Doesn't count as newline while in QUOTESCOPE. Add the newline char, reset the
						 * charIndex (will update to 0 for next iteration), read in the next line, then then continue to
						 * next character.
						 */
						sb.append('\n');
						charIndex = -1;
						line = lnr.readLine();
						if( line == null ) {
							throw new SuperCSVException(
								"File ended unexpectedly while reading a quoted cell starting on line: "
									+ linenoQuoteState, new CSVContext(linenoQuoteState, 0));
						}
						line += '\n'; // add newline to simplify parsing
						break;
						
					} else if( c == quote ) {
						
						if( line.charAt(charIndex + 1) == quote ) {
							/*
							 * An escaped quote (""). Add a single quote, then move the cursor so the next iteration of
							 * the loop will read the character following the escaped quote.
							 */
							sb.append(c);
							charIndex++;
							break;
							
						} else {
							/*
							 * A single quote ("). Update to NORMAL (but don't save quote), then continue to next
							 * character.
							 */
							state = PARSERSTATE.NORMAL;
							break;
						}
					} else {
						/*
						 * Just a normal character, delimiter (they don't count in QUOTESCOPE) or space. Add the character,
						 * then continue to next character.
						 */
						sb.append(c);
						break;
					}
				default:
					throw new RuntimeException("this can never happen!");
					
			}
			
			charIndex++; // read next char of the line
		}
	}
}
