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
import static org.junit.Assert.assertTrue;
import static org.supercsv.util.ReflectionUtils.findGetter;
import static org.supercsv.util.ReflectionUtils.findSetter;

import java.lang.reflect.Constructor;
import java.security.Permission;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.mock.ReflectionBean;

/**
 * Test the ReflectionUtils class.
 * 
 * @author James Bassett
 */
public class ReflectionUtilsTest {
	
	private static final double DOUBLE_ASSERT_DELTA = 1e-15;
	
	private ReflectionBean bean;
	
	/**
	 * Sets up before the test.
	 */
	@Before
	public void setUp() {
		bean = new ReflectionBean();
	}
	
	/**
	 * Tidies up after the test.
	 */
	@After
	public void tearDown() {
		bean = null;
	}
	
	/**
	 * Sets the SecurityManager to a mock implementation which throws a SecurityException whenever checkMemberAccess()
	 * is called. clearSecurityManager() should be called immediately after the test to restore things back to normal.
	 * 
	 * @param allowedCalls
	 *            the number of calls to checkMemberAccess() that should be allowed before throwing the exception.
	 */
	private static void initSecurityManager(final int allowedCalls) {
		System.setSecurityManager(new SecurityManager() {
			
			private int allowedCallsLeft = allowedCalls;
			
			@Override
			public void checkMemberAccess(Class<?> clazz, int which) {
				if( allowedCallsLeft-- <= 0 ) {
					throw new SecurityException("Computer says no!");
				}
				
			}
			
			@Override
			public void checkPermission(Permission perm) {
				// allow resetting the SecurityManager
			}
		});
	}
	
	/**
	 * Clears the SecurityManager.
	 */
	private static void clearSecurityManager() {
		System.setSecurityManager(null);
	}
	
	/**
	 * Tests the findGetter() method.
	 */
	@Test
	public void testFindGetter() throws Exception {
		String name = "Bob";
		bean.setName(name);
		assertEquals(name, findGetter(bean, "name").invoke(bean));
	}
	
	/**
	 * Tests the findSetter() method.
	 */
	@Test
	public void testFindSetter() throws Exception {
		String name = "Bob";
		findSetter(bean, "name", String.class).invoke(bean, name);
		assertEquals(name, bean.getName());
	}
	
	/**
	 * Tests the findSetter() method with a field type that is a subtype of the setter parameter type.
	 */
	@Test
	public void testFindSetterWithSubtype() throws Exception {
		findSetter(bean, "favouriteNumber", Integer.class).invoke(bean, Integer.valueOf(123));
		assertEquals(123, bean.getFavouriteNumber().intValue());
	}
	
	/**
	 * Tests the findSetter() method with a field type that's compatible with 2 setters (the exact match should always
	 * be used).
	 */
	@Test
	public void testFindSetterWithTwoOptions() throws Exception {
		findSetter(bean, "overloaded", Number.class).invoke(bean, Integer.valueOf(123));
		assertEquals(123, bean.getOverloaded().intValue());
	}
	
	/**
	 * Tests the findSetter() method with a field type that's compatible with 1 setter but has the same name as another
	 * method (which should be ignored).
	 */
	@Test
	public void testFindSetterWithMethodOfSameName() throws Exception {
		findSetter(bean, "sailForTreasure", Boolean.class).invoke(bean, Boolean.TRUE);
		assertTrue(bean.getSailForTreasure());
	}
	
