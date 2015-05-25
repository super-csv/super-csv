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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests the CsvContext class.
 * 
 * @author James Bassett
 */
public class CsvContextTest {
	
	/**
	 * Tests the getters/setters.
	 */
	@Test
	public void testGettersAndSetters() {
		final CsvContext context = new CsvContext(0, 0, 0);
		context.setLineNumber(1);
		context.setRowNumber(2);
		context.setColumnNumber(3);
		final Object[] rowSource = new Object[] { "one", "two", "three" };
		context.setRowSource(Arrays.asList(rowSource));
		assertEquals(1, context.getLineNumber());
		assertEquals(2, context.getRowNumber());
		assertEquals(3, context.getColumnNumber());
		assertTrue(Arrays.equals(rowSource, context.getRowSource().toArray()));
		
	}
	
	/**
	 * Tests the hashCode() method.
	 */
	@Test
	public void testHashCode() {
		// two identical contexts with no line source
		final CsvContext context1 = new CsvContext(1, 2, 3);
		final CsvContext context2 = new CsvContext(1, 2, 3);
		assertEquals(context1.hashCode(), context2.hashCode());
		
		// two identical contexts with a line source
		final CsvContext contextWithSource1 = new CsvContext(1, 2, 3);
		contextWithSource1.setRowSource(Arrays.asList(new Object[] { "one", "two", "three" }));
		final CsvContext contextWithSource2 = new CsvContext(1, 2, 3);
		contextWithSource2.setRowSource(Arrays.asList(new Object[] { "one", "two", "three" }));
		assertEquals(contextWithSource1.hashCode(), contextWithSource2.hashCode());
	}
	
	/**
	 * Tests the toString() method.
	 */
	@Test
	public void testToString() {
		
		// no line source
		final CsvContext context1 = new CsvContext(1, 2, 3);
		assertEquals("{lineNo=1, rowNo=2, columnNo=3, rowSource=null}", context1.toString());
		
		// with line source
		final CsvContext context2 = new CsvContext(1, 2, 3);
		context2.setRowSource(Arrays.asList(new Object[] { "one", "two", "three" }));
		assertEquals("{lineNo=1, rowNo=2, columnNo=3, rowSource=[one, two, three]}", context2.toString());
		
	}
	
	/**
	 * Tests the equals() method.
	 */
	@Test
	public void testEquals() {
		
		final CsvContext context = new CsvContext(1, 2, 3);
		
		final CsvContext contextWithSource = new CsvContext(1, 2, 3);
		contextWithSource.setRowSource(Arrays.asList(new Object[] { "one", "two", "three" }));
		
		// same object
		assertTrue(context.equals(context));
		
		// null
		assertFalse(context.equals(null));
		
		// different class
		assertFalse(context.equals("A String, not a CsvContext!"));
		
		// different column
		assertFalse(context.equals(new CsvContext(1, 2, 4)));
		
		// different row
		assertFalse(context.equals(new CsvContext(1, 4, 3)));
		
		// different line no
		assertFalse(context.equals(new CsvContext(4, 2, 3)));
		
		// only 1 context with line source
		assertFalse(context.equals(contextWithSource));
		
		// same column/line no, but different line sources
		final CsvContext contextWithDifferentSource = new CsvContext(1, 2, 3);
		contextWithDifferentSource.setRowSource(Arrays.asList(new Object[] { "four", "five", "six" }));
		assertFalse(contextWithSource.equals(contextWithDifferentSource));
		
		// same with no line source
		assertTrue(context.equals(new CsvContext(1, 2, 3)));
		
		// same with line source
		final CsvContext same = new CsvContext(1, 2, 3);
		same.setRowSource(Arrays.asList(new Object[] { "one", "two", "three" }));
		assertTrue(contextWithSource.equals(same));
	}
	
	@Test
	public void testCopyConstructor() {
		// Test with the mandatory parameters. The rowSource is not set
		// to make sure that copy constructor does not dereference a null
		CsvContext original = new CsvContext(1,2,3);
		CsvContext clone = new CsvContext(original);
		assertFalse(original == clone);   // The clone is a new object
		assertEquals(original, clone);    // that passes the equals test
		
		// Test with a homogeneous rowSource
		original.setRowSource(Arrays.asList(new Object[] { "four", "five", "six" }));
		CsvContext clone2 = new CsvContext(original);
		assertFalse(original == clone2);   // The clone is a new object
		assertEquals(original, clone2);    // that passes the equals test
		
		// Corner cases
		original.setRowSource(Arrays.asList(new Object[] { null, "five", new Integer(6) }));
		CsvContext clone3 = new CsvContext(original);
		assertFalse(original == clone3);   // The clone is a new object
		assertEquals(original, clone3);    // that passes the equals test
	}
}
