package org.supercsv.cellprocessor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Dominique De Vito
 */
public class HashMapperTest {

private static final CSVContext CTXT = TestConstants.ANONYMOUS_CSVCONTEXT;
private static final Map<Object,Object> VALUE_MAP = new HashMap<Object,Object>();

static {
	VALUE_MAP.put(1, "1");
	VALUE_MAP.put(2, "2");
	VALUE_MAP.put(3, "3");
}

HashMapper cp;
CellProcessor ccp;

@Before
public void setUp() throws Exception {
	cp = new HashMapper(VALUE_MAP);
}

@Test 
public void testChaining() throws Exception {
	Integer I_VALUE = 1;
	String O_VALUE = Integer.toString(I_VALUE);
	ccp = new HashMapper(VALUE_MAP, new ComparerCellProcessor(O_VALUE));
	Assert.assertEquals("chaining test", true, ccp.execute(I_VALUE, CTXT)); 
}

@Test 
public void testValidInput() throws Exception {
	Integer I_VALUE = 1;
	String O_VALUE = Integer.toString(I_VALUE);
	ccp = new HashMapper(VALUE_MAP);
	Assert.assertEquals("valid input", true, O_VALUE.equals(ccp.execute(I_VALUE, CTXT))); 
}

@Test 
public void testNotFoundInput() throws Exception {
	Integer I_VALUE = 4;
	ccp = new HashMapper(VALUE_MAP);
	Assert.assertEquals("invalid input", true,  ccp.execute(I_VALUE, CTXT) == null); 
}

@Test 
public void testNotFoundButDefaultInput() throws Exception {
	Integer I_VALUE = 4;
	String O_VALUE = Integer.toString(I_VALUE);
	ccp = new HashMapper(VALUE_MAP, O_VALUE);
	Assert.assertEquals("invalid input", true,  O_VALUE.equals(ccp.execute(I_VALUE, CTXT))); 
}

@Test(expected = SuperCSVException.class)
public void testNullInput() throws Exception {
	cp.execute(null, CTXT);
}


}
