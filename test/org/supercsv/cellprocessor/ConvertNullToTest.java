package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

public class ConvertNullToTest {
CSVContext context;

@Before
public void setUp() {
	context = new CSVContext(1, 1);
}

@Test
public void should_chain() {
	final ConvertNullTo cp = new ConvertNullTo("retVal", new ComparerCellProcessor("retVal"));
	assertEquals("retVal", cp.execute(null, context));
}

@Test
public void should_handle_stdarg() {
	final ConvertNullTo cp = new ConvertNullTo("retVal");
	assertEquals("retVal", cp.execute(null, context));
}
}
