package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor checks if the input is 'null', and
 * raises an exception in that case or does nothing
 * in the other case.
 * 
 * @author Dominique De Vito (ddv36a78@yahoo.fr)
 */
public class NotNull extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {

public NotNull() {
	super();
}

public NotNull(final CellProcessor next) {
	super(next);
}
 
/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             upon receiving a 'null' value
 * @return the argument value transformed by next processors
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) {
		throw new NullInputException("Input cannot be null", context, this);
	}
	
	return next.execute(value, context);
}
}
