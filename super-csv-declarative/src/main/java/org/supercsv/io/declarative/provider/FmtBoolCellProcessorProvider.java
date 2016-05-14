package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.declarative.annotation.FmtBool;

public class FmtBoolCellProcessorProvider implements CellProcessorProvider<FmtBool> {
	
	public CellProcessor create(FmtBool annotation) {
		return new org.supercsv.cellprocessor.FmtBool(annotation.trueValue(), annotation.falseValue());
	}
	
	public Class<FmtBool> getType() {
		return FmtBool.class;
	}
	
}
