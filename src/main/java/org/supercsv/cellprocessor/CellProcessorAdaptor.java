package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * Abstract super class containing shared behaviour of all cell processors. Processors are linked together in a linked
 * list. The end element of this list should always be an instance of <tt>NullObjectPattern</tt>.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public abstract class CellProcessorAdaptor implements CellProcessor {
	
	/** the next processor in the chain */
	protected final CellProcessor next;
	
	/**
	 * Constructor used by CellProcessors to indicate that they are the last processor in the chain.
	 */
	protected CellProcessorAdaptor() {
		super();
		this.next = this instanceof NullObjectPattern ? null : NullObjectPattern.INSTANCE;
	}
	
	/**
	 * Constructor used by CellProcessors that require <tt>CellProcessor</tt> chaining (further processing is required).
	 * 
	 * @param next
	 *            the next <tt>CellProcessor</tt> in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	protected CellProcessorAdaptor(final CellProcessor next) {
		super();
		if( next == null ) {
			throw new NullPointerException("next CellProcessor should not be null");
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
	 * @throws NullInputException
	 *             if value is null
	 * @since 1.6.0
	 */
	protected final void validateInputNotNull(final Object value, final CSVContext context) {
		if( value == null ) {
			throw new NullInputException("this processor does not accept null input", context, this);
		}
	}
	
	/**
	 * Returns the CellProccessor's fully qualified class name.
	 */
	@Override
	public String toString() {
		return getClass().getName();
	}
	
}
