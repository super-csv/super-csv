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
 * Tests the ParseInt processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseIntTest {
	
	private static final int POSITIVE_VAL = 17;
	private static final String POSITIVE_STRING = "17";
	private static final int NEGATIVE_VAL = -43;
	private static final String NEGATIVE_STRING = "-43";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseInt();
		processorChain = new ParseInt(new IdentityTransform());
	}

	/**
	 * Tests unchained/chained execution with valid ints as input.
	 */
	@Test
	public void testValidInts(){
		// positive values
		assertEquals(POSITIVE_VAL, processor.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals(POSITIVE_VAL, processorChain.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		
		// negative values
		assertEquals(NEGATIVE_VAL, processor.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals(NEGATIVE_VAL, processorChain.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests unchained/chained execution with valid int Strings as input.
	 */
	@Test
	public void testValidIntStrings(){
		// positive values
		assertEquals(POSITIVE_VAL, processor.execute(POSITIVE_STRING, ANONYMOUS_CSVCONTEXT));
		assertEquals(POSITIVE_VAL, processorChain.execute(POSITIVE_STRING, ANONYMOUS_CSVCONTEXT));
		
		// negative values
		assertEquals(NEGATIVE_VAL, processor.execute(NEGATIVE_STRING, ANONYMOUS_CSVCONTEXT));
		assertEquals(NEGATIVE_VAL, processorChain.execute(NEGATIVE_STRING, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with an badly formatted String input (should throw an exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithInvalidFormatString() {
		processor.execute("123.45", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non Integer input (should throw an exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonIntInput() {
		processor.execute(1.23, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
