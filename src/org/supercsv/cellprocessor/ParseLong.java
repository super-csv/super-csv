package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a long
 * 
 * @author Kasper B. Graversen
 */
public class ParseLong extends CellProcessorAdaptor {

	public ParseLong() {
		super();
	}

	public ParseLong(final LongCellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws NumberFormatException {
		final Long result;
		if(value instanceof Long)
			result = (Long) value;
		else if(value instanceof String)
			result = Long.parseLong((String) value);
		else
			throw new SuperCSVException("Can't convert \"" + value + "\" to long. Input is not of type Long nor type String but of type " + value.getClass().getName());

		return next.execute(result, context);
	}
}