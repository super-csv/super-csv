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
package org.supercsv.exception;

import static org.junit.Assert.*;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.util.CsvContext;

/**
 * Tests the ClassCastInputCSVException class.
 * 
 * @author James Bassett
 */
public class ClassCastInputCSVExceptionTest {
	
	private static final String MSG = "What class is that supposed to be?";
	private static final Throwable THROWABLE = new RuntimeException("I'm the cause of the problem");
	private static final CellProcessor PROCESSOR = new IdentityTransform();
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstructor1() {
		ClassCastInputCSVException e = new ClassCastInputCSVException(MSG, ANONYMOUS_CSVCONTEXT, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, context and throwable
		e = new ClassCastInputCSVException(null, (CsvContext) null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstructor2() {
		ClassCastInputCSVException e = new ClassCastInputCSVException(MSG, ANONYMOUS_CSVCONTEXT);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		e.printStackTrace();
		
		// test with null msg and context
		e = new ClassCastInputCSVException(null, (CsvContext) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		e.printStackTrace();
	}
	
	/**
	 * Tests the third constructor.
	 */
	@Test
	public void testConstructor3() {
		ClassCastInputCSVException e = new ClassCastInputCSVException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		e.printStackTrace();
		
		// test with mull msg, context and processor
		e = new ClassCastInputCSVException(null, (CsvContext) null, (CellProcessor) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getOffendingProcessor());
		e.printStackTrace();
	}
	
	/**
	 * Tests the fourth constructor.
	 */
	@Test
	public void testConstructor4() {
		ClassCastInputCSVException e = new ClassCastInputCSVException(MSG);
		assertEquals(MSG, e.getMessage());
		e.printStackTrace();
		
		// test with null msg
		e = new ClassCastInputCSVException(null);
		assertNull(e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * Tests the fifth constructor.
	 */
	@Test
	public void testConstructor5() {
		ClassCastInputCSVException e = new ClassCastInputCSVException(Integer.valueOf(23), String.class,
			ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		e.printStackTrace();
		
		// test with null received value
		e = new ClassCastInputCSVException(null, String.class, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		e.printStackTrace();
		
		// test with null received, expected, context and processor
		try {
			e = new ClassCastInputCSVException(null, null, (CsvContext) null, (CellProcessor) null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException npe) {}
		
	}
	
}
