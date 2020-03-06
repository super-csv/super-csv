package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Tests the FmtStringList processor.
 * 
 * @author Jim Judd
 */
public class FmtStringListTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() throws Exception {
		processor = new FmtStringList();
		processorChain = new FmtStringList(new NotNull());
	}

	/**
	 * Tests unchained/chained execution with a string list of unquoted characters.
	 */
	@Test
	public void testFmtStringList() {
		List<String> strList = new ArrayList<String>();
		strList.add("AAA");
		strList.add("BBB");
		strList.add("CCC");
		String formattedString = "AAA,BBB,CCC";
		
		assertEquals(formattedString, processor.execute(strList, ANONYMOUS_CSVCONTEXT));
		assertEquals(formattedString, processorChain.execute(strList, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests unchained/chained execution with a string list with an embedded comma.
	 */	
	@Test
	public void testFmtStringListWithQuote() {
		List<String> strList = new ArrayList<String>();
		strList.add("AAA");
		strList.add("B\"B\"B");
		strList.add("CCC");
		String formattedString = "AAA,\"B\"\"B\"\"B\",CCC";
		
		assertEquals(formattedString, processor.execute(strList, ANONYMOUS_CSVCONTEXT));
		assertEquals(formattedString, processorChain.execute(strList, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a string list with more embedded commas.
	 */	
	@Test
	public void testFmtStringListWithComma() {
		List<String> strList = new ArrayList<String>();
		strList.add("AAA");
		strList.add("B,B,B");
		strList.add("CCC");
		String formattedString = "AAA,\"B,B,B\",CCC";
		
		assertEquals(formattedString, processor.execute(strList, ANONYMOUS_CSVCONTEXT));
		assertEquals(formattedString, processorChain.execute(strList, ANONYMOUS_CSVCONTEXT));
	}

	/**
	 * Tests unchained/chained execution with a string list with an embedded comma and double quotes.
	 */	
	@Test
	public void testFmtStringListWithCRLF() {
		List<String> strList = new ArrayList<String>();
		strList.add("AAA");
		strList.add("BB\r\nB");
		strList.add("CCC");
		String formattedString = "AAA,\"BB\r\nB\",CCC";
		
		assertEquals(formattedString, processor.execute(strList, ANONYMOUS_CSVCONTEXT));
		assertEquals(formattedString, processorChain.execute(strList, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a string list with an embedded commas and double quotes.
	 */	
	@Test
	public void testFmtStringListWithSingle() {
		List<String> strList = new ArrayList<String>();
		strList.add("AAA");

		String formattedString = "AAA";
		
		assertEquals(formattedString, processor.execute(strList, ANONYMOUS_CSVCONTEXT));
		assertEquals(formattedString, processorChain.execute(strList, ANONYMOUS_CSVCONTEXT));
	}
}
