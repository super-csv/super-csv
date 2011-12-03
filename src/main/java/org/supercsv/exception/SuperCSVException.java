package org.supercsv.exception;

import java.io.Serializable;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * If anything goes wrong, we throw one of these bad boys!
 * 
 * @author Kasper B. Graversen
 */
public class SuperCSVException extends RuntimeException implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private CSVContext csvContext;
	
	private CellProcessor offendingProcessor;
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 */
	public SuperCSVException(final String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 */
	public SuperCSVException(final String msg, final CSVContext context) {
		super(msg);
		this.csvContext = context;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 * @param t
	 *            the nested exception
	 */
	public SuperCSVException(final String msg, final CSVContext context, final Throwable t) {
		super(t.getMessage() + "\n" + msg, t);
		this.csvContext = context;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param processor
	 *            the offending processor
	 */
	public SuperCSVException(final String msg, final CellProcessor processor) {
		super(msg);
		this.offendingProcessor = processor;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 */
	public SuperCSVException(final String msg, final CSVContext context, final CellProcessor processor) {
		super(msg);
		this.csvContext = context;
		this.offendingProcessor = processor;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 * @param t
	 *            the nested exception
	 */
	public SuperCSVException(final String msg, final CSVContext context, final CellProcessor processor,
		final Throwable t) {
		super(t.getMessage() + "\n" + msg, t);
		this.csvContext = context;
		this.offendingProcessor = processor;
	}
	
	/**
	 * The context may be null when exceptions are thrown before or after processing, such as in cell
	 * offendingProcessor's <code>init()</code> methods.
	 * 
	 * @return the current CSV context, or <tt>null</tt>
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
	
	/**
	 * Returns the processor executing when the exception occurred.
	 * 
	 * @return the processor executing when the exception occurred
	 */
	public CellProcessor getOffendingProcessor() {
		return offendingProcessor;
	}
	
	/**
	 * Returns the String representation of this exception.
	 */
	@Override
	public String toString() {
		return String.format("%s context: %s offending processor: %s", getMessage(), csvContext, offendingProcessor);
	}
}
