package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class FmtNumberTest {
CellProcessor cp = null, ccp = null;

@Before
public void setUp() throws Exception {
	cp = new FmtNumber("00.00");
}

@Test
public void testChaining() throws Exception {
	ccp = new FmtNumber("00.00", new ComparerCellProcessor("12,34")); // chain
	// processors
	Assert.assertEquals("make number", true, ccp.execute(12.34, TestConstants.ANONYMOUS_CSVCONTEXT));
	
}

@Test(expected = SuperCSVException.class)
public void test_null_Input() throws Exception {
	cp.execute(null, TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = ClassCastInputCSVException.class)
public void testInvalidInput() throws Exception {
	cp.execute("text-not-a-number", TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("round up", "12,34", cp.execute(12.339, TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("round down", "12,34", cp.execute(12.344, TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("round down", "12,34", cp.execute(12.344, TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("always 2 decimals", "12,10", new FmtNumber("00.00").execute(12.1,
		TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("no decimals", "12", new FmtNumber("00").execute(12.344, TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void invalidZeroPad() throws Exception {
	Assert.assertFalse("can't leftpad", "000012".equals(new FmtNumber("00000").execute(12,
		TestConstants.ANONYMOUS_CSVCONTEXT)));
}
}
