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
 * This processor returns <tt>null</tt> if it encounters empty String (""). It is a simple customisation of
 * <tt>Token</tt>. If you need to return values other than <tt>null</tt>, use {@link Token} instead.
 * 
 * @author Kasper B. Graversen
 */
public class Optional extends Token {
	
	/**
	 * Constructs a new <tt>Optional</tt> processor, which when encountering empty String ("") will return <tt>null</tt>
	 * , for all other values it will return the value unchanged.
	 */
	public Optional() {
		super("", null);
	}
	
	/**
	 * Constructs a new <tt>Optional</tt> processor, which when encountering empty String ("") will return <tt>null</tt>
	 * , for all other values it will call the next processor in the chain.
	 * 
	 * @throws NullPointerException
	 *             if next is null
	 */
	public Optional(final CellProcessor next) {
		super("", null, next);
	}
}
