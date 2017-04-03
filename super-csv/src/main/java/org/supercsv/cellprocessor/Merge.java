package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

public class Merge extends CellProcessorAdaptor implements StringCellProcessor {
    private String separator;

    public Merge(String separator) {
        this.separator = separator;
    }

    public Object execute(final Object value, final CsvContext context) {
        validateInputNotNull(value, context);
        return next.execute(value + separator, context);
    }
}
