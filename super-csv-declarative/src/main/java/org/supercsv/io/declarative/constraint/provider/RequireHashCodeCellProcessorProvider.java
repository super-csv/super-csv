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
package org.supercsv.io.declarative.constraint.provider;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.declarative.constraint.annotation.RequireHashCode;
import org.supercsv.io.declarative.provider.CellProcessorProvider;

/**
 * CellProcessorProvider for RequireHashCode
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class RequireHashCodeCellProcessorProvider implements CellProcessorProvider<RequireHashCode> {
	
	/**
	 * {@inheritDoc}
	 */
	public CellProcessor create(RequireHashCode annotation, CellProcessor next) {
		return new org.supercsv.cellprocessor.constraint.RequireHashCode(annotation.requiredHashCodes(), next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Class<RequireHashCode> getType() {
		return RequireHashCode.class;
	}
	
}
