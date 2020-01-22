package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.supercsv.comment.CommentMatcher;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.AbstractTokenizer;
import org.supercsv.prefs.CsvPreference;

/**
 * As per RFC 4180 every quote in value must be escaped by another quote. i.e,<code> Hello "World" -> Hello ""World"" </code>
 * If file does not comply RFC, this tokenizer rejects that record. In other words, this is strict RFC tokenizer.
 * 
 * <p>
 * Example : <br>
 * 	NonEscaped Quotes (Rejected)  <br>
 * <code>
 * 		"123466","Hello "World","ABC" <br>
 * 		"123466",""Hello" World","ABC" <br>
 * 		"123466","Hello",World","ABC" <br>
 * </code>
 * Escaped Quotes (Accepted)  <br>
 * <code>
 * 		"123466","Hello Wor""ld","ABC" -> [123466, Hello Wor"ld, ABC] <br>
 * 		"123466","Hello ""World""","ABC" -> [123466, Hello "World", ABC] <br>
 * </code>
 * </p>
 * 
 * @author Nilesh.Akhade
 *
 */
public class RejectNonEscapedQuotesTokenizer extends AbstractTokenizer {

	private static final char NEWLINE = '\n';
	
	private static final char SPACE = ' ';
	
	private final StringBuilder currentColumn = new StringBuilder();
	
	/* the raw, untokenized CSV row (may span multiple lines) */
	private final StringBuilder currentRow = new StringBuilder();
	
	private final int quoteChar;
	
	private final int delimeterChar;
	
	private final boolean surroundingSpacesNeedQuotes;
	
	private final boolean ignoreEmptyLines;
	
	private final CommentMatcher commentMatcher;

	private final int maxLinesPerRow;

	private final EmptyColumnParsing emptyColumnParsing;
	
	//private QuoteMode quoteMode;
	
	/**
	 * Enumeration of tokenizer states. QUOTE_MODE is activated between quotes.
	 */
	private enum TokenizerState {
		NORMAL, QUOTE_MODE;
	}
	
	public RejectNonEscapedQuotesTokenizer(Reader reader, CsvPreference preferences) {
		super(reader, preferences);
		this.quoteChar = preferences.getQuoteChar();
		this.delimeterChar = preferences.getDelimiterChar();
		this.surroundingSpacesNeedQuotes = preferences.isSurroundingSpacesNeedQuotes();
		this.ignoreEmptyLines = preferences.isIgnoreEmptyLines();
		this.commentMatcher = preferences.getCommentMatcher();
		this.emptyColumnParsing = preferences.getEmptyColumnParsing();
		this.maxLinesPerRow = preferences.getMaxLinesPerRow();
//		this.quoteMode = preferences.getQuoteMode();
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
					//columns.add(currentColumn.length() > 0 ? currentColumn.toString() : null); // "" -> null
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
					} else if( (line = readLine()) == null ) {
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
			
			if( TokenizerState.NORMAL.equals(state) ) {
				
				/*
				 * NORMAL mode (not within quotes).
				 */
				
				if( c == delimeterChar ) {
					/*
					 * Delimiter. Save the column (trim trailing space if required) then continue to next character.
					 */
					if( !surroundingSpacesNeedQuotes ) {
						appendSpaces(currentColumn, potentialSpaces);
					}
					//columns.add(currentColumn.length() > 0 ? currentColumn.toString() : null); // "" -> null
					addColumn(columns, line, charIndex);
					potentialSpaces = 0;
					currentColumn.setLength(0);
					
				} else if( c == SPACE ) {
					/*
					 * Space. Remember it, then continue to next character.
					 */
					potentialSpaces++;
					
				} else if( c == quoteChar ) {
					/*
					 * A single quote ("). Update to QUOTESCOPE (but don't save quote), then continue to next character.
					 */
					state = TokenizerState.QUOTE_MODE;
					quoteScopeStartingLine = getLineNumber();
					
					// cater for spaces before a quoted section (be lenient!)
					if( !surroundingSpacesNeedQuotes || currentColumn.length() > 0 ) {
						appendSpaces(currentColumn, potentialSpaces);
					}
					potentialSpaces = 0;
					
				} else {
					/*
					 * Just a normal character. Add any required spaces (but trim any leading spaces if surrounding
					 * spaces need quotes), add the character, then continue to next character.
					 */
					if( !surroundingSpacesNeedQuotes || currentColumn.length() > 0 ) {
						appendSpaces(currentColumn, potentialSpaces);
					}
					
					potentialSpaces = 0;
					currentColumn.append(c);
				}
				
			} else {
				
				/*
				 * QUOTE_MODE (within quotes).
				 */
				
				if( c == quoteChar ) {
					/*
					 * Case when c is last char in line
					 * "Hello World","Java"
					 *                    ^
					 *                 charIndex = 19
					 *             line.length() = 20
					 *       availableCharacters = false
					 */
					int nextCharIndex = charIndex + 1;
					boolean availableCharacters = nextCharIndex < line.length();
					boolean nextCharIsNotDelimiter = availableCharacters && line.charAt(nextCharIndex) != delimeterChar;
					if( nextCharIsNotDelimiter) {
						/*
						 * Hello",orld  (case when nextCharIsNotDelimiter is false because nextChar is ,)
						 * Hello""orld  (case when nextCharIsNotDelimiter is true because nextChar is ")
						 * Hello"World  (case when nextCharIsNotDelimiter is true because nextChar is W)
						 *      ^
						 *   charIndex
						 * We want to reject non escaped quotes. Hence if next character is not comma then
						 * it must be quote(i.e, It should have been escaped, Hello""orld)
						 */
						
						boolean nextCharIsQuote = line.charAt(nextCharIndex) == quoteChar;
						if (nextCharIsQuote) {
							/*
							 * Hello""World  (case when nextCharIsQuote is true because nextChar is ")
							 * This is escaped quote. Thats perfectly fine. We will increment charIndex so
							 * as to skip extra quote.
							 */
							currentColumn.append(c);
							charIndex++;
						} else {
							/*
							 * Hello"World  (case when nextCharIsNotDelimiter is true because nextChar is W)
							 * We should raise the exception.
							 */
							throw new SuperCsvException( "Unescaped double quotes found in this record" );
						}
						
					} else {
						/*
						 * A single quote ("). Update to NORMAL (but don't save quote), then continue to next character.
						 */
						state = TokenizerState.NORMAL;
						quoteScopeStartingLine = -1; // reset ready for next multi-line cell
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
