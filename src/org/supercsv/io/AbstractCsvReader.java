package org.supercsv.io;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

/**
 * This is a set of functionality used across the various readers
 * 
 * @author Kasper B. Graversen
 */
public abstract class AbstractCsvReader implements ICsvReader {

/** A reference to the last read line */
protected List<String> line;

/** the tokenizer */
protected ITokenizer tokenizer;

/** the preferences */
protected CsvPreference preferences;

protected AbstractCsvReader() {
	line = new ArrayList<String>();
}

/**
 * {@inheritDoc}
 */
public void close() throws IOException {
	tokenizer.close();
}

/**
 * {@inheritDoc}
 */
public String get(final int N) throws IOException, IndexOutOfBoundsException {
	return line.get(N);
}

/**
 * A convenience method for reading the header of a csv file as a string array. This array can serve as input when
 * reading maps or beans.
 * 
 * @param firstLineCheck
 *            check to ensure that this method is only applied to the first line of the CSV file.
 * @since 1.0
 */
public String[] getCSVHeader(final boolean firstLineCheck) throws IOException {
	if( firstLineCheck && tokenizer.getLineNumber() != 0 ) {
		throw new SuperCSVException("CSV header can only be fetched as the first read operation on a source!");
	}
	final List<String> tmp = new ArrayList<String>();
	String[] res = null;
	if( tokenizer.readStringList(tmp) ) {
		res = tmp.toArray(new String[0]);
	}
	return res;
}

/**
 * {@inheritDoc}
 */
public int getLineNumber() {
	return tokenizer.getLineNumber();
}

/**
 * {@inheritDoc}
 */
public int length() throws IOException {
	return line.size();
}

/**
 * Sets the input stream
 */
public ICsvReader setInput(final Reader stream) {
	tokenizer = new Tokenizer(stream, this.preferences);
	return this;
}

/**
 * {@inheritDoc}
 */
public ICsvReader setPreferences(final CsvPreference preference) {
	this.preferences = preference;
	return this;
}

/**
 * {@inheritDoc}
 */
public ICsvReader setTokenizer(final ITokenizer tokenizer) {
	this.tokenizer = tokenizer;
	return this;
}

}
