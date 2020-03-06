package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 * Tests the ParseStringList processor.
 * 
 * @author Jim Judd
 */
public class ParseStringListTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;

	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setup() {
		processor = new ParseStringList();
		processorChain = new ParseStringList(new NotNull());		
	}
	
	/**
	 * Tests unchained/chained execution with a string list of unquoted characters.
	 */
	@Test
	public void testFmtStringListWrite() {
		String formattedString = "AAA,BBB,CCC";
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("AAA");
		expectedList.add("BBB");
		expectedList.add("CCC");

		assertEquals(expectedList, processor.execute(formattedString, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedList, processorChain.execute(formattedString, ANONYMOUS_CSVCONTEXT));
		
	}

	/**
	 * Tests unchained/chained execution with a string list with an embedded comma.
	 */	
	@Test
	public void testFmtStringListWithComma() {
		String formattedString = "AAA,BBB,\"CCC,\"";
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("AAA");
		expectedList.add("BBB");
		expectedList.add("CCC,");

		assertEquals(expectedList, processor.execute(formattedString, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedList, processorChain.execute(formattedString, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests unchained/chained execution with a string list with more embedded commas.
	 */	
	@Test
	public void testFmtStringListWithComma2() {
		String formattedString = "AAA,\"BBB,\",\"CCC,\"";
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("AAA");
		expectedList.add("BBB,");
		expectedList.add("CCC,");

		assertEquals(expectedList, processor.execute(formattedString, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedList, processorChain.execute(formattedString, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a string list with an embedded comma and double quotes.
	 */	
	@Test
	public void testFmtStringListWithCommaAndQuote() {
		String formattedString = "AAA,BBB,\"CCC\"\",\"\"\"";
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("AAA");
		expectedList.add("BBB");
		expectedList.add("CCC\",\"");

		assertEquals(expectedList, processor.execute(formattedString, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedList, processorChain.execute(formattedString, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a string list with an embedded commas and double quotes.
	 */	
	@Test
	public void testFmtStringListWithCommaAndQuote2() {
		String formattedString = "AAA,\"B,B\"\"B\"\",\",\"CCC\"\",\"\"\"";
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("AAA");
		expectedList.add("B,B\"B\",");
		expectedList.add("CCC\",\"");

		assertEquals(expectedList, processor.execute(formattedString, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedList, processorChain.execute(formattedString, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests bad formatted lists and validates that a SuperCsvCellProcessorException is thrown
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testSuperCsvCellProcessorException() {
		String formattedString = "\"A,B";

		processor.execute(formattedString, ANONYMOUS_CSVCONTEXT);
	}

}
