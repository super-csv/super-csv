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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the BeanInterfaceProxy class.
 * 
 * @author James Bassett
 */
public class BeanInterfaceProxyTest {
	
	private TestInterface testInterface;
	
	/**
	 * Creates the proxy for the test.
	 */
	@Before
	public void setUp() {
		testInterface = BeanInterfaceProxy.createProxy(TestInterface.class);
	}
	
	/**
	 * Tests the proxy.
	 */
	@Test
	public void testProxy() {
		// no state saved yet
		assertNull(testInterface.getValue());
		
		// modify state
		String value = "value";
		testInterface.setValue(value);
		
		// check state was saved
		assertEquals(value, testInterface.getValue());
		
		// modify state (to null)
		testInterface.setValue(null);
		
		// check state was saved
		assertNull(testInterface.getValue());
	}
	
	/**
	 * Tests the proxy with chained setters (the proxy allows for setters which return the modified object).
	 */
	@Test
	public void testChainedSetters() {
		// no state saved yet
		assertNull(testInterface.getValue());
		
		// modify state
		String value = "value";
		String value2 = "value2";
		testInterface.setValue(value).setValue2(value2);
		
		// check state was saved
		assertEquals(value, testInterface.getValue());
		assertEquals(value2, testInterface.getValue2());
	}
	
	/**
	 * Tests createProxy() with null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateProxyWithNull() {
		BeanInterfaceProxy.createProxy(null);
	}
	
	/**
	 * Tests invocation of a method that isn't a standard getter/setter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNotGetterOrSetter() {
		testInterface.isValueSet();
	}
	
	/**
	 * Tests invocation of a getter with a parameter (not a real getter).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetterWithParam() {
		testInterface.getWithParam("param");
	}
	
	/**
	 * Tests invocation of a method beginning with 'set' that takes many parameters (not a real setter).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMultiParamSetter() {
		testInterface.setManyValues("value1", "value2");
	}
	
	/**
	 * Tests invocation of a setter without any parameters (not a real setter).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetterWithNoParams() {
		testInterface.setNothing();
	}
	
	/**
	 * Tests invocation of the <tt>getClass</tt> method, inherited from <tt>Object</tt> (doesn't call the
	 * <tt>InvocationHandler</tt>, but returns the proxy class!).
	 */
	@Test
	public void testGetClass() {
		assertNotNull(testInterface.getClass());
		
	}
	
	/**
	 * An interface to use for testing.
	 */
	private interface TestInterface {
		
		public TestInterface setValue(String value); // proxy allows setters that returned the modified object
		
		public void setValue2(String value);
		
		public String getValue();
		
		public String getValue2();
		
		public String getWithParam(String value1); // getters with params not allowed
		
		public void setManyValues(String value1, String value2); // multi param setters not allowed
		
		public void setNothing(); // setters should have 1 param
		
		public boolean isValueSet(); // methods not starting with get/set are not allowed (yes, even boolean getters!)
		
	}
	
}
