package org.supercsv.cellprocessor.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts the input to a String and ensures that it doesn't contain any of the supplied substrings. For example, this
 * constraint might be handy when reading/writing filenames and wanting to ensure no filename contains ":", "/", etc.
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ForbidSubStr extends CellProcessorAdaptor implements StringCellProcessor {
	
	private final List<String> forbiddenSubStrings = new ArrayList<String>();
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings.
	 * 
	 * @param forbiddenSubStrings
	 *            the List of forbidden substrings
	 * @throws NullPointerException
	 *             if forbiddenSubStrings or one of its elements is null
	 * @throws IllegalArgumentException
	 *             if forbiddenSubStrings is empty
	 */
	public ForbidSubStr(final List<String> forbiddenSubStrings) {
		super();
		checkPreconditions(forbiddenSubStrings);
		checkAndAddForbiddenStrings(forbiddenSubStrings);
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 * @throws NullPointerException
	 *             if forbiddenSubStrings or one of its elements is null
	 * @throws IllegalArgumentException
	 *             if forbiddenSubStrings is empty
	 */
	public ForbidSubStr(final String... forbiddenSubStrings) {
		super();
		checkPreconditions(forbiddenSubStrings);
		checkAndAddForbiddenStrings(forbiddenSubStrings);
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings, then calls the next processor in the chain.
	 * 
	 * @param forbiddenSubStrings
	 *            the List of forbidden substrings
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if forbiddenSubStrings, one of its elements or next is null
	 * @throws IllegalArgumentException
	 *             if forbiddenSubStrings is empty
	 */
	public ForbidSubStr(final List<String> forbiddenSubStrings, final CellProcessor next) {
		super(next);
		checkPreconditions(forbiddenSubStrings);
		checkAndAddForbiddenStrings(forbiddenSubStrings);
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain the supplied substring,
	 * then calls the next processor in the chain.
	 * 
	 * @param forbiddenSubString
	 *            the forbidden substring
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if forbiddenSubString or next is null
	 */
	public ForbidSubStr(final String forbiddenSubString, final CellProcessor next) {
		this(new String[] { forbiddenSubString }, next);
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings, then calls the next processor in the chain.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if forbiddenSubStrings, one of its elements or next is null
	 * @throws IllegalArgumentException
	 *             if forbiddenSubStrings is empty
	 */
	public ForbidSubStr(final String[] forbiddenSubStrings, final CellProcessor next) {
		super(next);
		checkPreconditions(forbiddenSubStrings);
		checkAndAddForbiddenStrings(forbiddenSubStrings);
	}
	
	/**
	 * Checks the preconditions for creating a new ForbidSubStr processor with a List of forbidden substrings.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 * @throws NullPointerException
	 *             if forbiddenSubStrings is null
	 * @throws IllegalArgumentException
	 *             if forbiddenSubStrings is empty
	 */
	private static void checkPreconditions(final List<String> forbiddenSubStrings) {
		if( forbiddenSubStrings == null ) {
			throw new NullPointerException("forbiddenSubStrings list should not be null");
		} else if( forbiddenSubStrings.isEmpty() ) {
			throw new IllegalArgumentException("forbiddenSubStrings list should not be empty");
		}
	}
	
	/**
	 * Checks the preconditions for creating a new ForbidSubStr processor with an array of forbidden substrings.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 * @throws NullPointerException
	 *             if forbiddenSubStrings is null
	 * @throws IllegalArgumentException
	 *             if forbiddenSubStrings is empty
	 */
	private static void checkPreconditions(final String... forbiddenSubStrings) {
		if( forbiddenSubStrings == null ) {
			throw new NullPointerException("forbiddenSubStrings array should not be null");
		} else if( forbiddenSubStrings.length == 0 ) {
			throw new IllegalArgumentException("forbiddenSubStrings array should not be empty");
		}
	}
	
	/**
	 * Adds each forbidden substring, checking that it's not null.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 * @throws NullPointerException
	 *             if a forbidden substring is null
	 */
	private void checkAndAddForbiddenStrings(final String... forbiddenSubStrings) {
		checkAndAddForbiddenStrings(Arrays.asList(forbiddenSubStrings));
	}
	
	/**
	 * Adds each forbidden substring, checking that it's not null.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 * @throws NullPointerException
	 *             if a forbidden substring is null
	 */
	private void checkAndAddForbiddenStrings(final List<String> forbiddenSubStrings) {
		for( String forbidden : forbiddenSubStrings ) {
			if( forbidden == null ) {
				throw new NullPointerException("forbidden substring should not be null");
			}
			this.forbiddenSubStrings.add(forbidden);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value is in the list of forbidden
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final String stringValue = value.toString();
		
		for( String forbidden : forbiddenSubStrings ) {
			if( stringValue.contains(forbidden) ) {
				throw new SuperCSVException(String.format("'%s' contains the forbidden substring '%s'", value,
					forbidden), context, this);
			}
		}
		
		return next.execute(value, context);
	}
}
