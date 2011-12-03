package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a String to a Character. If the String has a length > 1, then an Exception is thrown.
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 */
public class ParseChar extends CellProcessorAdaptor implements StringCellProcessor {
	
	/**
	 * Constructs a new <tt>ParseChar</tt> processor, which converts a String to a Character.
	 */
	public ParseChar() {
		super();
	}
	
	/**
	 * Constructs a new <tt>ParseChar</tt> processor, which converts a String to a Character, then calls the next
	 * processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseChar(final DoubleCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final Character result;
		if( value instanceof Character ) {
			result = (Character) value;
		} else if( value instanceof String ) {
			final String stringValue = (String) value;
			if( stringValue.length() == 1 ) {
				result = Character.valueOf(stringValue.charAt(0));
			} else {
				throw new SuperCSVException("Can't convert \"" + value
					+ "\" to a char. It must have a length of 1 to be a valid char.", context, this);
			}
		} else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to char. Input is not of type Character nor type String, but of type "
				+ value.getClass().getName(), context, this);
		}
		
		return next.execute(result, context);
	}
}
