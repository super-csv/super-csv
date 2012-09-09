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
package org.supercsv.io.dozer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used internally by CsvDozerBeanReader and CsvDozerBeanWriter for Dozer mapping between CSV columns and beans. As Dozer
 * supports index-based mapping, the Reader/Writer's DozerBeanMapper just needs to be configured with the mappings
 * between the column index and the associated field in the bean.
 * 
 * @author James Bassett
 * @since 2.0.0
 */
public class CsvDozerBeanData {
	
	private List<Object> columns = new ArrayList<Object>();
	
	/**
	 * Gets the List of columns
	 * 
	 * @return the List of columns
	 */
	public List<Object> getColumns() {
		return columns;
	}
	
	/**
	 * Sets the List of columns
	 * 
	 * @param columns
	 *            the List of columns
	 */
	public void setColumns(final List<Object> columns) {
		// TODO Dozer replaces the List every damn time - is there a better way??
		this.columns = columns;
	}
	
}
