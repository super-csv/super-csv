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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.supercsv.exception.SuperCSVReflectionException;

/**
 * Provides useful utility methods for reflection.
 * 
 * @author James Bassett
 * @since 2.0.0
 */
public final class ReflectionUtils {
	
	public static final String SET_PREFIX = "set";
	public static final String GET_PREFIX = "get";
	
	/**
	 * A map of primitives and their associated wrapper classes, to cater for autoboxing.
	 */
	private static final Map<Class<?>, Class<?>> AUTOBOXING_CONVERTER = new HashMap<Class<?>, Class<?>>();
	static {
		AUTOBOXING_CONVERTER.put(long.class, Long.class);
		AUTOBOXING_CONVERTER.put(Long.class, long.class);
		AUTOBOXING_CONVERTER.put(int.class, Integer.class);
		AUTOBOXING_CONVERTER.put(Integer.class, int.class);
		AUTOBOXING_CONVERTER.put(char.class, Character.class);
		AUTOBOXING_CONVERTER.put(Character.class, char.class);
		AUTOBOXING_CONVERTER.put(byte.class, Byte.class);
		AUTOBOXING_CONVERTER.put(Byte.class, byte.class);
		AUTOBOXING_CONVERTER.put(short.class, Short.class);
		AUTOBOXING_CONVERTER.put(Short.class, short.class);
		AUTOBOXING_CONVERTER.put(boolean.class, Boolean.class);
		AUTOBOXING_CONVERTER.put(Boolean.class, boolean.class);
		AUTOBOXING_CONVERTER.put(double.class, Double.class);
		AUTOBOXING_CONVERTER.put(Double.class, double.class);
		AUTOBOXING_CONVERTER.put(float.class, Float.class);
		AUTOBOXING_CONVERTER.put(Float.class, float.class);
	}
	
	// no instantiation
	private ReflectionUtils() {
	}
	
	/**
	 * Returns the getter method associated with the object's field.
	 * 
	 * @param object
	 *            the object
	 * @param fieldName
	 *            the name of the field
	 * @return the getter method
	 * @throws NullPointerException
	 *             if object or fieldName is null
	 * @throws SuperCSVReflectionException
	 *             if the getter doesn't exist or is not visible
	 */
	public static Method findGetter(final Object object, final String fieldName) {
		if( object == null ) {
			throw new NullPointerException("object should not be null");
		} else if( fieldName == null ) {
			throw new NullPointerException("fieldName should not be null");
		}
		
		String getterName = getMethodNameForField(GET_PREFIX, fieldName);
		Class<?> clazz = object.getClass();
		try {
			return clazz.getMethod(getterName);
		}
		catch(final NoSuchMethodException e) {
			throw assembleExceptionForFindGetter(clazz, getterName, e);
		}
		catch(final SecurityException e) {
			throw assembleExceptionForFindGetter(clazz, getterName, e);
		}
		
	}
	
	/**
	 * Returns the setter method associated with the object's field.
	 * <p>
	 * This method handles any autoboxing/unboxing of the argument passed to the setter (e.g. if the setter type is a
	 * primitive {@code int} but the argument passed to the setter is an {@code Integer}) by looking for a setter with
	 * the same type, and failing that checking for a setter with the corresponding primitive/wrapper type.
	 * <p>
	 * At present, it doesn't allow for an argument type that is a subclass or implementation of the setter type (when
	 * the setter type is an {@code Object} or {@code interface} respectively).
	 * 
	 * @param object
	 *            the object
	 * @param fieldName
	 *            the name of the field
	 * @param argumentType
	 *            the type to be passed to the setter
	 * @return the setter method
	 * @throws NullPointerException
	 *             if object, fieldName or fieldType is null
	 * @throws SuperCSVReflectionException
	 *             if the setter doesn't exist or is not visible
	 */
	public static Method findSetter(final Object object, final String fieldName, final Class<?> argumentType) {
		if( object == null ) {
			throw new NullPointerException("object should not be null");
		} else if( fieldName == null ) {
			throw new NullPointerException("fieldName should not be null");
		} else if( argumentType == null ) {
			throw new NullPointerException("argumentType should not be null");
		}
		
		String setterName = getMethodNameForField(SET_PREFIX, fieldName);
		Class<?> clazz = object.getClass();
		
		try {
			return clazz.getMethod(setterName, argumentType);
		}
		catch(final NoSuchMethodException e) {
			
			if( !AUTOBOXING_CONVERTER.containsKey(argumentType) ) {
				throw assembleExceptionForFindSetter(clazz, setterName, argumentType, e);
			}
			
			// check for methods which allow autoboxing/unboxing (setter type is a primitive or its wrapper class)
			try {
				return clazz.getMethod(setterName, AUTOBOXING_CONVERTER.get(argumentType));
			}
			catch(final NoSuchMethodException e1) {
				throw assembleExceptionForFindSetter(clazz, setterName, argumentType, e1);
			}
			catch(final SecurityException e1) {
				throw assembleExceptionForFindSetter(clazz, setterName, argumentType, e1);
			}
			
		}
		catch(final SecurityException e) {
			throw assembleExceptionForFindSetter(clazz, setterName, argumentType, e);
		}
		
	}
	
	/**
	 * Assembles a generic SuperCSVReflectionException with the details of what went wrong when trying to find the
	 * getter.
	 * 
	 * @param clazz
	 *            the class on which the reflection was being performed
	 * @param getterName
	 *            the name of the getter that couldn't be located
	 * @param e
	 *            the reflection exception
	 * @return the assembled SuperCSVReflectionException
	 */
	private static SuperCSVReflectionException assembleExceptionForFindGetter(final Class<?> clazz,
		final String getterName, Throwable e) {
		return new SuperCSVReflectionException(
			String.format(
				"unable to find method %s() in class %s - check that the corresponding nameMapping element matches the field name in the bean",
				getterName, clazz.getName()), e);
	}
	
	/**
	 * Assembles a generic SuperCSVReflectionException with the details of what went wrong when trying to find the
	 * setter.
	 * 
	 * @param clazz
	 *            the class on which the reflection was being performed
	 * @param setterName
	 *            the name of the setter that couldn't be located
	 * @param argumentType
	 *            type to be passed to the setter
	 * @param e
	 *            the reflection exception
	 * @return the assembled SuperCSVReflectionException
	 */
	private static SuperCSVReflectionException assembleExceptionForFindSetter(final Class<?> clazz,
		final String setterName, final Class<?> argumentType, Throwable e) {
		return new SuperCSVReflectionException(
			String.format(
				"unable to find method %s(%s) in class %s - check that the corresponding nameMapping element matches the field name in the bean, "
					+ "and the cell processor returns a type compatible with the field", setterName,
				argumentType.getName(), clazz.getName()), e);
	}
	
	/**
	 * Gets the camelcase getter/setter method name for a field.
	 * 
	 * @param prefix
	 *            the method prefix
	 * @param fieldName
	 *            the field name
	 * @return the method name
	 */
	private static String getMethodNameForField(final String prefix, final String fieldName) {
		return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
}
