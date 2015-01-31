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
package org.supercsv.cellprocessor.joda;

import java.util.Locale;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.joda.mock.IdentityTransform;

/**
 * Tests the AbstractJodaFormattingProcessor cell processor. This is purely for
 * coverage of the constructors.
 */
public class AbstractJodaFormattingProcessorTest {

	@Test(expected = NullPointerException.class)
	public void testConstructor1WithNullJodaType() {
		new FmtNothing(null);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor2WithNullJodaType() {
		new FmtNothing(null, new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor3WithNullJodaType() {
		new FmtNothing(null, ISODateTimeFormat.dateTime());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor4WithNullJodaType() {
		new FmtNothing(null, ISODateTimeFormat.dateTime(),
				new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor5WithNullJodaType() {
		new FmtNothing(null, "some pattern");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor6WithNullJodaType() {
		new FmtNothing(null, "some pattern", new IdentityTransform());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor7WithNullJodaType() {
		new FmtNothing(null, "some pattern", Locale.ENGLISH);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor8WithNullJodaType() {
		new FmtNothing(null, "some pattern", Locale.ENGLISH,
				new IdentityTransform());
	}

	private class FmtNothing extends AbstractJodaFormattingProcessor<String> {

		public FmtNothing(Class<String> jodaClass, CellProcessor next) {
			super(jodaClass, next);
		}

		public FmtNothing(Class<String> jodaClass, DateTimeFormatter formatter,
				CellProcessor next) {
			super(jodaClass, formatter, next);
		}

		public FmtNothing(Class<String> jodaClass, DateTimeFormatter formatter) {
			super(jodaClass, formatter);
		}

		public FmtNothing(Class<String> jodaClass, String pattern,
				CellProcessor next) {
			super(jodaClass, pattern, next);
		}

		public FmtNothing(Class<String> jodaClass, String pattern,
				Locale locale, CellProcessor next) {
			super(jodaClass, pattern, locale, next);
		}

		public FmtNothing(Class<String> jodaClass, String pattern, Locale locale) {
			super(jodaClass, pattern, locale);
		}

		public FmtNothing(Class<String> jodaClass, String pattern) {
			super(jodaClass, pattern);
		}

		public FmtNothing(Class<String> jodaClass) {
			super(jodaClass);
		}

		@Override
		protected String format(String jodaType, DateTimeFormatter formatter) {
			return null;
		}

		@Override
		protected String format(String jodaType, String pattern, Locale locale) {
			return null;
		}

		@Override
		protected String format(String jodaType) {
			return null;
		}

	}

}
