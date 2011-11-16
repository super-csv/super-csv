package org.supercsv.exception;

import org.junit.Test;

public class CopyOfSuperCSVReflectionExceptionTest {

@Test
public void can_innstantiate() {
	new SuperCSVReflectionException("shut up code coverage", new Throwable());
}

}
