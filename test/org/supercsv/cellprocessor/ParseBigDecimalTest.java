package org.supercsv.cellprocessor;

import java.math.BigDecimal;

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
public class ParseBigDecimalTest {
private static final CSVContext CSVCONTEXT = TestConstants.ANONYMOUS_CSVCONTEXT;
private static final BigDecimal VAL2 = new BigDecimal("-43.0");
private static final String VAL2STR = "-43.0";
private static final BigDecimal VAL1 = new BigDecimal("17.3");
private static final String VAL1_STR_ = "17.3";
CellProcessor cp = null, ccp = null;

@Test(expected = SuperCSVException.class)
public void invalid_input() {
	Assert.assertEquals(cp.execute('C', CSVCONTEXT), 'C');
}

@Before
public void setUp() throws Exception {
	cp = new ParseBigDecimal();
}

@Test(expected = SuperCSVException.class)
public void test_null_Input() throws Exception {
	cp.execute(null, CSVCONTEXT);
}

@Test
public void testChaining() throws Exception {
	ccp = new ParseBigDecimal(new ComparerCellProcessor(VAL1));
	Assert.assertEquals("convert possitive ", true, ccp.execute(VAL1_STR_, CSVCONTEXT));
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
	Assert.assertEquals("convert possitive", VAL1, cp.execute(VAL1_STR_, CSVCONTEXT));
	Assert.assertEquals("convert negative", VAL2, cp.execute(VAL2STR, CSVCONTEXT));
}

}
