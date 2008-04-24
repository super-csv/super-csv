package org.supercsv.exception;

import java.io.Serializable;

import org.supercsv.util.CSVContext;

/** If anything goes wrong, we throw one of these bad boys here */
public class SuperCSVException extends RuntimeException implements Serializable {
private static final long serialVersionUID = 1L;
private CSVContext csvContext;

public SuperCSVException(final String msg) {
	super(msg);
}

public SuperCSVException(final String msg, final CSVContext context) {
	super(msg);
	this.csvContext = context;
}

public SuperCSVException(final String msg, final CSVContext context, final Throwable t) {
	super(msg, t);
	this.csvContext = context;
}

/**
 * The context may be null when exceptions are thrown before or after processing, such as in cell processor's
 * <code>init()</code> methods.
 * 
 * @return null, or the context of the cvs file
 */
public CSVContext getCsvContext() {
	return csvContext;
}

/**
 * Think twice before invoking this...
 * 
 * @param csvContext
 *            the new context
 */
public void setCsvContext(final CSVContext csvContext) {
	this.csvContext = csvContext;
}
}
