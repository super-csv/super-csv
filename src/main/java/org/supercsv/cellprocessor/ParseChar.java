package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
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
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseChar(final DoubleCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastInputCSVException
	 *             if value isn't a Character or String
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value is a String of multiple characters
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final Character result;
		if( value instanceof Character ) {
			result = (Character) value;
		} else if( value instanceof String ) {
			final String stringValue = (String) value;
			if( stringValue.length() == 1 ) {
				result = Character.valueOf(stringValue.charAt(0));
			} else {
				throw new SuperCSVException(String.format(
					"'%s' cannot be parsed as a char as it is a String longer than 1 character", stringValue), context,
					this);
			}
		} else {
			String actualClassName = value.getClass().getName();
			throw new ClassCastInputCSVException(String.format(
				"the input value should be of type Character or String but is of type %s", actualClassName), context,
				this);
		}
		
		return next.execute(result, context);
	}
}
