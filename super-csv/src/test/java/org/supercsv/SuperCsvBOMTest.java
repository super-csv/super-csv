package org.supercsv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseSqlTime;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.mock.CustomerBean;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

/**
 * Test the super-csv read API processing with BOM file
 */
public class SuperCsvBOMTest {
	
	public static final String ROOT_PATH = SuperCsvBOMTest.class.getResource("/").getPath() + "/";
	
	public static final String UTF8_FILE = ROOT_PATH + "customers_utf8.csv";
	
	public static final String UTF8_NO_BOM_FILE = ROOT_PATH + "customers_utf8_nobom.csv";
	
	public static final String UTF16BE_FILE = ROOT_PATH + "customers_utf16be.csv";
	
	public static final String UTF16LE_FILE = ROOT_PATH + "customers_utf16le.csv";
	
	/**
	 * Test the super-csv read API processing UTF8 without BOM file.
	 */
	@Test
	public void testUTF8() throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(UTF8_FILE), "UTF-8")
		);
		ReadTestCSVFile(reader);
	}
	
	/**
	 * Test the super-csv read API processing UTF8 with BOM file.
	 */
	@Test
	public void testUTF8WithoutBom() throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(UTF8_NO_BOM_FILE), "UTF-8")
		);
		ReadTestCSVFile(reader);
	}
	
	/**
	 * Test the super-csv read API processing UTF16BE file.
	 */
	@Test
	public void testUTF16BE() throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(UTF16BE_FILE), "UTF-16be")
		);
		ReadTestCSVFile(reader);
	}
	
	/**
	 * Test the super-csv read API processing UTF16 file.
	 */
	@Test
	public void testUTF16LE() throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(UTF16LE_FILE), "UTF-16le")
		);
		ReadTestCSVFile(reader);
	}
	
	public void ReadTestCSVFile(Reader reader) throws IOException {
		ICsvBeanReader beanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
		final String[] header = beanReader.getHeader(true);
		assertEquals("customerNo", header[0]);
		CustomerBean customer = null;
		final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
		StrRegEx.registerMessage(emailRegex, "must be a valid email address");
		final CellProcessor[] processors = new CellProcessor[]{new UniqueHashCode(), // customerNo (must be unique)
				new NotNull(), // firstName
				new NotNull(), // lastName
				new ParseDate("dd/MM/yyyy"), // birthDate
				new ParseSqlTime("HH:mm:ss"), // birthTime
				new NotNull(), // mailingAddress
				new Optional(new ParseBool()), // married
				new Optional(new ParseInt()), // numberOfKids
				new NotNull(), // favouriteQuote
				new StrRegEx(emailRegex), // email
				new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
		};
		customer = beanReader.read(CustomerBean.class, header, processors);
		assertEquals("1", customer.getCustomerNo());
		assertEquals("John", customer.getFirstName());
		assertEquals("jdunbar@gmail.com", customer.getEmail());
		assertEquals(0, customer.getLoyaltyPoints());
		beanReader.close();
	}
}
