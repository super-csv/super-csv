package org.supercsv.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCSVReflectionException;

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
		testInterface = (TestInterface) new BeanInterfaceProxy().createProxy(TestInterface.class);
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
	 * Tests invocation of a method that isn't a standard getter/setter.
	 */
	@Test(expected = SuperCSVReflectionException.class)
	public void testNotGetterOrSetter() {
		testInterface.isValueSet();
	}
	
	/**
	 * Tests invocation of a method beginning with 'set' that takes many parameters (not a real setter).
	 */
	@Test(expected = SuperCSVReflectionException.class)
	public void testMultiParamSetter() {
		testInterface.setManyValues("value1", "value2");
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
		
		public void setManyValues(String value1, String value2); // multi param setters not allowed
		
		public boolean isValueSet(); // methods not starting with get/set are not allowed (yes, even boolean getters!)
		
	}
	
}
