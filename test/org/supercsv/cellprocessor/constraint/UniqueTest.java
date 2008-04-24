package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class UniqueTest {
CellProcessor cp, ccp;

@Before
public void setUp() throws Exception {
	cp = new Unique();
}

@Test(expected = SuperCSVException.class)
public void testInNonUniqueInput() throws Exception {
	Assert.assertEquals("first insert ok", "zello", cp.execute("zello", new CSVContext(0, 0)));
	Assert.assertEquals("second insert must fail", "zello", cp.execute("zello", new CSVContext(0, 0)));
}

public void testInValidInput() throws Exception {
	Assert.assertEquals("first insert ok", "zello", cp.execute("zello", new CSVContext(0, 0)));
}

@Test
public void validInputAndChainingTest() throws Exception {
	ccp = new Unique(new ParseLong());
	// test numbers and chaining
	Assert.assertEquals("test numbers", 17L, ccp.execute("17", new CSVContext(0, 0)));
	Assert.assertEquals("test negative numbers", -17L, ccp.execute("-17", new CSVContext(0, 0)));
}

@Test
public void validInputTest() throws Exception {
	// simpletest
	Assert.assertEquals("test lowercase", "hello", cp.execute("hello", new CSVContext(0, 0)));
	Assert.assertEquals("test uppercase", "HELLO", cp.execute("HELLO", new CSVContext(0, 0)));
	Assert.assertEquals("test empty", "", cp.execute("", new CSVContext(0, 0)));
	
}
}
