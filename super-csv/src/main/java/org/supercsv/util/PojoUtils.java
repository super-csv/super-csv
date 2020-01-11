package org.supercsv.util;

import java.lang.reflect.Field;

public class PojoUtils {
	public static <T> Field[] getAllFields(Class clazz) {
		Field[] allFields = clazz.getDeclaredFields();
		Class superClass = clazz.getSuperclass();
		while (superClass != null) {
			Field[] superFields = superClass.getDeclaredFields();
			allFields = concat(allFields, superFields);
			superClass = superClass.getSuperclass();
		}
		return allFields;
	}

	public static Field searchFieldByName(Field[] fields, String name) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(name)) {
				return fields[i];
			}
		}
		return null;
	}

	public static <T> T[] concat(T[] first, T[] second) {
		final int firstLength = first.length;
		final int secondLength = second.length;
		if (firstLength == 0) {
			return second;
		}
		if (secondLength == 0) {
			return first;
		}
		T[] result = (T[]) java.lang.reflect.Array.newInstance(
				first.getClass().getComponentType(), firstLength + secondLength);
		System.arraycopy(first, 0, result, 0, firstLength);
		System.arraycopy(second, 0, result, firstLength, secondLength);
		return result;
	}
}
