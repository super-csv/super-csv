package org.supercsv.prefs;

/**
 * Before reading or writing CSV files, you must supply the reader/writer with a setup (this object)
 * 
 * @author Kasper B. Graversen
 */
public class CsvPreference {
	/**
	 * Ready to use configuration. This one should cover 99% of all usages of the package
	 */
	public static final CsvPreference STANDARD_PREFERENCE = new CsvPreference('"', ',', "\r\n");
	
	/**
	 * Ready to use configuration for reading Windows Excel exported CSV files.
	 */
	public static final CsvPreference EXCEL_PREFERENCE = new CsvPreference('"', ',', "\n");
	
	/**
	 * Ready to use configuration for north European excel CSV files (columns are separated by ";" instead of ",")
	 */
	public static final CsvPreference EXCEL_NORTH_EUROPE_PREFERENCE = new CsvPreference('"', ';', "\n");
	
	/**
	 * Ready to use configuration. Reading and making sure no data is accidently parsed as comments
	 */
	public static final CsvPreference NO_COMMENT_PREFERENCE = new CsvPreference('"', ',', "\n");
	
	/**
	 * The quote character
	 */
	protected char quoteChar;
	
	/**
	 * The delimiter character
	 */
	protected int delimiterChar;
	
	/**
	 * Only used when writing. Recommended "\n" or for mac "\r" or if special sequences are needed such as "<EOL>\n"
	 */
	protected String endOfLineSymbols;
	
	/**
	 * Constructs a new <tt>CsvPreference</tt> to use when reading or writing (the end of line symbols are only used for
	 * writing, however).
	 * 
	 * @param quoteChar
	 *            specifies that matching pairs of this character delimit string constants in this tokenizer.
	 * @param delimiterChar
	 *            specifies the character separating each column
	 * @param endOfLineSymbols
	 *            one or more symbols terminating the line, e.g. "\n". This parameter only makes sense for writers
	 */
	public CsvPreference(final char quoteChar, final int delimiterChar, final String endOfLineSymbols) {
		setQuoteChar(quoteChar);
		setDelimiterChar(delimiterChar);
		setEndOfLineSymbols(endOfLineSymbols);
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
	public int getQuoteChar() {
		return quoteChar;
	}
	
	/**
	 * Sets the delimiter character
	 * 
	 * @param delimiterChar
	 *            the character to use as a delimiter
	 * @return the updated preference
	 */
	public CsvPreference setDelimiterChar(final int delimiterChar) {
		this.delimiterChar = delimiterChar;
		return this;
	}
	
	/**
	 * Sets the end of line symbols
	 * 
	 * @param endOfLineSymbols
	 *            the end of line symbols
	 * @return the updated preference
	 */
	public CsvPreference setEndOfLineSymbols(final String endOfLineSymbols) {
		this.endOfLineSymbols = endOfLineSymbols;
		return this;
	}
	
	/**
	 * Sets the quote character
	 * 
	 * @param quoteChar
	 *            the quote character
	 * @return the updated preference
	 */
	public CsvPreference setQuoteChar(final char quoteChar) {
		this.quoteChar = quoteChar;
		return this;
	}
}
