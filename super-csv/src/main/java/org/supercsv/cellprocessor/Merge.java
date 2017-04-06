/*
 * Copyright 2007 Kasper B. Graversen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

/**
 * Merges multiple adjacent cells into one cell. Only use with {@code CsvListReader} or {@code CsvListWriter}.
 *
 * @author Jamie Baggott
 */
public class Merge extends CellProcessorAdaptor implements StringCellProcessor {
    private String separator;

    /**
     * Creates new {@code Merge} instance, using a blank {@code String} as separator.
     */
    public Merge() {
        this("");
    }

    /**
     * Creates new {@code Merge} instance, using a given separator as separator.
     * @param separator String to use as separator
     */
    public Merge(String separator) {
        this.separator = separator;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Object value, final CsvContext context) {
        validateInputNotNull(value, context);
        return next.execute(value + separator, context);
    }
}
