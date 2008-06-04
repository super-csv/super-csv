package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * Abstract super class for sharing behavior of all cell processors. Processors are linked together in a linked list.
 * The end element of this list should always be an instance of <tt>NullObjectPattern</tt>. T denotes the type of
 * information the cellprocessor is currently carrying.
 * 
 * @author Kasper B. Graversen
 */
public abstract class CellProcessorAdaptor implements CellProcessor {
/** the next reference for the chain */
protected CellProcessor next = null; // must be untyped as it must hold any kind of type

/** This constructor MUST ONLY be used by the class <tt>NullObjectPattern</tt> */
protected CellProcessorAdaptor() {
	super();
	if( !(this instanceof NullObjectPattern) ) {
		next = NullObjectPattern.INSTANCE;
	}
}

/**
 * General constructor for all processors to call to get them properly registered
 * 
 * @param next
 */
protected CellProcessorAdaptor(final CellProcessor next) {
	super();
	if( next == null ) {
		throw new NullInputException("argument was null", this);
	}
	
	this.next = next;
}

/**
 * This method is invoked by the framework when the processor needs to process data or check constraints.
 * 
 * @since 1.0
 */
public abstract Object execute(final Object value, CSVContext context);
}
