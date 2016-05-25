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
package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.declarative.annotation.Converter;
import org.supercsv.util.CsvContext;

/**
 * Tests the Convert processor
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class ConvertTest {
	private static final CsvContext CONTEXT = new CsvContext(1, 2, 3);
	
	@Test
	public void callsProvidedMapper() {
		Converter keyValueMapper = Mockito.mock(Converter.class);
		Convert processor = new Convert(keyValueMapper);
		
		processor.execute("42", CONTEXT);
		
		Mockito.verify(keyValueMapper).convert("42");
	}
	
	@Test
	public void returnsMapperResult() {
		Converter keyValueMapper = Mockito.mock(Converter.class);
		Mockito.when(keyValueMapper.convert(Mockito.anyObject())).thenReturn("abc");
		Convert processor = new Convert(keyValueMapper);
		
		Object result = processor.execute("42", CONTEXT);
		
		Assert.assertEquals("abc", result);
	}
	
	@Test
	public void callsNextProcessorWithResult() {
		Converter keyValueMapper = Mockito.mock(Converter.class);
		CellProcessor next = Mockito.mock(CellProcessor.class);
		Mockito.when(keyValueMapper.convert(Mockito.anyObject())).thenReturn("abc");
		Convert processor = new Convert(keyValueMapper, next);
		
		processor.execute("42", CONTEXT);
		
		Mockito.verify(next).execute("abc", CONTEXT);
	}
	
	@Test
	public void returnsNextProcessorResult() {
		Converter keyValueMapper = Mockito.mock(Converter.class);
		CellProcessor next = Mockito.mock(CellProcessor.class);
		Mockito.when(next.execute(Mockito.anyObject(), Mockito.<CsvContext> any())).thenReturn("abc");
		Convert processor = new Convert(keyValueMapper, next);
		
		Object result = processor.execute("42", CONTEXT);
		
		Assert.assertEquals("abc", result);
	}
	
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		Convert processor = new Convert(Mockito.mock(Converter.class));
		
		processor.execute(null, new CsvContext(1, 2, 3));
	}
	
}
