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
package org.supercsv.io.declarative;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

/**
 * Used by {@link CsvDeclarativeBeanReader} to chain processors which are created by reflection and may not have a
 * constructor which accepts another processor.
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
class ChainableCellProcessor extends CellProcessorAdaptor {
	private CellProcessor adaptee;
	
	public ChainableCellProcessor(CellProcessor adaptee, CellProcessor next) {
		super(next);
		this.adaptee = adaptee;
	}
	
	public <T> T execute(Object value, CsvContext context) {
		return adaptee.execute(next.execute(value, context), context);
	}
}
