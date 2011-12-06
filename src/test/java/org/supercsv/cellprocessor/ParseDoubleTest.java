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
 * Tests the ParseDouble processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseDoubleTest {
	
	private static final double POSITIVE_VAL = 17.3;
	private static final String POSITIVE_STRING = "17.3";
	private static final double NEGATIVE_VAL = -43.0;
	private static final String NEGATIVE_STRING = "-43.0";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseDouble();
		processorChain = new ParseDouble(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid doubles as input.
	 */
	@Test
	public void testValidDoubles(){
		// positive values
		assertEquals(POSITIVE_VAL, processor.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals(POSITIVE_VAL, processorChain.execute(POSITIVE_VAL, ANONYMOUS_CSVCONTEXT));
		
		// negative values
		assertEquals(NEGATIVE_VAL, processor.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
		assertEquals(NEGATIVE_VAL, processorChain.execute(NEGATIVE_VAL, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests unchained/chained execution with valid double Strings as input.
	 */
	@Test
	public void testValidDoubleStrings(){
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
		processor.execute("123.45s", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non Double input (should throw an exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonDoubleInput() {
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
