package com.sijobe.spc.util;

import java.lang.reflect.Field;

public class AccessHelper
{
	public static void setInt(Object obj, String name, int value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Class clazz = obj.getClass();
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		field.setInt(obj, value);
	}
}
