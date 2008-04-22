package org.supercsv.cellprocessor;

/**
 * @author Kasper B. Graversen
 */
import org.junit.Test;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;
import static org.junit.Assert.assertEquals;

public class StrReplaceTest {

CellProcessorAdaptor cp = new StrReplace("\n", "@n");

@Test(expected = SuperCSVException.class)
public void should_fail_no_effect() throws Exception {
	cp = new StrReplace("", "e");
}

@Test(expected = SuperCSVException.class)
public void should_fail_null() throws Exception {
	cp = new StrReplace(null, null);
}

@Test
public void should_replace() throws Exception {
	assertEquals("a@nb", cp.execute("a\nb", new CSVContext(0, 0)));
}

}
