package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * @author Kasper B. Graversen
 */
public class LMinMaxTest {
LMinMax cp;
CellProcessor ccp;

@Test(expected = SuperCSVException.class)
public void invalidmaxInputTest() throws Exception {
	Assert.assertEquals("max boundary +1", LMinMax.MAXC + 1, cp.execute(LMinMax.MAXC + 1,
		TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void invalidminInputTest() throws Exception {
	Assert.assertEquals("min boundary -1", 0 - 1, cp.execute(0 - 1, TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void invalidminMaxTest() throws Exception {
	Assert.assertEquals("max < min", 0, new LMinMax(TestConstants.VALUE_100_AS_INT, TestConstants.VALUE_17_AS_LONG));
}

@Test(expected = SuperCSVException.class)
public void invalidminMaxTest_c2() throws Exception {
	Assert.assertEquals("max < min", 0, new LMinMax(TestConstants.VALUE_100_AS_INT, TestConstants.VALUE_17_AS_LONG,
		null));
}

@Before
public void setUp() throws Exception {
	cp = new LMinMax(0, LMinMax.MAXC);
}

@Test
public void validChainingTest() throws Exception {
	// chaining
	ccp = new LMinMax(1, TestConstants.VALUE_100_AS_INT, new Optional());
	Assert.assertEquals("test chaining ", TestConstants.VALUE_17_AS_LONG, ccp.execute(TestConstants.VALUE_17_AS_STRING,
		TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("test chaining ", TestConstants.VALUE_17_AS_LONG, ccp.execute(new Long(
		TestConstants.VALUE_17_AS_INT), TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("min boundary as text", 0L, cp.execute("0", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("min boundary as number", 0L, cp.execute(0, TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("max boundary as number", (long) LMinMax.MAXC, cp.execute(LMinMax.MAXC,
		TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test(expected = SuperCSVException.class)
public void should_not_allow_non_number() {
	new LMinMax(0, TestConstants.VALUE_100_AS_INT).execute("non number", TestConstants.ANONYMOUS_CSVCONTEXT);
}

}
