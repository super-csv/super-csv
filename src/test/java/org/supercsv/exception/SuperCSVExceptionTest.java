package org.supercsv.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.util.CSVContext;

public class SuperCSVExceptionTest {

@Test
public void get_should_work() {
	final CSVContext context = new CSVContext(1, 2);
	final SuperCSVException ex = new SuperCSVException("msg", context);
	
	assertEquals(context, ex.getCsvContext());
}

@Test
public void set_should_work() {
	final CSVContext context = new CSVContext(1, 2);
	final SuperCSVException ex = new SuperCSVException("msg");
	ex.setCsvContext(context);
	assertEquals(context, ex.getCsvContext());
}
}
