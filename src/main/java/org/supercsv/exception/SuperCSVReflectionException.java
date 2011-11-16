package org.supercsv.exception;

/**
 * Wraps the following reflection related checked exceptions:
 * <p>
 * <tt>
 * ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException,
 * NoSuchMethodException</tt>
 * <p>
 * 
 * @since 1.30
 * @author Kasper B. Graversen, (c) 2007
 */
public class SuperCSVReflectionException extends SuperCSVException {

/**
 * 
 */
private static final long serialVersionUID = 1L;

public SuperCSVReflectionException(final String msg) {
	super(msg);
}

public SuperCSVReflectionException(final String msg, final Throwable t) {
	super(msg, null, t);
}

}
