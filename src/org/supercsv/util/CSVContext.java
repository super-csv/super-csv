package org.supercsv.util;

import java.util.List;

/**
 * This object represents the current context of a given CSV file being either read or written.
 * 
 * @author Kasper B. Graversen
 */
public class CSVContext {
public int lineNumber;
public int columnNumber;
public List<? extends Object> lineSource;

public CSVContext() {
}

public CSVContext(final int lineNumber, final int columnNumber) {
	this.lineNumber = lineNumber;
	this.columnNumber = columnNumber;
}

@Override
public String toString() {
	return String.format("Line: %d Column: %d Raw line:\n%s\n", lineNumber, columnNumber, lineSource);
}
}
