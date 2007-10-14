package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to an integer
 * 
 * @author Kasper B. Graversen
 */
public class ParseInt extends CellProcessorAdaptor {

	public ParseInt() {
		super();
	}

	public ParseInt(final LongCellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws NumberFormatException {
		final Integer result;
		if(value instanceof Integer)
			result = (Integer) value;

		else if(value instanceof String)
			result = new Integer((String) value);
		else
			throw new SuperCSVException("Can't convert \"" + value + "\" to integer. Input is not of type Integer nor type String but of type " + value.getClass().getName());

		return next.execute(result, context);
	}
}
