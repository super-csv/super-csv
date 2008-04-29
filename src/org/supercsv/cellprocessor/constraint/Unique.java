package org.supercsv.cellprocessor.constraint;

import java.util.HashMap;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that upon processing a CSV file (reading or writing), that values of the column are all unique. Comparison is
 * based upon each elements <tt>hashCode()</tt> method and if a collision, then <tt>equals()</tt> is used. Lookup
 * takes O(1) and each object queried for uniqueness is stored in memory.
 * 
 * @author Kasper B. Graversen
 */
public class Unique extends CellProcessorAdaptor {
final static Object tokenForMap = new Object();
protected HashMap<Integer, Object> uniqueMap = new HashMap<Integer, Object>();

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
	// check for uniqueness
	final int hash = value.hashCode();
	if( uniqueMap.containsKey(hash) ) { throw new SuperCSVException("Duplicate entry \"" + value + "\" found!", this); }
	
	// if not found add it
	uniqueMap.put(hash, tokenForMap);
	
	// chaining
	return next.execute(value, context);
}
}
