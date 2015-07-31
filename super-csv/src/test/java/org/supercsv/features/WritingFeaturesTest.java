package org.supercsv.features;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;

/**
 * Test class which checks all features listed on <a href="http://csveed.org/comparison-matrix.html">comparison page</a>
 * for "write" operations.
 *
 * @author Michał Ziober
 */
public class WritingFeaturesTest {
	
	@Test
	public void testCustomSeparator() throws IOException {
		List<String> data = Arrays.asList("John", "Connor");
		CellProcessor[] processors = { new NotNull(), new NotNull() };
		
		char customSeparator = '+';
		CsvPreference customPreference = new Builder('"', customSeparator, "").build();
		String result = writeToCsv(data, processors, customPreference);
		
		Assert.assertEquals("John+Connor", result);
	}
	
	@Test
	public void testCustomQuote() throws IOException {
		List<String> data = Collections.singletonList("John \n Connor");
		CellProcessor[] processors = { new NotNull() };
		
		char customQuote = '|';
		CsvPreference customPreference = new Builder(customQuote, ',', "").build();
		String result = writeToCsv(data, processors, customPreference);
		
		Assert.assertEquals("|John  Connor|", result);
	}
	
	@Ignore
	@Test
	public void testCustomEscape() throws IOException {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Test
	public void testCustomEOL() throws IOException {
		List<String> data = Collections.singletonList("John Connor");
		CellProcessor[] processors = { new NotNull() };
		
		String customEndOfLine = ">\r\n";
		CsvPreference customPreference = new Builder('"', ',', customEndOfLine).build();
		String result = writeToCsv(data, processors, customPreference);
		
		Assert.assertEquals("John Connor>\r\n", result);
	}
	
	@Test
	public void testNewLineInDelimitedField() throws IOException {
		List<String> data = Arrays.asList("Jo\nhn", "Con\nnor");
		CellProcessor[] processors = { new NotNull(), new NotNull() };
		
		CsvPreference customPreference = new Builder('"', ',', "\n").build();
		String result = writeToCsv(data, processors, customPreference);
		
		Assert.assertEquals("\"Jo\nhn\",\"Con\nnor\"\n", result);
	}
	
	@Test
	public void testEscapedQuoteInQuotedField() throws IOException {
		List<String> data = Arrays.asList("Joh\"n", "Con\"nor");
		CellProcessor[] processors = { new NotNull(), new NotNull() };
		
		CsvPreference customPreference = new Builder('"', ',', "").build();
		String result = writeToCsv(data, processors, customPreference);
		
		Assert.assertEquals("\"Joh\"\"n\",\"Con\"\"nor\"", result);
	}
	
	@Ignore
	@Test
	public void testDifferentEscapeAndQuote() throws IOException {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Test
	public void testDealWithLeadingTrailingWhitespace() throws IOException {
		List<String> data = Arrays.asList("     John   ", "   Connor     ");
		CellProcessor[] processors = { new Trim(), new Trim() };
		
		char customQuote = '"';
		CsvPreference customPreference = new Builder(customQuote, ',', "").surroundingSpacesNeedQuotes(false).build();
		String result = writeToCsv(data, processors, customPreference);
		
		Assert.assertEquals("John,Connor", result);
	}
	
	@Ignore
	@Test
	public void testColumnIndexBasedMapping() throws IOException {
		throw new UnsupportedOperationException("not implemented yet!");
	}
	
	@Test
	public void testColumnNameBasedMapping() throws IOException {
		FeatureBean character = new FeatureBean("John", "Connor", 16);
		String[] mapping = { "lastName", "firstName" };
		StringWriter writer = new StringWriter();
		CsvBeanWriter beanWriter = new CsvBeanWriter(writer, STANDARD_PREFERENCE);
		beanWriter.write(character, mapping);
		beanWriter.close();
		
		String csv = writer.toString();
		Assert.assertNotNull(csv);
		Assert.assertEquals("Connor,John\r\n", csv);
	}
	
	@Ignore
	@Test
	public void testSupportsAnnotations() throws IOException {
		throw new UnsupportedOperationException("Annotations are not implemented yet!");
	}
	
	@Test
	public void testConvertsToPrimitives() throws IOException {
		FeatureBean character = new FeatureBean("John", "Connor", 16);
		String[] mapping = { "lastName", "firstName", "age" };
		
		StringWriter writer = new StringWriter();
		CsvBeanWriter beanWriter = new CsvBeanWriter(writer, STANDARD_PREFERENCE);
		beanWriter.write(character, mapping);
		beanWriter.close();
		
		String csv = writer.toString();
		Assert.assertNotNull(csv);
		Assert.assertEquals("Connor,John,16\r\n", csv);
	}
	
	@Test
	public void testConvertsToBasicObjects() throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1999);
		calendar.set(Calendar.MONTH, 6);
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		
		FeatureBean character = new FeatureBean("John", "Connor", 16);
		character.setSavings(new BigDecimal(6.65));
		character.setBirthDate(calendar.getTime());
		
		String[] mapping = { "lastName", "firstName", "age", "birthDate", "savings" };
		DecimalFormat formatter = new DecimalFormat();
		formatter.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance());
		CellProcessor[] processors = { new NotNull(), new NotNull(), new NotNull(), new FmtDate("yyyy-MM-dd"),
			new FmtNumber(formatter) };
		
