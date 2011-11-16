package org.supercsv.cellprocessor;

import java.text.DecimalFormat;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a double into a formatted string using the {@link DecimalFormat} class. This is useful, when you need to
 * show numbers with a specific number of digits.
 * <p>
 * In the format string, the following characters are defined as : <br>
 * 
 * <pre>
 * 0   - means Digit
 * #   - means Digit, zero shows as absent (works only as zero padding on the right hand side of the number)
 * .   - means Decimal separator or monetary decimal separator
 * -   - means Minus sign
 * ,   - means Grouping separator
 * </pre>
 * 
 * <br>
 * If you want to convert from a String to a decimal, use the {@link ParseDouble} or {@link ParseBigDecimal} processor.
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 */
public class FmtNumber extends CellProcessorAdaptor implements DoubleCellProcessor, LongCellProcessor {

protected DecimalFormat formatter;

public FmtNumber(final String format) {
	this(new DecimalFormat(format));
}

public FmtNumber(final String format, final StringCellProcessor next) {
	this(new DecimalFormat(format),next);
}

public FmtNumber(final DecimalFormat formatter) {
	super();
	this.formatter = formatter;
}

public FmtNumber(final DecimalFormat formatter, final StringCellProcessor next) {
	super(next);
	this.formatter = formatter;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " column " + context.columnNumber, context, this); }
	
	if( !(value instanceof Number) ) { throw new ClassCastInputCSVException("the value '" + value
		+ "' is not of type Number", context, this); }
	
	final String result = formatter.format(value);
	return next.execute(result, context);
}
}
