package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * String replacer.
 * @author Kasper B. Graversen
 * @author Dominique De Vito (ddv36a78@yahoo.fr)
 */
public class StrReplace extends CellProcessorAdaptor implements BoolCellProcessor,
DateCellProcessor, DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
private String searchText, replaceText;

/**
 * String replace
 * 
 * @param searchText
 *            text to search for
 * @param replaceText
 *            text to replace with
 */
public StrReplace(final String searchText, final String replaceText) {
	super();
	handleArguments(searchText, replaceText);
}

public StrReplace(final String searchText, final String replaceText, final StringCellProcessor next) {
	super(next);
	handleArguments(searchText, replaceText);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if (value == null) {
		throw new NullInputException("Input cannot be null", context, this); 
	}
	return value.toString().replaceAll(searchText, replaceText);
}

private void handleArguments(final String searchText, final String replaceText) throws IllegalArgumentException {
	if( searchText == null ) { throw new NullInputException("searchtext cannot be null", this); }
	if( replaceText == null ) { throw new NullInputException("replacettext cannot be null", this); }
	if( searchText.equals("") ) { throw new SuperCSVException(
		"argument searchText cannot be \"\" as this has no effect", this); }
	this.searchText = searchText;
	this.replaceText = replaceText;
}
}
