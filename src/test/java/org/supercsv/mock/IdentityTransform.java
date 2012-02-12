package org.supercsv.mock;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * CellProcessor to use for unit tests that test chaining (it simply returns whatever is passed into it).
 * 
 * @author James Bassett
 */
public class IdentityTransform extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	
	/**
	 * Constructs a new <tt>IdentityTransform</tt> processor, which simply returns whatever is passed into it.
	 */
	public IdentityTransform() {
		super();
	}
	
	/**
	 * Constructs a new <tt>IdentityTransform</tt> processor which just calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public IdentityTransform(CellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(Object value, CSVContext context) {
		return next.execute(value, context);
	}
	
}
