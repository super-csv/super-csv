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
 * Ready to use configuration for north european excel CSV files (columns are separated by ";" instead of ",")
 */
public static final CsvPreference EXCEL_NORTH_EUROPE_PREFERENCE = new CsvPreference('"', ';', "\n");

/**
 * Ready to use configuration. Reading and making sure no data is accidently parsed as comments
 */
public static final CsvPreference NO_COMMENT_PREFERENCE = new CsvPreference('"', ',', "\n");

protected char quoteChar;

protected int delimiterChar;

/**
 * Only used when writing. Recommended "\n" or for mac "\r" or if special sequences are needed such as "<EOL>\n"
 */
protected String endOfLineSymbols;

/**
 * Set the preference for readers and writers
 * 
 * @param quoteChar
 *            Specifies that matching pairs of this character delimit string constants in this tokenizer.
 * @param delimiterChar
 *            Specifies the character separating each column
 * @param endOfLineSymbols
 *            one or more symbols terminating the line, e.g. "\n". This parameter only makes sense for writers
 */
public CsvPreference(final char quoteChar, final int delimiterChar, final String endOfLineSymbols) {
	// setCommentChar(commentChar);
	setQuoteChar(quoteChar);
	setDelimiterChar(delimiterChar);
	setEndOfLineSymbols(endOfLineSymbols);
}

public int getDelimiterChar() {
	return delimiterChar;
}

public String getEndOfLineSymbols() {
	return endOfLineSymbols;
}

public int getQuoteChar() {
	return quoteChar;
}

public CsvPreference setDelimiterChar(final int delimiterChar) {
	this.delimiterChar = delimiterChar;
	return this;
}

public CsvPreference setEndOfLineSymbols(final String endOfLineSymbols) {
	this.endOfLineSymbols = endOfLineSymbols;
	return this;
}

public CsvPreference setQuoteChar(final char quoteChar) {
	this.quoteChar = quoteChar;
	return this;
}
}
