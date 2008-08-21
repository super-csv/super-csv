package org.supercsv.util;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.supercsv.exception.SuperCSVException;
import org.supercsv.exception.SuperCSVReflectionException;

import spiffy.core.util.HashMapBuilder;
import spiffy.core.util.ThreeDHashMap;
import spiffy.core.util.TwoDHashMap;

/**
 * This class cache's method lookup. Hence first time it introspects the instance's class, while subsequent method
 * lookups are super fast.
 */
public class MethodCache {
/**
 * defines a lookup between classes and what class signature they may match due to the autoboxing feature in java
 */
private final HashMap<Class, Class> autoboxingConverter = new HashMapBuilder<Class, Class>()//
	.add(long.class, Long.class)//
	.add(Long.class, long.class)//
	.add(int.class, Integer.class)//
	.add(Integer.class, int.class)//
	.add(char.class, Character.class)//
	.add(Character.class, char.class)//
	.add(byte.class, Byte.class)//
	.add(Byte.class, byte.class)//
	.add(short.class, Short.class)//
	.add(Short.class, short.class)//
	.add(boolean.class, Boolean.class)//
	.add(Boolean.class, boolean.class)//
	.add(double.class, Double.class)//
	.add(Double.class, double.class)//
	.add(float.class, Float.class)//
	.add(Float.class, float.class)//
	.build();

/**
 * A map containing mapping "classname -> HashMap". The inner HashMap is a "methodname->Method" mapping
 */
private final ThreeDHashMap<Class /* class */, Class /* type */, String /* variableName */, Method> setMethodsCache = new ThreeDHashMap<Class, Class, String, Method>();

/**
 * A map containing mapping "classname -> HashMap". The inner HashMap is a "methodname->Method" mapping
 */
// private final HashMap<String, HashMap<String, Method>> getCache = new HashMap<String, HashMap<String, Method>>();
private final TwoDHashMap<String, String, Method> getCache = new TwoDHashMap<String, String, Method>();

/** Given an instance and a variable name, return the given method */
public Method getGetMethod(final Object destinationObject, final String variableName) {
	return getMethod(getCache, destinationObject, "get", variableName);
}

/**
 * using either get or set cache lookup a method. This approach saves a subString(), concatenations and toUpperCase()
 * since now the variable name uniquely identify either get or set method access
 */
// Method getMethod(final HashMap<String, HashMap<String, Method>> cache, final Object destinationObject,
// final String methodPrefix, final String variableName) {
// final String className = destinationObject.getClass().getName();
// HashMap<String, Method> methodCache = cache.get(className);
// if( methodCache == null ) {
// methodCache = new HashMap<String, Method>();
// cache.put(className, methodCache);
// }
// Method method = methodCache.get(variableName);
// if( method == null ) {
// method = inspectClass(destinationObject, methodPrefix, variableName, 0);
// methodCache.put(variableName, method);
// }
//	
// return method;
// }
Method getMethod(final TwoDHashMap<String, String, Method> cache, final Object destinationObject,
	final String methodPrefix, final String variableName) {
	Method method = cache.get(destinationObject.getClass().getName(), variableName);
	if( method == null ) {
		method = inspectClass(destinationObject, methodPrefix, variableName, 0);
		cache.set(destinationObject.getClass().getName(), variableName, method);
	}
	return method;
}

public <T> Method getSetMethod(final Object destinationObject, final String variableName, final Class<?> variableType) {
	Method method = setMethodsCache.get(destinationObject.getClass(), variableType, variableName);
	if( method == null ) {
		// we don't know the destination type for the set method, just use whatever we can find
		if( variableType == null ) {
			method = findSetMethodWithNonPrimitiveParameter(destinationObject, variableName);
		}
		else {
			method = inspectClassForSetMethods(destinationObject, variableType, variableName);
		}
		setMethodsCache.set(destinationObject.getClass(), variableType, variableName, method);
	}
	return method;
}

/**
 * @param destinationObject
 *            the object on which to call the method
 * @param methodPrefix
 *            "get" (not used with "set" anymore due to overloading lookup
 * @param variableName
 *            specifies method to search for
 * @param requiredNumberOfArgs
 *            the number of arguments the method to search for has to have
 * @return
 */
private Method inspectClass(final Object destinationObject, final String methodPrefix, final String variableName,
	final int requiredNumberOfArgs) {
	final String methodName = methodPrefix + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
	
	// find method by traversal of the object
	for( final Method meth : destinationObject.getClass().getMethods() ) {
		if( meth.getName().equals(methodName) //
			&& meth.getParameterTypes().length == requiredNumberOfArgs ) {
			// System.out.println("found method " + meth.toString());
			return meth;
		}
	}
	throw new SuperCSVReflectionException(String.format(//
		"Can't find method '%s' in class '%s'", methodName, destinationObject.getClass().getName()));
}

private Method findSetMethodWithNonPrimitiveParameter(final Object destinationObject, final String variableName) {
	final String methodName = "set" + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
	
	// find method by traversal of the object
	for( final Method meth : destinationObject.getClass().getMethods() ) {
		if( meth.getName().equals(methodName) //
			&& meth.getParameterTypes().length == 1 //
			&& meth.getParameterTypes()[0].isPrimitive() == false ) {
			// System.out.println("found method " + meth.toString());
			return meth;
		}
	}
	throw new SuperCSVReflectionException(String.format(//
		"Can't find method '%s' in class '%s'", methodName, destinationObject.getClass().getName()));
}

/**
 * find the method by inspecting the objects' class or throws an exception
 * 
 * @param destinationObject
 * @param variableType
 * @param variableName
 * @throws SuperCSVReflectionException
 *             when the method is not found
 * @return the method
 */
Method inspectClassForSetMethods(final Object destinationObject, final Class variableType, final String variableName) {
	final String methodName = "set" + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
	
	try {
		return destinationObject.getClass().getMethod(methodName, variableType);
	}
	catch(final SecurityException e) {
		throwException(destinationObject, variableType, methodName, e);
	}
	catch(final NoSuchMethodException e) {
		// retry again due to autoboxing in java we need to try both cases
		try {
			if( autoboxingConverter.containsKey(variableType) == false ) {
				throwException(destinationObject, variableType, methodName, e);
			}
			return destinationObject.getClass().getMethod(methodName, autoboxingConverter.get(variableType));
		}
		catch(final SecurityException e1) {
			throwException(destinationObject, variableType, methodName, e1);
		}
		catch(final NoSuchMethodException e1) {
			throwException(destinationObject, variableType, methodName, e1);
		}
	}
	throw new SuperCSVException("This can never happen!");
}

/**
 * @param destinationObject
 * @param variableType
 * @param methodName
 * @param e
 * @throws SuperCSVReflectionException
 */
private void throwException(final Object destinationObject, final Class variableType, final String methodName,
	final Exception e) throws SuperCSVReflectionException {
	e.printStackTrace();
	throw new SuperCSVReflectionException(String.format("Can't find method '%s(%s)' in class '%s'. "
		+ "Is the name correctly spelled in the NameMapping? "
		+ "Have you forgot to convert the data so that a wrong set method is called?", methodName, variableType,
		destinationObject.getClass().getName()), e);
}
}
