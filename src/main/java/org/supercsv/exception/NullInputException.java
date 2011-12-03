package org.supercsv.exception;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * Exception thrown when a mandatory input to a <tt>CellProcessor</tt>, reader or writer is <tt>null</tt>.
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 * @author Dominique de Vito
 */
public class NullInputException extends SuperCSVException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 */
	public NullInputException(final String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param offendingProcessor
	 *            the processor requiring the input to be supplied
	 * @param t
	 *            the nested exception
	 */
	public NullInputException(final String msg, final CellProcessor offendingProcessor, final Throwable t) {
		super(msg, null, offendingProcessor, t);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the current CSV context
	 * @param t
	 *            the nested exception
	 */
	public NullInputException(final String msg, final CSVContext context, final Throwable t) {
		super(msg, context, null, t);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the current CSV context
	 * @param offendingProcessor
	 *            the processor requiring the input to be supplied
	 */
	public NullInputException(final String msg, final CSVContext context, final CellProcessor offendingProcessor) {
		super(msg, context, offendingProcessor);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param offendingProcessor
	 *            the processor requiring the input to be supplied
	 */
	public NullInputException(final String msg, final CellProcessor offendingProcessor) {
		super(msg, offendingProcessor);
	}
	
}
