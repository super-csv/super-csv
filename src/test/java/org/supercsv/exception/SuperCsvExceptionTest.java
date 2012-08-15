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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Test;
import org.supercsv.util.CsvContext;

/**
 * Tests the SuperCsvException class.
 * 
 * @author James Bassett
 */
public class SuperCsvExceptionTest {
	
	private static final String MSG = "Something terrible happened!";
	private static final Throwable THROWABLE = new RuntimeException("I'm the cause of the problem");
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstuctor1() {
		SuperCsvException e = new SuperCsvException(MSG);
		assertEquals(MSG, e.getMessage());
		e.printStackTrace();
		
		// test with null msg
		e = new SuperCsvException(null);
		assertNull(e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstuctor2() {
		SuperCsvException e = new SuperCsvException(MSG, ANONYMOUS_CSVCONTEXT);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		e.printStackTrace();
		
		// test with null msg and context
		e = new SuperCsvException(null, (CsvContext) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		e.printStackTrace();
	}
	
	/**
	 * Tests the third constructor.
	 */
	@Test
	public void testConstuctor3() {
		SuperCsvException e = new SuperCsvException(MSG, ANONYMOUS_CSVCONTEXT, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, context and throwable
		e = new SuperCsvException(null, (CsvContext) null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
}
