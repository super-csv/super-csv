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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * @since 2.5
 * @author Dominik Schlosser
 */
public class CsvDeclarativeBeanWriterTest {
	private static final CsvPreference PREFS = CsvPreference.STANDARD_PREFERENCE;
	
	private StringWriter result = new StringWriter();
	private CsvDeclarativeBeanWriter beanWriter = new CsvDeclarativeBeanWriter(result, PREFS);
	
	@After
	public void tearDown() throws IOException {
		if( beanWriter != null ) {
			beanWriter.close();
		}
	}
	
	@Test
	public void writeSimpleBeanWithoutAnnotations() throws IOException {
		BeanWithoutAnnotations john = new BeanWithoutAnnotations("John", "Doe", 42, 100.5);
		BeanWithoutAnnotations max = new BeanWithoutAnnotations("Max", "Mustermann", 22, 21.4);
		
		beanWriter.write(john);
		beanWriter.write(max);
		
		assertEquals("John,Doe,42,100.5\r\nMax,Mustermann,22,21.4\r\n", result.toString());
	}
	
	@Test
	public void writeSimpleBeanWithSimpleAnnotations() throws IOException {
		BeanWithSimpleAnnotations john = new BeanWithSimpleAnnotations(null, "Doe", 42, 100.5);
		BeanWithSimpleAnnotations max = new BeanWithSimpleAnnotations("Max", "Mustermann ", 22, 21.4);
		
		beanWriter.write(john);
		beanWriter.write(max);
		
		assertEquals(",Doe,42,100.5\r\nMax,Mustermann,22,21.4\r\n", result.toString());
	}
	
	@Test
	public void writeSimpleBeanWithChainedAnnotations() throws IOException {
		BeanWithChainedAnnotations john = new BeanWithChainedAnnotations(null, "Doe", 42, 100.5);
		BeanWithChainedAnnotations max = new BeanWithChainedAnnotations("Max", "Mustermann", 22, 21.4);
		
		beanWriter.write(john);
		beanWriter.write(max);
		
		assertEquals("test,Doe,42,100.5\r\nMax,Mus,22,21.4\r\n", result.toString());
	}
	
	@Test
	public void writeBeanWithInheritedProperties() throws IOException {
		BeanWithInheritedProperties john = new BeanWithInheritedProperties("John", "Doe", 42, 100.5, "Note 1");
		BeanWithInheritedProperties max = new BeanWithInheritedProperties("Max", "Mustermann", 22, 21.4, "Note 2");
		
		beanWriter.write(john);
		beanWriter.write(max);
		
		assertEquals("John,Doe,42,100.5,Note 1\r\nMax,Mustermann,22,21.4,Note 2\r\n", result.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void writeWithNullBeanClass() throws IOException {
		beanWriter.write(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void writeProcessorsWithNullBeanClass() throws IOException {
		beanWriter.write(null);
	}
	
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void writerConstructorWithNullwriter() {
		new CsvDeclarativeBeanWriter((Writer) null, PREFS);
	}
	
	@SuppressWarnings("resource")
	@Test(expected = NullPointerException.class)
	public void writerConstructorWithNullPreferences() {
		new CsvDeclarativeBeanWriter(new StringWriter(), null);
	}
}
