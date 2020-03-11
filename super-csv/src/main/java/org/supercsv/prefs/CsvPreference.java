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
package org.supercsv.prefs;

import org.supercsv.comment.CommentMatcher;
import org.supercsv.encoder.CsvEncoder;
import org.supercsv.encoder.DefaultCsvEncoder;
import org.supercsv.io.EmptyColumnParsing;
import org.supercsv.quote.NormalQuoteMode;
import org.supercsv.quote.QuoteMode;

/**
 * Before reading or writing CSV files, you must supply the reader/writer with some preferences.
 * <p>
 * <strong>Please note:</strong> the end of line symbols are <em>only</em> used for writing.
 * </p>
 * <table border="0" cellpadding="1" >
 * <caption>Predefined configurations</caption>
 * <tbody>
 * <tr>
 * <th align="left">Constant</th>
 * <th align="left">Quote character</th>
 * <th align="left">Delimiter character</th>
 * <th align="left">End of line symbols</th>
 * </tr>
 * <tr>
 * <td><code>STANDARD_PREFERENCE</code></td>
 * <td><code>"</code></td>
 * <td><code>,</code></td>
 * <td><code>\r\n</code></td>
 * </tr>
 * <tr>
 * <td><code>EXCEL_PREFERENCE</code></td>
 * <td><code>"</code></td>
 * <td><code>,</code></td>
 * <td><code>\n</code></td>
 * </tr>
 * <tr>
 * <td><code>EXCEL_NORTH_EUROPE_PREFERENCE</code></td>
 * <td><code>"</code></td>
 * <td><code>;</code></td>
 * <td><code>\n</code></td>
 * </tr>
 * <tr>
 * <td><code>TAB_PREFERENCE</code></td>
 * <td><code>"</code></td>
 * <td><code>\t</code></td>
 * <td><code>\n</code></td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * By default, spaces surrounding an unquoted cell are treated as part of the data. In versions of Super CSV prior to
 * 2.0.0 this wasn't the case, and any surrounding spaces that weren't within quotes were ignored when reading (and
 * quotes were automatically added to Strings containing surrounding spaces when writing).
 * </p>
 * <p>
 * If you wish enable this functionality again, then you can create a CsvPreference with the
 * <tt>surroundingSpacesNeedQuotes</tt> flag set to true (the default is false). This means that surrounding spaces
 * without quotes will be trimmed when reading, and quotes will automatically be added for Strings containing
 * surrounding spaces when writing.
 * </p>
 * <p>
 * You can apply the surroundingSpacesNeedQuotes property to an existing preference as follows:
 * </p>
 * <p>
 * {@code private static final CsvPreference STANDARD_SURROUNDING_SPACES_NEED_QUOTES = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build();}
 * </p>
 * <p>
 * You can also create your own preferences. For example if your file was pipe-delimited, you could use the following:
 * </p>
 * <p>
 * {@code private static final CsvPreference PIPE_DELIMITED = new CsvPreference.Builder('"', '|', "\n").build();}
 * </p>
 * Other preferences include:
 * <ul>
 * <li>using a custom {@link CsvEncoder} when writing CSV (if you want complete control over how the CSV is encoded)</li>
 * <li>using a custom {@link QuoteMode} when writing CSV (if you want to enable quotes when they're not normally
 * required). You can use the existing {@link org.supercsv.quote.AlwaysQuoteMode AlwaysQuoteMode},
 * {@link org.supercsv.quote.ColumnQuoteMode ColumnQuoteMode}, or supply your own.</li>
 * <li>using a custom {@link CommentMatcher} to skip comments when reading CSV. You can use the existing
 * {@link org.supercsv.comment.CommentStartsWith CommentStartsWith}, {@link org.supercsv.comment.CommentMatches
 * CommentMatches}, or supply your own.</li>
 * <li>ignoring empty lines (enabled by default)</li>
 * <li>setting the maximum number of lines a row of CSV can span (useful for debugging files with mismatched quotes)</li>
 * </ul>
 * <p>
 * <strong>Tip:</strong>Create a CsvPreference instance for each writer instead of using predefined static preferences
 * in CsvPreference When writing in multi thread.
 * </p>
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Pietro Aragona
 * @author Kai Hackemesser
 */
public final class CsvPreference {
	
	/**
	 * Ready to use configuration that should cover 99% of all usages.
	 */
	public static final CsvPreference STANDARD_PREFERENCE = new CsvPreference.Builder('"', ',', "\r\n").build();
	
	/**
	 * Ready to use configuration for Windows Excel exported CSV files.
	 */
	public static final CsvPreference EXCEL_PREFERENCE = new CsvPreference.Builder('"', ',', "\n").build();
	
