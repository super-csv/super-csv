/**
 *
 */
package org.supercsv.cellprocessor.constraint;

import java.util.List;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts the input to a String and ensures that the input contains at least one of the specified substrings.
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 */
public class RequireSubStr extends CellProcessorAdaptor implements StringCellProcessor {
	
	String[] requiredSubStrings;
	
	/**
	 * Converts the input to a String, ensures that the input contains at least one of the specified substrings, then
	 * calls the next processor in the chain.
	 * 
	 * @param requiredSubStrings
	 *            the List of required substrings
	 * @param next
	 *            the next processor in the chain
	 */
	public RequireSubStr(final List<String> requiredSubStrings, final CellProcessor next) {
		this(requiredSubStrings.toArray(new String[0]), next);
	}
	
	/**
	 * Converts the input to a String and ensures that the input contains at least one of the specified substrings.
	 * 
	 * @param requiredSubStrings
	 *            the required substrings
	 */
	public RequireSubStr(final String... requiredSubStrings) {
		super();
		this.requiredSubStrings = requiredSubStrings.clone();
	}
	
	/**
	 * Converts the input to a String, ensures that the input contains the specified substring, then calls the next
	 * processor in the chain.
	 * 
	 * @param requiredSubString
	 *            the required substring
	 * @param next
	 *            the next processor in the chain
	 */
	public RequireSubStr(final String requiredSubString, final CellProcessor next) {
		this(new String[] { requiredSubString }, next);
	}
	
	/**
	 * Converts the input to a String, ensures that the input contains at least one of the specified substrings, then
	 * calls the next processor in the chain.
	 * 
	 * @param requiredSubStrings
	 *            the List of required substrings
	 * @param next
	 *            the next processor in the chain
	 */
	public RequireSubStr(final String[] requiredSubStrings, final CellProcessor next) {
		super(next);
		this.requiredSubStrings = requiredSubStrings.clone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final String sval = value.toString(); // cast
		
		boolean found = false;
		for( final String required : requiredSubStrings ) {
			if( sval.indexOf(required) != -1 ) {
				found = true;
				break;
			}
		}
		if( !found ) {
			throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
				+ context.columnNumber + " doesn't contain any of the required substrings", context, this);
		}
		
		return next.execute(value, context);
	}
}
