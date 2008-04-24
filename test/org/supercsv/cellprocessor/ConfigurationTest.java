package org.supercsv.cellprocessor;

import org.junit.Test;
import org.supercsv.prefs.CsvPreference;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * This is testing the parts of the configuration that the other tests have not covered
 * 
 * @author Kasper B. Graversen
 */
public class ConfigurationTest {

@Test
public void getsetTest() {
	final CsvPreference pref = new CsvPreference('"', ',', "\n");
	assertThat(pref.getDelimiterChar(), is((int) ','));
	assertThat(pref.getQuoteChar(), is((int) '"'));
	assertThat(pref.getEndOfLineSymbols(), is("\n"));
}
}
