package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import java.util.regex.PatternSyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the StrReplace processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class StrReplaceTest {
	
	private static final String REGEX = "\\s+"; // whitespace
	private static final String REPLACEMENT = "_";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new StrReplace(REGEX, REPLACEMENT);
		processorChain = new StrReplace(REGEX, REPLACEMENT, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid input.
	 */
	@Test
	public void testValidInput() {
		String input = "This is a \tString with some \n whitespace";
		String expected = "This_is_a_String_with_some_whitespace";
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with non-String input (toString() is called first).
	 */
	@Test
	public void testNonStringInput() {
		StringBuilder input = new StringBuilder("This is a \tString with some \n whitespace"); // how lazy!
		String expected = "This_is_a_String_with_some_whitespace";
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null regex (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullRegex() {
		processor = new StrReplace(null, REPLACEMENT);
	}
	
	/**
	 * Tests execution with a null replacement (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullReplacement() {
		processor = new StrReplace(REGEX, null);
	}
	
	/**
	 * Tests execution with an empty regex (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithEmptyRegex() {
		processor = new StrReplace("", REPLACEMENT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = PatternSyntaxException.class)
	public void testWithInvalidRegex() {
		processor = new StrReplace("***", REPLACEMENT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
