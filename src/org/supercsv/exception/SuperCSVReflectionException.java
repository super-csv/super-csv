package org.supercsv.exception;

/**
 * Wraps the following reflection related checked exceptions:
 * <p>
 * <tt>
 * ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException,
 * NoSuchMethodException</tt>
 * <p>
 * 
 * @author Kasper B. Graversen, (c) 2007
 */
public class SuperCSVReflectionException extends SuperCSVException {

	public SuperCSVReflectionException(final String msg, Throwable t) {
		super(msg, null, t);
	}

	public SuperCSVReflectionException(final String msg) {
		super(msg);
	}

}
