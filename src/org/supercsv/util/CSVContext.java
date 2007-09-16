package org.supercsv.util;

/**
 * This object represents the current context of a given CSV file being either read or written.
 * 
 * @author Kasper B. Graversen
 */
public class CSVContext {
	public int lineNumber;
	public int columnNumber;

	public CSVContext() {
	}

	public CSVContext(final int lineNumber, final int columnNumber) {
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

}
