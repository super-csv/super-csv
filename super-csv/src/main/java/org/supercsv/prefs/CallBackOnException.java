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
package org.supercsv.prefs;

/**
 * Delay CellProcessor Exception callback function interface.
 *
 * @param <T>
 *
 * @author Chen Guoping
 */
public interface CallBackOnException<T> {

	/**
	 * Delay CellProcessor Exception callBack method
	 *
	 * @param rawColumns
	 *           the raw Columns from csv file
	 * @param <T>
	 * @return result value
	 */
	<T> T process(Object rawColumns);
}
