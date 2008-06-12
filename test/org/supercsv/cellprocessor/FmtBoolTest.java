package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Dominique De Vito
 */
public class FmtBoolTest {

private static final String TRUE_VALUE = "y";
private static final String FALSE_VALUE = "n";

CellProcessor cp, ccp;

@Before
public void setUp() throws Exception {
	cp = new FmtBool(TRUE_VALUE, FALSE_VALUE);
}

@Test
public void testChaining() throws Exception {
	ccp = new FmtBool(TRUE_VALUE, FALSE_VALUE, new ComparerCellProcessor(TRUE_VALUE)); // chain
	// processors
	Assert.assertEquals("make boolean", true, ccp.execute(Boolean.TRUE, new CSVContext(0, 0)));
	
	ccp = new FmtBool(TRUE_VALUE, FALSE_VALUE, new ComparerCellProcessor(FALSE_VALUE)); // chain
	// processors
	Assert.assertEquals("make boolean", true, ccp.execute(Boolean.FALSE, new CSVContext(0, 0)));
	
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute(null, new CSVContext(0, 0));
}

@Test(expected = ClassCastInputCSVException.class)
public void testInvalidInput() throws Exception {
	cp.execute("text-not-a-boolean", new CSVContext(0, 0));
}


}
