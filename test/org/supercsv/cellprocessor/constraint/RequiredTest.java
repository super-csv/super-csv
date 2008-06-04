package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class RequiredTest {
CellProcessor cp = null;
CellProcessor ccp = null;

@Test(expected = SuperCSVException.class)
public void invalidInputTest1() throws Exception {
	Assert.assertEquals("test length", "holp".hashCode(), cp.execute("help".hashCode(),
		TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void invalidInputTest2() throws Exception {
	Assert.assertEquals("can't add the same twice", 1, new Required(1, 1).execute("help".hashCode(), new CSVContext(0,
		0)));
}

@Before
public void setUp() throws Exception {
	cp = new Required(1, 3, 6, "hello".hashCode()); // several
	// constructors
}

@Test
public void validChainingTest() throws Exception {
	// chaining
	ccp = new Required(3, new Optional());
	Assert.assertEquals("test chaining ", 3, ccp.execute(3, TestConstants.ANONYMOUS_CSVCONTEXT));
	// chaining
	ccp = new Required(new int[] { 3, 4 }, new Optional());
	Assert.assertEquals("test chaining and array of init", 3, ccp.execute(3, TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("known array hashes", "hello".hashCode(), cp.execute("hello".hashCode(),
		TestConstants.ANONYMOUS_CSVCONTEXT));
	
	final CellProcessor cp2 = new Required("hello".hashCode()); // one arg
	// constructor
	Assert.assertEquals("one number constructor test", "hello".hashCode(), cp2.execute("hello".hashCode(),
		TestConstants.ANONYMOUS_CSVCONTEXT));
}

}
