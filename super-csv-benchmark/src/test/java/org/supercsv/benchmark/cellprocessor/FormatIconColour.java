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
package org.supercsv.benchmark.cellprocessor;

import org.supercsv.benchmark.model.IconColour;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

/**
 * Cell processor to format an icon colour for writing.
 * 
 * @author James Bassett
 */
public class FormatIconColour extends CellProcessorAdaptor {

	/**
	 * Constructs a new FormatIconColour processor.
	 */
	public FormatIconColour() {
		super();
	}

	/**
	 * Constructs a new FormatIconColour processor.
	 */
	public FormatIconColour(CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object execute(Object value, CsvContext context) {

		validateInputNotNull(value, context);

		IconColour colour = (IconColour) value;
		return next.execute(colour.name().toLowerCase(), context);
	}

}
