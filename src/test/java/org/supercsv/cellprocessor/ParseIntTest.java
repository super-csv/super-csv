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
public class ParseIntTest {
private static final CSVContext CSVCONTEXT = TestConstants.ANONYMOUS_CSVCONTEXT;
static final int VAL1 = 17;
static final String VAL1_STR = "17";
CellProcessor cp = null, ccp = null;

@Test(expected = SuperCSVException.class)
public void invalid_input() {
	Assert.assertEquals(cp.execute('C', CSVCONTEXT), 'C');
}

@Before
public void setUp() throws Exception {
	cp = new ParseInt();
}

@Test
public void shouldHandleInputOfTypeDoubleWithoutExtraConversion() {
	Assert.assertEquals(VAL1, new ParseInt(new Optional()).execute(VAL1_STR, CSVCONTEXT));
}

@Test
public void testChaining() throws Exception {
	ccp = new ParseInt(new ComparerCellProcessor(VAL1)); // chain processors
	Assert.assertEquals("convert possitive int", true, ccp.execute(VAL1_STR, CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void testInValidInput() throws Exception {
	Assert.assertEquals(cp.execute("hello", CSVCONTEXT), "");
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("convert possitive int", VAL1, cp.execute(VAL1_STR, CSVCONTEXT));
	Assert.assertEquals("convert negative int", -43, cp.execute("-43", CSVCONTEXT));
}

}
