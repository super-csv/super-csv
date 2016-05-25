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

import java.util.regex.PatternSyntaxException;

import org.junit.Test;

/**
 * Tests the CommentMatches comment matcher.
 * 
 * @author James Bassett
 */
public class CommentMatchesTest {
	
	/**
	 * Tests the CommentMatches comment matcher.
	 */
	@Test
	public void testMatches() {
		CommentMatcher matcher = new CommentMatches("<!--.*-->");
		assertTrue(matcher.isComment("<!-- yep, it's a comment -->"));
		assertFalse(matcher.isComment("<not a comment>"));
	}
	
	/**
	 * Tests the CommentMatches constructor with a null regex.
	 */
	@Test(expected = NullPointerException.class)
	public void testMatchesConstructorWithNull() {
		new CommentMatches(null);
	}
	
	/**
	 * Tests the CommentMatches constructor with an empty String value.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMatchesConstructorWithEmptyString() {
		new CommentMatches("");
	}
	
	/**
	 * Tests the CommentMatches constructor with an invalid regex.
	 */
	@Test(expected = PatternSyntaxException.class)
	public void testMatchesConstructorWithInvalidRegex() {
		new CommentMatches("*****");
	}
	
}
