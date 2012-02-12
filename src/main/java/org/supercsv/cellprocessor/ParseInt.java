package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
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
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseInt(final LongCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastInputCSVException
	 *             if value isn't an Integer or String
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value can't be parsed as an Integer
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final Integer result;
		if( value instanceof Integer ) {
			result = (Integer) value;
		} else if( value instanceof String ) {
			try {
				result = Integer.valueOf((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException(String.format("'%s' could not be parsed as an Integer", value), context,
					this, e);
			}
		} else {
			String actualClassName = value.getClass().getName();
			throw new ClassCastInputCSVException(String.format(
				"the input value should be of type Integer or String but is of type %s", actualClassName), context,
				this);
		}
		
		return next.execute(result, context);
	}
}
