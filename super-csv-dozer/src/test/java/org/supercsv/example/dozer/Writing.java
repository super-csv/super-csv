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

import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.Token;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.io.dozer.ICsvDozerBeanWriter;
import org.supercsv.mock.dozer.Answer;
import org.supercsv.mock.dozer.SurveyResponse;
import org.supercsv.prefs.CsvPreference;

/**
 * Dozer writing examples.
 */
public class Writing {
	
	private static final String[] FIELD_MAPPING = new String[] { 
		"age",                   // simple field mapping (like CsvBeanWriter)
		"consentGiven",          // as above
		"answers[0].questionNo", // indexed (first element) + deep mapping
		"answers[0].answer", 
		"answers[1].questionNo", // indexed (second element) + deep mapping
		"answers[1].answer", 
		"answers[2].questionNo", 
		"answers[2].answer" };
	
	public static void main(String[] args) throws Exception {
		writeWithDozerCsvBeanWriter();
		partialWriteWithCsvDozerBeanWriter();
		appendingWriteWithCsvDozerBeanWriter();
	}
	
	/**
	 * An example of writing using CsvDozerBeanWriter.
	 */
	private static void writeWithDozerCsvBeanWriter() throws Exception {
		
		final CellProcessor[] processors = new CellProcessor[] { 
			new Token(0, null),     // age
			new FmtBool("Y", "N"),  // consent
			new NotNull(),          // questionNo 1
			new Optional(),         // answer 1
			new NotNull(),          // questionNo 2
			new Optional(),         // answer 2
			new NotNull(),          // questionNo 3
			new Optional() };       // answer 3
		
		// create the survey responses to write
		SurveyResponse response1 = new SurveyResponse(18, true, Arrays.asList(new Answer(1, "Twelve"), new Answer(2,
			"Albert Einstein"), new Answer(3, "Big Bang Theory")));
		SurveyResponse response2 = new SurveyResponse(0, true, Arrays.asList(new Answer(1, "Thirteen"), new Answer(2,
			"Nikola Tesla"), new Answer(3, "Stargate")));
		SurveyResponse response3 = new SurveyResponse(42, false, Arrays.asList(new Answer(1, null), new Answer(2,
			"Carl Sagan"), new Answer(3, "Star Wars")));
		final List<SurveyResponse> surveyResponses = Arrays.asList(response1, response2, response3);
		
		ICsvDozerBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvDozerBeanWriter(new FileWriter("target/writeWithCsvDozerBeanWriter.csv"),
				CsvPreference.STANDARD_PREFERENCE);
			
			// configure the mapping from the fields to the CSV columns
			beanWriter.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
			
			// write the header
			beanWriter.writeHeader("age", "consentGiven", "questionNo1", "answer1", "questionNo2", "answer2",
				"questionNo3", "answer3");
			
			// write the beans
			for( final SurveyResponse surveyResponse : surveyResponses ) {
				beanWriter.write(surveyResponse, processors);
			}
			
		}
		finally {
			if( beanWriter != null ) {
				beanWriter.close();
			}
		}
	}
	
	/**
	 * An example of partial reading using CsvDozerBeanWriter.
	 */
	private static void partialWriteWithCsvDozerBeanWriter() throws Exception {
		
		// null ages and answers are converted to something more meaningful
		final CellProcessor[] partialProcessors = new CellProcessor[] { 
			new Token(0, "age not supplied"), // age
			new FmtBool("Y", "N"),                 // consent
			new NotNull(),                         // questionNo 1
			new ConvertNullTo("not answered"),     // answer 1
			new NotNull(),                         // questionNo 2
			new ConvertNullTo("not answered"),     // answer 2
			new NotNull(),                         // questionNo 3
			new ConvertNullTo("not answered")};    // answer 3
		
		// create the survey responses to write
		SurveyResponse response1 = new SurveyResponse(18, true, Arrays.asList(new Answer(1, "Twelve"), new Answer(2,
			"Albert Einstein"), new Answer(3, "Big Bang Theory")));
		SurveyResponse response2 = new SurveyResponse(0, true, Arrays.asList(new Answer(1, "Thirteen"), new Answer(2,
			"Nikola Tesla"), new Answer(3, "Stargate")));
		SurveyResponse response3 = new SurveyResponse(42, false, Arrays.asList(new Answer(1, null), new Answer(2,
			"Carl Sagan"), new Answer(3, "Star Wars")));
		final List<SurveyResponse> surveyResponses = Arrays.asList(response1, response2, response3);
		
		ICsvDozerBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvDozerBeanWriter(new FileWriter("target/partialWriteWithCsvDozerBeanWriter.csv"),
				CsvPreference.STANDARD_PREFERENCE);
			
			// configure the mapping from the fields to the CSV columns
			beanWriter.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
			
			// write the header
			beanWriter.writeHeader("age", "consentGiven", "questionNo1", "answer1", "questionNo2", "answer2",
				"questionNo3", "answer3");
			
			// write the beans
			for( final SurveyResponse surveyResponse : surveyResponses ) {
				beanWriter.write(surveyResponse, partialProcessors);
			}
			
		}
		finally {
			if( beanWriter != null ) {
				beanWriter.close();
			}
		}
	}
	
	/**
	 * An example of appending writing using CsvDozerBeanWriter
	 */
	public static void appendingWriteWithCsvDozerBeanWriter() throws Exception {
		
		final CellProcessor[] processors = new CellProcessor[] {
				new Token(0, null),     // age
				new FmtBool("Y", "N"),  // consent
				new NotNull(),          // questionNo 1
				new Optional(),         // answer 1
				new NotNull(),          // questionNo 2
				new Optional(),         // answer 2
				new NotNull(),          // questionNo 3
				new Optional() };       // answer 3
		
		// create the survey responses to write
		SurveyResponse response1 = new SurveyResponse(18, true, Arrays.asList(new Answer(1, "Twelve"), new Answer(2,
				"Albert Einstein"), new Answer(3, "Big Bang Theory")));
		SurveyResponse response2 = new SurveyResponse(0, true, Arrays.asList(new Answer(1, "Thirteen"), new Answer(2,
				"Nikola Tesla"), new Answer(3, "Stargate")));
		SurveyResponse response3 = new SurveyResponse(42, false, Arrays.asList(new Answer(1, null), new Answer(2,
				"Carl Sagan"), new Answer(3, "Star Wars")));

		ICsvDozerBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvDozerBeanWriter(new FileWriter("target/appendingWriteWithCsvDozerBeanWriter.csv"),
					CsvPreference.STANDARD_PREFERENCE);

			// configure the mapping from the fields to the CSV columns
			beanWriter.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);

			// write the header
			beanWriter.writeHeader("age", "consentGiven", "questionNo1", "answer1", "questionNo2", "answer2",
					"questionNo3", "answer3");

			// write the beans
			beanWriter.write(response1, processors);
		}
		finally {
			if( beanWriter != null ) {
				beanWriter.close();
			}
		}
		
		// appending write
		try {
			beanWriter = new CsvDozerBeanWriter(
					new FileWriter("target/appendingWriteWithCsvDozerBeanWriter.csv", true),
					CsvPreference.STANDARD_PREFERENCE);
			beanWriter.configureBeanMapping(SurveyResponse.class, FIELD_MAPPING);
			beanWriter.write(response2, processors);
			beanWriter.write(response3, processors);
		}
		finally {
			if( beanWriter != null ){
				beanWriter.close();
			}
		}
	}
}
