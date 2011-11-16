package org.supercsv.webtests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class ReadWebExamplesTest {
public static class UserBean {
String username, password, town;
Date date;
int zip;

public Date getDate() {
	return date;
}

public String getPassword() {
	return password;
}

public String getTown() {
	return town;
}

public String getUsername() {
	return username;
}

public int getZip() {
	return zip;
}

public void setDate(final Date date) {
	this.date = date;
}

public void setPassword(final String password) {
	this.password = password;
}

public void setTown(final String town) {
	this.town = town;
}

public void setUsername(final String username) {
	this.username = username;
}

public void setZip(final int zip) {
	this.zip = zip;
}
}

CsvBeanReader inFile = null;

final CellProcessor[] processors = new CellProcessor[] { new Unique(new StrMinMax(5, 20)), new StrMinMax(8, 35),
	new ParseDate("dd/MM/yy"), new Optional(new ParseInt()), null };

@Before
public void setUp() throws Exception {
	final String fileWithHeader = "username, password, date, zip, town\n"
		+ "Klaus,  qwexyKiks, 1/10/2007,  4328,    New York\n";
	inFile = new CsvBeanReader(new StringReader(fileWithHeader), CsvPreference.EXCEL_PREFERENCE);
}

@Test
public void testProcesssedRead() throws Exception {
	UserBean user;
	final String[] header = inFile.getCSVHeader(true);
	assertThat(header[2], is("date"));
	user = inFile.read(UserBean.class, header, processors);
	
	Assert.assertEquals("read elem ", "Klaus", user.getUsername());
	Assert.assertEquals("read elem ", "qwexyKiks", user.getPassword());
	final Date cal = new Date(2007 - 1900, 10 - 1, 1);
	Assert.assertEquals(cal, user.getDate());
	Assert.assertEquals("read elem ", 4328, user.getZip());
	Assert.assertEquals("read elem ", "New York", user.getTown());
}
}
