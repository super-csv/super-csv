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

private static final CSVContext CTXT = new CSVContext(0, 0);
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
	Assert.assertEquals("make boolean", true, ccp.execute(Boolean.TRUE, CTXT));
	
	ccp = new FmtBool(TRUE_VALUE, FALSE_VALUE, new ComparerCellProcessor(FALSE_VALUE)); // chain
	// processors
	Assert.assertEquals("make boolean", true, ccp.execute(Boolean.FALSE, CTXT));
	
}

@Test
public void testGoAndBack() throws Exception { 
	ccp = new FmtBool(TRUE_VALUE, FALSE_VALUE, new ParseBool(TRUE_VALUE, FALSE_VALUE)); // chain
	// processors
	Assert.assertEquals("go and back", true, Boolean.TRUE.equals(ccp.execute(Boolean.TRUE, CTXT)));
	Assert.assertEquals("go and back", true, Boolean.FALSE.equals(ccp.execute(Boolean.FALSE, CTXT)));

	ccp = new ParseBool(TRUE_VALUE, FALSE_VALUE, new FmtBool(TRUE_VALUE, FALSE_VALUE)); // chain
	// processors
	Assert.assertEquals("go and back", true, TRUE_VALUE.equals(ccp.execute(TRUE_VALUE, CTXT)));
	Assert.assertEquals("go and back", true, FALSE_VALUE.equals(ccp.execute(FALSE_VALUE, CTXT)));
	
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute(null, CTXT);
}

@Test(expected = ClassCastInputCSVException.class)
public void testInvalidInput() throws Exception {
	cp.execute("text-not-a-boolean", CTXT);
}


}
