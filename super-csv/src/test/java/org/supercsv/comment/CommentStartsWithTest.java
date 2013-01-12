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
package org.supercsv.comment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the CommentStartsWith comment matcher.
 * 
 * @author James Bassett
 */
public class CommentStartsWithTest {
	
	/**
	 * Tests the CommentStartsWith comment matcher.
	 */
	@Test
	public void testStartsWith() {
		CommentMatcher matcher = new CommentStartsWith("//");
		assertTrue(matcher.isComment("// yep, it's a comment"));
		assertFalse(matcher.isComment("/ not a comment"));
	}
	
	/**
	 * Tests the CommentStartsWith constructor with a null value.
	 */
	@Test(expected = NullPointerException.class)
	public void testStartsWithConstructorWithNull() {
		new CommentStartsWith(null);
	}
	
	/**
	 * Tests the CommentStartsWith constructor with an empty String value.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStartsWithConstructorWithEmptyString() {
		new CommentStartsWith("");
	}
	
}
