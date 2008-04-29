package org.supercsv.exception;

import java.io.Serializable;

import org.supercsv.util.CSVContext;

/**
 * This exception is raised by processors when receiving
 * a value with a type different than the one expected.
 * 
 * @author Dominique De Vito (ddv36a78@yahoo.fr)
 */
public class ClassCastInputCSVException extends SuperCSVException implements Serializable {
	private static final long	serialVersionUID	= 1L;

	public ClassCastInputCSVException(String msg, CSVContext context, Throwable t) {
		super(msg, context, t);
	}

	public ClassCastInputCSVException(String msg, CSVContext context) {
		super(msg, context);
	}

	public ClassCastInputCSVException(String msg) {
		super(msg);
	}

	public ClassCastInputCSVException(Object receivedValue, Class expectedClass, CSVContext context) {
		super(getDefaultMessage(receivedValue, expectedClass), context);
	}
	
	private static String getDefaultMessage(Object receivedValue,
			Class expectedClass) {
		String printedReceivedClass = (receivedValue == null) ? "?" : receivedValue.getClass().toString();
		String printedExpectedClass = expectedClass.toString();
		return "unexpected input value '" + receivedValue + "' of class " + printedReceivedClass + 
			" while expecting a value of class " + printedExpectedClass;
	}
  
}
