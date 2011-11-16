package org.supercsv.cellprocessor;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a big decimal. It constructs BigDecimals from strings as recommended by the Javadoc (e.g.
 * <tt>new BigDecimal(0.1)</tt> yields unpredictable results, while <tt>new BidDecimal("0.1")</tt> yields
 * predictable results.
 * 
 * @since 1.30
 * @author Kasper B. Graversen
 */
public class ParseBigDecimal extends CellProcessorAdaptor implements StringCellProcessor {

private DecimalFormatSymbols symbols;

public ParseBigDecimal() {
    this((DecimalFormatSymbols)null);
}

public ParseBigDecimal(DecimalFormatSymbols symbols) {
    super();
    this.symbols = symbols;
}

public ParseBigDecimal(final CellProcessor next) {
    this(null,next);
}

public ParseBigDecimal(DecimalFormatSymbols symbols, final CellProcessor next) {
    super(next);
    this.symbols = symbols;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	final BigDecimal result;
	if( value instanceof String ) {
		try {
            if (symbols == null) {
                result = new BigDecimal((String) value);
            } else {
                if (symbols.getDecimalSeparator() != '.') {
                    String s = (String) value;
                    result = new BigDecimal(s.replace(symbols.getDecimalSeparator(), '.'));
                } else {
                    result = new BigDecimal((String) value);
                }
            }
        }
		catch(final Exception e) {
			throw new SuperCSVException("Parser error", context, this, e);
		}
	} else {
		throw new SuperCSVException("Can't convert \"" + value
			+ "\" to a BigDecimal. Input is not of type String, but of type " + value.getClass().getName(), context,
			this);
	}
	
	return next.execute(result, context);
}
}
