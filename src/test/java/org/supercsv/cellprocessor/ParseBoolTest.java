package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class ParseBoolTest {
CellProcessor cp = null, ccp = null;

@Before
public void setUp() throws Exception {
	cp = new ParseBool();
}

@Test
public void testChaining() throws Exception {
	ccp = new ParseBool(new ComparerCellProcessor(true)); // chain
	// processors
	Assert.assertEquals("parse true", true, ccp.execute("1", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void testInValidInput() throws Exception {
	cp.execute("foo", TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("convert true", true, cp.execute("true", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert false", false, cp.execute("false", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert 1", true, cp.execute("1", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert 0", false, cp.execute("0", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert y", true, cp.execute("y", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert n", false, cp.execute("n", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert t", true, cp.execute("t", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("convert f", false, cp.execute("f", TestConstants.ANONYMOUS_CSVCONTEXT));
}
}
