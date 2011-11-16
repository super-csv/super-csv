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

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + columnNumber;
	result = prime * result + lineNumber;
	result = prime * result + ((lineSource == null) ? 0 : lineSource.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if( this == obj ) {
		return true;
	}
	if( obj == null ) {
		return false;
	}
	if( getClass() != obj.getClass() ) {
		return false;
	}
	final CSVContext other = (CSVContext) obj;
	if( columnNumber != other.columnNumber ) {
		return false;
	}
	if( lineNumber != other.lineNumber ) {
		return false;
	}
	if( lineSource == null ) {
		if( other.lineSource != null ) {
			return false;
		}
	}
	else
		if( !lineSource.equals(other.lineSource) ) {
			return false;
		}
	return true;
}

}
