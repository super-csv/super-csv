package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * Abstract super class containing shared behaviour of all cell processors. Processors are linked together in a linked
 * list. The end element of this list should always be an instance of <tt>NullObjectPattern</tt>.
 * 
 * @author Kasper B. Graversen
 */
public abstract class CellProcessorAdaptor implements CellProcessor {
	
	/** the next processor in the chain */
	protected CellProcessor next = null; // must be untyped as it must hold any kind of type
	
	/**
	 * Constructor used by CellProcessors to indicate that they are the last processor in the chain.
	 */
	protected CellProcessorAdaptor() {
		super();
		if( !(this instanceof NullObjectPattern) ) {
			next = NullObjectPattern.INSTANCE;
		}
	}
	
	/**
	 * Constructor used by CellProcessors that require <tt>CellProcessor</tt> chaining (further processing is
	 * required).
	 * 
	 * @param next
	 *            the next <tt>CellProcessor</tt> in the chain
	 */
	protected CellProcessorAdaptor(final CellProcessor next) {
		super();
		if( next == null ) {
			throw new NullInputException("next CellProcessor in the chain cannot be null", this);
		}
		
		this.next = next;
	}
	
	/**
	 * Checks that the input value is not <tt>null</tt>, throwing a <tt>NullInputException</tt> if it is. This method
	 * should be called by all processors that need to ensure the input is not <tt>null</tt>.
	 * 
	 * @param value
	 *            the input value
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the processor being executed
	 * @since 1.6.0
	 */
	protected static void validateInputNotNull(final Object value, final CSVContext context, final CellProcessor processor) {
		if( value == null ) {
			throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column "
				+ context.columnNumber, context, processor);
		}
	}
	
}
