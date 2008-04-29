package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that upon processing a CSV file (reading or writing), 
 * that values of the column are all unique. 
 * Comparison is based upon each elements <tt>hashCode()</tt> method 
 * of the objects and lookup takes O(1).
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 */
public class UniqueHashCode extends CellProcessorAdaptor {
protected HashSet<Integer> uniqueSet = new HashSet<Integer>();

public UniqueHashCode() {
	super();
}

public UniqueHashCode(final CellProcessor next) {
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
	// check for uniqueness
	final int hash = value.hashCode();
	if( uniqueSet.contains(hash) ) { 
		throw new SuperCSVException("Duplicate entry \"" + value + "\" found with same hash code!", 
				context, this);
	}
	
	// if not found add it
	uniqueSet.add(hash);
	
	// chaining
	return next.execute(value, context);
}
}
