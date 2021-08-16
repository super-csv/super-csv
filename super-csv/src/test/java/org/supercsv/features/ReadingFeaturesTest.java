/*
 * Copyright 2007 Kasper B. Graversen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.features;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentMatches;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;

import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;

/**
 * Test class which checks all features listed on <a href="http://csveed.org/comparison-matrix.html">comparison page</a>
 * for "read" operations.
 *
 * @author Michał Ziober
 */
public class ReadingFeaturesTest {

	private DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();

	@Test
	public void testCustomSeparator() throws IOException {
		String csv = "John+Connor";
		CellProcessor[] processors = { new NotNull(), new NotNull() };

		String customSeparator = "+";
		CsvPreference customPreference = new Builder('"', customSeparator, "").build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<Object> result = listReader.read(processors);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("John", result.get(0));
		Assert.assertEquals("Connor", result.get(1));
	}

	@Test
	public void testCustomQuote() throws IOException {
		String csv = "|John  Connor|";
		CellProcessor[] processors = { new NotNull() };

		char customQuote = '|';
		CsvPreference customPreference = new Builder(customQuote, ",", "").build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<Object> result = listReader.read(processors);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("John  Connor", result.get(0));
	}

	@Ignore
	@Test
	public void testCustomEscape() throws IOException {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Test
	public void testCustomEOL() throws IOException {
		String csv = "John,Connor\r>\n";
		CellProcessor[] processors = { new NotNull(), new NotNull() };

		String customEndOfLine = "\r>\n";
		CsvPreference customPreference = new Builder('"', ",", customEndOfLine).build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<Object> result = listReader.read(processors);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("John", result.get(0));
		Assert.assertEquals("Connor", result.get(1));
	}

	@Test
	public void testNewLineInDelimitedField() throws IOException {
		String csv = "\"Jo\nhn\",\"Con\nnor\"\n";
		CellProcessor[] processors = { new NotNull(), new NotNull() };

		CsvPreference customPreference = new Builder('"', ",", "\n").build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<Object> result = listReader.read(processors);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("Jo\nhn", result.get(0));
		Assert.assertEquals("Con\nnor", result.get(1));
	}

	@Test
	public void testEscapedQuoteInQuotedField() throws IOException {
		String csv = "\"Joh\"\"n\",\"Con\"\"nor\"";
		CellProcessor[] processors = { new NotNull(), new NotNull() };

		CsvPreference customPreference = new Builder('"', ",", "").build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<Object> result = listReader.read(processors);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("Joh\"n", result.get(0));
		Assert.assertEquals("Con\"nor", result.get(1));
	}

	@Ignore
	@Test
	public void testDifferentEscapeAndQuote() throws IOException {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Test
	public void testDealWithLeadingTrailingWhitespace() throws IOException {
		String csv = "     John    ,     Connor   ";
		CellProcessor[] processors = { new Trim(), new Trim() };

		char customQuote = '"';
		CsvPreference customPreference = new Builder(customQuote, ",", "").build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<Object> result = listReader.read(processors);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("John", result.get(0));
		Assert.assertEquals("Connor", result.get(1));
	}

	@Test
	public void testGetByColumnIndex() throws IOException {
		String csv = "John,Connor";

		CsvListReader listReader = new CsvListReader(new StringReader(csv), STANDARD_PREFERENCE);
		List<String> line = listReader.read();

		Assert.assertNotNull(line);
		Assert.assertEquals(2, line.size());
		Assert.assertEquals("John", line.get(0));
		Assert.assertEquals("Connor", line.get(1));
	}

	@Test
	public void testGetByColumnName() throws IOException {
		String csv = "John,Connor";
		String[] mapping = { "firstName", "lastName" };

		CsvMapReader mapReader = new CsvMapReader(new StringReader(csv), STANDARD_PREFERENCE);
		Map<String, String> line = mapReader.read(mapping);

		Assert.assertNotNull(line);
		Assert.assertEquals(2, line.size());
		Assert.assertEquals("John", line.get("firstName"));
		Assert.assertEquals("Connor", line.get("lastName"));
	}

	@Test
	public void testReadSingleLineStreaming() throws IOException {
		String csv = "Sarah,Connor\r\nJohn,Connor\r\nKyle,Reese";

		CsvListReader listReader = new CsvListReader(new StringReader(csv), STANDARD_PREFERENCE);
		// skip first line
		listReader.read();
		// read second line
		List<String> line = listReader.read();

		Assert.assertNotNull(line);
		Assert.assertEquals(2, line.size());
		Assert.assertEquals("John", line.get(0));
		Assert.assertEquals("Connor", line.get(1));
	}

	@Ignore
	@Test
	public void testReadAllLines() throws IOException {
		throw new UnsupportedOperationException("'reader.readAll()' not implemented yet!");
	}

	@Test
	public void testSkipCommentLines() throws IOException {
		String csv = "<!--Sarah,Connor-->\r\nJohn,Connor\r\n<!--Kyle,Reese-->";

		CsvPreference customPreference = new Builder(STANDARD_PREFERENCE).skipComments(new CommentMatches("<!--.*-->"))
			.build();
		CsvListReader listReader = new CsvListReader(new StringReader(csv), customPreference);
		List<String> line = listReader.read();
		List<String> emptyLine = listReader.read();

		Assert.assertNotNull(line);
		Assert.assertEquals(2, line.size());
		Assert.assertEquals("John", line.get(0));
		Assert.assertEquals("Connor", line.get(1));
		Assert.assertNull(emptyLine);
	}

	@Test
	public void testIgnoreEmptyLines() throws IOException {
		String csv = "\r\n\n\nJohn,Connor\r\n\r\n";

		CsvListReader listReader = new CsvListReader(new StringReader(csv), STANDARD_PREFERENCE);
		List<String> line = listReader.read();
		List<String> emptyLine = listReader.read();

		Assert.assertNotNull(line);
		Assert.assertEquals(2, line.size());
		Assert.assertEquals("John", line.get(0));
		Assert.assertEquals("Connor", line.get(1));
		Assert.assertNull(emptyLine);
	}

	@Ignore
	@Test
	public void testColumnIndexBasedMapping() throws IOException {
		throw new UnsupportedOperationException("not implemented yet!");
	}

	@Test
	public void testColumnNameBasedMapping() throws IOException {
		String csv = "Connor,John\r\n";
		String[] mapping = { "lastName", "firstName" };

		CsvBeanReader beanReader = new CsvBeanReader(new StringReader(csv), STANDARD_PREFERENCE);
		FeatureBean character = beanReader.read(FeatureBean.class, mapping);

		Assert.assertNotNull(character);
		Assert.assertEquals("John", character.getFirstName());
		Assert.assertEquals("Connor", character.getLastName());
	}

	@Ignore
	@Test
	public void testSupportsAnnotations() throws IOException {
		throw new UnsupportedOperationException("Annotations are not implemented yet!");
	}

	@Test
	public void testConvertsToPrimitives() throws IOException {
		String csv = "Connor,John,16\r\n";
		String[] mapping = { "lastName", "firstName", "age" };
		CellProcessor[] processors = { new NotNull(), new NotNull(), new ParseInt() };

		CsvBeanReader beanReader = new CsvBeanReader(new StringReader(csv), STANDARD_PREFERENCE);
		FeatureBean character = beanReader.read(FeatureBean.class, mapping, processors);

		Assert.assertNotNull(character);
		Assert.assertEquals("John", character.getFirstName());
		Assert.assertEquals("Connor", character.getLastName());
		Assert.assertEquals(16, character.getAge());
	}

	@Test
	public void testConvertsToBasicObjects() throws Exception {
		String csv = "Connor|John|16|1999-07-12|6" + decimalFormatSymbols.getDecimalSeparator() + "65\r\n";
		String[] mapping = { "lastName", "firstName", "age", "birthDate", "savings" };
		CellProcessor[] processors = { new NotNull(), new NotNull(), new ParseInt(), new ParseDate("yyyy-MM-dd"),
			new ParseBigDecimal(decimalFormatSymbols) };

		CsvPreference customPreference = new Builder('"', "|", "\r\n").build();
		CsvBeanReader beanReader = new CsvBeanReader(new StringReader(csv), customPreference);
		FeatureBean character = beanReader.read(FeatureBean.class, mapping, processors);

		Assert.assertNotNull(character);
		Assert.assertEquals("John", character.getFirstName());
		Assert.assertEquals("Connor", character.getLastName());
		Assert.assertEquals(16, character.getAge());
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-12"), character.getBirthDate());
		Assert.assertEquals(new BigDecimal(6.65, new MathContext(3)), character.getSavings());
	}

	@Test
	public void testConverterSupport() throws Exception {
		String csv = "Connor|John|16|1999-07-12|6" + decimalFormatSymbols.getDecimalSeparator() + "65\r\n";
		String[] mapping = { "lastName", "firstName", "age", "birthDate", "savings" };
		CellProcessor[] processors = { new NotNull(), new NotNull(), new ParseInt(), new ParseDate("yyyy-MM-dd"),
			new ParseBigDecimal(decimalFormatSymbols) };

		CsvPreference customPreference = new Builder('"', "|", "\r\n").build();
		CsvBeanReader beanReader = new CsvBeanReader(new StringReader(csv), customPreference);
		FeatureBean character = beanReader.read(FeatureBean.class, mapping, processors);

		Assert.assertNotNull(character);
		Assert.assertEquals("John", character.getFirstName());
		Assert.assertEquals("Connor", character.getLastName());
		Assert.assertEquals(16, character.getAge());
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-12"), character.getBirthDate());
		Assert.assertEquals(new BigDecimal(6.65, new MathContext(3)), character.getSavings());
	}

	@Test
	public void testDateSupport() throws Exception {
		String csv = "1999-07-12";
		String[] mapping = { "birthDate" };
		CellProcessor[] processors = { new ParseDate("yyyy-MM-dd") };

		CsvBeanReader beanReader = new CsvBeanReader(new StringReader(csv), STANDARD_PREFERENCE);
		FeatureBean character = beanReader.read(FeatureBean.class, mapping, processors);

		Assert.assertNotNull(character);
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1999-07-12"), character.getBirthDate());
	}

	@Test
	public void testReturnsTypedBean() throws Exception {
		int methodCounter = 0;
		Method[] methods = CsvBeanReader.class.getMethods();
		for( Method method : methods ) {
			if( method.getName().equals("read") ) {
				try {
					Type genericReturnType = method.getGenericReturnType();
					Assert.assertNotNull(genericReturnType);
					Assert.assertEquals("T", genericReturnType.getTypeName());
					methodCounter++;
				}
				catch(Exception e) {
					Assert.fail(e.getMessage());
				}
			}
		}
		Assert.assertTrue(methodCounter > 0);
	}

	@Test
	public void testDeepConversion() {
		Assert.assertNotNull("See org.supercsv.example.dozer.Reading class!");
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
}
