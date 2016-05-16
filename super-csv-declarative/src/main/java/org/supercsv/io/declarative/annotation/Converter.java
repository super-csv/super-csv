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
package org.supercsv.io.declarative.annotation;

/**
 * Implement this interface and annotate a field with {@link Convert} for custom mappings
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public interface Converter {
	/**
	 * Maps a csv-value to another value
	 * 
	 * @param key
	 *            a value from csv (possibly already transformed by other processors)
	 * @return a replacement for the passed value
	 */
	Object convert(Object key);
}