	/**
	 * Ready to use configuration for north European excel CSV files (columns are separated by ";" instead of ",")
	 */
	public static final CsvPreference EXCEL_NORTH_EUROPE_PREFERENCE = new CsvPreference.Builder('"', ';', "\n").build();
	
	/**
	 * Ready to use configuration for tab-delimited files.
	 */
	public static final CsvPreference TAB_PREFERENCE = new CsvPreference.Builder('"', '\t', "\n").build();
	
	private final char quoteChar;
	
	private final int delimiterChar;
	
	private final String endOfLineSymbols;
	
	private final boolean surroundingSpacesNeedQuotes;
	
	private final boolean ignoreEmptyLines;
	
	private final CsvEncoder encoder;
	
	private final QuoteMode quoteMode;
	
	private final CommentMatcher commentMatcher;

	private final EmptyColumnsStrategy emptyColumnsStrategy;

	private int maxLinesPerRow = 0;
	
	private final EmptyColumnParsing emptyColumnParsing;

	private final char quoteEscapeChar;
	
	/**
	 * Constructs a new <tt>CsvPreference</tt> from a Builder.
	 */
	private CsvPreference(Builder builder) {
		this.quoteChar = builder.quoteChar;
		this.delimiterChar = builder.delimiterChar;
		this.endOfLineSymbols = builder.endOfLineSymbols;
		this.surroundingSpacesNeedQuotes = builder.surroundingSpacesNeedQuotes;
		this.ignoreEmptyLines = builder.ignoreEmptyLines;
		this.commentMatcher = builder.commentMatcher;
		this.encoder = builder.encoder;
		this.quoteMode = builder.quoteMode;
		this.maxLinesPerRow = builder.maxLinesPerRow;
		this.emptyColumnParsing = builder.emptyColumnParsing;
		this.quoteEscapeChar = builder.quoteEscapeChar;
		this.emptyColumnsStrategy = builder.emptyColumnsStrategy;
	}
	
	/**
	 * Returns the delimiter character
	 * 
	 * @return the delimiter character
	 */
	public int getDelimiterChar() {
		return delimiterChar;
	}
	
	/**
	 * Returns the end of line symbols
	 * 
	 * @return the end of line symbols
	 */
	public String getEndOfLineSymbols() {
		return endOfLineSymbols;
	}
	
	/**
	 * Returns the quote character
	 * 
	 * @return the quote character
	 */
	public char getQuoteChar() {
		return quoteChar;
	}
	
	/**
	 * Returns the surroundingSpacesNeedQuotes flag.
	 * 
	 * @return the surroundingSpacesNeedQuotes flag
	 */
	public boolean isSurroundingSpacesNeedQuotes() {
		return surroundingSpacesNeedQuotes;
	}
	
	/**
	 * Returns the ignoreEmptyLines flag.
	 * 
	 * @return the ignoreEmptyLines flag
	 */
	public boolean isIgnoreEmptyLines() {
		return ignoreEmptyLines;
	}
	
	/**
	 * Returns the CSV encoder.
	 * 
	 * @return the CSV encoder
	 */
	public CsvEncoder getEncoder() {
		return encoder;
	}
	
	/**
	 * Returns the quote mode.
	 * 
	 * @return the quote mode
	 */
	public QuoteMode getQuoteMode() {
		return quoteMode;
	}
	
	/**
	 * Returns the comment matcher.
	 * 
	 * @return the comment matcher
	 */
	public CommentMatcher getCommentMatcher() {
		return commentMatcher;
	}
	
	/**
	 * Returns the maximum number of lines a row can span.
	 *
	 * @return the maximum number of lines a row can span
	 */
	public int getMaxLinesPerRow() {
		return maxLinesPerRow;
	}
	
	/**
	 * Returns the EmptyColumnParsing to determine whether empty String (i.e. "") should be read as empty string or as
	 * null.
	 * 
	 * @return the emptyColumnParsing
	 */
	
	public EmptyColumnParsing getEmptyColumnParsing() {
		return emptyColumnParsing;
	}

	/**
	 * Returns the quote escape character
	 *
	 * @return the quote escape character
	 */
	public char getQuoteEscapeChar() {
		return quoteEscapeChar;
	}

	/**
	 * @return the strategy to handle rows with all empty fields, i.e. only delimiters.
	 * By default, such rows will be processed. To change this behavior, use the
	 * {@link Builder#ignoreEmptyColumnsLines(boolean)} method.
	 */
	public EmptyColumnsStrategy getEmptyColumnsStrategy() {
		return emptyColumnsStrategy;
	}

	/**
	 * Builds immutable <tt>CsvPreference</tt> instances. The builder pattern allows for additional preferences to be
	 * added in the future.
	 */
	public static class Builder {
		
