package org.supercsv.exception;

import java.io.Serializable;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * This exception is thrown by <tt>CellProcessor</tt>s when receiving a value with a type different than the one
 * expected.
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class ClassCastInputCSVException extends SuperCSVException implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message, context and nested exception.
	 * 
	 * @param msg
	 *            the error message
	 * @param context
	 *            the CSV context
	 * @param t
	 *            the nested exception
	 */
	public ClassCastInputCSVException(String msg, CSVContext context, Throwable t) {
		super(msg, context, t);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message and context.
	 * 
	 * @param msg
	 *            the error message
	 * @param context
	 *            the CSV context
	 */
	public ClassCastInputCSVException(String msg, CSVContext context) {
		super(msg, context);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message, context and the offending
	 * processor.
	 * 
	 * @param msg
	 *            the error message
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 */
	public ClassCastInputCSVException(String msg, CSVContext context, CellProcessor processor) {
		super(msg, context, processor);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message.
	 * 
	 * @param msg
	 *            the error message
	 */
	public ClassCastInputCSVException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the received value, expected class, context and the
	 * offending processor.
	 * 
	 * @param receivedValue
	 *            the received value
	 * @param expectedClass
	 *            the expected class
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 */
	public ClassCastInputCSVException(Object receivedValue, Class<?> expectedClass, CSVContext context,
		CellProcessor processor) {
		super(getDefaultMessage(receivedValue, expectedClass), context, processor);
	}
	
	/**
	 * Assembles a default error message.
	 * 
	 * @param receivedValue
	 *            the received value
	 * @param expectedClass
	 *            the expected class
	 * @return the assembled error message
	 */
	private static String getDefaultMessage(Object receivedValue, Class<?> expectedClass) {
		String printedReceivedClass = (receivedValue == null) ? "? (null was provided)" : receivedValue.getClass()
			.getName();
		String printedExpectedClass = expectedClass.getName();
		return "unexpected input value '" + receivedValue + "' of class " + printedReceivedClass
			+ " while expecting a value of class " + printedExpectedClass;
	}
	
}
