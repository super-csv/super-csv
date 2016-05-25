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

import org.junit.Test;

/**
 * Tests the SuperCsvReflectionException class.
 * 
 * @author James Bassett
 */
public class SuperCsvReflectionExceptionTest {
	
	private static final String MSG = "Reflection failed";
	private static final Throwable THROWABLE = new RuntimeException("Mirror is broken");
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstructor1() {
		SuperCsvReflectionException e = new SuperCsvReflectionException(MSG);
		assertEquals(MSG, e.getMessage());
		
		// test with null msg
		e = new SuperCsvReflectionException(null);
		assertNull(e.getMessage());
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstructor2() {
		SuperCsvReflectionException e = new SuperCsvReflectionException(MSG, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(THROWABLE, e.getCause());
		
		// test with null msg
		e = new SuperCsvReflectionException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());
	}
	
}
