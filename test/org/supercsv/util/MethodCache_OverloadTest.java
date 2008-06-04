package org.supercsv.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * test lookup on overloaded methods
 * 
 * @author Kasper B. Graversen, (c) 2007-2008
 */
public class MethodCache_OverloadTest {

/** we use objects of this class as access test */
static class ObjectMock {
int i = STARTVAL;
long l = STARTVAL;
Integer integer = new Integer(STARTVAL);
String s = "" + STARTVAL;

public int getI() {
	return i;
}

public Integer getInteger() {
	return integer;
}

public String getS() {
	return s;
}

public void setInteger(final Integer integer) {
	this.integer = integer;
}

public void setVal(final int i) {
	this.i = i;
}

public void setVal(final String s) {
	this.s = s;
}

public Long getL() {
	return l;
}

public void setL(Long l) {
	this.l = l;
}

}

private static final int STARTVAL = 42;
private static final int ENDVAL = 43;
private static final long ENDVAL_L = 43L;

@Test
public void test_Set_Lookup_int_objectType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	
	final Method setMethod = cache.getSetMethod(om, "val", Integer.class);
	setMethod.invoke(om, new Integer(ENDVAL));
	assertEquals(ENDVAL, om.getI());
}

@Test
public void test_Set_Lookup_int_simpleType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals(STARTVAL, om.getInteger().intValue());
	
	final Method setMethod = cache.getSetMethod(om, "val", int.class);
	setMethod.invoke(om, ENDVAL);
	assertEquals(ENDVAL, om.getI());
}

@Test
public void test_Set_Lookup_long_simpleType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals(STARTVAL, om.getL().intValue());
	
	final Method setMethod = cache.getSetMethod(om, "l", long.class);
	setMethod.invoke(om, ENDVAL_L);
	assertEquals(ENDVAL_L, om.getL().longValue());
}

@Test
public void test_Set_Lookup_integer_simpleType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals(STARTVAL, om.getInteger().intValue());
	
	// test - call method taking an Integer with an int
	final Method setMethod = cache.getSetMethod(om, "integer", int.class);
	setMethod.invoke(om, ENDVAL);
	assertEquals(ENDVAL, om.getInteger().intValue());
}

@Test
public void test_Set_Lookup_string() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals("confirm start val", "" + STARTVAL, om.getS());
	
	// test
	final Method setMethod = cache.getSetMethod(om, "val", String.class);
	setMethod.invoke(om, "" + ENDVAL);
	assertEquals("new val", "" + ENDVAL, om.getS());
}

@Test
public void test_Set_Lookup_string_null() throws Exception {
	// if the return value of a cell is null, the cache must still be able to find a method to invoke on
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals("confirm start val", "" + STARTVAL, om.getS());
	
	// test
	final Method setMethod = cache.getSetMethod(om, "val", null);
	setMethod.invoke(om, new Object[] { null });
	assertEquals("new val", null, om.getS());
}

}
