package org.supercsv.cellprocessor;

import java.util.Calendar;
import java.util.Date;

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
public class FmtDateTest {
private static final CSVContext CTXT = new CSVContext(0, 0);
CellProcessor cp = null, ccp = null;

@Before
public void setUp() throws Exception {
	cp = new FmtDate("dd/MM/yyyy");
}

@Test
public void testChaining() throws Exception {
	ccp = new FmtDate("dd/MM/yyyy", new ComparerCellProcessor("17/04/2007")); // chain
	// processors
	Assert.assertEquals("get date", true, ccp.execute(getDayDate(2007,4,17), CTXT));
	
	ccp = new FmtDate("dd-MM-yyyy", new ComparerCellProcessor("17-04-2007")); // chain
	// processors
	Assert.assertEquals("get date", true, ccp.execute(getDayDate(2007,4,17), CTXT));
}

@Test
public void testGoAndBack() throws Exception { 
	ccp = new FmtDate("dd/MM/yyyy", new ParseDate("dd/MM/yyyy")); // chain
	// processors
	Date date = getDayDate(2007,4,17);
	Assert.assertEquals("go and back", true, date.equals(ccp.execute(date, CTXT)));

	ccp = new ParseDate("dd/MM/yyyy", new FmtDate("dd/MM/yyyy")); // chain
	// processors
	String sDate = "17/04/2007";
	Assert.assertEquals("go and back", true, sDate.equals(ccp.execute(sDate, CTXT)));
}

@Test(expected = SuperCSVException.class)
public void test_null_Input() throws Exception {
	cp.execute(null, CTXT);
}

@Test(expected = SuperCSVException.class)
public void testEmptyInput() throws Exception {
	cp.execute("", CTXT);
}

@Test(expected = ClassCastInputCSVException.class)
public void testInvalidInput() throws Exception {
	cp.execute("text-not-a-date", CTXT);
}

private static Date getDayDate(int year, int month, int day) {
	Calendar cal = Calendar.getInstance();
	cal.set(year, month-1, day, 0, 0, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
}
}
