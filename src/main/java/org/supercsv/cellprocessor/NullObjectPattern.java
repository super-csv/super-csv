package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * This is an implementation-specific processor and should only be used by the <tt>CellProcessorAdaptor</tt> class. It
 * is the implementation of "the null object pattern" and is always the last <tt>CellProcessor</tt> in the chain.
 * 
 * @author Kasper B. Graversen
 */
public final class NullObjectPattern extends CellProcessorAdaptor implements CellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor, BoolCellProcessor {
	
	/** The instance of the NullObjectPattern */
	protected static final NullObjectPattern INSTANCE = new NullObjectPattern();
	
	/*
	 * This processor must not be instantiated.
	 */
	private NullObjectPattern() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		return value;
	}
}
