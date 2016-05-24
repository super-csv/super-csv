package org.supercsv.i18n;

import static org.junit.Assert.assertEquals;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;
import static org.supercsv.SuperCsvTestUtils.date;
import static org.supercsv.SuperCsvTestUtils.time;

import java.util.Locale;

import org.junit.Test;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.FmtSqlTime;
import org.supercsv.exception.SuperCsvException;

/**
 * Tests I18N messages.
 * 
 * @author Jesús G. Vences
 */
public class I18NTest {
	private static final Locale EN = Locale.ENGLISH;
	private static final Locale ES = new Locale("es");

	/**
	 * Tests execution of FmtDate with a invalid input type (should throw an Exception with a message in English).
	 */
	@Test
	public void testFmtDateWithInvalidInputTypeEnglish() {
		FmtDate invalidDateFormatProcessor = new FmtDate("abcd");
		SuperCsvMessages.setDefaultLocale(EN);
		try {
			invalidDateFormatProcessor.execute(10, ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("the input value should be of type java.util.Date but is java.lang.Integer", e.getMessage());
		}
	}

	/**
	 * Tests execution of FmtDate with a invalid input type (should throw an Exception with a message in Spanish).
	 */
	@Test
	public void testFmtDateWithInvalidInputTypeSpanish() {
		FmtDate invalidDateFormatProcessor = new FmtDate("abcd");
		SuperCsvMessages.setDefaultLocale(ES);
		try {
			invalidDateFormatProcessor.execute(10, ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("el valor de entrada debe ser de tipo java.util.Date sin embargo es java.lang.Integer", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtDate with a invalid input (should throw an Exception with a message in English).
	 */
	@Test
	public void testFmtDateWithInvalidDateFormatEnglish() {
		FmtDate invalidDateFormatProcessor = new FmtDate("abcd");
		SuperCsvMessages.setDefaultLocale(EN);
		try {
			invalidDateFormatProcessor.execute(date(2011, 12, 25), ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("'abcd' is not a valid date format", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtDate with a invalid input (should throw an Exception with a message in Spanish).
	 */
	@Test
	public void testFmtDateWithInvalidDateFormatSpanish() {
		FmtDate invalidDateFormatProcessor = new FmtDate("abcd");
		SuperCsvMessages.setDefaultLocale(ES);
		try {
			invalidDateFormatProcessor.execute(date(2011, 12, 25), ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("'abcd' no es un formato de fecha válido", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtNumber with a invalid input (should throw an Exception with a message in English).
	 */
	@Test
	public void testFmtNumberWithInvalidNumberFormatEnglish() {
		FmtNumber invalidNumberFormatProcessor = new FmtNumber("abcd");
		SuperCsvMessages.setDefaultLocale(EN);
		try {
			invalidNumberFormatProcessor.execute(10, ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("'abcd' is not a valid decimal format", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtNumber with a invalid input (should throw an Exception with a message in Spanish).
	 */
	@Test
	public void testFmtNumberWithInvalidNumberFormatSpanish() {
		FmtNumber invalidNumberFormatProcessor = new FmtNumber("abcd");
		SuperCsvMessages.setDefaultLocale(ES);
		try {
			invalidNumberFormatProcessor.execute(10, ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("'abcd' no es un formato de número decimal", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtSql with a invalid input (should throw an Exception with a message in Spanish).
	 */
	@Test
	public void testFmtSqlTimeWithInvalidDateFormatSpanish() {
		FmtSqlTime invalidDateFormatProcessor = new FmtSqlTime("abcd");
		SuperCsvMessages.setDefaultLocale(ES);
		try {
			invalidDateFormatProcessor.execute(time(1, 12, 25), ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("'abcd' no es un formato de fecha válido", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtSql with a invalid input (should throw an Exception with a message in English).
	 */
	@Test
	public void testFmtSqlTimeWithInvalidDateFormatEnglish() {
		FmtSqlTime invalidDateFormatProcessor = new FmtSqlTime("abcd");
		SuperCsvMessages.setDefaultLocale(EN);
		try {
			invalidDateFormatProcessor.execute(time(1, 12, 25), ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("'abcd' is not a valid date format", e.getMessage());
		}
	}
}
