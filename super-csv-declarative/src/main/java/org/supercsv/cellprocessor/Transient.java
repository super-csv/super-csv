package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class Transient extends CellProcessorAdaptor {
	public Transient() {
	}
	
	public Transient(final CellProcessor next) {
		super(next);
	}
	
	public <T> T execute(Object value, CsvContext context) {
		return next.execute(value, context);
	}

}
