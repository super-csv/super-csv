package org.supercsv.cellprocessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a date to a string using the <code>SimpleDateFormat</code> class. 
 * Examples of arguments to this cellprocessor are: <br>
 * <code>"MM/dd/yy"</code> to print date such as "01/29/02" <br>
 * <code>"dd-MMM-yy"</code> <br>
 * To print dates such as "29-Jan-02" <br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> <br>
 * To print dates such as "2002.01.29.08.36.33" <br>
 * Or even <code>"E, dd MMM yyyy HH:mm:ss Z"</code> <br>
 * To print "Tue, 29 Jan 2002 22:14:02 -0500"
 * 
 * @author Dominique De Vito (ddv36a78@yahoo.fr)
 */
public class ToDate extends CellProcessorAdaptor implements DateCellProcessor {

DateFormat formatter;

public ToDate(final String format) {
	super();
	this.formatter = new SimpleDateFormat(format);
}

public ToDate(final String format, final StringCellProcessor next) {
	super(next);
	this.formatter = new SimpleDateFormat(format);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if (!(value instanceof Date)) {
		throw new ClassCastInputCSVException("the value '" + value + "' is not of type Date", context);
	}
	final String result = formatter.format((Date) value);
	return next.execute(result, context); 
}
}

