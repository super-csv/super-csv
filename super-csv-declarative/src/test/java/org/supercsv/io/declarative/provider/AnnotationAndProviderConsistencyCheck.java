package org.supercsv.io.declarative.provider;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reflections.Reflections;
import org.supercsv.io.declarative.CellProcessor;

@RunWith(Parameterized.class)
public class AnnotationAndProviderConsistencyCheck {
	@Parameters
	public static Collection<Class<?>> getCellProcessorAnnotations() {
		Reflections reflections = new Reflections("org.supercsv");
		return reflections.getTypesAnnotatedWith(CellProcessor.class);
	}
	
	private Class<?> cellProcessorAnnotationType;
	
	public AnnotationAndProviderConsistencyCheck(Class<?> cellProcessorAnnotationType) {
		this.cellProcessorAnnotationType = cellProcessorAnnotationType;
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws InstantiationException, IllegalAccessException {
		CellProcessor cellProcessorAnnotation = cellProcessorAnnotationType.getAnnotation(CellProcessor.class);
		Class<? extends CellProcessorProvider> providerType = cellProcessorAnnotation.provider();
		
		CellProcessorProvider provider = providerType.newInstance();
		
		Assert.assertEquals(cellProcessorAnnotationType, provider.getType());
	}
}
