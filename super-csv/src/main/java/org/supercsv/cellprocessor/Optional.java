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

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This processor is used to indicate that a cell is optional, and will avoid executing further processors if it
 * encounters <code>null</code>. It is a simple customization of <code>ConvertNullTo</code>.
 * <p>
 * Prior to version 2.0.0, this processor returned <code>null</code> for empty String (""), but was updated because
 * Tokenizer now reads empty columns as <code>null</code>. It also means that Optional can now be used when writing as well
 * (instead of using {@code ConvertNullTo("")}).
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class Optional extends ConvertNullTo {
	
	/**
	 * Constructs a new <code>Optional</code> processor, which when encountering <code>null</code> will return <code>null</code>,
	 * for all other values it will return the value unchanged.
	 */
	public Optional() {
		super(null);
	}
	
	/**
	 * Constructs a new <code>Optional</code> processor, which when encountering <code>null</code> will return <code>null</code> ,
	 * for all other values it will call the next processor in the chain.
	 * 
	 * @param next
	 *            the next CellProcessor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public Optional(final CellProcessor next) {
		super(null, next);
	}
	
}
