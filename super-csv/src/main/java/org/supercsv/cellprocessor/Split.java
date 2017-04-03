package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

public class Split extends CellProcessorAdaptor implements StringCellProcessor {
    private String delimiter;
    private int times;

    public Split(String delimiter) {
        this(delimiter, -1);
    }

    public Split(String delimiter, int times) {
        this.delimiter = delimiter;
        this.times = times;
    }

    public Object execute(final Object value, final CsvContext context) {
        validateInputNotNull(value, context);

        String[] cellArray;
        if (times == -1)
            cellArray = ((String) value).split(delimiter);
        else
            cellArray = ((String) value).split(delimiter, times);

        return next.execute(cellArray, context);
    }
}
