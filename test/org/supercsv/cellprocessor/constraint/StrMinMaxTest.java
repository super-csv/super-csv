package org.supercsv.cellprocessor.constraint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class StrMinMaxTest {
static final int MINVAL = 2, MAXVAL = 10;
StrMinMax cp = null;
CellProcessor ccp = null;

@Test(expected = SuperCSVException.class)
public void invalidInputTest() throws Exception {
	assertEquals("test length", "", cp.execute("helphelphelphelphelp", TestConstants.ANONYMOUS_CSVCONTEXT)); // too
	// long
	// input
}

@Test(expected = SuperCSVException.class)
public void invalidminMaxTest() throws Exception {
	assertEquals("max < min", 0, new StrMinMax(MAXVAL, MINVAL));
}

@Test(expected = SuperCSVException.class)
public void invalidminMaxTest_c2() throws Exception {
	assertEquals("max < min", 0, new StrMinMax(MAXVAL, MINVAL, new Optional()));
}

@Before
public void setUp() throws Exception {
	cp = new StrMinMax(MINVAL, MAXVAL);
}

@Test
public void testCastValueAndChaining() throws Exception {
	ccp = new StrMinMax(MINVAL, MAXVAL, new ComparerCellProcessor("17"));
	assertEquals("number changed to string", true, ccp.execute(17, TestConstants.ANONYMOUS_CSVCONTEXT)); // convert
	// number
	// 17
	// to a
	// string
}

@Test(expected = SuperCSVException.class)
public void testInValidInput() throws Exception {
	new StrMinMax(-1, MAXVAL); // cannot pass negative
}

@Test
public void testValidInput() throws Exception {
	
}

@Test
public void validChainingTest() throws Exception {
	ccp = new StrMinMax(MINVAL, MAXVAL, new Optional());
	assertEquals("test chaining and ", "17", ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
}
}
