package org.supercsv.cellprocessor;

import java.util.regex.Pattern;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * Replaces each substring of the input string that matches the given regular expression with the given replacement.
 * 
 * @author Dominique De Vito
 * @since 1.50
 */
public class StrRegExReplace extends CellProcessorAdaptor implements StringCellProcessor {

private final Pattern regexPattern;
private final String replacement;

public StrRegExReplace(final String regex, final String replacement) {
	super();
	if( regex == null ) { throw new NullInputException("Regex cannot be null", this); }
	this.regexPattern = Pattern.compile(regex);
	this.replacement = replacement;
}

public StrRegExReplace(final String regex, final String replacement, final BoolCellProcessor next) {
	super(next);
	if( regex == null ) { throw new NullInputException("Regex cannot be null", this); }
	this.regexPattern = Pattern.compile(regex);
	this.replacement = replacement;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	String result = regexPattern.matcher((String) value).replaceAll(replacement);
	return next.execute(result, context);
}
}
