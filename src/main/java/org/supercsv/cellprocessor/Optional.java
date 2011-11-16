package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This processor returns <tt>null</tt> if it meets the empty String in a column and does not call subsequent processors in its
 * chain. It is a simple customization of <tt>Token</tt>. If you need to return values other than <tt>null</tt>, use
 * {@link Token} instead.
 * 
 * @author Kasper B. Graversen
 */
public class Optional extends Token {
	/**
	 * Upon meeting the empty string in a column, return null
	 */
	public Optional() {
		super("", null);
	}
	
	/**
	 * Upon meeting the empty string in a column, return null
	 */
	public Optional(final CellProcessor next) {
		super("", null, next);
	}
}