		StringWriter writer = new StringWriter();
		CsvPreference customPreference = new Builder('"', '|', "\r\n").build();
		CsvBeanWriter beanWriter = new CsvBeanWriter(writer, customPreference);
		beanWriter.write(character, mapping, processors);
		beanWriter.close();
		
		String csv = writer.toString();
		Assert.assertNotNull(csv);
		Assert.assertEquals("Connor|John|16|1999-07-12|" + formatter.format(character.getSavings()) + "\r\n", csv);
	}
	
	@Test
	public void testConverterSupport() throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1999);
		calendar.set(Calendar.MONTH, 6);
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		
		FeatureBean character = new FeatureBean("John", "Connor", 16);
		character.setSavings(new BigDecimal(6.65));
		character.setBirthDate(calendar.getTime());
		
		String[] mapping = { "lastName", "firstName", "age", "birthDate", "savings" };
		DecimalFormat formatter = new DecimalFormat();
		formatter.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance());
		CellProcessor[] processors = { new NotNull(), new NotNull(), new NotNull(), new FmtDate("yyyy-MM-dd"),
			new FmtNumber(formatter) };
		
		StringWriter writer = new StringWriter();
		CsvPreference customPreference = new Builder('"', '|', "\r\n").build();
		CsvBeanWriter beanWriter = new CsvBeanWriter(writer, customPreference);
		beanWriter.write(character, mapping, processors);
		beanWriter.close();
		
		String csv = writer.toString();
		Assert.assertNotNull(csv);
		Assert.assertEquals("Connor|John|16|1999-07-12|" + formatter.format(character.getSavings()) + "\r\n", csv);
	}
	
	@Test
	public void testDateSupport() throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1999);
		calendar.set(Calendar.MONTH, 6);
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		
		FeatureBean character = new FeatureBean("John", "Connor", 16);
		character.setBirthDate(calendar.getTime());
		
		String[] mapping = { "birthDate" };
		DecimalFormat formatter = new DecimalFormat();
		formatter.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance());
		CellProcessor[] processors = { new FmtDate("yyyy-MM-dd") };
		
		StringWriter writer = new StringWriter();
		CsvBeanWriter beanWriter = new CsvBeanWriter(writer, STANDARD_PREFERENCE);
		beanWriter.write(character, mapping, processors);
		beanWriter.close();
		
		String csv = writer.toString();
		Assert.assertNotNull(csv);
		Assert.assertEquals("1999-07-12\r\n", csv);
	}
	
	@Test
	public void testDeepConversion() {
		Assert.assertNotNull("See org.supercsv.example.dozer.Writing class!");
	}
	
	@Ignore
	@Test
	public void testSplitCellToMultipleProperties() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Ignore
	@Test
	public void testJoinMultipleCellsIntoOneProperty() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Test
	public void testWriteToWriter() throws IOException {
		StringWriter writer = new StringWriter();
		new CsvListWriter(writer, STANDARD_PREFERENCE);
		new CsvMapWriter(writer, STANDARD_PREFERENCE);
		new CsvBeanWriter(writer, STANDARD_PREFERENCE);
	}
	
	private String writeToCsv(List<String> data, CellProcessor[] processors, CsvPreference preference)
		throws IOException {
		StringWriter writer = new StringWriter();
		CsvListWriter listWriter = new CsvListWriter(writer, preference);
		listWriter.write(data, processors);
		listWriter.close();
		
		return writer.toString();
	}
}
