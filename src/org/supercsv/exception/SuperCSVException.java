package org.supercsv.exception;

import java.io.Serializable;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/** If anything goes wrong, we throw one of these bad boys here */
public class SuperCSVException extends RuntimeException implements Serializable {
private static final long serialVersionUID = 1L;
private CSVContext csvContext;
private CellProcessor offendingProcessor;

public SuperCSVException(final String msg) {
	super(msg);
}

public SuperCSVException(final String msg, final CSVContext context) {
	super(msg);
	this.csvContext = context;
}

public SuperCSVException(final String msg, final CSVContext context, final Throwable t) {
	super(t.getMessage() + "\n" + msg, t);
	this.csvContext = context;
}

public SuperCSVException(final String msg, final CellProcessor processor) {
	super(msg);
	this.offendingProcessor = processor;
}

public SuperCSVException(final String msg, final CSVContext context, final CellProcessor processor) {
	super(msg);
	this.csvContext = context;
	this.offendingProcessor = processor;
}

public SuperCSVException(final String msg, final CSVContext context, final CellProcessor processor, final Throwable t) {
	super(t.getMessage() + "\n" + msg, t);
	this.csvContext = context;
	this.offendingProcessor = processor;
}

/**
 * The context may be null when exceptions are thrown before or after processing, such as in cell offendingProcessor's
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

public CellProcessor getOffendingProcessor() {
	return offendingProcessor;
}

@Override
public String toString() {
	return String.format("%s context: %s offending processor: %s", getMessage(), csvContext, offendingProcessor);
}
}
