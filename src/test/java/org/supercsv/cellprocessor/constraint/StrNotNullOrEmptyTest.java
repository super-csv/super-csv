package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Dominique De Vito
 */
public class StrNotNullOrEmptyTest {

private static final CSVContext CTXT = TestConstants.ANONYMOUS_CSVCONTEXT;

StrNotNullOrEmpty cp;
CellProcessor ccp;

@Before
public void setUp() throws Exception {
	cp = new StrNotNullOrEmpty();
}

public void testChaining() throws Exception {
	String VALUE = "some value";
	ccp = new StrNotNullOrEmpty(new ComparerCellProcessor(VALUE));
	Assert.assertEquals("chaining test", true, VALUE.equals(ccp.execute(VALUE, CTXT))); 
}

@Test 
public void testValidInput() throws Exception {
	Assert.assertEquals("test length", "help", cp.execute("help", CTXT));
}

@Test(expected = SuperCSVException.class)
public void testNullInput() throws Exception {
	cp.execute(null, CTXT);
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", CTXT);
}
}
