package org.supercsv.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import org.supercsv.exception.SuperCSVReflectionException;

/**
 * This is part of the internal implementation of Super CSV.
 * <p>
 * This class creates bean instances based on an interface. This allows you, given an interface for a bean (but no
 * implementation), to generate a bean implementation on-the-fly. This instance can then be used for fetching and
 * storing state. It assumes all get methods starts with "get" and all set methods start with "set" and takes only 1
 * argument.
 * 
 * @author Kasper B. Graversen
 */
public class BeanInterfaceProxy implements InvocationHandler {
	
	private static final int GET_SET_PREFIX_LENGTH = 3;
	
	private final HashMap<String, Object> beanState = new HashMap<String, Object>();
	
	/**
	 * Creates a proxy object which implements a given bean interface. This proxy object will act as an implementation
	 * of the interface.
	 * 
	 * @param anInterface
	 *            Interface for which to create a proxy
	 * @return the proxy implementation
	 */
	@SuppressWarnings("unchecked")
	public <T> T createProxy(final Class<T> anInterface) {
		return (T) Proxy.newProxyInstance(anInterface.getClassLoader(), new Class[] { anInterface }, this);
	}
	
	/**
	 * This method is invoked every time a method is invoked on the proxy. If a getter method is encountered then this
	 * method returns the stored value from the bean state (or null if the field has not been set). If a setter methods
	 * is encountered then the bean state is updated with the value of the first argument and the value is returned (to
	 * allow for method chaining)
	 */
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Exception {
		if( method.getName().startsWith("get") ) {
			return beanState.get(method.getName().substring(GET_SET_PREFIX_LENGTH));
		}
		if( method.getName().startsWith("set") ) {
			if( args.length == 1 ) {
				beanState.put(method.getName().substring(GET_SET_PREFIX_LENGTH), args[0]);
				return proxy;
			} else {
				throw new SuperCSVReflectionException("Method should only take 1 argument");
			}
		}
		throw new SuperCSVReflectionException(
			"Can only understand method calls starting with 'get' or 'set'. Got method '" + method.getName() + "'");
	}
}
