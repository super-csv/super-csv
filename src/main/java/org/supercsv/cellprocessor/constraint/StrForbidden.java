package org.supercsv.cellprocessor.constraint;

import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Converts the input to a String and ensures that it doesn't contain any of the supplied substrings.
 * 
 * @deprecated Use {@link ForbidSubStr} instead (it's a better name)
 * @author Kasper B. Graversen
 */
@Deprecated
public class StrForbidden extends ForbidSubStr {
	
	/**
	 * Constructs a new <tt>StrForbidden</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings.
	 * 
	 * @param forbiddenStrings
	 *            the List of forbidden substrings
	 * @throws NullPointerException
	 *             if forbiddenStrings is null
	 * @throws IllegalArgumentException
	 *             if forbiddenStrings is empty
	 */
	public StrForbidden(final List<String> forbiddenStrings) {
		super(forbiddenStrings);
	}
	
	/**
	 * Constructs a new <tt>StrForbidden</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings, then calls the next processor in the chain.
	 * 
	 * @param forbiddenStrings
	 *            the List of forbidden substrings
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if forbiddenStrings or next is null
	 * @throws IllegalArgumentException
	 *             if forbiddenStrings is empty
	 */
	public StrForbidden(final List<String> forbiddenStrings, final CellProcessor next) {
		super(forbiddenStrings, next);
	}
	
	/**
	 * Constructs a new <tt>StrForbidden</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings.
	 * 
	 * @param forbiddenStrings
	 *            the forbidden substrings
	 * @throws NullPointerException
	 *             if forbiddenStrings is null
	 * @throws IllegalArgumentException
	 *             if forbiddenStrings is empty
	 */
	public StrForbidden(final String... forbiddenStrings) {
		super(forbiddenStrings);
	}
	
	/**
	 * Constructs a new <tt>StrForbidden</tt> processor which ensures the input doesn't contain the supplied substring,
	 * then calls the next processor in the chain.
	 * 
	 * @param forbiddenString
	 *            the forbidden substring
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if forbiddenStrings or next is null
	 */
	public StrForbidden(final String forbiddenString, final CellProcessor next) {
		super(forbiddenString, next);
	}
	
	/**
	 * Constructs a new <tt>StrForbidden</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings, then calls the next processor in the chain.
	 * 
	 * @param forbiddenStrings
	 *            the forbidden substrings
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if forbiddenStrings or next is null
	 * @throws IllegalArgumentException
	 *             if forbiddenStrings is empty
	 */
	public StrForbidden(final String[] forbiddenStrings, final CellProcessor next) {
		super(forbiddenStrings, next);
	}
	
}
