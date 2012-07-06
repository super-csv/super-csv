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
package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

/**
 * Reads the CSV file, line by line. If you want the line-reading functionality of this class, but want to define your
 * own implementation of {@link #readColumns(List)}, then consider writing your own Tokenizer by extending
 * AbstractTokenizer.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class Tokenizer extends AbstractTokenizer {
	
	private static final char NEWLINE = '\n';
	
	private static final char SPACE = ' ';
	
	private final StringBuilder currentColumn = new StringBuilder();
	
	/* the raw, untokenized CSV row (may span multiple lines) */
	private final StringBuilder currentRow = new StringBuilder();
	
	private final int quoteChar;
	
	private final int delimeterChar;
	
	private final boolean trimMode;
	
	/**
	 * Enumeration of tokenizer states. QUOTE_MODE is activated between quotes.
	 */
	private enum TokenizerState {
		NORMAL, QUOTE_MODE;
	}
	
	/**
	 * Constructs a new <tt>Tokenizer</tt>, which reads the CSV file, line by line.
	 * 
	 * @param reader
	 *            the reader
	 * @param preferences
	 *            the CSV preferences
	 * @throws NullPointerException
	 *             if reader or preferences is null
	 */
	public Tokenizer(final Reader reader, final CsvPreference preferences) {
		super(reader, preferences);
		this.quoteChar = preferences.getQuoteChar();
		this.delimeterChar = preferences.getDelimiterChar();
		this.trimMode = preferences.isTrimMode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean readColumns(final List<String> columns) throws IOException {
		
		if( columns == null ) {
			throw new NullPointerException("columns should not be null");
		}
		
		// clear the reusable List and StringBuilders
		columns.clear();
		currentColumn.setLength(0);
		currentRow.setLength(0);
		
		// keep reading lines until data is found
		String line;
		do {
			line = readLine();
			if( line == null ) {
				return false; // EOF
			}
		}
		while( line.isEmpty() );
		
		// update the untokenized CSV row
		currentRow.append(line);
		
		// add a newline to determine end of line (making parsing easier)
		line += NEWLINE;
		
		// process each character in the line, catering for surrounding quotes (QUOTE_MODE)
		TokenizerState state = TokenizerState.NORMAL;
		int quoteScopeStartingLine = -1; // the line number where a potential multi-line cell starts
		int potentialSpaces = 0; // keep track of spaces (so leading/trailing space can be removed in trim mode)
		int charIndex = 0;
		while( true ) {
			
			final char c = line.charAt(charIndex);
			
			switch( state ) {
			
				case NORMAL:
					
					/*
					 * Transitioned from QUOTE_MODE to NORMAL (left a double-quoted section) and more data is remaining.
					 * In non-trim mode, only a delimiter may follow a quoted field. In trim mode, trailing spaces are
					 * allowed as well.
					 */
					if( quoteScopeStartingLine > 0 && c != NEWLINE ) {
						
						if( (!trimMode && c != delimeterChar) || (trimMode && c != delimeterChar && c != SPACE) ) {
							throw new SuperCSVException(String.format(
								"illegal character [%c] following quoted field on line: %d, char: %d", c,
								getLineNumber(), charIndex + 1));
							
						}
						
						quoteScopeStartingLine = -1; // reset ready for next multi-line cell
					}
					
					if( c == delimeterChar ) {
						/*
						 * Delimiter. Save the column (trim trailing space if required) then continue to next character.
						 */
						if( !trimMode ) {
							appendSpaces(currentColumn, potentialSpaces);
						}
						columns.add(currentColumn.toString());
						potentialSpaces = 0;
						currentColumn.setLength(0);
						break;
						
					} else if( c == SPACE ) {
						/*
						 * Space. Remember it, then continue to next character.
						 */
						potentialSpaces++;
						break;
						
					} else if( c == NEWLINE ) {
						/*
						 * Newline. Add any required spaces (if not in trim mode) and return (we've read a line!).
						 */
						if( !trimMode ) {
							appendSpaces(currentColumn, potentialSpaces);
						}
						columns.add(currentColumn.toString());
						return true;
						
					} else if( c == quoteChar ) {
						
						/*
						 * Ensures that a quote is the first char (or is only preceded by spaces in trim mode).
						 */
						if( currentColumn.length() > 0 || (!trimMode && potentialSpaces > 0) ) {
							throw new SuperCSVException(String.format(
								"the quoteChar [%c] must be the first character in a field, line: %d, char: %d", c,
								getLineNumber(), charIndex + 1));
							
						} else {
							/*
							 * A single quote ("). Update to QUOTESCOPE (but don't save quote), then continue to next
							 * character.
							 */
							state = TokenizerState.QUOTE_MODE;
							quoteScopeStartingLine = getLineNumber();
							break;
						}
					} else {
						/*
						 * Just a normal character. Add any required spaces (but trim any leading spaces in trim mode),
						 * add the character, then continue to next character.
						 */
						if( !trimMode || currentColumn.length() > 0 ) {
							appendSpaces(currentColumn, potentialSpaces);
						}
						
						potentialSpaces = 0;
						currentColumn.append(c);
						break;
					}
					
				case QUOTE_MODE:
					
					if( c == NEWLINE ) {
						
						/*
						 * Newline. Doesn't count as newline while in QUOTESCOPE. Add the newline char, reset the
						 * charIndex (will update to 0 for next iteration), read in the next line, then then continue to
						 * next character.
						 */
						currentColumn.append(NEWLINE);
						currentRow.append(NEWLINE); // specific line terminator lost, but \n should be good enough??
						
						charIndex = -1;
						line = readLine();
						if( line == null ) {
							throw new SuperCSVException(
								String
									.format(
										"unexpected end of file while reading quoted column beginning on line %d and ending on line %d",
										quoteScopeStartingLine, getLineNumber()));
						}
						
						currentRow.append(line); // update untokenized CSV row
						
						line += NEWLINE; // add newline to simplify parsing
						break;
						
					} else if( c == quoteChar ) {
						
						if( line.charAt(charIndex + 1) == quoteChar ) {
							/*
							 * An escaped quote (""). Add a single quote, then move the cursor so the next iteration of
							 * the loop will read the character following the escaped quote.
							 */
							currentColumn.append(c);
							charIndex++;
							break;
							
						} else {
							/*
							 * A single quote ("). Update to NORMAL (but don't save quote), then continue to next
							 * character.
							 */
							state = TokenizerState.NORMAL;
							break;
						}
					} else {
						/*
						 * Just a normal character, delimiter (they don't count in QUOTESCOPE) or space. Add the
						 * character, then continue to next character.
						 */
						currentColumn.append(c);
						break;
					}
				default:
					throw new AssertionError(); // this can never happen
					
			}
			
			charIndex++; // read next char of the line
		}
	}
	
	/**
	 * Appends the required number of spaces to the StringBuilder.
	 * 
	 * @param sb
	 *            the StringBuilder
	 * @param spaces
	 *            the required number of spaces to append
	 */
	private static void appendSpaces(final StringBuilder sb, final int spaces) {
		for( int i = 0; i < spaces; i++ ) {
			sb.append(SPACE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUntokenizedRow() {
		return currentRow.toString();
	}
}
