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
package org.supercsv.io.dozer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ITokenizer;
import org.supercsv.io.Tokenizer;
import org.supercsv.mock.dozer.SurveyResponse;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvDozerBeanReader class.
 * 
 * @author James Bassett
 */
public class CsvDozerBeanReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private Reader reader;
	
	private CsvDozerBeanReader beanReader;
	private CsvDozerBeanReader beanReaderWithMapper;
	private CsvDozerBeanReader beanReaderWithConfiguredMapper;
	private CsvDozerBeanReader tokenizerBeanReader;
	private CsvDozerBeanReader tokenizerBeanReaderWithMapper;
	private ITokenizer tokenizer;
	private DozerBeanMapper beanMapper;
	private DozerBeanMapper configuredBeanMapper;
	
	private static final String[] FIELD_MAPPING = new String[] { "age", "consentGiven", "answers[0].questionNo",
		"answers[0].answer", "answers[1].questionNo", "answers[1].answer", "answers[2].questionNo", "answers[2].answer" };
	
	private static final CellProcessor[] PROCESSORS = new CellProcessor[] { new ParseInt(), new ParseBool(),
		new ParseInt(), new Optional(), new ParseInt(), new Optional(), new ParseInt(), new Optional() };
	
	private static final String CSV = "age,consentGiven,questionNo1,answer1,questionNo2,answer2,questionNo3,answer3\n"
		+ "23,Y,1,Nikola Tesla,2,\"\"\"A brief history of time\"\" by Steven Hawking\",3,Theoretical physicist\n"
		+ "16,Y,1,Genghis Kahn,2,\"\"\"Monsoon\"\" by Wilbur Smith\",3,\n"
		+ "44,Y,1,,2,,3,\"I hate surveys, thanks for wasting my time!\"";
	
	/**
	 * Sets up the readers for the tests (all four constructor varieties are tested!).
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV);
		beanReader = new CsvDozerBeanReader(reader, PREFS);
		
		tokenizer = new Tokenizer(reader, PREFS);
		tokenizerBeanReader = new CsvDozerBeanReader(tokenizer, PREFS);
		
		beanMapper = new DozerBeanMapper();
		beanReaderWithMapper = new CsvDozerBeanReader(reader, PREFS, beanMapper);
		
		configuredBeanMapper = new DozerBeanMapper(Arrays.asList("reference.xml"));
		beanReaderWithConfiguredMapper = new CsvDozerBeanReader(reader, PREFS, configuredBeanMapper);
		
		tokenizerBeanReaderWithMapper = new CsvDozerBeanReader(tokenizer, PREFS, beanMapper);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		beanReader.close();
		tokenizerBeanReader.close();
		beanReaderWithMapper.close();
		beanReaderWithConfiguredMapper.close();
		tokenizerBeanReaderWithMapper.close();
	}
	
	/**
	 * Tests the read() method without any processors for a standard bean reader.
	 */
	@Test
	public void testReadForBeanReader() throws IOException {
		testRead(beanReader, false, false);
	}
	
	/**
	 * Tests the read() method using processors for a standard bean reader.
	 */
	@Test
	public void testReadForBeanReaderUsingProcessors() throws IOException {
		testRead(beanReader, true, false);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with custom tokenizer.
	 */
	@Test
	public void testReadForTokenizerBeanReader() throws IOException {
		testRead(tokenizerBeanReader, false, false);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with custom tokenizer.
	 */
	@Test
	public void testReadForTokenizerBeanReaderUsingProcessors() throws IOException {
		testRead(tokenizerBeanReader, true, false);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testReadForBeanReaderWithMapper() throws IOException {
		testRead(beanReaderWithMapper, false, false);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testReadForBeanReaderWithMapperUsingProcessors() throws IOException {
		testRead(beanReaderWithMapper, true, false);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with a pre-configured DozerBeanMapper (no need
	 * to call configureBeanMapping() at all).
	 */
	@Test
	public void testReadForBeanReaderWithConfiguredMapper() throws IOException {
		testRead(beanReaderWithConfiguredMapper, false, true);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with a pre-configured DozerBeanMapper (no need to call
	 * configureBeanMapping() at all).
	 */
	@Test
	public void testReadForBeanReaderWithConfiguredMapperUsingProcessors() throws IOException {
		testRead(beanReaderWithConfiguredMapper, true, true);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with custom tokenizer and DozerBeanMapper.
	 */
	@Test
	public void testReadForTokenizerBeanReaderWithMapper() throws IOException {
		testRead(tokenizerBeanReaderWithMapper, false, false);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with custom tokenizer and DozerBeanMapper.
	 */
	@Test
	public void testReadForTokenizerBeanReaderWithMapperUsingProcessors() throws IOException {
		testRead(tokenizerBeanReaderWithMapper, true, false);
	}
	
	/**
	 * Tests the read() method with or without processors. The great thing here is that Dozer is smart enough to map the
	 * Booleans and Integers even if no processors are used, so the results should be identical!
	 * 
	 * @param beanReader
	 *            the bean reader to use for the test
	 * @param useProcessors
	 *            whether processors should be used for the test
	 * @param configured
	 *            whether the reader is already configured
	 * @throws IOException
	 */
	private void testRead(final CsvDozerBeanReader beanReader, final boolean useProcessors, final boolean configured)
		throws IOException {
		
		beanReader.getHeader(true);
		
		if( !configured ) {
			beanReader.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
		}
		
		SurveyResponse response1 = useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class);
		assertEquals(23, response1.getAge().intValue());
		assertEquals(Boolean.TRUE, response1.getConsentGiven());
		assertEquals(3, response1.getAnswers().size());
		assertEquals(1, response1.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Nikola Tesla", response1.getAnswers().get(0).getAnswer());
		assertEquals(2, response1.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"A brief history of time\" by Steven Hawking", response1.getAnswers().get(1).getAnswer());
		assertEquals(3, response1.getAnswers().get(2).getQuestionNo().intValue());
		assertEquals("Theoretical physicist", response1.getAnswers().get(2).getAnswer());
		
		SurveyResponse response2 = useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class);
		assertEquals(16, response2.getAge().intValue());
		assertEquals(Boolean.TRUE, response2.getConsentGiven());
		assertEquals(3, response2.getAnswers().size());
		assertEquals(1, response2.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Genghis Kahn", response2.getAnswers().get(0).getAnswer());
		assertEquals(2, response2.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"Monsoon\" by Wilbur Smith", response2.getAnswers().get(1).getAnswer());
		assertEquals(3, response2.getAnswers().get(2).getQuestionNo().intValue());
		assertNull(response2.getAnswers().get(2).getAnswer());
		
		SurveyResponse response3 = useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class);
		assertEquals(44, response3.getAge().intValue());
		assertEquals(Boolean.TRUE, response3.getConsentGiven());
		assertEquals(3, response3.getAnswers().size());
		assertEquals(1, response3.getAnswers().get(0).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(0).getAnswer());
		assertEquals(2, response3.getAnswers().get(1).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(1).getAnswer());
		assertEquals(3, response3.getAnswers().get(2).getQuestionNo().intValue());
		assertEquals("I hate surveys, thanks for wasting my time!", response3.getAnswers().get(2).getAnswer());
		
		assertNull(useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class));
		
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * standard bean reader.
	 */
	@Test
	public void testPartialReadForBeanReader() throws IOException {
		testPartialRead(beanReader, false);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a standard
	 * bean reader.
	 */
	@Test
	public void testPartialReadForBeanReaderUsingProcessors() throws IOException {
		testPartialRead(beanReader, true);
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * bean reader with custom tokenizer.
	 */
	@Test
	public void testPartialReadForTokenizerBeanReader() throws IOException {
		testPartialRead(tokenizerBeanReader, false);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a bean
	 * reader with custom tokenizer.
	 */
	@Test
	public void testPartialReadForTokenizerBeanReaderUsingProcessors() throws IOException {
		testPartialRead(tokenizerBeanReader, true);
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForBeanReaderWithMapper() throws IOException {
		testPartialRead(beanReaderWithMapper, false);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a bean
	 * reader with custom DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForBeanReaderWithMapperUsingProcessors() throws IOException {
		testPartialRead(beanReaderWithMapper, true);
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * bean reader with custom tokenizer and DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForTokenizerBeanReaderWithMapper() throws IOException {
		testPartialRead(tokenizerBeanReaderWithMapper, false);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a bean
	 * reader with custom tokenizer and DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForTokenizerBeanReaderWithMapperUsingProcessors() throws IOException {
		testPartialRead(tokenizerBeanReaderWithMapper, true);
	}
	
	/**
	 * Tests the read() method with or without any processors with null elements in the fieldMapping (ignored columns).
	 * As Dozer is smart enough to do basic type mapping, the result will be the same regardless of whether processors
	 * are used.
	 * 
	 * @param beanReader
	 *            the bean reader to use for the test
	 * @param useProcessors
	 *            whether processors should be used for the test
	 * @throws IOException
	 */
	private void testPartialRead(final CsvDozerBeanReader beanReader, final boolean useProcessors) throws IOException {
		
		beanReader.getHeader(true);
		
		// ignore age, and the last question/answer
		final String[] partialMapping = new String[] { null, "consentGiven", "answers[0].questionNo",
			"answers[0].answer", "answers[1].questionNo", "answers[1].answer", null, null };
		beanReader.configureBeanMapping(SurveyResponse.class, partialMapping);
		
		SurveyResponse response1 = useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class);
		assertNull(response1.getAge());
		assertEquals(Boolean.TRUE, response1.getConsentGiven());
		assertEquals(2, response1.getAnswers().size());
		assertEquals(1, response1.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Nikola Tesla", response1.getAnswers().get(0).getAnswer());
		assertEquals(2, response1.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"A brief history of time\" by Steven Hawking", response1.getAnswers().get(1).getAnswer());
		
		SurveyResponse response2 = useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class);
		assertNull(response2.getAge());
		assertEquals(Boolean.TRUE, response2.getConsentGiven());
		assertEquals(2, response2.getAnswers().size());
		assertEquals(1, response2.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Genghis Kahn", response2.getAnswers().get(0).getAnswer());
		assertEquals(2, response2.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"Monsoon\" by Wilbur Smith", response2.getAnswers().get(1).getAnswer());
		
		SurveyResponse response3 = useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class);
		assertNull(response3.getAge());
		assertEquals(Boolean.TRUE, response3.getConsentGiven());
		assertEquals(2, response3.getAnswers().size());
		assertEquals(1, response3.getAnswers().get(0).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(0).getAnswer());
		assertEquals(2, response3.getAnswers().get(1).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(1).getAnswer());
		
		assertNull(useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
			.read(SurveyResponse.class));
		
	}
	
	/**
	 * Tests all of the constructors with null values (should throw an Exception).
	 */
	@Test
	public void testConstructorsWithNulls() {
		
		// constructor one - null reader
		try {
			new CsvDozerBeanReader((Reader) null, PREFS);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor one - null prefs
		try {
			new CsvDozerBeanReader(reader, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor two - null tokenizer
		try {
			new CsvDozerBeanReader((ITokenizer) null, PREFS);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor two - null prefs
		try {
			new CsvDozerBeanReader(tokenizer, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor three - null reader
		try {
			new CsvDozerBeanReader((Reader) null, PREFS, beanMapper);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor three - null prefs
		try {
			new CsvDozerBeanReader(reader, null, beanMapper);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor three - null dozerBeanMapper
		try {
			new CsvDozerBeanReader(reader, PREFS, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor four - null tokenizer
		try {
			new CsvDozerBeanReader((ITokenizer) null, PREFS, beanMapper);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor four - null prefs
		try {
			new CsvDozerBeanReader(tokenizer, null, beanMapper);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor four - null dozerBeanMapper
		try {
			new CsvDozerBeanReader(tokenizer, PREFS, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConfigureBeanMappingWithNullClazz() {
		beanReader.configureBeanMapping(null, FIELD_MAPPING);
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null fieldMapping array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConfigureBeanMappingWithNullFieldMapping() {
		beanReader.configureBeanMapping(SurveyResponse.class, null);
	}
	
	/**
	 * Tests the read() method with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullClazz() throws IOException {
		beanReader.read(null);
	}
	
	/**
	 * Tests the read() method (with processors) with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithProcessorsWithNullClazz() throws IOException {
		beanReader.read(null, PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors) with a null cell processor array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithProcessorsWithNullProcessors() throws IOException {
		beanReader.read(SurveyResponse.class, (CellProcessor[]) null);
	}
	
}
