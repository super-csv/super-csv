package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Deprecated due to bad naming. Use {@link RequireHashCode} instead.
 * 
 * @author Kasper B. Graversen
 */
@Deprecated
public class Required extends RequireHashCode {

	/**
	 * Constructs a new <tt>Required</tt> processor, which converts the input to a String, and ensures that the
	 * input's hash function matches any of a given set of hashcodes.
	 * 
	 * @param requiredHashcodes
	 *            one or more hashcodes
	 */
	public Required(final int... requiredHashcodes) {
		super(requiredHashcodes);
	}
	
	/**
	 * Constructs a new <tt>Required</tt> processor, which converts the input to a String, ensures that the
	 * input's hash function matches the supplied hashcode, then calls the next processor in the chain.
	 * 
	 * @param requiredHashcode
	 *            the required hashcode
	 * @param next
	 *            the next processor in the chain
	 */
	public Required(final int requiredHashcode, final CellProcessor next) {
		this(new int[] { requiredHashcode }, next);
	}
	
	/**
	 * Constructs a new <tt>Required</tt> processor, which converts the input to a String, ensures that the
	 * input's hash function matches any of a given set of hashcodes, then calls the next processor in the chain.
	 * 
	 * @param requiredHashcodes
	 *            one or more hashcodes
	 * @param next
	 *            the next processor in the chain
	 */
	public Required(final int[] requiredHashcodes, final CellProcessor next) {
		super(requiredHashcodes, next);
	}
	

}
