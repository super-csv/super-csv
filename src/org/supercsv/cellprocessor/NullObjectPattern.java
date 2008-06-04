package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * This is an implementation-specific processor and should NOT be used by anyone other than in the implementation of
 * cell processors. It is the implementation of "the null object pattern".
 * 
 * @author Kasper B. Graversen
 */
public class NullObjectPattern extends CellProcessorAdaptor implements CellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor, BoolCellProcessor {

/** There is no need to create many instances of this class in order to fulfill the null-object pattern. */
public static final NullObjectPattern INSTANCE = new NullObjectPattern();

NullObjectPattern() {
	super();
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	return value;
}
}
