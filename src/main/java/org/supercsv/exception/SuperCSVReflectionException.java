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
 * @author Kasper B. Graversen
 */
public class SuperCSVReflectionException extends SuperCSVException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new <tt>SuperCSVReflectionException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 */
	public SuperCSVReflectionException(final String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>SuperCSVReflectionException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param t
	 *            the nested exception
	 */
	public SuperCSVReflectionException(final String msg, final Throwable t) {
		super(msg, null, t);
	}
	
}
