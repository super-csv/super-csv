package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.Trim;

public class TrimCellProcessorProvider extends
	AbstractCellProcessorProvider<Trim, org.supercsv.io.declarative.annotation.Trim> {
	
	public TrimCellProcessorProvider() {
		super(Trim.class, org.supercsv.io.declarative.annotation.Trim.class);
	}
	
}
