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
Integer integer = new Integer(STARTVAL);
String s = "" + STARTVAL;

public int getI() {
	return i;
}

public String getS() {
	return s;
}

public void setVal(int i) {
	this.i = i;
}

public void setVal(String s) {
	this.s = s;
}

public Integer getInteger() {
	return integer;
}

public void setInteger(Integer integer) {
	this.integer = integer;
}
}

private static final int STARTVAL = 42;
private static final int ENDVAL = 43;

@Test
public void test_Set_Lookup_string() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals("confirm start val", "" + STARTVAL, om.getS());
	
	// test
	Method setMethod = cache.getSetMethod(om, "val", String.class);
	setMethod.invoke(om, "" + ENDVAL);
	assertEquals("new val", "" + ENDVAL, om.getS());
}

@Test
// if the return value of a cell is null, still find a method to invoke on
public void test_Set_Lookup_string_null() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals("confirm start val", "" + STARTVAL, om.getS());
	
	// test
	Method setMethod = cache.getSetMethod(om, "val", null);
	setMethod.invoke(om, new Object[] { null });
	assertEquals("new val", null, om.getS());
}

@Test
public void test_Set_Lookup_int_simpleType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	
	Method setMethod = cache.getSetMethod(om, "val", int.class);
	setMethod.invoke(om, ENDVAL);
	assertEquals(ENDVAL, om.getI());
}

@Test
public void test_Set_Lookup_int_objectType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	
	Method setMethod = cache.getSetMethod(om, "val", Integer.class);
	setMethod.invoke(om, new Integer(ENDVAL));
	assertEquals(ENDVAL, om.getI());
}

@Test
public void test_Set_Lookup_integer_simpleType() throws Exception {
	final MethodCache cache = new MethodCache();
	final ObjectMock om = new ObjectMock();
	assertEquals(STARTVAL, om.getInteger().intValue());
	
	// test - call method taking an Integer with an int
	Method setMethod = cache.getSetMethod(om, "integer", int.class);
	setMethod.invoke(om, ENDVAL);
	assertEquals(ENDVAL, om.getInteger().intValue());
}

}
