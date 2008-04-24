package org.supercsv.cellprocessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a date using the <code>SimpleDateFormat</code> class. examples of arguments to the
 * cellprocessor are: <br>
 * <code>"MM/dd/yy"</code> to parse dase such as "01/29/02" <br>
 * <code>"dd-MMM-yy"</code> <br>
 * To parse dates such as "29-Jan-02" <br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> <br>
 * To parse dates such as "2002.01.29.08.36.33" <br>
 * Or even <code>"E, dd MMM yyyy HH:mm:ss Z"</code> <br>
 * To parse "Tue, 29 Jan 2002 22:14:02 -0500"
 * 
 * @author Kasper B. Graversen
 */
public class ParseDate extends CellProcessorAdaptor {
DateFormat formatter;

public ParseDate(final String format) {
	super();
	this.formatter = new SimpleDateFormat(format);
}

public ParseDate(final String format, final DateCellProcessor next) {
	super(next);
	this.formatter = new SimpleDateFormat(format);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	try {
		final Date result = formatter.parse((String) value);
		return next.execute(result, context);
	}
	catch(final ParseException e) {
		throw new SuperCSVException("Problems parsing '" + value + "' as a date", context, e);
	}
}
}