	/**
	 * Tests the findSetter() method by passing primitives and wrapper classes to setters that expect wrapper classes
	 * and primitives respectively (this tests the autoboxing logic).
	 */
	@Test
	public void testAutoboxing() throws Exception {
		
		// first try setting a wrapper values onto the primitive setters
		
		findSetter(bean, "primitiveInt", Integer.class).invoke(bean, Integer.valueOf(1));
		assertEquals(1, bean.getPrimitiveInt());
		
		findSetter(bean, "primitiveShort", Short.class).invoke(bean, Short.valueOf("2"));
		assertEquals(Short.parseShort("2"), bean.getPrimitiveShort());
		
		findSetter(bean, "primitiveLong", Long.class).invoke(bean, Long.valueOf("3"));
		assertEquals(Long.parseLong("3"), bean.getPrimitiveLong());
		
		findSetter(bean, "primitiveDouble", Double.class).invoke(bean, Double.valueOf("4.0"));
		assertEquals(Double.parseDouble("4.0"), bean.getPrimitiveDouble(), DOUBLE_ASSERT_DELTA);
		
		findSetter(bean, "primitiveFloat", Float.class).invoke(bean, Float.valueOf("5.0"));
		assertEquals(Float.parseFloat("5.0"), bean.getPrimitiveFloat(), DOUBLE_ASSERT_DELTA);
		
		findSetter(bean, "primitiveChar", Character.class).invoke(bean, Character.valueOf('a'));
		assertEquals('a', bean.getPrimitiveChar());
		
		findSetter(bean, "primitiveByte", Byte.class).invoke(bean, Byte.valueOf("123"));
		assertEquals(Byte.parseByte("123"), bean.getPrimitiveByte());
		
		// now try setting primitive values onto the wrapper setters
		
		findSetter(bean, "integerWrapper", int.class).invoke(bean, 1);
		assertEquals(Integer.valueOf(1), bean.getIntegerWrapper());
		
		findSetter(bean, "shortWrapper", short.class).invoke(bean, Short.parseShort("2"));
		assertEquals(Short.valueOf("2"), bean.getShortWrapper());
		
		findSetter(bean, "longWrapper", long.class).invoke(bean, Long.parseLong("3"));
		assertEquals(Long.valueOf("3"), bean.getLongWrapper());
		
		findSetter(bean, "doubleWrapper", double.class).invoke(bean, Double.parseDouble("4.0"));
		assertEquals(Double.valueOf("4.0"), bean.getDoubleWrapper());
		
		findSetter(bean, "floatWrapper", float.class).invoke(bean, Float.parseFloat("5.0"));
		assertEquals(Float.valueOf("5.0"), bean.getFloatWrapper());
		
		findSetter(bean, "charWrapper", char.class).invoke(bean, 'a');
		assertEquals(Character.valueOf('a'), bean.getCharWrapper());
		
		findSetter(bean, "byteWrapper", byte.class).invoke(bean, Byte.parseByte("123"));
		assertEquals(Byte.valueOf("123"), bean.getByteWrapper());
		
	}
	
	/**
	 * Tests the findGetter() method with a null object (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFindGetterWithNullObject() {
		findGetter(null, "name");
	}
	
	/**
	 * Tests the findGetter() method with a null field name (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFindGetterWithNullFieldName() {
		findGetter(bean, null);
	}
	
	/**
	 * Tests the findGetter() method with an invalid field name (should throw an exception).
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testFindGetterWithInvalidFieldName() {
		findGetter(bean, "invalid");
	}
	
	/**
	 * Tests the findGetter() method when a SecurityException is thrown (should throw an exception).
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testFindGetterWithSecurityException() {
		try {
			initSecurityManager(0);
			findGetter(bean, "name");
		}
		finally {
			clearSecurityManager();
		}
		
	}
	
	/**
	 * Tests the findSetter() method with a null object (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFindSetterWithNullObject() {
		findSetter(null, "name", String.class);
	}
	
	/**
	 * Tests the findSetter() method with a null field name (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFindSetterWithNullFieldName() {
		findSetter(bean, null, String.class);
	}
	
	/**
	 * Tests the findSetter() method with a null field type (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testFindSetterWithNullFieldType() {
		findSetter(bean, "name", null);
	}
	
	/**
	 * Tests the findSetter() method with an invalid field name (should throw an exception).
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testFindSetterWithInvalidFieldName() {
		findSetter(bean, "invalid", String.class);
	}
	
	/**
	 * Tests the findSetter() method with an invalid field name with a primitive parameter type (should throw an
	 * exception after trying both primitive and wrapper method signatures).
	 */
	@Test(expected = SuperCsvReflectionException.class)
	public void testFindSetterWithInvalidFieldNameAndPrimitiveType() {
		findSetter(bean, "invalid", int.class);
	}
	
	/**
	 * Tests the private constructor for test coverage (yes, this is stupid).
	 */
	@Test
	public void testPrivateConstructor() throws Exception {
		Constructor<?> c = ReflectionUtils.class.getDeclaredConstructors()[0];
		c.setAccessible(true);
		c.newInstance();
	}
	
}
