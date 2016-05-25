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
package org.supercsv.mock;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

/**
 * CellProcessor to use for unit tests that test chaining (it simply returns whatever is passed into it).
 * 
 * @author James Bassett
 */
public class IdentityTransform extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	
	/**
	 * Constructs a new <tt>IdentityTransform</tt> processor, which simply returns whatever is passed into it.
	 */
	public IdentityTransform() {
		super();
	}
	
	/**
	 * Constructs a new <tt>IdentityTransform</tt> processor which just calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public IdentityTransform(CellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(Object value, CsvContext context) {
		return next.execute(value, context);
	}
	
}
