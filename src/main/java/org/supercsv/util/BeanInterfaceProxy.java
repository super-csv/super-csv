package org.supercsv.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import org.supercsv.exception.SuperCSVReflectionException;

/**
 * This is part of the internal implementation of SuperCSV.
 * <p>
 * This class creates bean instances based on an interface. This allows you given an interface for a bean (but no
 * implementation), to on-the-fly generate a bean implementation. This instance you can then use for fetching and
 * storing state. It assumes all get methods starts with "get" and all set methods start with "set" and takes only 1
 * argument.
 * 
 * @author Kasper B. Graversen, (c) 2008
 */
public class BeanInterfaceProxy implements InvocationHandler {
private final HashMap<String, Object> beanState = new HashMap<String, Object>();

/**
 * Creates a proxy object which implements a given bean interface. This proxy object will act as an implementation of
 * the interface, hence just a data container
 * 
 * @param anInterface
 *            Interface for which to create a proxy
 * @return the proxy implementation
 */
public Object createProxy(final Class anInterface) {
	return Proxy.newProxyInstance(anInterface.getClassLoader(), new Class[] { anInterface }, this);
}

/**
 * {@inheritDoc} This method is invoked every time a method is invoked on our proxy. getMethods returns the value they
 * hold or null. SetMethods sets the state given by the first argument and returns itself (to use it with method
 * chaining)
 */
public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
	if( method.getName().startsWith("get") ) { return beanState.get(method.getName().substring(3)); }
	if( method.getName().startsWith("set") ) {
		if( args.length == 1 ) {
			beanState.put(method.getName().substring(3), args[0]);
			return proxy;
		} else {
			throw new SuperCSVReflectionException("Method should only take 1 argument");
		}
	}
	throw new SuperCSVReflectionException("Can only understand method calls starting with 'get' or 'set'. Got method '"
		+ method.getName() + "'");
}
}
