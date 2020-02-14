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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.mock.dozer.Answer;
import org.supercsv.mock.dozer.SurveyResponse;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Tests the CsvDozerBeanReader class.
 * 
 * @author James Bassett
 */
public class CsvDozerBeanReaderTest {
	
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	private static final boolean EXISTING_BEAN = true;
	private static final boolean CREATE_NEW_BEAN = false;
	private static final boolean CONFIGURED = true;
	private static final boolean NOT_CONFIGURED = false;
	private static final boolean USE_PROCESSORS = true;
	private static final boolean NO_PROCESSORS = false;
	
	private Reader reader;
	
	private CsvDozerBeanReader beanReader;
	private CsvDozerBeanReader beanReaderWithMapper;
	private CsvDozerBeanReader beanReaderWithConfiguredMapper;
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

	private static final String BROKEN_CSV = "age,consentGiven,questionNo1,answer1,questionNo2,answer2,questionNo3,answer3\n"
			+ "23,Y,1,Nikola Tesla\",2,A brief history of time by Steven Hawking,3,Theoretical physicist\n"
			+ "16,Y,1,Genghis Kahn,2,Monsoon by Wilbur Smith,3,\n"
			+ "44,Y,1,,2,,3,\"I hate surveys, thanks for wasting my time!\"";


	/**
	 * Sets up the readers for the tests (all four constructor varieties are tested!).
	 */
	@Before
	public void setUp() {
		reader = new StringReader(CSV);
		beanReader = new CsvDozerBeanReader(reader, PREFS);
		
		beanMapper = new DozerBeanMapper();
		beanReaderWithMapper = new CsvDozerBeanReader(reader, PREFS, beanMapper);
		
		configuredBeanMapper = new DozerBeanMapper(Arrays.asList("reference.xml"));
		beanReaderWithConfiguredMapper = new CsvDozerBeanReader(reader, PREFS, configuredBeanMapper);
	}
	
