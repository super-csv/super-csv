package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that Strings or String-representations of objects are trimmed to a maximum size. If you desire, you can append
 * a String to denote that the data has been trimmed (e.g. "...").
 * 
 * @author Kasper B. Graversen
 */
public class Trim extends CellProcessorAdaptor implements StringCellProcessor {
	
	private int maxSize;
	private String trimPostfix = "";
	
	/**
	 * Constructs a new <tt>Trim</tt> processor, which trims a String to ensure it is no longer than the specified size.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 */
	public Trim(final int maxSize) {
		super();
		if( maxSize < 1 ) {
			throw new SuperCSVException("argument maxSize must be > 0", this);
		}
		this.maxSize = maxSize;
	}
	
	/**
	 * Constructs a new <tt>Trim</tt> processor, which trims a String to ensure it is no longer than the specified size,
	 * then appends the <code>trimPostfix</code> String to indicate that the String has been trimmed.
	 * 
	 * @param maxSize
	 *            the maximum size of the String
	 * @param trimPostfix
	 *            the String to append if the input is trimmed (e.g. "...")
	 */
	public Trim(final int maxSize, final String trimPostfix) {
		this(maxSize);
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
	 */
	public Trim(final int maxSize, final String trimPostfix, final StringCellProcessor next) {
		this(maxSize, next);
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
	 */
	public Trim(final int maxSize, final StringCellProcessor next) {
		super(next);
		this.maxSize = maxSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final String stringValue = value.toString();
		
		String result;
		if( stringValue.length() <= maxSize ) {
			result = stringValue;
		} else {
			result = stringValue.substring(0, maxSize) + trimPostfix;
		}
		
		return next.execute(result, context);
	}
}
