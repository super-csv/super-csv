package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.declarative.annotation.FmtDate;

public class FmtDateCellProcessorProvider implements CellProcessorProvider<FmtDate> {
	
	public CellProcessor create(FmtDate annotation) {
		return new org.supercsv.cellprocessor.FmtDate(annotation.format());
	}
	
	public Class<FmtDate> getType() {
		return FmtDate.class;
	}
	
}