	/**
	 * Closes the readers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		beanReader.close();
		beanReaderWithMapper.close();
		beanReaderWithConfiguredMapper.close();
	}
	
	/**
	 * Tests the read() method without any processors for a standard bean reader.
	 */
	@Test
	public void testReadForBeanReader() throws IOException {
		testRead(beanReader, NO_PROCESSORS, NOT_CONFIGURED, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors for a standard bean reader.
	 */
	@Test
	public void testReadForBeanReaderWithExistingBean() throws IOException {
		testRead(beanReader, NO_PROCESSORS, NOT_CONFIGURED, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method using processors for a standard bean reader.
	 */
	@Test
	public void testReadForBeanReaderUsingProcessors() throws IOException {
		testRead(beanReader, USE_PROCESSORS, NOT_CONFIGURED, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method using processors for a standard bean reader.
	 */
	@Test
	public void testReadForBeanReaderUsingProcessorsWithExistingBean() throws IOException {
		testRead(beanReader, USE_PROCESSORS, NOT_CONFIGURED, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testReadForBeanReaderWithMapper() throws IOException {
		testRead(beanReaderWithMapper, NO_PROCESSORS, NOT_CONFIGURED, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testReadForBeanReaderWithMapperWithExistingBean() throws IOException {
		testRead(beanReaderWithMapper, NO_PROCESSORS, NOT_CONFIGURED, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testReadForBeanReaderWithMapperUsingProcessors() throws IOException {
		testRead(beanReaderWithMapper, USE_PROCESSORS, NOT_CONFIGURED, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testReadForBeanReaderWithMapperUsingProcessorsWithExistingBean() throws IOException {
		testRead(beanReaderWithMapper, USE_PROCESSORS, NOT_CONFIGURED, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with a pre-configured DozerBeanMapper (no need
	 * to call configureBeanMapping() at all).
	 */
	@Test
	public void testReadForBeanReaderWithConfiguredMapper() throws IOException {
		testRead(beanReaderWithConfiguredMapper, NO_PROCESSORS, CONFIGURED, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors for a bean reader with a pre-configured DozerBeanMapper (no need
	 * to call configureBeanMapping() at all).
	 */
	@Test
	public void testReadForBeanReaderWithConfiguredMapperWithExistingBean() throws IOException {
		testRead(beanReaderWithConfiguredMapper, NO_PROCESSORS, CONFIGURED, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with a pre-configured DozerBeanMapper (no need to call
	 * configureBeanMapping() at all).
	 */
	@Test
	public void testReadForBeanReaderWithConfiguredMapperUsingProcessors() throws IOException {
		testRead(beanReaderWithConfiguredMapper, USE_PROCESSORS, CONFIGURED, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method using processors for a bean reader with a pre-configured DozerBeanMapper (no need to call
	 * configureBeanMapping() at all).
	 */
	@Test
	public void testReadForBeanReaderWithConfiguredMapperUsingProcessorsWithExistingBean() throws IOException {
		testRead(beanReaderWithConfiguredMapper, USE_PROCESSORS, CONFIGURED, EXISTING_BEAN);
	}

	@Test
	public void testReadForBeanReaderWithBrokenMaxSingleLineQuotes() throws IOException {
		reader = new StringReader(BROKEN_CSV);
		final CsvDozerBeanReader brokenCsvBeanReader = new CsvDozerBeanReader(reader, new CsvPreference.Builder(PREFS).maxLinesPerRow(1).build(), beanMapper);
		final List<SurveyResponse> responses = new ArrayList<SurveyResponse>();
		testReadForBrokenCSV(brokenCsvBeanReader, responses, "unexpected end of line while reading quoted column on line 2", 2);
	}

	@Test
	public void testReadForBeanReaderWithBrokenMaxTwoLineQuotes() throws IOException {
		reader = new StringReader(BROKEN_CSV);
		final CsvDozerBeanReader brokenCsvBeanReader = new CsvDozerBeanReader(reader, new CsvPreference.Builder(PREFS).maxLinesPerRow(2).build(), beanMapper);
		final List<SurveyResponse> responses = new ArrayList<SurveyResponse>();
		testReadForBrokenCSV(brokenCsvBeanReader, responses, "max number of lines to read exceeded while reading quoted column beginning on line 2 and ending on line 3", 1);
	}

	private void testReadForBrokenCSV(final CsvDozerBeanReader brokenCsvBeanReader, final List<SurveyResponse> responses,
			final String expectedMessage, final int expectedRowsRead) throws IOException {
		brokenCsvBeanReader.getHeader(true);
		brokenCsvBeanReader.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
		Exception expected = null;
		for(int i = 0; i < 4; i++) {
			try{
				final SurveyResponse response = readSurveyResponse(brokenCsvBeanReader, USE_PROCESSORS, EXISTING_BEAN);
				if(response != null) {
					responses.add(response);
				}
			} catch (final SuperCsvException e) {
				expected = e;
			}
		}
		assertNotNull(expected);
		assertEquals(expectedMessage, expected.getLocalizedMessage());
		assertEquals(expectedRowsRead, responses.size());
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
	 * @param useExistingBean
	 *            whether to map to an existing bean, or let Dozer create a new instance of a class
	 * @throws IOException
	 */
	private void testRead(final CsvDozerBeanReader beanReader, final boolean useProcessors, final boolean configured,
		final boolean useExistingBean) throws IOException {
		
		beanReader.getHeader(true);
		
		if( !configured ) {
			beanReader.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
		}
		
		SurveyResponse response1 = readSurveyResponse(beanReader, useProcessors, useExistingBean);
		assertEquals(23, response1.getAge());
		assertEquals(Boolean.TRUE, response1.getConsentGiven());
		assertEquals(3, response1.getAnswers().size());
		assertEquals(1, response1.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Nikola Tesla", response1.getAnswers().get(0).getAnswer());
		assertEquals(2, response1.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"A brief history of time\" by Steven Hawking", response1.getAnswers().get(1).getAnswer());
		assertEquals(3, response1.getAnswers().get(2).getQuestionNo().intValue());
		assertEquals("Theoretical physicist", response1.getAnswers().get(2).getAnswer());
		
		SurveyResponse response2 = readSurveyResponse(beanReader, useProcessors, useExistingBean);
		assertEquals(16, response2.getAge());
		assertEquals(Boolean.TRUE, response2.getConsentGiven());
		assertEquals(3, response2.getAnswers().size());
		assertEquals(1, response2.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Genghis Kahn", response2.getAnswers().get(0).getAnswer());
		assertEquals(2, response2.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"Monsoon\" by Wilbur Smith", response2.getAnswers().get(1).getAnswer());
		assertEquals(3, response2.getAnswers().get(2).getQuestionNo().intValue());
		assertNull(response2.getAnswers().get(2).getAnswer());
		
		SurveyResponse response3 = readSurveyResponse(beanReader, useProcessors, useExistingBean);
		assertEquals(44, response3.getAge());
		assertEquals(Boolean.TRUE, response3.getConsentGiven());
		assertEquals(3, response3.getAnswers().size());
		assertEquals(1, response3.getAnswers().get(0).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(0).getAnswer());
		assertEquals(2, response3.getAnswers().get(1).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(1).getAnswer());
		assertEquals(3, response3.getAnswers().get(2).getQuestionNo().intValue());
		assertEquals("I hate surveys, thanks for wasting my time!", response3.getAnswers().get(2).getAnswer());
		
		assertNull(readSurveyResponse(beanReader, useProcessors, useExistingBean));
		
	}
	
	/**
	 * Reads a SurveyResponse.
	 * 
	 * @param beanReader
	 *            the bean reader
	 * @param useProcessors
	 *            whether to use cell processors or not
	 * @param useExistingBean
	 *            whether to populate an existing bean, or let Dozer instantiate an instance of a class
	 * @return the SurveyResponse
	 * @throws IOException
	 */
	private SurveyResponse readSurveyResponse(final CsvDozerBeanReader beanReader, final boolean useProcessors,
		final boolean useExistingBean) throws IOException {
		if( useExistingBean ) {
			return useProcessors ? beanReader.read(new SurveyResponse(), PROCESSORS) : beanReader
				.read(new SurveyResponse());
		} else {
			return useProcessors ? beanReader.read(SurveyResponse.class, PROCESSORS) : beanReader
				.read(SurveyResponse.class);
		}
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * standard bean reader.
	 */
	@Test
	public void testPartialReadForBeanReader() throws IOException {
		testPartialRead(beanReader, NO_PROCESSORS, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * standard bean reader.
	 */
	@Test
	public void testPartialReadForBeanReaderWithExistingBean() throws IOException {
		testPartialRead(beanReader, NO_PROCESSORS, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a standard
	 * bean reader.
	 */
	@Test
	public void testPartialReadForBeanReaderUsingProcessors() throws IOException {
		testPartialRead(beanReader, USE_PROCESSORS, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a standard
	 * bean reader.
	 */
	@Test
	public void testPartialReadForBeanReaderUsingProcessorsWithExistingBean() throws IOException {
		testPartialRead(beanReader, USE_PROCESSORS, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForBeanReaderWithMapper() throws IOException {
		testPartialRead(beanReaderWithMapper, NO_PROCESSORS, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method without any processors with null elements in the fieldMapping (ignored columns) for a
	 * bean reader with custom DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForBeanReaderWithMapperWithExistingBean() throws IOException {
		testPartialRead(beanReaderWithMapper, NO_PROCESSORS, EXISTING_BEAN);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a bean
	 * reader with custom DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForBeanReaderWithMapperUsingProcessors() throws IOException {
		testPartialRead(beanReaderWithMapper, USE_PROCESSORS, CREATE_NEW_BEAN);
	}
	
	/**
	 * Tests the read() method using processors with null elements in the fieldMapping (ignored columns) for a bean
	 * reader with custom DozerBeanMapper.
	 */
	@Test
	public void testPartialReadForBeanReaderWithMapperUsingProcessorsWithExistingBean() throws IOException {
		testPartialRead(beanReaderWithMapper, USE_PROCESSORS, EXISTING_BEAN);
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
	 * @param useExistingBean
	 *            whether to populate an existing bean, or let Dozer instantiate an instance of a class
	 * @throws IOException
	 */
	private void testPartialRead(final CsvDozerBeanReader beanReader, final boolean useProcessors,
		final boolean useExistingBean) throws IOException {
		
		beanReader.getHeader(true);
		
		// ignore age, and the last question/answer
		final String[] partialMapping = new String[] { null, "consentGiven", "answers[0].questionNo",
			"answers[0].answer", "answers[1].questionNo", "answers[1].answer", null, null };
		beanReader.configureBeanMapping(SurveyResponse.class, partialMapping);
		
		SurveyResponse response1 = readSurveyResponse(beanReader, useProcessors, useExistingBean);
		assertEquals(0, response1.getAge());
		assertEquals(Boolean.TRUE, response1.getConsentGiven());
		assertEquals(2, response1.getAnswers().size());
		assertEquals(1, response1.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Nikola Tesla", response1.getAnswers().get(0).getAnswer());
		assertEquals(2, response1.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"A brief history of time\" by Steven Hawking", response1.getAnswers().get(1).getAnswer());
		
		SurveyResponse response2 = readSurveyResponse(beanReader, useProcessors, useExistingBean);
		assertEquals(0, response2.getAge());
		assertEquals(Boolean.TRUE, response2.getConsentGiven());
		assertEquals(2, response2.getAnswers().size());
		assertEquals(1, response2.getAnswers().get(0).getQuestionNo().intValue());
		assertEquals("Genghis Kahn", response2.getAnswers().get(0).getAnswer());
		assertEquals(2, response2.getAnswers().get(1).getQuestionNo().intValue());
		assertEquals("\"Monsoon\" by Wilbur Smith", response2.getAnswers().get(1).getAnswer());
		
		SurveyResponse response3 = readSurveyResponse(beanReader, useProcessors, useExistingBean);
		assertEquals(0, response3.getAge());
		assertEquals(Boolean.TRUE, response3.getConsentGiven());
		assertEquals(2, response3.getAnswers().size());
		assertEquals(1, response3.getAnswers().get(0).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(0).getAnswer());
		assertEquals(2, response3.getAnswers().get(1).getQuestionNo().intValue());
		assertNull(response3.getAnswers().get(1).getAnswer());
		
		assertNull(readSurveyResponse(beanReader, useProcessors, useExistingBean));
		
	}
	
	/**
	 * Tests mapping columns to indexed list elements (with no deep mapping). Dozer requires a hint in this situation.
	 */
	@Test
	public void testReadToListElement() throws IOException {
		final String csv = "age,answer1,answer2,answer3\n"
			+ "23,Nikola Tesla,\"\"\"A brief history of time\"\" by Steven Hawking\",Theoretical physicist\n"
			+ ",Genghis Kahn,\"\"\"Monsoon\"\" by Wilbur Smith\",\n"
			+ "44,,,\"I hate surveys, thanks for wasting my time!\"";
		reader = new StringReader(csv);
		beanReader = new CsvDozerBeanReader(reader, PREFS);
		
		beanReader.getHeader(true); // skip header
		
		final String[] fieldMapping = new String[] { "age", "answers[0]", "answers[1]", "answers[2]" };
		final Class<?>[] hintTypes = new Class<?>[] { null, Answer.class, Answer.class, Answer.class };
		beanReader.configureBeanMapping(SurveyResponse.class, fieldMapping, hintTypes);
		
		final CellProcessor parseAnswer = new CellProcessor() {
			public Object execute(Object value, CsvContext context) {
				return value == null ? null : new Answer(0, (String) value);
			}
		};
		final CellProcessor[] processors = new CellProcessor[] { new Optional(new ParseInt()), parseAnswer,
			parseAnswer, parseAnswer };
		
		SurveyResponse response1 = beanReader.read(SurveyResponse.class, processors);
		assertEquals(23, response1.getAge());
		assertEquals(3, response1.getAnswers().size());
		assertEquals("Nikola Tesla", response1.getAnswers().get(0).getAnswer());
		assertEquals("\"A brief history of time\" by Steven Hawking", response1.getAnswers().get(1).getAnswer());
		assertEquals("Theoretical physicist", response1.getAnswers().get(2).getAnswer());
		
		SurveyResponse response2 = beanReader.read(SurveyResponse.class, processors);
		assertEquals(0, response2.getAge());
		assertEquals(3, response2.getAnswers().size());
		assertEquals("Genghis Kahn", response2.getAnswers().get(0).getAnswer());
		assertEquals("\"Monsoon\" by Wilbur Smith", response2.getAnswers().get(1).getAnswer());
		assertNull(response2.getAnswers().get(2));
		
		SurveyResponse response3 = beanReader.read(SurveyResponse.class, processors);
		assertEquals(44, response3.getAge());
		assertEquals(3, response3.getAnswers().size());
		assertNull(response3.getAnswers().get(0));
		assertNull(response3.getAnswers().get(1));
		assertEquals("I hate surveys, thanks for wasting my time!", response3.getAnswers().get(2).getAnswer());
		
		assertNull(beanReader.read(SurveyResponse.class, processors));
		
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
		
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null clazz (should throw an exception).
	 */
	@Test
	public void testConfigureBeanMappingWithNullClazz() {
		try {
			beanReader.configureBeanMapping(null, FIELD_MAPPING);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		try {
			beanReader.configureBeanMapping(null, FIELD_MAPPING, new Class<?>[FIELD_MAPPING.length]);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null fieldMapping array (should throw an exception).
	 */
	@Test
	public void testConfigureBeanMappingWithNullFieldMapping() {
		try {
			beanReader.configureBeanMapping(SurveyResponse.class, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		try {
			beanReader.configureBeanMapping(SurveyResponse.class, null, new Class<?>[FIELD_MAPPING.length]);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null hintTypes array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConfigureBeanMappingWithNullHintTypes() {
		beanReader.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING, null);
	}
	
	/**
	 * Tests the configureBeanMapping() method with a hintTypes array of the wrong size (should throw an exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConfigureBeanMappingWithInvalidHintTypes() {
		beanReader.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING, new Class<?>[0]);
	}
	
	/**
	 * Tests the read() method with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullClazz() throws IOException {
		beanReader.read((Class<?>) null);
	}
	
	/**
	 * Tests the read() method with a null bean (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithNullBean() throws IOException {
		beanReader.read((Object) null);
	}
	
	/**
	 * Tests the read() method (with processors) with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithProcessorsWithNullClazz() throws IOException {
		beanReader.read((Class<?>) null, PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors) with a null bean (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithProcessorsWithNullBean() throws IOException {
		beanReader.read((Object) null, PROCESSORS);
	}
	
	/**
	 * Tests the read() method (with processors) with a null cell processor array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadWithProcessorsWithNullProcessors() throws IOException {
		beanReader.read(SurveyResponse.class, (CellProcessor[]) null);
	}
	
	/**
	 * Tests the read() method (with processors) with a null cell processor array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testReadBeanWithProcessorsWithNullProcessors() throws IOException {
		beanReader.read(new SurveyResponse(), (CellProcessor[]) null);
	}
	
}
