package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This processor returns <tt>null</tt> if it encounters empty String (""). It is a simple customisation of
 * <tt>Token</tt>. If you need to return values other than <tt>null</tt>, use {@link Token} instead.
 * 
 * @author Kasper B. Graversen
 */
public class Optional extends Token {
	
	/**
	 * Constructs a new <tt>Optional</tt> processor, which when encountering empty String ("") will return <tt>null</tt>
	 * , for all other values it will return the value unchanged.
	 */
	public Optional() {
		super("", null);
	}
	
	/**
	 * Constructs a new <tt>Optional</tt> processor, which when encountering empty String ("") will return <tt>null</tt>
	 * , for all other values it will call the next processor in the chain.
	 * 
	 * @throws NullPointerException
	 *             if next is null
	 */
	public Optional(final CellProcessor next) {
		super("", null, next);
	}
}
