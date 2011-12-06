package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the ParseChar processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseCharTest {
	
	private static final char CHAR = 'c';
	private static final String STRING = String.valueOf(CHAR);
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseChar();
		processorChain = new ParseChar(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a valid char input (should be returned unchanged).
	 */
	@Test
	public void testValidChar(){
		assertEquals(CHAR, processor.execute(CHAR, ANONYMOUS_CSVCONTEXT));
		assertEquals(CHAR, processorChain.execute(CHAR, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a valid single-char String input.
	 */
	@Test
	public void testStringWithSingleChar(){
		assertEquals(CHAR, processor.execute(STRING, ANONYMOUS_CSVCONTEXT));
		assertEquals(CHAR, processorChain.execute(STRING, ANONYMOUS_CSVCONTEXT));
	}
	
	
	/**
	 * Tests execution with an multi-character String input (should throw an exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithMultiCharString() {
		processor.execute("cc", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non char input (should throw an exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonCharInput() {
		processor.execute(1, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}

}
