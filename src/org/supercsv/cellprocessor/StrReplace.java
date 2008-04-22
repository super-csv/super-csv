package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

public class StrReplace extends CellProcessorAdaptor {
private String searchText, replaceText;

public StrReplace(String searchText, String replaceText) {
	super();
	handleArguments(searchText, replaceText);
}

public StrReplace(String searchText, String replaceText, StringCellProcessor next) {
	super(next);
	handleArguments(searchText, replaceText);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	return value.toString().replaceAll(searchText, replaceText);
}

private void handleArguments(String searchText, String replaceText) throws IllegalArgumentException {
	if( searchText == null ) { throw new SuperCSVException("argument searchText cannot be null"); }
	if( replaceText == null ) { throw new SuperCSVException("argument replaceText cannot be null"); }
	if( searchText.equals("") ) { throw new SuperCSVException(
		"argument searchText cannot be \"\" as this has no effect"); }
	this.searchText = searchText;
	this.replaceText = replaceText;
}
}
