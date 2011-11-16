package org.supercsv.exception;

import java.io.Serializable;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * This exception is raised by processors when receiving a value with a type different than the one expected. *
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class ClassCastInputCSVException extends SuperCSVException implements Serializable {
private static final long serialVersionUID = 1L;

public ClassCastInputCSVException(String msg, CSVContext context, Throwable t) {
	super(msg, context, t);
}

public ClassCastInputCSVException(String msg, CSVContext context) {
	super(msg, context);
}

public ClassCastInputCSVException(String msg, CSVContext context, CellProcessor processor) {
	super(msg, context, processor);
}

public ClassCastInputCSVException(String msg) {
	super(msg);
}

public ClassCastInputCSVException(Object receivedValue, Class expectedClass, CSVContext context, CellProcessor processor) {
	super(getDefaultMessage(receivedValue, expectedClass), context, processor);
}

private static String getDefaultMessage(Object receivedValue, Class expectedClass) {
	String printedReceivedClass = (receivedValue == null) ? "? (null was provided)" : receivedValue.getClass()
		.toString();
	String printedExpectedClass = expectedClass.toString();
	return "unexpected input value '" + receivedValue + "' of class " + printedReceivedClass
		+ " while expecting a value of class " + printedExpectedClass;
}

}
