package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class ParseDateTest {
ParseDate cp = null, ccp = null;

@Before
public void setUp() throws Exception {
	cp = new ParseDate("dd/MM/yy");
}

@Test
public void testChaining() throws Exception {
	ccp = new ParseDate("dd/MM/yyyy", new ComparerCellProcessor(TestConstants.EXPECTED_DATE)); // chain
	// processors
	Assert.assertEquals("get date", true, ccp.execute("17/4/2007", TestConstants.ANONYMOUS_CSVCONTEXT));
	
	ccp = new ParseDate("dd-MM-yyyy", new ComparerCellProcessor(TestConstants.EXPECTED_DATE)); // chain
	// processors
	Assert.assertEquals("get date", true, ccp.execute("17-4-2007", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void testInValidInput() throws Exception {
	Assert.assertEquals("never reached", cp.execute("21/21/21", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void test_weird_not_failing_on_InValidInput_wrong_year_format() throws Exception {
	cp.execute("17/04/2007", TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void testInValidInput2() throws Exception {
	Assert.assertEquals("never reached", cp.execute("a date", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("read date", TestConstants.EXPECTED_DATE, cp.execute("17/04/07",
		TestConstants.ANONYMOUS_CSVCONTEXT));
	
	cp = new ParseDate("MM-dd-yy");
	Assert.assertEquals("read date", TestConstants.EXPECTED_DATE, cp.execute("04-17-07",
		TestConstants.ANONYMOUS_CSVCONTEXT));
}
}
