package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that upon processing a CSV file (reading or writing), 
 * that values of the column all are unique. 
 * Comparison is based upon each elements <tt>equals()</tt> method 
 * of the objects and lookup takes O(1).
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 */
public class Unique extends CellProcessorAdaptor {

	protected HashSet<Object>	previousElements = new HashSet<Object>();

	public Unique() {
		super();
	}

	public Unique(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCSVException
	 *             upon detecting a duplicate entry
	 * @return the argument value if the value is unique
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
		if(previousElements.contains(value)) {
			throw new SuperCSVException("Duplicate entry \"" + value + "\" error", context, this);
		} else {
			previousElements.add(value);
		}
		
		// chaining
		return next.execute(value, context);
	}
}
