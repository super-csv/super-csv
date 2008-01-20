package org.supercsv.util;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.supercsv.exception.SuperCSVReflectionException;

/**
 * This class cache's method lookup. Hence first time it introspects the instance's class, while subsequent method
 * lookups are super fast.
 */
public class MethodCache {
	/**
	 * A map containing mapping "classname -> HashMap". The inner HashMap is a "methodname->Method" mapping
	 */
	HashMap<String, HashMap<String, Method>>	setCache	= new HashMap<String, HashMap<String, Method>>();

	/**
	 * A map containing mapping "classname -> HashMap". The inner HashMap is a "methodname->Method" mapping
	 */
	HashMap<String, HashMap<String, Method>>	getCache	= new HashMap<String, HashMap<String, Method>>();

	public void flushCaches() {
		setCache.clear();
		getCache.clear();
	}

	/** Given an instance and a variable name, return the given method */
	public Method getGetMethod(final Object destinationObject, final String variableName) {
		return getMethod(getCache, destinationObject, "get", variableName);
	}

	/**
	 * using either get or set cache lookup a method. This approach saves a subString(), concatenations and
	 * toUpperCase() since now the variable name uniquely identify either get or set method access
	 */
	Method getMethod(final HashMap<String, HashMap<String, Method>> cache, final Object destinationObject,
			final String methodPrefix, final String variableName) {
		final String className = destinationObject.getClass().getName();
		HashMap<String, Method> methodCache = cache.get(className);
		if(methodCache == null) {
			methodCache = new HashMap<String, Method>();
			cache.put(className, methodCache);
		}
		Method method = methodCache.get(variableName);
		if(method == null) {
			method = inspectClass(destinationObject, methodPrefix, variableName);
			methodCache.put(variableName, method);
		}

		return method;
	}

	public Method getSetMethod(final Object destinationObject, final String variableName) {
		return getMethod(setCache, destinationObject, "set", variableName);
	}

	Method inspectClass(final Object destinationObject, final String methodPrefix, final String variableName) {
		final String methodName = methodPrefix + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);

		// find method by traversal of the object
		for(final Method meth : destinationObject.getClass().getMethods()) {
			if(meth.getName().equals(methodName)) {
				// System.out.println("found method " + meth.toString());
				return meth;
			}
		}
		throw new SuperCSVReflectionException("Can't find method '" + methodName + "' in class '"
				+ destinationObject.getClass().getName() + "'");
	}
}
