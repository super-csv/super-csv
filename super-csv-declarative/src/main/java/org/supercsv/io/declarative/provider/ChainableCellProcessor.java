package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ChainableCellProcessor extends CellProcessorAdaptor {
	private CellProcessor adaptee;
	
	public ChainableCellProcessor(CellProcessor adaptee, CellProcessor next) {
		super(next);
		this.adaptee = adaptee;
	}
	
	public <T> T execute(Object value, CsvContext context) {
		return next.execute(adaptee.execute(value, context), context);
	}
}
