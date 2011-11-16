package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class ParseLongTest {
/**
 * 
 */
private static final CSVContext CSVCONTEXT = TestConstants.ANONYMOUS_CSVCONTEXT;
CellProcessor cp = null, ccp = null;

@Test(expected = SuperCSVException.class)
public void invalid_input() {
	Assert.assertEquals(cp.execute('C', CSVCONTEXT), 'C');
}

@Before
public void setUp() throws Exception {
	cp = new ParseLong();
}

@Test
public void testChaining() throws Exception {
	ccp = new ParseLong(new ComparerCellProcessor(17L)); // chain
	// processors
	Assert.assertEquals("chained convert possitive long", true, ccp.execute("17", CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void testInValidInput() throws Exception {
	Assert.assertEquals("", cp.execute("hello", CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("convert possitive long", 17L, cp.execute("17", CSVCONTEXT));
	Assert.assertEquals("convert negative long", -43L, cp.execute("-43", CSVCONTEXT));
	Assert.assertEquals("convert negative long", -43L, cp.execute(new Long(-43), CSVCONTEXT));
}
}
