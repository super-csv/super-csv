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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.dozer.Answer;
import org.supercsv.mock.dozer.SurveyResponse;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the CsvDozerBeanWriter class.
 * 
 * @author James Bassett
 */
public class CsvDozerBeanWriterTest {
	
	private static final CsvPreference PREFS = CsvPreference.EXCEL_PREFERENCE;
	
	private static final String[] FIELD_MAPPING = new String[] { "age", "consentGiven", "answers[0].questionNo",
		"answers[0].answer", "answers[1].questionNo", "answers[1].answer", "answers[2].questionNo", "answers[2].answer" };
	
	private static final CellProcessor[] PROCESSORS = new CellProcessor[] { new NotNull(), new FmtBool("Y", "N"),
		new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional() };
	
	private static final String CSV = "age,consentGiven,questionNo1,answer1,questionNo2,answer2,questionNo3,answer3\n"
		+ "66,true,1,Twelve,2,Albert Einstein?,3,Big Bang Theory\n" + "32,true,1,,2,Superman,3,Stargate\n"
		+ "19,true,1,\"Um, how am I supposed to know?\",2,Me,3,\"Last question, can I go now?\"\n" + "4,false,,,,,,\n";
	
	private static final String PROCESSORS_CSV = "age,consentGiven,questionNo1,answer1,questionNo2,answer2,questionNo3,answer3\n"
		+ "66,Y,1,Twelve,2,Albert Einstein?,3,Big Bang Theory\n"
		+ "32,Y,1,,2,Superman,3,Stargate\n"
		+ "19,Y,1,\"Um, how am I supposed to know?\",2,Me,3,\"Last question, can I go now?\"\n" + "4,N,,,,,,\n";
	
	private Writer writer;
	
	private CsvDozerBeanWriter beanWriter;
	private CsvDozerBeanWriter beanWriterWithMapper;
	private CsvDozerBeanWriter beanWriterWithConfiguredMapper;
	private DozerBeanMapper beanMapper;
	private DozerBeanMapper configuredBeanMapper;
	
	private SurveyResponse response1;
	private SurveyResponse response2;
	private SurveyResponse response3;
	private SurveyResponse response4;
	
	/**
	 * Sets up the writer for the tests.
	 */
	@Before
	public void setUp() {
		writer = new StringWriter();
		beanWriter = new CsvDozerBeanWriter(writer, PREFS);
		
		beanMapper = new DozerBeanMapper();
		beanWriterWithMapper = new CsvDozerBeanWriter(writer, PREFS, beanMapper);
		
		configuredBeanMapper = new DozerBeanMapper(Arrays.asList("reference.xml"));
		beanWriterWithConfiguredMapper = new CsvDozerBeanWriter(writer, PREFS, configuredBeanMapper);
	}
	
	/**
	 * Closes the bean writers after the test.
	 */
	@After
	public void tearDown() throws IOException {
		beanWriter.close();
		beanWriterWithMapper.close();
		beanWriterWithConfiguredMapper.close();
	}
	
	/**
	 * Creates the survey responses to use in the tests.
	 */
	@Before
	public void createResponses() {
		response1 = new SurveyResponse(66, true, Arrays.asList(new Answer(1, "Twelve"), new Answer(2,
			"Albert Einstein?"), new Answer(3, "Big Bang Theory")));
		response2 = new SurveyResponse(32, true, Arrays.asList(new Answer(1, null), new Answer(2, "Superman"),
			new Answer(3, "Stargate")));
		response3 = new SurveyResponse(19, true, Arrays.asList(new Answer(1, "Um, how am I supposed to know?"),
			new Answer(2, "Me"), new Answer(3, "Last question, can I go now?")));
		response4 = new SurveyResponse(4, false, null);
	}
	
	/**
	 * Tests the write() method with a normal bean writer and no processors.
	 */
	@Test
	public void testWriteWithBeanWriter() throws IOException {
		testWrite(beanWriter, false, false);
	}
	
	/**
	 * Tests the write() method with a normal bean writer and processors.
	 */
	@Test
	public void testWriteWithBeanWriterUsingProcessors() throws IOException {
		testWrite(beanWriter, true, false);
	}
	
