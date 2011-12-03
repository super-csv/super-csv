package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
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
	 */
	public ParseLong(final LongCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final Long result;
		if( value instanceof Long ) {
			result = (Long) value;
		} else if( value instanceof String ) {
			try {
				result = Long.parseLong((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException("Parser error", context, this, e);
			}
		} else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to long. Input is not of type Long nor type String but of type " + value.getClass().getName(),
				context, this);
		}
		
		return next.execute(result, context);
	}
}
