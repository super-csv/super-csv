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
import org.supercsv.comment.CommentMatcher;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;

/**
 * Reads the CSV file, line by line. If you want the line-reading functionality of this class, but want to define your
 * own implementation of {@link #readColumns(java.util.List)}, then consider writing your own Tokenizer by extending
 * AbstractTokenizer.
 *
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Pietro Aragona
 */
public class Tokenizer extends AbstractTokenizer {

	private static final char NEWLINE = '\n';

	private static final char SPACE = ' ';

	private final StringBuilder currentColumn = new StringBuilder();

	/* the raw, untokenized CSV row (may span multiple lines) */
	private final StringBuilder currentRow = new StringBuilder();

	private final char quoteChar;

	private final String delimiterSymbols;

	private final int delimiterSymbolsLength;

	private final boolean surroundingSpacesNeedQuotes;

	private final boolean ignoreEmptyLines;

	private final CommentMatcher commentMatcher;

	private final int maxLinesPerRow;

	private final EmptyColumnParsing emptyColumnParsing;

	private final char quoteEscapeChar;

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
	 *             if reader or preferences are null
	 */
	public Tokenizer(final Reader reader, final CsvPreference preferences) {
		super(reader, preferences);
		this.quoteChar = preferences.getQuoteChar();
		this.delimiterSymbols = preferences.getDelimiterSymbols();
		this.delimiterSymbolsLength = delimiterSymbols.length();
		this.surroundingSpacesNeedQuotes = preferences.isSurroundingSpacesNeedQuotes();
		this.ignoreEmptyLines = preferences.isIgnoreEmptyLines();
		this.commentMatcher = preferences.getCommentMatcher();
		this.maxLinesPerRow = preferences.getMaxLinesPerRow();
		this.emptyColumnParsing = preferences.getEmptyColumnParsing();
		this.quoteEscapeChar = preferences.getQuoteEscapeChar();
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

		// read a line (ignoring empty lines/comments if necessary)
		String line;
		do {
			line = readLine();
			if( line == null ) {
				return false; // EOF
			}
		}
		while( ignoreEmptyLines && line.length() == 0 || (commentMatcher != null && commentMatcher.isComment(line)) );

		// update the untokenized CSV row
		currentRow.append(line);

		// process each character in the line, catering for surrounding quotes (QUOTE_MODE)
		TokenizerState state = TokenizerState.NORMAL;
		int quoteScopeStartingLine = -1; // the line number where a potential multi-line cell starts
		int potentialSpaces = 0; // keep track of spaces (so leading/trailing space can be removed if required)
		int charIndex = 0;
		while( true ) {
			boolean endOfLineReached = charIndex == line.length();

			if( endOfLineReached )
			{
				if( TokenizerState.NORMAL.equals(state) ) {
					/*
					 * Newline. Add any required spaces (if surrounding spaces don't need quotes) and return (we've read
					 * a line!).
					 */
					if( !surroundingSpacesNeedQuotes ) {
						appendSpaces(currentColumn, potentialSpaces);
					}
					addColumn(columns, line, charIndex);
					return true;
				}
				else
				{
					/*
					 * Newline. Doesn't count as newline while in QUOTESCOPE. Add the newline char, reset the charIndex
					 * (will update to 0 for next iteration), read in the next line, then then continue to next
					 * character.
					 */
					currentColumn.append(NEWLINE);
					currentRow.append(NEWLINE); // specific line terminator lost, \n will have to suffice

					charIndex = 0;

					if (maxLinesPerRow > 0 && getLineNumber() - quoteScopeStartingLine + 1 >= maxLinesPerRow) {
						/*
						 * The quoted section that is being parsed spans too many lines, so to avoid excessive memory
						 * usage parsing something that is probably human error anyways, throw an exception. If each
						 * row is suppose to be a single line and this has been exceeded, throw a more descriptive
						 * exception
						 */
						String msg = maxLinesPerRow == 1 ?
								String.format("unexpected end of line while reading quoted column on line %d",
											  getLineNumber()) :
								String.format("max number of lines to read exceeded while reading quoted column" +
											  " beginning on line %d and ending on line %d",
											  quoteScopeStartingLine, getLineNumber());
						throw new SuperCsvException(msg);
					}
					else if( (line = readLine()) == null ) {
						throw new SuperCsvException(
							String
								.format(
									"unexpected end of file while reading quoted column beginning on line %d and ending on line %d",
									quoteScopeStartingLine, getLineNumber()));
					}

					currentRow.append(line); // update untokenized CSV row

				    if (line.length() == 0){
				    	// consecutive newlines
                        continue;
				    }
				}
			}

			final char c = line.charAt(charIndex);

			if (TokenizerState.NORMAL.equals(state)) {

				/*
				 * NORMAL mode (not within quotes).
				 */

				boolean isSeparator = false;
				if (c == delimiterSymbols.charAt(0)) {

					isSeparator = line.length() >= charIndex + delimiterSymbolsLength && line.regionMatches(charIndex,delimiterSymbols,0, delimiterSymbolsLength);
					if (isSeparator) {
					/*
					 * Delimiter. Save the column (trim trailing space if required) then continue to next character.
					 */
						if( !surroundingSpacesNeedQuotes ) {
							appendSpaces(currentColumn, potentialSpaces);
						}
						addColumn(columns, line, charIndex);
						potentialSpaces = 0;
						currentColumn.setLength(0);
						charIndex += delimiterSymbolsLength - 1;
					}
				}
				if (!isSeparator) {
					if( c == SPACE ) {
					    /*
					    * Space. Remember it, then continue to next character.
					    */
					    potentialSpaces++;

				    } else if (c == quoteChar) {
					    /*
					    * A single quote ("). Update to QUOTESCOPE (but don't save quote), then continue to next character.
					    */
						state = TokenizerState.QUOTE_MODE;
						quoteScopeStartingLine = getLineNumber();

						// cater for spaces before a quoted section (be lenient!)
						if (!surroundingSpacesNeedQuotes || currentColumn.length() > 0) {
							appendSpaces(currentColumn, potentialSpaces);
						}
						potentialSpaces = 0;
					} else {
					    /*
					    * Just a normal character. Add any required spaces (but trim any leading spaces if surrounding
					    * spaces need quotes), add the character, then continue to next character.
					    */
						if (!surroundingSpacesNeedQuotes || currentColumn.length() > 0) {
							appendSpaces(currentColumn, potentialSpaces);
						}

						potentialSpaces = 0;
						currentColumn.append(c);
					}
				}
			} else {

				/*
				 * QUOTE_MODE (within quotes).
				 */

				if( c == quoteEscapeChar ) {
					int nextCharIndex = charIndex + 1;
					boolean availableCharacters = nextCharIndex < line.length();
					boolean nextCharIsQuote = availableCharacters && line.charAt(nextCharIndex) == quoteChar;
					boolean nextCharIsEscapeQuoteChar = availableCharacters && line.charAt(nextCharIndex) == quoteEscapeChar;

					if( nextCharIsQuote ) {
						/*
						 * An escaped quote (e.g. "" or \"). Skip over the escape char, and add
						 * the following quote char as part of the column;
						 */
						charIndex++;
						currentColumn.append(quoteChar);
					} else if( nextCharIsEscapeQuoteChar ) {
						/*
						 * A double escape (normally \\). Save the escape char, then continue to
						 * next character.
						 */
						currentColumn.append(c);
						charIndex++;
					} else if( quoteEscapeChar == quoteChar ) {
						/*
						 * If the escape char is also the quote char and we didn't escape a
						 * subsequent character, then this is a lone quote and the end of the
						 * field.
						 */
						state = TokenizerState.NORMAL;
						quoteScopeStartingLine = -1; // reset ready for next multi-line cell
					} else {
						/*
						 * Escape char wasn't before either another escape char or a quote char,
						 * so process it normally.
						 */
						currentColumn.append(c);
					}
				}
				else if( c == quoteChar ) {

					/*
					 * A single quote ("). Update to NORMAL (but don't save quote), then continue to next character.
					 */
					state = TokenizerState.NORMAL;
					quoteScopeStartingLine = -1; // reset ready for next multi-line cell

					int nextCharIndex = charIndex + 1;
					boolean availableCharacters = nextCharIndex < line.length();
					boolean nextCharIsQuote = availableCharacters && line.charAt(nextCharIndex) == quoteChar;

					if( quoteEscapeChar != quoteChar && nextCharIsQuote ) {
						throw new SuperCsvException("Encountered repeat quote char (" +
								quoteChar + ") when quoteEscapeChar was (" + quoteEscapeChar + ")" +
								".  Cannot process data where quotes are escaped both with " +
								quoteChar + " and with " + quoteEscapeChar);
					}
				} else {
					/*
					 * Just a normal character, delimiter (they don't count in QUOTESCOPE) or space. Add the character,
					 * then continue to next character.
					 */
					currentColumn.append(c);
				}
			}

			charIndex++; // read next char of the line
		}
	}
	/**
	 * Adds the currentColumn to columns list managing the case with currentColumn.length() == 0
	 * It was introduced to manage the emptyColumnParsing.
	 *
	 * @param columns
	 * @param line
	 * @param charIndex
	 */
	private void addColumn(final List<String> columns, String line, int charIndex) {

		if(currentColumn.length() > 0){
			columns.add(currentColumn.toString());
		}
		else{
			int previousCharIndex = charIndex - 1;
			boolean availableCharacters = previousCharIndex >= 0 ;
			boolean previousCharIsQuote = availableCharacters && line.charAt(previousCharIndex) == quoteChar;
			String noValue = ( (previousCharIsQuote) && emptyColumnParsing.equals(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString)) ? "" : null;
			columns.add(noValue);
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
