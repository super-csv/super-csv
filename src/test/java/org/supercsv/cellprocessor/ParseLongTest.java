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
 * Tests the ParseLong processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseLongTest {
	
	private static final long POSITIVE_VAL = 1234567891011L;
	private static final String POSITIVE_STRING = "1234567891011";
	private static final long NEGATIVE_VAL = -246810121416L;
	private static final String NEGATIVE_STRING = "-246810121416";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseLong();
		processorChain = new ParseLong(new IdentityTransform());
	}

	/**
	 * Tests unchained/chained execution with valid longs as input.
	 */
	@Test
	public void testValidLongs(){
		
		// positive values
		assertEquals(POSITIVE_VAL, processor.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals(POSITIVE_VAL, processorChain.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		
		// negative values
		assertEquals(NEGATIVE_VAL, processor.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals(NEGATIVE_VAL, processorChain.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests unchained/chained execution with valid long Strings as input.
	 */
	@Test
	public void testValidLongStrings(){
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
	 * Tests execution with a non Long input (should throw an exception).
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
