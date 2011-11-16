package org.supercsv.mock;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen This is a mock-object to ensure that the outer processor remembers to call its chained
 *         processor
 */
public class ComparerCellProcessor extends CellProcessorAdaptor implements DoubleCellProcessor, BoolCellProcessor,
	DateCellProcessor, LongCellProcessor, StringCellProcessor {
Object expectedValue;

public ComparerCellProcessor(final Object expectedValue) {
	super();
	this.expectedValue = expectedValue;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	final boolean res = value.equals(expectedValue);
	if( res == false ) { throw new RuntimeException("expected '" + expectedValue + "' got '" + value + "'"); }
	return true;
}
}
