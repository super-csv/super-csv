package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the FmtNumber processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class FmtNumberTest {
	
	private static final String DECIMAL_FORMAT = "00.00";
	private static final String FORMATTED_NUMBER = "12.34";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new FmtNumber(DECIMAL_FORMAT);
		processor2 = new FmtNumber(new DecimalFormat(DECIMAL_FORMAT));
		processorChain = new FmtNumber(DECIMAL_FORMAT, new IdentityTransform());
		processorChain2 = new FmtNumber(new DecimalFormat(DECIMAL_FORMAT), new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution.
	 */
	@Test
	public void testFormat() {
		double number = 12.34;
		assertEquals(FORMATTED_NUMBER, processor.execute(number, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processor2.execute(number, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain.execute(number, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain2.execute(number, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with an input that should round up.
	 */
	@Test
	public void testRoundUp() {
		double toRoundUp = 12.339;
		assertEquals(FORMATTED_NUMBER, processor.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processor2.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain2.execute(toRoundUp, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with an input that should round down.
	 */
	@Test
	public void testRoundDown() {
		double toRoundDown = 12.341;
		assertEquals(FORMATTED_NUMBER, processor.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processor2.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_NUMBER, processorChain2.execute(toRoundDown, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-Number input (should throw an Exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonNumber() {
		processor.execute("abc", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid number format (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithInvalidNumberFormat() {
		double number = 12.34;
		CellProcessor invalidNumberFormatProcessor = new FmtNumber("%%%");
		invalidNumberFormatProcessor.execute(number, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid number format String (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullNumberFormatString() {
		double number = 12.34;
		CellProcessor invalidNumberFormatProcessor = new FmtNumber((String) null);
		invalidNumberFormatProcessor.execute(number, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid number format object (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWithNullNumberFormat() {
		double number = 12.34;
		CellProcessor invalidNumberFormatProcessor = new FmtNumber((DecimalFormat) null);
		invalidNumberFormatProcessor.execute(number, ANONYMOUS_CSVCONTEXT);
	}
	
}
