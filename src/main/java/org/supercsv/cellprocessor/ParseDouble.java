package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a String to a Double.
 * 
 * @author Kasper B. Graversen
 */
public class ParseDouble extends CellProcessorAdaptor implements StringCellProcessor {
	
	/**
	 * Constructs a new <tt>ParseDouble</tt> processor, which converts a String to a Double.
	 */
	public ParseDouble() {
		super();
	}
	
	/**
	 * Constructs a new <tt>ParseDouble</tt> processor, which converts a String to a Double, then calls the next
	 * processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseDouble(final DoubleCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		final Double result;
		if( value instanceof Double ) {
			result = (Double) value;
		} else if( value instanceof String ) {
			try {
				result = new Double((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException("Parser error", context, this, e);
			}
		} else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to double. Input is not of type Double nor type String, but of type "
				+ value.getClass().getName(), context, this);
		}
		
		return next.execute(result, context);
	}
}
