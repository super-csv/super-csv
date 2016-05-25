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
package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Collector processor.
 * 
 * @author James Bassett
 */
public class CollectorTest {
	
	private Collector listCollector;
	private Collector setCollector;
	private Collector chainedCollector;
	private List<Object> list;
	private Set<Object> set;
	private List<Object> chainedList;
	private List<Object> input = Arrays.asList(new Object[] { "one", null, 2, 3.0, 2, null, true, new Date() });
	private final int duplicates = 2;
	
	/**
	 * Sets up the Collectors.
	 */
	@Before
	public void setUp() {
		list = new ArrayList<Object>();
		set = new HashSet<Object>();
		chainedList = new ArrayList<Object>();
		listCollector = new Collector(list);
		setCollector = new Collector(set);
		chainedCollector = new Collector(chainedList, new IdentityTransform());
	}
	
	/**
	 * Tests a Collector with a List.
	 */
	@Test
	public void testListCollector() {
		
		for( Object o : input ) {
			listCollector.execute(o, ANONYMOUS_CSVCONTEXT);
		}
		
		assertTrue(listCollector.getCollection() == list);
		assertEquals(input.size(), list.size());
		for( int i = 0; i < input.size(); i++ ) {
			assertEquals(input.get(i), list.get(i));
		}
		
	}
	
	/**
	 * Tests a Collector with a Set.
	 */
	@Test
	public void testSetCollector() {
		
		for( Object o : input ) {
			setCollector.execute(o, ANONYMOUS_CSVCONTEXT);
		}
		
		assertTrue(setCollector.getCollection() == set);
		assertEquals(input.size() - duplicates, set.size());
		for( Object o : input ) {
			assertTrue(set.contains(o));
		}
		
	}
	
	/**
	 * Tests a Collector with a List, chained to another processor.
	 */
	@Test
	public void testChainedCollector() {
		
		for( Object o : input ) {
			chainedCollector.execute(o, ANONYMOUS_CSVCONTEXT);
		}
		
		assertTrue(chainedCollector.getCollection() == chainedList);
		assertEquals(input.size(), chainedList.size());
		for( int i = 0; i < input.size(); i++ ) {
			assertEquals(input.get(i), chainedList.get(i));
		}
		
	}
	
	/**
	 * Tests the 1 arg constructor with a null collection.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullCollection() {
		new Collector(null);
	}
	
	/**
	 * Tests the 2 arg constructor with a null collection.
	 */
	@Test(expected = NullPointerException.class)
	public void testChainedConstructorWithNullCollection() {
		new Collector(null, new IdentityTransform());
	}
	
}
