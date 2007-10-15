package org.supercsv.util;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.supercsv.exception.SuperCSVException;

public class MethodCacheTest {
	/** stupid subclass to shut up emma */
	protected final static class Ignore extends Util {
		public Ignore() {
			super();
		}
	}

	/** we use objects of this class as access test */
	static class ObjectMock {
		int	i	= STARTVAL;

		public int getA() {
			return i;
		}

		public int getB() {
			return i;
		}

		public int getC() {
			return i;
		}

		public int getD() {
			return i;
		}

		public int getI() {
			return i;
		}

		public void setA(final int i) {
			this.i = i;
		}

		public void setB(final int i) {
			this.i = i;
		}

		public void setC(final int i) {
			this.i = i;
		}

		public void setD(final int i) {
			this.i = i;
		}

		public void setI(final int i) {
			this.i = i;
		}
	}

	static final int	STARTVAL	= 42;
	static final int	ENDTVAL		= 43;

	@Test
	public void testGetLookup() throws Exception {
		MethodCache cache = new MethodCache();
		cache.flushCaches(); // to make timing meassure correctly

		final ObjectMock om = new ObjectMock();
		long time1 = System.nanoTime();
		Method getMethod = cache.getGetMethod(om, "i");
		getMethod = cache.getGetMethod(om, "b");
		getMethod = cache.getGetMethod(om, "a");
		Assert.assertEquals("object read", STARTVAL, getMethod.invoke(om));
		time1 = System.nanoTime() - time1;

		// fetch again to activate the cache
		long time2 = System.nanoTime();
		getMethod = cache.getGetMethod(om, "i");
		getMethod = cache.getGetMethod(om, "b");
		getMethod = cache.getGetMethod(om, "a");
		Assert.assertEquals("object read", STARTVAL, getMethod.invoke(om));
		time2 = System.nanoTime() - time2;
		Assert.assertTrue("Cache lookup should be faster " + time1 + " > " + time2, time1 > time2);
	}

	@Test(expected = SuperCSVException.class)
	public void test_InvalidmethodCall() {
		MethodCache cache = new MethodCache();
		cache.getGetMethod(new ObjectMock(), "bibibabibibaib");
	}

	@Test
	public void test_Make_Hidden_Abstract_ConstructorTested() {
		new Ignore();
	}

	@Test
	public void test_Set_Lookup() throws Exception {
		MethodCache cache = new MethodCache();
		cache.flushCaches(); // to make timing meassure correctly

		final ObjectMock om = new ObjectMock();

		Assert.assertEquals("object read", STARTVAL, cache.getGetMethod(om, "i").invoke(om));
		long time1 = System.nanoTime();
		Method setMethod = cache.getSetMethod(om, "i");
		setMethod.invoke(om, ENDTVAL);
		time1 = System.nanoTime() - time1;
		Assert.assertEquals("object read", ENDTVAL, cache.getGetMethod(om, "i").invoke(om));

		// fetch again
		Assert.assertEquals("object read", ENDTVAL, cache.getGetMethod(om, "i").invoke(om));
		long time2 = System.nanoTime();
		setMethod = cache.getSetMethod(om, "i");
		setMethod.invoke(om, ENDTVAL + 1);
		time2 = System.nanoTime() - time2;
		Assert.assertEquals("object read", ENDTVAL + 1, cache.getGetMethod(om, "i").invoke(om));
		// System.out.println("set t1 " + time1 + " t2 " + time2);
		Assert.assertTrue("Cache lookup should be faster", time1 > time2);
	}
}
