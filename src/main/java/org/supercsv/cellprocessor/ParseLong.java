package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a String to a Long.
 * 
 * @author Kasper B. Graversen
 */
public class ParseLong extends CellProcessorAdaptor implements StringCellProcessor {
	
	/**
	 * Constructs a new <tt>ParseLong</tt> processor, which converts a String to a Long.
	 */
	public ParseLong() {
		super();
	}
	
	/**
	 * Constructs a new <tt>ParseLong</tt> processor, which converts a String to a Long, then calls the next processor
	 * in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseLong(final LongCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastInputCSVException
	 *             if value isn't a Long or String
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value can't be parsed as a Long
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final Long result;
		if( value instanceof Long ) {
			result = (Long) value;
		} else if( value instanceof String ) {
			try {
				result = Long.parseLong((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException(String.format("'%s' could not be parsed as an Long", value), context, this,
					e);
			}
		} else {
			String actualClassName = value.getClass().getName();
			throw new ClassCastInputCSVException(String.format(
				"the input value should be of type Long or String but is of type %s", actualClassName), context, this);
		}
		
		return next.execute(result, context);
	}
}
