package org.supercsv.io.declarative.provider;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import org.supercsv.io.declarative.CellProcessor;

public class AnnotationAndProviderConsistencyCheck {
	@Test
	public void test() throws InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("org.supercsv");
		Set<Class<?>> cellProcessorAnnotations = reflections.getTypesAnnotatedWith(CellProcessor.class);
		
		for( Class<?> annotationType : cellProcessorAnnotations ) {
			CellProcessor cellProcessorAnnotation = annotationType.getAnnotation(CellProcessor.class);
			Class<? extends CellProcessorProvider> providerType = cellProcessorAnnotation.provider();
			
			CellProcessorProvider provider = providerType.newInstance();
			
			Assert.assertEquals(annotationType, provider.getType());
			
		}
	}
}
