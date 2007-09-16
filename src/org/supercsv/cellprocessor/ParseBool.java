package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a boolean. The strings "true", "false", "0", "1", "N", "Y", "F", "T" are the only accepted
 * strings
 * 
 * @author Kasper B. Graversen
 * @since 1.0
 */
public class ParseBool extends CellProcessorAdaptor {

	public ParseBool() {
		super();
	}

	public ParseBool(final BoolCellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCSVException
	 *             when the value is some value that cannot be translated into a boolean value
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
		Boolean result;
		final String sval = ((String) value).toLowerCase();
		if(sval.equals("0") || sval.equalsIgnoreCase("false") || sval.equalsIgnoreCase("f") || sval.equalsIgnoreCase("n"))
			result = Boolean.FALSE;
		else if(sval.equals("1") || sval.equalsIgnoreCase("true") || sval.equalsIgnoreCase("t") || sval.equalsIgnoreCase("y"))
			result = Boolean.TRUE;
		else
			throw new SuperCSVException("Cannot parse \"" + value + "\" to a boolean on line " + context.lineNumber + " column "
					+ context.columnNumber);

		return next.execute(result, context);
	}
}
