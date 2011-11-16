package org.supercsv.cellprocessor;

import java.util.Map;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Translate a value into another one, given some value mapping.
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class HashMapper extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {

private final Map<Object, Object> mapping;
private final Object defaultValue;

public HashMapper(final Map<Object, Object> mapping) {
	super();
	this.mapping = mapping;
	this.defaultValue = null;
}

public HashMapper(final Map<Object, Object> mapping, final Object defaultValue) {
	super();
	this.mapping = mapping;
	this.defaultValue = defaultValue;
}

public HashMapper(final Map<Object, Object> mapping, final BoolCellProcessor next) {
	this(mapping, null, next);
}

public HashMapper(final Map<Object, Object> mapping, final Object defaultValue, final BoolCellProcessor next) {
	super(next);
	this.mapping = mapping;
	this.defaultValue = defaultValue;
	if( mapping == null ) { throw new NullInputException("Mapping cannot be null", this); }
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	Object result = mapping.get(value);
	if( result == null ) {
		result = defaultValue;
	}
	return next.execute(result, context);
}
}
