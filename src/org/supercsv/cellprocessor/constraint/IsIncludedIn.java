package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;
import java.util.Set;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor enforces the input value to belong to a specific set of given values.
 * <p>
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class IsIncludedIn extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
protected Set<Object> possibleValues;

public IsIncludedIn(final Set<Object> possibleValues) {
	super();
	this.possibleValues = possibleValues;
}

public IsIncludedIn(final Set<Object> possibleValues, final CellProcessor next) {
	super(next);
	this.possibleValues = possibleValues;
}

public IsIncludedIn(final Object[] possibleValues) {
	super();
	this.possibleValues = createSet(possibleValues);
}

public IsIncludedIn(final Object[] possibleValues, final CellProcessor next) {
	super(next);
	this.possibleValues = createSet(possibleValues);
}
 
private static Set<Object> createSet(Object[] arr) {
	int nb = (arr == null) ? 0 : arr.length;
	if (nb == 0) {
		return new HashSet<Object>();
	} else {
		HashSet<Object> set = new HashSet<Object>((4 * nb / 3) + 1);
		for( int i = 0; i < arr.length; i++ ) {
			set.add(arr[i]);
		}
		return set;
	}
}
/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             upon receiving a string of an un-accepted length
 * @throws ClassCastException
 *             is the parameter value cannot be cast to a String
 * @return the argument value if the value is unique
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	// check for required hash
	if( !possibleValues.contains(value) ) {
		
		throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
			+ context.columnNumber + " is not accepted as a possible value", context, this);
	}
	
	return next.execute(value, context);
}
}
