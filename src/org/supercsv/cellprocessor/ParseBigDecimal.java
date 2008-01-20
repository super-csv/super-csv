package org.supercsv.cellprocessor;

import java.math.BigDecimal;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a big decimal. It constructs BigDecimals from strings as recommended by the Javadoc (e.g.
 * <tt>new BigDecimal(0.1)</tt> yields unpredictable results, while <tt>new BidDecimal("0.1")</tt> yields
 * predictable results.
 * 
 * @author Kasper B. Graversen
 */
public class ParseBigDecimal extends CellProcessorAdaptor {

	public ParseBigDecimal() {
		super();
	}

	public ParseBigDecimal(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
		final BigDecimal result;
		if(value instanceof String) {
			try {
				result = new BigDecimal((String) value);
			}
			catch(Exception e) {
				throw new SuperCSVException("Parser error", context, e);
			}
		}
		else {
			throw new SuperCSVException("Can't convert \"" + value
					+ "\" to a BigDecimal. Input is not of type String, but of type " + value.getClass().getName(),
					context);
		}

		return next.execute(result, context);
	}
}
