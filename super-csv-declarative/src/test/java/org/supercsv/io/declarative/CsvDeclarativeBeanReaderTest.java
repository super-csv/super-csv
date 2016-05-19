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
package org.supercsv.io.declarative;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.After;
import org.junit.Test;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.Tokenizer;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the {@link CsvDeclarativeBeanReader}
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class CsvDeclarativeBeanReaderTest {
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private static final String SIMPLE_BEAN_CSV = "/simpleBean.csv";
	private static final String SIMPLE_BEAN_SIMPLE_ANNOTATIONS_CSV = "/simpleBeanWithSimpleAnnotations.csv";
	private static final String BEAN_WITH_INHERITED_PROPERTIES = "/beanWithInheritedProperties.csv";
	private static final String BEAN_FOR_DEFAULT_OVERRIDING_TEST = "/beanForDefaultOverridingTest.csv";
	
	private CsvDeclarativeBeanReader beanReader;
	
	@After
	public void tearDown() throws IOException {
		if( beanReader != null ) {
			beanReader.close();
		}
	}
	
	@Test
	public void readSimpleBeanWithoutAnnotations() throws IOException {
		setupBeanReader(SIMPLE_BEAN_CSV);
		BeanWithoutAnnotations john = new BeanWithoutAnnotations("John", "Doe", 42, 100.5);
		BeanWithoutAnnotations max = new BeanWithoutAnnotations("Max", "Mustermann", 22, 21.4);
		
		assertEquals(john, beanReader.read(BeanWithoutAnnotations.class));
		assertEquals(max, beanReader.read(BeanWithoutAnnotations.class));
		assertNull(beanReader.read(BeanWithoutAnnotations.class));
	}
	
	@Test
	public void readSimpleBeanWithSimpleAnnotations() throws IOException {
		setupBeanReader(SIMPLE_BEAN_SIMPLE_ANNOTATIONS_CSV);
		BeanWithSimpleAnnotations john = new BeanWithSimpleAnnotations(null, "Doe", 42, 100.5);
		BeanWithSimpleAnnotations max = new BeanWithSimpleAnnotations("Max", "Mustermann", 22, 21.4);
		
		assertEquals(john, beanReader.read(BeanWithSimpleAnnotations.class));
		assertEquals(max, beanReader.read(BeanWithSimpleAnnotations.class));
		assertNull(beanReader.read(BeanWithSimpleAnnotations.class));
	}
	
	@Test
	public void readSimpleBeanWithChainedAnnotations() throws IOException {
		setupBeanReader(SIMPLE_BEAN_SIMPLE_ANNOTATIONS_CSV);
		BeanWithChainedAnnotations john = new BeanWithChainedAnnotations(null, "Doe", 42, 100.5);
		BeanWithChainedAnnotations max = new BeanWithChainedAnnotations("Max", "Mus", 22, 21.4);
		
		assertEquals(john, beanReader.read(BeanWithChainedAnnotations.class));
		assertEquals(max, beanReader.read(BeanWithChainedAnnotations.class));
		assertNull(beanReader.read(BeanWithChainedAnnotations.class));
	}
	
	@Test
	public void readBeanWithInheritedProperties() throws IOException {
		setupBeanReader(BEAN_WITH_INHERITED_PROPERTIES);
		BeanWithInheritedProperties john = new BeanWithInheritedProperties("John", "Doe", 42, 100.5, "Note 1");
		BeanWithInheritedProperties max = new BeanWithInheritedProperties("Max", "Mustermann", 22, 21.4, "Note 2");
		
		assertEquals(john, beanReader.read(BeanWithInheritedProperties.class));
		assertEquals(max, beanReader.read(BeanWithInheritedProperties.class));
		assertNull(beanReader.read(BeanWithInheritedProperties.class));
	}
	
	@Test
	public void overrideDefaultProcessor() throws IOException {
		setupBeanReader(BEAN_FOR_DEFAULT_OVERRIDING_TEST);
		BeanForDefaultOverridingTest firstRow = new BeanForDefaultOverridingTest(true);
		BeanForDefaultOverridingTest secondRow = new BeanForDefaultOverridingTest(false);
		
		assertEquals(firstRow, beanReader.read(BeanForDefaultOverridingTest.class));
		assertEquals(secondRow, beanReader.read(BeanForDefaultOverridingTest.class));
		assertNull(beanReader.read(BeanWithInheritedProperties.class));
	}
	
	@Test(expected = SuperCsvReflectionException.class)
	public void readWithNonJavabean() throws IOException {
		setupBeanReader(BEAN_WITH_INHERITED_PROPERTIES);
		beanReader.read(Integer.class);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void readWithNullBeanClass() throws IOException {
		setupBeanReader(BEAN_WITH_INHERITED_PROPERTIES);
		beanReader.read(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void beanFieldCountAndCsvFieldCountDoNotMatch() throws IOException {
		setupBeanReader(BEAN_WITH_INHERITED_PROPERTIES);
		beanReader.read(BeanWithoutAnnotations.class);
	}
	
	@Test(expected = NullPointerException.class)
	public void readProcessorsWithNullBeanClass() throws IOException {
		beanReader.read(null);
	}
	
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void readerConstructorWithNullReader() {
		new CsvBeanReader((Reader) null, PREFS);
	}
	
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void readerConstructorWithNullPreferences() {
		new CsvBeanReader(new StringReader(""), null);
	}
	
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void tokenizerConstructorWithNullReader() {
		new CsvBeanReader((Tokenizer) null, PREFS);
	}
	
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void tokenizerConstructorWithNullPreferences() {
		new CsvBeanReader(new Tokenizer(new StringReader(""), PREFS), null);
	}
	
	@Test(expected = SuperCsvReflectionException.class)
	public void beanInstantationThrowingIllegalAccessException() throws IOException {
		setupBeanReader(BEAN_WITH_INHERITED_PROPERTIES);
		beanReader.read(IllegalAccessBean.class);
	}
	
	public static class IllegalAccessBean {
		
		public IllegalAccessBean() throws IllegalAccessException {
			throw new IllegalAccessException("naughty naughty!");
		}
		
	}
	
	private void setupBeanReader(String inputFileName) {
		beanReader = new CsvDeclarativeBeanReader(new InputStreamReader(
			CsvDeclarativeBeanReaderTest.class.getResourceAsStream(inputFileName)), PREFS);
	}
}
