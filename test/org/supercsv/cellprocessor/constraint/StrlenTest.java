package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
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
public class StrlenTest {
Strlen cp;
CellProcessor ccp;

@Test(expected = SuperCSVException.class)
public void invalidInputTest() throws Exception {
	Assert.assertEquals("test length", "help", cp.execute("help", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Before
public void setUp() throws Exception {
	cp = new Strlen(2);
}

public void testCastValueAndChaining() throws Exception {
	ccp = new Strlen(2, new ComparerCellProcessor("17"));
	Assert.assertEquals("number changed to string", true, ccp.execute(17, TestConstants.ANONYMOUS_CSVCONTEXT)); // convert
	// number 17 to
	// a string
}

@Test(expected = SuperCSVException.class)
public void testInValidArrInput() throws Exception {
	new Strlen(2, -1); // cannot pass a 0
}

@Test(expected = SuperCSVException.class)
public void testInValidInput() throws Exception {
	new Strlen(-1); // cannot pass a 0
}

@Test
public void validChainingTest() throws Exception {
	// chaining
	ccp = new Strlen(2, new Optional());
	Assert.assertEquals("test chaining and ", "17", ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
	ccp = new Strlen(new int[] { 2 }, new Optional());
	Assert.assertEquals("test chaining and ", "17", ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("test length", "he", cp.execute("he", TestConstants.ANONYMOUS_CSVCONTEXT));
	cp = new Strlen(1, 3);
	Assert.assertEquals("one of many req. lengths", "hel", cp.execute("hel", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("one of many req. lengths", "h", cp.execute("h", TestConstants.ANONYMOUS_CSVCONTEXT));
}
}
