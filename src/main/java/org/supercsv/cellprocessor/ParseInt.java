package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a String to an Integer.
 * 
 * @author Kasper B. Graversen
 */
public class ParseInt extends CellProcessorAdaptor implements StringCellProcessor {
	
	/**
	 * Constructs a new <tt>ParseInt</tt> processor, which converts a String to an Integer.
	 */
	public ParseInt() {
		super();
	}
	
	/**
	 * Constructs a new <tt>ParseInt</tt> processor, which converts a String to an Integer, then calls the next
	 * processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseInt(final LongCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		final Integer result;
		if( value instanceof Integer ) {
			result = (Integer) value;
		} else if( value instanceof String ) {
			try {
				result = Integer.valueOf((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException("Parser error", context, this, e);
			}
		} else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to integer. Input is not of type Integer nor type String but of type "
				+ value.getClass().getName(), context, this);
		}
		
		return next.execute(result, context);
	}
}