		private final char quoteChar;
		
		private final int delimiterChar;
		
		private final String endOfLineSymbols;

		private boolean surroundingSpacesNeedQuotes = false;
		
		private boolean ignoreEmptyLines = true;
		
		private CsvEncoder encoder;
		
		private QuoteMode quoteMode;
		
		private CommentMatcher commentMatcher;
		
		private int maxLinesPerRow = 0;
		
		private EmptyColumnParsing emptyColumnParsing;

		private char quoteEscapeChar;

		private EmptyColumnsStrategy emptyColumnsStrategy = EmptyColumnsStrategy.PASS;

		/**
		 * Constructs a Builder with all of the values from an existing <tt>CsvPreference</tt> instance. Useful if you
		 * want to base your preferences off one of the existing CsvPreference constants.
		 * 
		 * @param preference
		 *            the existing preference
		 */
		public Builder(final CsvPreference preference) {
			this.quoteChar = preference.quoteChar;
			this.delimiterChar = preference.delimiterChar;
			this.endOfLineSymbols = preference.endOfLineSymbols;
			this.surroundingSpacesNeedQuotes = preference.surroundingSpacesNeedQuotes;
			this.ignoreEmptyLines = preference.ignoreEmptyLines;
			this.encoder = preference.encoder;
			this.quoteMode = preference.quoteMode;
			this.commentMatcher = preference.commentMatcher;
			this.maxLinesPerRow = preference.maxLinesPerRow;
			this.emptyColumnParsing = preference.emptyColumnParsing;
			this.quoteEscapeChar = preference.quoteEscapeChar;
		}
		
		/**
		 * Constructs a Builder with the mandatory preference values.
		 * 
		 * @param quoteChar
		 *            matching pairs of this character are used to escape columns containing the delimiter
		 * @param delimiterChar
		 *            the character separating each column
		 * @param endOfLineSymbols
		 *            one or more symbols terminating the line, e.g. "\n". Only used for writing.
		 * @throws IllegalArgumentException
		 *             if quoteChar and delimiterChar are the same character
		 * @throws NullPointerException
		 *             if endOfLineSymbols is null
		 */
		public Builder(final char quoteChar, final int delimiterChar, final String endOfLineSymbols) {
			if( quoteChar == delimiterChar ) {
				throw new IllegalArgumentException(String.format(
					"quoteChar and delimiterChar must not be the same character: %c", quoteChar));
			} else if( endOfLineSymbols == null ) {
				throw new NullPointerException("endOfLineSymbols should not be null");
			}
			this.quoteChar = quoteChar;
			this.delimiterChar = delimiterChar;
			this.endOfLineSymbols = endOfLineSymbols;

			// by default (RFC-spec) the quoteEscapeChar is the quoteChar
			this.quoteEscapeChar = quoteChar;
		}
		
		/**
		 * Flag indicating whether spaces at the beginning or end of a cell should be ignored if they're not surrounded
		 * by quotes (applicable to both reading and writing CSV). The default is <tt>false</tt>, as spaces
		 * "are considered part of a field and should not be ignored" according to RFC 4180.
		 * 
		 * @since 2.0.0
		 * @param surroundingSpacesNeedQuotes
		 *            flag indicating whether spaces at the beginning or end of a cell should be ignored if they're not
		 *            surrounded by quotes
		 * @return the updated Builder
		 */
		public Builder surroundingSpacesNeedQuotes(final boolean surroundingSpacesNeedQuotes) {
			this.surroundingSpacesNeedQuotes = surroundingSpacesNeedQuotes;
			return this;
		}
		
		/**
		 * Flag indicating whether empty lines (i.e. containing only end of line symbols) should be ignored. The default
		 * is <tt>true</tt>.
		 * 
		 * @since 2.2.1
		 * @param ignoreEmptyLines
		 *            flag indicating whether empty lines should be ignored
		 * @return the updated Builder
		 */
		public Builder ignoreEmptyLines(final boolean ignoreEmptyLines) {
			this.ignoreEmptyLines = ignoreEmptyLines;
			return this;
		}
		
		/**
		 * Enables the skipping of comments. You can supply your own comment matcher or use one of the predefined ones:
		 * {@link org.supercsv.comment.CommentStartsWith CommentStartsWith} or
		 * {@link org.supercsv.comment.CommentMatches CommentMatches}
		 * 
		 * @since 2.1.0
		 * @param commentMatcher
		 *            the comment matcher to use
		 * @return the updated Builder
		 * @throws NullPointerException
		 *             if commentMatcher is null
		 */
		public Builder skipComments(final CommentMatcher commentMatcher) {
			if( commentMatcher == null ) {
				throw new NullPointerException("commentMatcher should not be null");
			}
			this.commentMatcher = commentMatcher;
			return this;
		}
		
