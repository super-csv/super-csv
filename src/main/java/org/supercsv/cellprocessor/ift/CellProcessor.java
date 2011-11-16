package org.supercsv.cellprocessor.ift;

import org.supercsv.util.CSVContext;

public interface CellProcessor {
public abstract Object execute(final Object value, final CSVContext context);
}
