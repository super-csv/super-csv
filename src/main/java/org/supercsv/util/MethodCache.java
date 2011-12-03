package org.supercsv.util;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.supercsv.exception.SuperCSVException;
import org.supercsv.exception.SuperCSVReflectionException;

import org.supercsv.util.ThreeDHashMap;
import org.supercsv.util.TwoDHashMap;

/**
 * This class cache's method lookups. Hence first time it introspects the instance's class, while subsequent method
 * lookups are super fast.
 */
public class MethodCache {
	
	/**
	 * A map of primitives and their associated wrapper classes, to cater for autoboxing.
	 */
	private static final HashMap<Class<?>, Class<?>> AUTOBOXING_CONVERTER = new HashMap<Class<?>, Class<?>>();
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
	
	/**
	 * A cache of setter methods. The three keys are the class the setter is being invoked on, the parameter type of the
	 * setter, and the variable name. The value is the setter method.
	 */
	private final ThreeDHashMap<Class<?>, Class<?>, String, Method> setMethodsCache = new ThreeDHashMap<Class<?>, Class<?>, String, Method>();
	
	/**
	 * A cache of getter methods. The two keys are the name of the class the getter is being invoked on, and the
	 * variable name. The value is the getter method.
	 */
	private final TwoDHashMap<String, String, Method> getCache = new TwoDHashMap<String, String, Method>();
	
	/**
	 * Returns the getter method for field on an object.
	 * 
	 * @param object
	 *            the object
	 * @param fieldName
	 *            the field name
	 * @return the getter associated with the field on the object
	 */
	public Method getGetMethod(final Object object, final String fieldName) {
		return getMethod(getCache, object, "get", fieldName);
	}
	
	/**
	 * Returns the setter method for the field on an object.
	 * 
	 * @param <T>
	 * @param object
	 *            the object
	 * @param fieldName
	 *            the field name
	 * @param fieldType
	 *            the field type
	 * @return the setter method associated with the field on the object
	 */
	public <T> Method getSetMethod(final Object object, final String fieldName, final Class<?> fieldType) {
		Method method = setMethodsCache.get(object.getClass(), fieldType, fieldName);
		if( method == null ) {
			// we don't know the destination type for the set method, just use whatever we can find
			if( fieldType == null ) {
				method = findSetMethodWithNonPrimitiveParameter(object, fieldName);
			} else {
				method = inspectClassForSetMethods(object, fieldType, fieldName);
			}
			setMethodsCache.set(object.getClass(), fieldType, fieldName, method);
		}
		return method;
	}
	
	/**
	 * Returns the method associated with the object's field from the cache.
	 * 
	 * @param cache
	 *            the cache of getter methods
	 * @param object
	 *            the object
	 * @param methodPrefix
	 *            the method prefix
	 * @param fieldName
	 *            the name of the field
	 * @return the method
	 */
	public Method getMethod(final TwoDHashMap<String, String, Method> cache, final Object object,
		final String methodPrefix, final String fieldName) {
		Method method = cache.get(object.getClass().getName(), fieldName);
		if( method == null ) {
			method = inspectClass(object, methodPrefix, fieldName, 0);
			cache.set(object.getClass().getName(), fieldName, method);
		}
		return method;
	}
	
	/**
	 * Inspects a class for a method.
	 * 
	 * @param object
	 *            the object on which to call the method
	 * @param methodPrefix
	 *            the method prefix
	 * @param fieldName
	 *            the field name associated with the method
	 * @param requiredNumberOfArgs
	 *            the number of arguments the method must have
	 * @return the Method
	 */
	private Method inspectClass(final Object object, final String methodPrefix, final String fieldName,
		final int requiredNumberOfArgs) {
		final String methodName = methodPrefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		// find method by traversal of the object
		for( final Method meth : object.getClass().getMethods() ) {
			if( meth.getName().equals(methodName) //
				&& meth.getParameterTypes().length == requiredNumberOfArgs ) {
				return meth;
			}
		}
		throw new SuperCSVReflectionException(String.format(//
			"Can't find method '%s' in class '%s'", methodName, object.getClass().getName()));
	}
	
	/**
	 * Finds a setter method with a non-primitive parameter.
	 * 
	 * @param object
	 *            the object to inspect
	 * @param fieldName
	 *            the field name
	 * @return the setter Method
	 */
	private Method findSetMethodWithNonPrimitiveParameter(final Object object, final String fieldName) {
		final String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		// find method by traversal of the object
		for( final Method meth : object.getClass().getMethods() ) {
			if( meth.getName().equals(methodName) //
				&& meth.getParameterTypes().length == 1 //
				&& !meth.getParameterTypes()[0].isPrimitive() ) {
				return meth;
			}
		}
		throw new SuperCSVReflectionException(String.format(//
			"Can't find method '%s' in class '%s'", methodName, object.getClass().getName()));
	}
	
	/**
	 * Inspects a class for a setter method.
	 * 
	 * @param object
	 *            the object to inspect
	 * @param fieldType
	 *            the field type of the setter
	 * @param fieldName
	 *            the field name
	 * @return the setter Method
	 */
	private Method inspectClassForSetMethods(final Object object, final Class<?> fieldType, final String fieldName) {
		final String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		try {
			return object.getClass().getMethod(methodName, fieldType);
		}
		catch(final SecurityException e) {
			throwException(object, fieldType, methodName, e);
		}
		catch(final NoSuchMethodException e) {
			// retry again due to autoboxing in java we need to try both cases
			try {
				if( !AUTOBOXING_CONVERTER.containsKey(fieldType) ) {
					throwException(object, fieldType, methodName, e);
				}
				return object.getClass().getMethod(methodName, AUTOBOXING_CONVERTER.get(fieldType));
			}
			catch(final SecurityException e1) {
				throwException(object, fieldType, methodName, e1);
			}
			catch(final NoSuchMethodException e1) {
				throwException(object, fieldType, methodName, e1);
			}
		}
		throw new SuperCSVException("This can never happen!");
	}
	
	/**
	 * Throws a SuperCSVReflectionException with the relevant details.
	 * 
	 * @param object
	 *            the object
	 * @param fieldType
	 *            the field type
	 * @param methodName
	 *            the method name
	 * @param e
	 *            the exception
	 * @throws SuperCSVReflectionException
	 */
	private void throwException(final Object object, final Class<?> fieldType, final String methodName,
		final Exception e) throws SuperCSVReflectionException {
		throw new SuperCSVReflectionException(String.format("Can't find method '%s(%s)' in class '%s'. "
			+ "Is the name correctly spelled in the NameMapping? "
			+ "Have you forgot to convert the data so that a wrong set method is called?", methodName, fieldType,
			object.getClass().getName()), e);
	}
}
