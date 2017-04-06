package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

/**
 * Splits a single cell into multiple cells. The cell is split on the given delimiter, and the number of resulting
 * parts comes from the given parts. Only use with {@code CsvListReader} or {@code CsvListWriter}.
 *
 * @author Jamie Baggott
 */
public class Split extends CellProcessorAdaptor implements StringCellProcessor {
    private String delimiter;
    private int parts;

    /**
     * Creates new {@code Split} instance, using the given delimiter to split on and setting parts to 0. When parts is
     * 0, there is no limit on the amount of parts this processor produces.
     * @param delimiter String to split cell on
     */
    public Split(String delimiter) {
        this(delimiter, 0);
    }

    /**
     * Creates new {@code Split} instance, using the given delimiter to split the cell on, and the given parts as the
     * amount of parts to return.
     * @param delimiter String to split cell on
     * @param parts Parts to divide cell into
     */
    public Split(String delimiter, int parts) {
        this.delimiter = delimiter;
        this.parts = parts;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object value, final CsvContext context) {
        validateInputNotNull(value, context);

        String[] cellArray;
        if (parts == 0)
            cellArray = ((String) value).split(delimiter);
        else
            cellArray = ((String) value).split(delimiter, parts);

        return next.execute(cellArray, context);
    }
}