		/**
		 * Uses a custom CsvEncoder to escape CSV for writing.
		 * 
		 * @since 2.1.0
		 * @param encoder
		 *            the custom encoder
		 * @return the updated Builder
		 * @throws NullPointerException
		 *             if encoder is null
		 */
		public Builder useEncoder(final CsvEncoder encoder) {
			if( encoder == null ) {
				throw new NullPointerException("encoder should not be null");
			}
			this.encoder = encoder;
			return this;
		}
		
		/**
		 * Uses a custom QuoteMode to determine if surrounding quotes should be applied when writing (only applicable if
		 * a column doesn't contain any special characters and wouldn't otherwise be quoted). You can supply your own
		 * quote mode or use one of the predefined ones: {@link org.supercsv.quote.AlwaysQuoteMode AlwaysQuoteMode} or
		 * {@link org.supercsv.quote.ColumnQuoteMode ColumnQuoteMode}
		 * 
		 * @since 2.1.0
		 * @param quoteMode
		 *            the quote mode
		 * @return the updated Builder
		 * @throws NullPointerException
		 *             if quoteMode is null
		 */
		public Builder useQuoteMode(final QuoteMode quoteMode) {
			if( quoteMode == null ) {
				throw new NullPointerException("quoteMode should not be null");
			}
			this.quoteMode = quoteMode;
			return this;
		}
		
		/**
		 * The maximum number of lines that a row can span before an exception is thrown (only applicable when reading
		 * CSV). This option allows CSV readers to fail fast when encountering CSV with mismatching quotes - the normal
		 * behaviour would be to continue reading until the matching quote is found, which could potentially mean
		 * reading the whole file (and exhausting all available memory). Zero or a negative value will disable this
		 * option. The default is <tt>0</tt>.
		 * 
		 * @since 2.4.0
		 * @param maxLinesPerRow
		 *            the maximum number of lines a row can span before an exception is thrown
		 * @return the updated Builder
		 */
		public Builder maxLinesPerRow(final int maxLinesPerRow) {
			this.maxLinesPerRow = maxLinesPerRow;
			return this;
		}
		
		/**
		 * Uses an EmptyColumnParsing to determine whether empty String (i.e. "") should be read as empty string instead as null.
		 * The default is <tt>ParseEmptyColumnsAsNull</tt>.
		 *
		 * @since 2.4.1
		 * @param emptyColumnParsing
		 *            the emptyColumnParsing
		 * @return the updated Builder
		 */
		public Builder setEmptyColumnParsing(final EmptyColumnParsing emptyColumnParsing) {
			if( emptyColumnParsing == null ) {
				throw new NullPointerException("emptyColumnParsing should not be null");
			}
			this.emptyColumnParsing = emptyColumnParsing;
			return this;
		}

		/**
		 * Value indicating the character to use for escaping a quote char.  The default value is
		 * the quote char (which is a double-quote <tt>"</tt> character by default).  This value
		 * must not be the same as <tt>delimiterChar</tt>
		 *
		 * @since 2.5.0
		 * @param quoteEscapeChar
		 *            value indicating the character to use for escaping a quote character
		 * @return the updated Builder
		 */
		public Builder setQuoteEscapeChar(final char quoteEscapeChar) {
			this.quoteEscapeChar = quoteEscapeChar;
			return this;
		}
		
		/**
		 * Builds the CsvPreference instance.
		 * 
		 * @return the immutable CsvPreference instance
		 * @throws IllegalArgumentException
		 *             if quoteEscapeChar and delimiterChar are the same character
		 */
		public CsvPreference build() {
			
			if( encoder == null ) {
				encoder = new DefaultCsvEncoder();
			}
			
			if( quoteMode == null ) {
				quoteMode = new NormalQuoteMode();
			}
			
			if( emptyColumnParsing == null ) {
				emptyColumnParsing = EmptyColumnParsing.ParseEmptyColumnsAsNull;
			}

			if( quoteEscapeChar == delimiterChar ) {
				throw new IllegalArgumentException(String.format(
						"quoteEscapeChar and delimiterChar must not be the same character: %c",
						quoteEscapeChar));
			}
			
			return new CsvPreference(this);
		}

		/**
		 * Configures the behavior in regards to lines only containing column delimiters. By default, a row of empty
		 * columns is returned
		 * @param ignore if set to true, lines containing only column delimiters will be skipped.
		 * @return the builder.
		 */
		public Builder ignoreEmptyColumnsLines(boolean ignore) {
			this.emptyColumnsStrategy = ignore? EmptyColumnsStrategy.FILTER : EmptyColumnsStrategy.PASS;
			return this;
		}
	}
	
}
