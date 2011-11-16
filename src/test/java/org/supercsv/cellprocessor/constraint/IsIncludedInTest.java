package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Dominique De Vito
 */
public class IsIncludedInTest {

private static final CSVContext CTXT = TestConstants.ANONYMOUS_CSVCONTEXT;
private static final Set<Object> VALUE_SET = new HashSet<Object>();

static {
	VALUE_SET.add(1);
	VALUE_SET.add(2);
	VALUE_SET.add(3);
}

IsIncludedIn cp;
CellProcessor ccp;

@Before
public void setUp() throws Exception {
	cp = new IsIncludedIn(VALUE_SET);
}

@Test 
public void testChaining() throws Exception {
	Integer VALUE = 1;
	ccp = new IsIncludedIn(VALUE_SET, new ComparerCellProcessor(VALUE));
	Assert.assertEquals("chaining test", true, ccp.execute(VALUE, CTXT)); 
}

@Test 
public void testValidInput() throws Exception {
	Integer VALUE = 1;
	ccp = new IsIncludedIn(VALUE_SET);
	Assert.assertEquals("valid input", true, VALUE.equals(ccp.execute(VALUE, CTXT))); 
}

@Test(expected = SuperCSVException.class)
public void testInvalidInput() throws Exception {
	Integer VALUE = 4;
	ccp = new IsIncludedIn(VALUE_SET);
	Assert.assertEquals("invalid input", true, VALUE.equals(ccp.execute(VALUE, CTXT))); 
}

@Test(expected = SuperCSVException.class)
public void testNullInput() throws Exception {
	cp.execute(null, CTXT);
}


}
