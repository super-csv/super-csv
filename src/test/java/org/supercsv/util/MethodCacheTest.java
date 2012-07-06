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

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.mock.ReflectionBean;

/**
 * Tests the MethodCache class.
 * 
 * @author James Bassett
 */
public class MethodCacheTest {
	
	private MethodCache cache;
	
	/**
	 * Sets up before the test.
	 */
	@Before
	public void setUp() {
		cache = new MethodCache();
	}
	
	/**
	 * Tidies up after the test.
	 */
	@After
	public void tearDown() {
		cache = null;
	}
	
	/**
	 * Tests getGetMethod().
	 */
	@Test
	public void testGetGetMethod() throws Exception {
		ReflectionBean bean = new ReflectionBean();
		
		// first time - not cached
		final long start = System.currentTimeMillis();
		final Method uncachedGetter = cache.getGetMethod(bean, "name");
		final long uncachedTime = System.currentTimeMillis() - start;
		
		// second time - cached
		final long start2 = System.currentTimeMillis();
		final Method cachedGetter = cache.getGetMethod(bean, "name");
		final long cachedTime = System.currentTimeMillis() - start2;
		
		// retrieval from cache should be at least as fast as reflection
		System.out.println(String.format("getGetMethod uncachedTime: %d, cachedTime: %d", uncachedTime, cachedTime));
		assertTrue(cachedTime <= uncachedTime);
		
		// the same getter method should be returned in both calls
		assertEquals(uncachedGetter, cachedGetter);
		
		// test the uncached getter works
		final String name1 = "uncached";
		bean.setName(name1);
		assertEquals(name1, uncachedGetter.invoke(bean));
		
		// test the cached getter works
		final String name2 = "cached";
		bean.setName(name2);
		assertEquals(name2, cachedGetter.invoke(bean));
	}
	
	/**
	 * Tests getSetMethod().
	 */
	@Test
	public void testGetSetMethod() throws Exception {
		ReflectionBean bean = new ReflectionBean();
		
		// first time - not cached
		final long start = System.currentTimeMillis();
		final Method uncachedSetter = cache.getSetMethod(bean, "name", String.class);
		final long uncachedTime = System.currentTimeMillis() - start;
		
		// second time - cached
		final long start2 = System.currentTimeMillis();
		final Method cachedSetter = cache.getSetMethod(bean, "name", String.class);
		final long cachedTime = System.currentTimeMillis() - start2;
		
		// retrieval from cache should be at least as fast as reflection
		System.out.println(String.format("getSetMethod uncachedTime: %d, cachedTime: %d", uncachedTime, cachedTime));
		assertTrue(cachedTime <= uncachedTime);
		
		// the same setter method should be returned in both calls
		assertEquals(uncachedSetter, cachedSetter);
		
		// test the uncached setter works
		final String name1 = "uncached";
		uncachedSetter.invoke(bean, name1);
		assertEquals(name1, bean.getName());
		
		// test the cached setter works
		final String name2 = "cached";
		cachedSetter.invoke(bean, name2);
		assertEquals(name2, bean.getName());
	}
	
	/**
	 * Tests getGetMethod() with a null object (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testGetGetMethodWithNullObject() {
		cache.getGetMethod(null, "name");
	}
	
	/**
	 * Tests getGetMethod() with a null field name (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testGetGetMethodWithNullFieldName() {
		cache.getGetMethod(new ReflectionBean(), null);
	}
	
	/**
	 * Tests getSetMethod() with a null object (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testGetSetMethodWithNullObject() {
		cache.getSetMethod(null, "name", String.class);
	}
	
	/**
	 * Tests getSetMethod() with a null field name (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testGetSetMethodWithNullFieldName() {
		cache.getSetMethod(new ReflectionBean(), null, String.class);
	}
	
	/**
	 * Tests getSetMethod() with a null argument type (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testGetSetMethodWithNullArgumentType() {
		cache.getSetMethod(new ReflectionBean(), "name", null);
	}
	
}
