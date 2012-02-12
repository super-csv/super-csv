package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
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
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseDouble(final DoubleCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastInputCSVException
	 *             if value isn't a Double or String
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             is value can't be parsed as a Double
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final Double result;
		if( value instanceof Double ) {
			result = (Double) value;
		} else if( value instanceof String ) {
			try {
				result = new Double((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException(String.format("'%s' could not be parsed as a Double", value), context,
					this, e);
			}
		} else {
			String actualClassName = value.getClass().getName();
			throw new ClassCastInputCSVException(String.format(
				"the input value should be of type Double or String but is of type %s", actualClassName), context, this);
		}
		
		return next.execute(result, context);
	}
}
