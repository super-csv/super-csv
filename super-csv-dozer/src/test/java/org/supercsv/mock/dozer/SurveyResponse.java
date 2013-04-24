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
package org.supercsv.mock.dozer;

import java.util.List;

/**
 * A person's response to a survey.
 */
public class SurveyResponse {
	
	private int age;
	
	private Boolean consentGiven;
	
	private List<Answer> answers;
	
	public SurveyResponse() {
	}

	public SurveyResponse(final int age, final Boolean consentGiven, final List<Answer> answers) {
		this.age = age;
		this.consentGiven = consentGiven;
		this.answers = answers;
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public Boolean getConsentGiven() {
		return consentGiven;
	}
	
	public void setConsentGiven(Boolean consentGiven) {
		this.consentGiven = consentGiven;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}
	
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	@Override
	public String toString() {
		return String.format("SurveyResponse [age=%s, consentGiven=%s, answers=%s]", age, consentGiven, answers);
	}
	
}