	/**
	 * Tests the write() method with a bean writer using a custom DozerBeanMapper and no processors.
	 */
	@Test
	public void testWriteWithBeanWriterCustomMapper() throws IOException {
		testWrite(beanWriterWithMapper, false, false);
	}
	
	/**
	 * Tests the write() method with a bean writer using a custom DozerBeanMapper and processors.
	 */
	@Test
	public void testWriteWithBeanWriterCustomMapperUsingProcessors() throws IOException {
		testWrite(beanWriterWithMapper, true, false);
	}
	
	/**
	 * Tests the write() method with a bean writer using a custom DozerBeanMapper and no processors.
	 */
	@Test
	public void testWriteWithBeanWriterConfiguredMapper() throws IOException {
		testWrite(beanWriterWithConfiguredMapper, false, true);
	}
	
	/**
	 * Tests the write() method with a bean writer using a custom DozerBeanMapper and processors.
	 */
	@Test
	public void testWriteWithBeanWriterConfiguredMapperUsingProcessors() throws IOException {
		testWrite(beanWriterWithConfiguredMapper, true, true);
	}
	
	/**
	 * Tests the write() methods with the supplied writer, with or without using processors.
	 * 
	 * @param beanWriter
	 *            the dozer bean writer to use for the tests
	 * @param useProcessors
	 *            whether to use processors for the test
	 * @param configured
	 *            whether the writer is already configured
	 * @throws IOException
	 */
	private void testWrite(final CsvDozerBeanWriter beanWriter, final boolean useProcessors, final boolean configured)
		throws IOException {
		
		if (!configured){
			beanWriter.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
		}
		
		beanWriter.writeHeader("age", "consentGiven", "questionNo1", "answer1", "questionNo2", "answer2",
			"questionNo3", "answer3");
		
		if( useProcessors ) {
			beanWriter.write(response1, PROCESSORS);
			beanWriter.write(response2, PROCESSORS);
			beanWriter.write(response3, PROCESSORS);
			beanWriter.write(response4, PROCESSORS);
			beanWriter.flush();
			assertEquals(PROCESSORS_CSV, writer.toString());
			
		} else {
			beanWriter.write(response1);
			beanWriter.write(response2);
			beanWriter.write(response3);
			beanWriter.write(response4);
			beanWriter.flush();
			assertEquals(CSV, writer.toString());
		}
		
	}
	
	/**
	 * Tests all of the constructors with null values (should throw an Exception).
	 */
	@Test
	public void testConstructorsWithNulls() {
		
		// constructor one - null Writer
		try {
			new CsvDozerBeanWriter((Writer) null, PREFS);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor one - null prefs
		try {
			new CsvDozerBeanWriter(writer, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor two - null Writer
		try {
			new CsvDozerBeanWriter((Writer) null, PREFS, beanMapper);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor two - null prefs
		try {
			new CsvDozerBeanWriter(writer, null, beanMapper);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
		// constructor two - null dozerBeanMapper
		try {
			new CsvDozerBeanWriter(writer, PREFS, null);
			fail("should have thrown NullPointerException");
		}
		catch(NullPointerException e) {}
		
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConfigureBeanMappingWithNullClazz() {
		beanWriter.configureBeanMapping(null, FIELD_MAPPING);
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null fieldMapping array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConfigureBeanMappingWithNullFieldMapping() {
		beanWriter.configureBeanMapping(SurveyResponse.class, null);
	}
	
	/**
	 * Tests the configureBeanMapping() method with a null fieldMapping element (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConfigureBeanMappingWithNullFieldMappingElement() {
		beanWriter.configureBeanMapping(SurveyResponse.class, new String[] { "age", null, "consentGiven" });
	}
	
	/**
	 * Tests the write() method with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithNullClazz() throws IOException {
		beanWriter.write(null);
	}
	
	/**
	 * Tests the write() method (with processors) with a null clazz (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithProcessorsWithNullClazz() throws IOException {
		beanWriter.write(null, PROCESSORS);
	}
	
	/**
	 * Tests the write() method (with processors) with a null cell processor array (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testWriteWithProcessorsWithNullProcessors() throws IOException {
		beanWriter.write(SurveyResponse.class, (CellProcessor[]) null);
	}
	
}
