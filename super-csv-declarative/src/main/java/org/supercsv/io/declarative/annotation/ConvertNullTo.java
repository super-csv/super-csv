package org.supercsv.io.declarative.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.supercsv.io.declarative.CellProcessor;
import org.supercsv.io.declarative.provider.ConvertToNullCellProcessorProvider;

@CellProcessor(provider = ConvertToNullCellProcessorProvider.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ConvertNullTo {
	String value();
}
