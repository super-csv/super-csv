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
package org.supercsv.util;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Slf4j-formatting wrapper for usage outside of logging (without exceptions or crazy template-syntax like in
 * String.format)
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public abstract class Form {
	private Form() {
		
	}
	
	public static String at(String template, Object... params) {
		FormattingTuple formatted = MessageFormatter.arrayFormat(template, params);
		
		return formatted.getMessage();
	}
}
