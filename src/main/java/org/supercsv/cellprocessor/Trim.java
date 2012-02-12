package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that Strings or String-representations of objects are trimmed to a maximum size. If you desire, you can append
 * a String to denote that the data has been trimmed (e.g. "...").
 * 
 * @author Kasper B. Graversen
 */
public class Trim extends CellProcessorAdaptor implements StringCellProcessor {
	
	private static final String EMPTY_STRING = "";
	
	private final int maxSize;
	private final String trimPostfix;
	
	/**
	 * Constructs a new <tt>Trim</tt> processor, which trims a String to ensure it is no longer than the specified size.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 * @throws IllegalArgumentException
	 *             if maxSize <= 0
	 */
	public Trim(final int maxSize) {
		this(maxSize, EMPTY_STRING);
	}
	
	/**
	 * Constructs a new <tt>Trim</tt> processor, which trims a String to ensure it is no longer than the specified size,
	 * then appends the <code>trimPostfix</code> String to indicate that the String has been trimmed.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 * @param trimPostfix
	 *            the String to append if the input is trimmed (e.g. "...")
	 * @throws IllegalArgumentException
	 *             if maxSize <= 0
	 * @throws NullPointerException
	 *             if trimPostfix is null
	 */
	public Trim(final int maxSize, final String trimPostfix) {
		checkPreconditions(maxSize, trimPostfix);
		this.maxSize = maxSize;
		this.trimPostfix = trimPostfix;
	}
	
	/**
	 * Constructs a new <tt>Trim</tt> processor, which trims a String to ensure it is no longer than the specified size,
	 * then appends the <code>trimPostfix</code> String to indicate that the String has been trimmed and calls the next
	 * processor in the chain.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 * @param trimPostfix
	 *            the String to append if the input is trimmed (e.g. "...")
	 * @param next
	 *            the next processor in the chain
	 * @throws IllegalArgumentException
	 *             if maxSize <= 0
	 * @throws NullPointerException
	 *             if trimPostfix or next is null
	 */
	public Trim(final int maxSize, final String trimPostfix, final StringCellProcessor next) {
		super(next);
		checkPreconditions(maxSize, trimPostfix);
		this.maxSize = maxSize;
		this.trimPostfix = trimPostfix;
	}
	
	/**
	 * Constructs a new <tt>Trim</tt> processor, which trims a String to ensure it is no longer than the specified size,
	 * then calls the next processor in the chain.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 * @param next
	 *            the next processor in the chain
	 * @throws IllegalArgumentException
	 *             if maxSize <= 0
	 * @throws NullPointerException
	 *             if next is null
	 */
	public Trim(final int maxSize, final StringCellProcessor next) {
		this(maxSize, EMPTY_STRING, next);
	}
	
	/**
	 * Checks the preconditions for creating a new Trim processor.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 * @param trimPostfix
	 *            the String to append if the input is trimmed (e.g. "...")
	 * @throws IllegalArgumentException
	 *             if maxSize <= 0
	 * @throws NullPointerException
	 *             if trimPostfix is null
	 */
	private static void checkPreconditions(final int maxSize, final String trimPostfix) {
		if( maxSize <= 0 ) {
			throw new IllegalArgumentException(String.format("maxSize should be > 0 but was %d", maxSize));
		}
		if( trimPostfix == null ) {
			throw new NullPointerException("trimPostfix should not be null");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullInputException
	 *             if value is null
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final String stringValue = value.toString();
		final String result;
		if( stringValue.length() <= maxSize ) {
			result = stringValue;
		} else {
			result = stringValue.substring(0, maxSize) + trimPostfix;
		}
		
		return next.execute(result, context);
	}
}
