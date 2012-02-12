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
 * Tests the StrRegExReplace processor.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 */
@SuppressWarnings("deprecation")
public class StrRegExReplaceTest {
	
	private static final String REGEX = "\\s+"; // whitespace
	private static final String REPLACEMENT = "_";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new StrRegExReplace(REGEX, REPLACEMENT);
		processorChain = new StrRegExReplace(REGEX, REPLACEMENT, new IdentityTransform());
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
	 * Tests unchained/chained execution with non-String input (this is where this processor differs from StrReplace and
	 * throws an exception).
	 */
	@Test(expected = ClassCastException.class)
	public void testNonStringInput() {
		StringBuilder input = new StringBuilder("This is a \tString with some \n whitespace"); // how lazy!
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null regex (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullRegex() {
		processor = new StrRegExReplace(null, REPLACEMENT);
	}
	
	/**
	 * Tests execution with a null replacement (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullReplacement() {
		processor = new StrRegExReplace(REGEX, null);
	}
	
	/**
	 * Tests execution with an empty regex (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithEmptyRegex() {
		processor = new StrRegExReplace("", REPLACEMENT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = PatternSyntaxException.class)
	public void testWithInvalidRegex() {
		processor = new StrRegExReplace("***", REPLACEMENT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
