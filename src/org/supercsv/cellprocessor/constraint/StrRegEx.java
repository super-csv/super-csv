package org.supercsv.cellprocessor.constraint;

import java.util.regex.Pattern;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that the input data 
 * matches the given regular expression.
 * 
 * @author Dominique De Vito
 * @since 1.50
 */
public class StrRegEx extends CellProcessorAdaptor implements StringCellProcessor {

private final String regex;
private final Pattern regexPattern;

public StrRegEx(final String regex) {
	super();
	handleArguments(regex); 
	this.regexPattern = Pattern.compile(regex);
	this.regex = regex;
}

public StrRegEx(final String regex, final StringCellProcessor next) {
	super(next);
	handleArguments(regex); 
	this.regexPattern = Pattern.compile(regex);
	this.regex = regex;
}

private void handleArguments(final String regex)  {
	if( regex == null ) { throw new NullInputException("the regular expression cannot be null", this); }
	if( regex.equals("") ) { throw new SuperCSVException(
		"the regular expression  cannot be \"\" as this has no effect", this); }
}
/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	
	boolean found = regexPattern.matcher((String) value).find();
	if (!found) {
		throw new SuperCSVException(
			"Entry \"" + value + "\" does not respect the regular expression '" + regex + "'", context, this);
	}
	return next.execute(value, context);
}
}
