package org.supercsv.cellprocessor;

/**
 * @author Dominique De Vito
 */
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

public class StrRegExReplaceTest {

private static final CSVContext CTXT = TestConstants.ANONYMOUS_CSVCONTEXT;
CellProcessorAdaptor cp = new StrRegExReplace("\n", "@n");

@Test(expected = SuperCSVException.class)
public void should_fail_no_effect() throws Exception {
	new StrRegExReplace("", "e");
}

@Test(expected = SuperCSVException.class)
public void should_fail_null() throws Exception {
	new StrRegExReplace(null, null);
}

@Test(expected = SuperCSVException.class)
public void should_fail_secondarg_null() throws Exception {
	new StrRegExReplace("", null);
}

@Test
public void should_replace() throws Exception {
	assertEquals("a@nb", cp.execute("a\nb", CTXT));
}


}
