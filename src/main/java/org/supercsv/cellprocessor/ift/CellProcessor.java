package org.supercsv.cellprocessor.ift;

import org.supercsv.util.CSVContext;

/**
 * Defines the interface of all <tt>CellProcessor</tt>s.
 */
public interface CellProcessor {
	
	/**
	 * This method is invoked by the framework when the processor needs to process data or check constraints.
	 * 
	 * @since 1.0
	 */
	Object execute(final Object value, final CSVContext context);
}
