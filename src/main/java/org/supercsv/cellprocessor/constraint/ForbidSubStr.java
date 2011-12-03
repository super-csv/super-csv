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
 * Converts the input to a String and ensures that it doesn't contain any of the supplied substrings. For example, this
 * constraint might be handy when reading/writing filenames and wanting to ensure no filename contains ":", "/", etc.
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 */
public class ForbidSubStr extends CellProcessorAdaptor implements StringCellProcessor {
	
	String[] forbiddenSubStrings;
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings.
	 * 
	 * @param forbiddenSubStrings
	 *            the List of forbidden substrings
	 */
	public ForbidSubStr(final List<String> forbiddenSubStrings) {
		this(forbiddenSubStrings.toArray(new String[0]));
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings, then calls the next processor in the chain.
	 * 
	 * @param forbiddenSubStrings
	 *            the List of forbidden substrings
	 * @param next
	 *            the next processor in the chain
	 */
	public ForbidSubStr(final List<String> forbiddenSubStrings, final CellProcessor next) {
		this(forbiddenSubStrings.toArray(new String[0]), next);
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain any of the supplied
	 * substrings.
	 * 
	 * @param forbiddenSubStrings
	 *            the forbidden substrings
	 */
	public ForbidSubStr(final String... forbiddenSubStrings) {
		super();
		this.forbiddenSubStrings = forbiddenSubStrings.clone();
	}
	
	/**
	 * Constructs a new <tt>ForbidSubStr</tt> processor which ensures the input doesn't contain the supplied
	 * substring, then calls the next processor in the chain.
	 * 
	 * @param forbiddenSubString
	 *            the forbidden substring
	 * @param next
	 *            the next processor in the chain
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
	 */
	public ForbidSubStr(final String[] forbiddenSubStrings, final CellProcessor next) {
		super(next);
		this.forbiddenSubStrings = forbiddenSubStrings.clone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final String sval = value.toString(); // cast
		
		// check for forbidden strings
		for( final String forbidden : forbiddenSubStrings ) {
			if( sval.indexOf(forbidden) != -1 ) {
				throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
					+ context.columnNumber + " contains the forbidden substring \"" + forbidden + "\"", context, this);
			}
		}
		
		return next.execute(value, context);
	}
}
