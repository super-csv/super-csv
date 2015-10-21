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
package org.supercsv.util;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * <p>A holder for the name of the field in a bean (which is to be instantiated from CSV row via CsvBeanReader)
 * and optional CellProcessor instance (which can be used to process CSV data before populating the
 * field).</p>
 *
 * <p>Note that <tt>equals()</tt> and <tt>hashCode()</tt> methods are based
 * on field name value and cell processor <b>identity</b> (unless cell processor implementation overrides
 * <tt>equals()</tt> and <tt>hashCode()</tt>, which is not the case for built-in cell processors).
 * Therefore the following code will return false.</p>
 *
 * <pre>
 * {@code map.put("columnName", BeanField.of("fieldName", new ParseDate("dd/MM/yyyy"));
 *  return map.containsValue(BeanField.of("fieldName", new ParseDate("dd/MM/yyyy"));}
 * </pre>
 *
 * @author Vyacheslav Pushkin
 *
 */
public class BeanField {
	private final String fieldName;
	private final CellProcessor cellProcessor;

	private BeanField(final String fieldName, final CellProcessor cellProcessor) {
		this.fieldName = fieldName;
		this.cellProcessor = cellProcessor;
	}

	/**
	 * Returns a BeanField instance created from supplied field name and cell processor.
	 *
	 * @param fieldName bean field name
	 * @param cellProcessor cell processor (can be null)
	 * @return BeanField instance created from supplied field name and cell processor
	 */
	public static BeanField of(final String fieldName, final CellProcessor cellProcessor) {
		return new BeanField(fieldName, cellProcessor);
	}

	/**
	 * @see BeanField#of(String, CellProcessor)
	 *
	 * @param fieldName bean field name
	 * @return BeanField instance created from supplied field name and cell processor
	 *
	 */
	public static BeanField of(final String fieldName) {
		return of(fieldName, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellProcessor == null) ? 0 : cellProcessor.hashCode());
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( !(obj instanceof BeanField) )
			return false;
		BeanField other = (BeanField) obj;
		if( cellProcessor == null ) {
			if( other.cellProcessor != null )
				return false;
		} else if( !cellProcessor.equals(other.cellProcessor) )
			return false;
		if( fieldName == null ) {
			if( other.fieldName != null )
				return false;
		} else if( !fieldName.equals(other.fieldName) )
			return false;
		return true;
	}

	/**
	 * Returns bean field name 
	 * 
	 * @return bean field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Returns cell processor
	 * 
	 * @return cell processor
	 */
	public CellProcessor getCellProcessor() {
		return cellProcessor;
	}

}
