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
import org.supercsv.io.declarative.annotation.CsvKeyValueMapper;
import org.supercsv.util.CsvContext;

/**
 * Tests the Map processor
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class MapperTest {
	private static final CsvContext CONTEXT = new CsvContext(1, 2, 3);
	
	@Test
	public void callsProvidedMapper() {
		CsvKeyValueMapper keyValueMapper = Mockito.mock(CsvKeyValueMapper.class);
		Mapper processor = new Mapper(keyValueMapper);
		
		processor.execute("42", CONTEXT);
		
		Mockito.verify(keyValueMapper).map("42");
	}
	
	@Test
	public void returnsMapperResult() {
		CsvKeyValueMapper keyValueMapper = Mockito.mock(CsvKeyValueMapper.class);
		Mockito.when(keyValueMapper.map(Mockito.anyObject())).thenReturn("abc");
		Mapper processor = new Mapper(keyValueMapper);
		
		Object result = processor.execute("42", CONTEXT);
		
		Assert.assertEquals("abc", result);
	}
	
	@Test
	public void callsNextProcessorWithResult() {
		CsvKeyValueMapper keyValueMapper = Mockito.mock(CsvKeyValueMapper.class);
		CellProcessor next = Mockito.mock(CellProcessor.class);
		Mockito.when(keyValueMapper.map(Mockito.anyObject())).thenReturn("abc");
		Mapper processor = new Mapper(keyValueMapper, next);
		
		processor.execute("42", CONTEXT);
		
		Mockito.verify(next).execute("abc", CONTEXT);
	}
	
	@Test
	public void returnsNextProcessorResult() {
		CsvKeyValueMapper keyValueMapper = Mockito.mock(CsvKeyValueMapper.class);
		CellProcessor next = Mockito.mock(CellProcessor.class);
		Mockito.when(next.execute(Mockito.anyObject(), Mockito.<CsvContext> any())).thenReturn("abc");
		Mapper processor = new Mapper(keyValueMapper, next);
		
		Object result = processor.execute("42", CONTEXT);
		
		Assert.assertEquals("abc", result);
	}
	
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		Mapper processor = new Mapper(Mockito.mock(CsvKeyValueMapper.class));
		
		processor.execute(null, new CsvContext(1, 2, 3));
	}
	
}
