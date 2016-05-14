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
import org.supercsv.io.declarative.annotation.CsvKeyValueMapper;
import org.supercsv.io.declarative.annotation.Map;
import org.supercsv.util.CsvContext;

/**
 * Uses a {@link CsvKeyValueMapper} to map from one object to another, with the input as the key. Shouldn't be used
 * directly since it is designed for use with the {@link Map}-annotation (you should just implement your own
 * CellProcessor in direct-use scenarios)
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class Mapper extends CellProcessorAdaptor {
	private CsvKeyValueMapper mapper;
	
	/**
	 * Constructs a new <tt>Mapper</tt> processor, which maps from one object to another.
	 * 
	 * @param mapper
	 *            the mapper used to do the actual mapping
	 * @throws NullPointerException
	 *             if mapping or next is null
	 * @throws IllegalArgumentException
	 *             if mapping is empty
	 */
	public Mapper(CsvKeyValueMapper mapper) {
		this.mapper = mapper;
	}
	
	/**
	 * Constructs a new <tt>Mapper</tt> processor, which maps from one object to another.
	 * 
	 * @param mapper
	 *            the mapper used to do the actual mapping
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if mapping or next is null
	 * @throws IllegalArgumentException
	 *             if mapping is empty
	 */
	public Mapper(CsvKeyValueMapper mapper, CellProcessor next) {
		super(next);
		this.mapper = mapper;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <T> T execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);
		
		Object mappedValue = mapper.map(value);
		
		return next.execute(mappedValue, context);
	}
	
}
