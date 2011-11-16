package org.supercsv.cellprocessor;

/**
 * @author Kasper B. Graversen
 */
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.exception.SuperCSVException;

public class StrReplaceTest {

CellProcessorAdaptor cp = new StrReplace("\n", "@n");

@Test(expected = SuperCSVException.class)
public void should_fail_no_effect() throws Exception {
	new StrReplace("", "e");
}

@Test(expected = SuperCSVException.class)
public void should_fail_null() throws Exception {
	new StrReplace(null, null);
}

@Test(expected = SuperCSVException.class)
public void should_fail_secondarg_null() throws Exception {
	new StrReplace("", null);
}

@Test
public void should_replace() throws Exception {
	assertEquals("a@nb", cp.execute("a\nb", TestConstants.ANONYMOUS_CSVCONTEXT));
}

}
