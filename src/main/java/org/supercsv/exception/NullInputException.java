package org.supercsv.exception;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * Wraps the NullPouinterException to ensure only SuperCSVException's are thrown from cell processors.
 * <p>
 * 
 * @since 1.50
 * @author Kasper B. Graversen, (c) 2007
 * @author Dominique de Vito
 */
public class NullInputException extends SuperCSVException {
private static final long serialVersionUID = 1L;

public NullInputException(final String msg) {
	super(msg);
}

public NullInputException(final String msg, final CellProcessor offendingProcessor, final Throwable t) {
	super(msg, null, offendingProcessor, t);
}

public NullInputException(final String msg, final CSVContext context, final Throwable t) {
	super(msg, context, null, t);
}

public NullInputException(final String msg, final CSVContext context, final CellProcessor offendingProcessor) {
	super(msg, context, offendingProcessor);
}

public NullInputException(final String msg, final CellProcessor offendingProcessor) {
	super(msg, offendingProcessor);
}

}
