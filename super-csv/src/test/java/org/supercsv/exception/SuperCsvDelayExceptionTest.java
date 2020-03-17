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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the SuperCsvDelayExceptions class
 *
 * @author Chen Guoping
 */
public class SuperCsvDelayExceptionTest {
	private static final String MSG = "Something terrible happened!";

	/**
	 * Test the constructor
	 */
	@Test public void testConstructor() {
		SuperCsvDelayException exception = new SuperCsvDelayException(MSG);
		assertEquals(MSG, exception.getMessage());
	}

	/**
	 * Test the toString method
	 */
	@Test public void testToString() {
		SuperCsvDelayException exception = new SuperCsvDelayException(MSG);
		assertEquals(MSG, exception.toString());
	}
}
