package org.supercsv.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BeanInterfaceProxyTest {

@Test
public void call_set_and_test_on_proxy() {
	final TestInterface ti = (TestInterface) new BeanInterfaceProxy().createProxy(TestInterface.class);
	ti.setName("foo name");
	assertEquals("foo name", ti.getName());
}
}
