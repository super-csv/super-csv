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
package org.supercsv.example.dozer;

import java.io.FileReader;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.mock.dozer.Answer;
import org.supercsv.mock.dozer.SurveyResponse;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Dozer reading examples.
 */
public class Reading {
	
	private static final String CSV_FILENAME = "src/test/resources/surveyresponses.csv";
	
	private static final String[] FIELD_MAPPING = new String[] { 
		"age",                   // simple field mapping (like CsvBeanReader)
		"consentGiven",          // as above
		"answers[0].questionNo", // indexed (first element) + deep mapping
		"answers[0].answer", 
		"answers[1].questionNo", // indexed (second element) + deep mapping
		"answers[1].answer", 
		"answers[2].questionNo", 
		"answers[2].answer" };
	
	public static void main(String[] args) throws Exception {
		readWithCsvDozerBeanReader();
		partialReadWithCsvDozerBeanReader();
		readWithCsvDozerBeanReaderUsingIndexMappingAndHints();
	}
	
	/**
	 * An example of reading using CsvDozerBeanReader.
	 */
	private static void readWithCsvDozerBeanReader() throws Exception {
		
		final CellProcessor[] processors = new CellProcessor[] { 
			new Optional(new ParseInt()), // age
			new ParseBool(),              // consent
			new ParseInt(),               // questionNo 1
			new Optional(),               // answer 1
			new ParseInt(),               // questionNo 2
			new Optional(),               // answer 2
			new ParseInt(),               // questionNo 3
			new Optional()                // answer 3
		};
		
		ICsvDozerBeanReader beanReader = null;
		try {
			beanReader = new CsvDozerBeanReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			beanReader.getHeader(true); // ignore the header
			beanReader.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
			
			SurveyResponse surveyResponse;
			while( (surveyResponse = beanReader.read(SurveyResponse.class, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, surveyResponse=%s", beanReader.getLineNumber(),
					beanReader.getRowNumber(), surveyResponse));
			}
			
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
	
	/**
	 * An example of partial reading using CsvDozerBeanReader.
	 */
	private static void partialReadWithCsvDozerBeanReader() throws Exception {
		
		// ignore age, and question/answer 3
		final String[] partialFieldMapping = new String[] { null, "consentGiven", "answers[0].questionNo",
			"answers[0].answer", "answers[1].questionNo", "answers[1].answer", null, null };
		
		// set processors for ignored columns to null for efficiency (could have used full array if we wanted them to execute anyway)
		final CellProcessor[] processors = new CellProcessor[] { null, new ParseBool(), new ParseInt(), new Optional(),
			new ParseInt(), new Optional(), null, null };
		
		ICsvDozerBeanReader beanReader = null;
		try {
			beanReader = new CsvDozerBeanReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			beanReader.getHeader(true); // ignore the header
			beanReader.configureBeanMapping(SurveyResponse.class, partialFieldMapping);
			
			SurveyResponse surveyResponse;
			while( (surveyResponse = beanReader.read(SurveyResponse.class, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, surveyResponse=%s", beanReader.getLineNumber(),
					beanReader.getRowNumber(), surveyResponse));
			}
			
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
	
	/**
	 * An example of reading using CsvDozerBeanReader that uses indexed mapping and a cell processor
	 * to read into a List of Answer beans (this requires a hint).
	 */
	private static void readWithCsvDozerBeanReaderUsingIndexMappingAndHints() throws Exception {
		
		// simple cell processor that creates an Answer with a value
		final CellProcessor parseAnswer = new CellProcessorAdaptor() {
			public Object execute(Object value, CsvContext context) {
				return new Answer(null, (String) value);
			}
		};
		
		final CellProcessor[] processors = new CellProcessor[] { 
			new Optional(new ParseInt()), // age
			null,                         // consent
			null,                         // questionNo 1
			new Optional(parseAnswer),    // answer 1
			null,                         // questionNo 2
			new Optional(parseAnswer),    // answer 2
			null,                         // questionNo 3
			new Optional(parseAnswer)     // answer 3
		};
		
		// no deep mapping (answers[0].answer) required as we're using a cell processor to create the bean
		final String[] fieldMapping = {"age", null, null, "answers[0]", null, "answers[1]", null, "answers[2]"};
		
		// the indexed mappings need a hint for Dozer to work
		final Class<?>[] hintTypes = {null, null, null, Answer.class, null, Answer.class, null, Answer.class};
		
		ICsvDozerBeanReader beanReader = null;
		try {
			beanReader = new CsvDozerBeanReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			
			beanReader.getHeader(true); // ignore the header
			beanReader.configureBeanMapping(SurveyResponse.class, fieldMapping, hintTypes);
			
			SurveyResponse surveyResponse;
			while( (surveyResponse = beanReader.read(SurveyResponse.class, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, surveyResponse=%s", beanReader.getLineNumber(),
					beanReader.getRowNumber(), surveyResponse));
			}
			
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}
	}
	
}
