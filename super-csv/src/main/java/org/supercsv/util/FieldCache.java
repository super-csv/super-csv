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

import java.lang.reflect.Field;

import org.supercsv.exception.SuperCsvReflectionException;

/**
 * This class cache's field lookups. Hence first time it introspects the instance's class, while subsequent field
 * lookups are super fast.
 */
public class FieldCache {
	
	/**
	 * A cache of fields. The two keys are the name of the class and the variable name. The value is the field.
	 */
	private final TwoDHashMap<String, String, Field> cache = new TwoDHashMap<String, String, Field>();
	
	/**
	 * Get the field in object by field name
	 *
	 * @param object
	 *             the object
	 * @param fieldName
	 *             the field name
	 * @return the field which name is same as field name
	 * @throws NullPointerException
	 *             if the object or fieldName is null
	 * @throws SuperCsvReflectionException
	 *             if field doesn't exist
	 */
	public Field getField(final  Object object, final String fieldName) {
		if( object == null ) {
			throw new NullPointerException("object should not be null");
		} else if( fieldName == null ) {
			throw new NullPointerException("fieldName should not be null");
		}

		Field field = cache.get(object.getClass().getName(), fieldName);
		if( field == null ){
			field = ReflectionUtils.findField(object, fieldName);
			cache.set(object.getClass().getName(), fieldName, field);
		}
		return field;
	}
}
