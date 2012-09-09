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

/**
 * An individual survey answer.
 */
public class Answer {
	
	private Integer questionNo;
	
	private String answer;
	
	public Answer() {
	}

	public Answer(final Integer questionNo, final String answer) {
		this.questionNo = questionNo;
		this.answer = answer;
	}

	public Integer getQuestionNo() {
		return questionNo;
	}
	
	public void setQuestionNo(Integer questionNo) {
		this.questionNo = questionNo;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@Override
	public String toString() {
		return String.format("Answer [questionNo=%s, answer=%s]", questionNo, answer);
	}
	
}
