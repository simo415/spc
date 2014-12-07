package com.sijobe.spc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AccessHelper
{
	public static void setInt(Object obj, String name, int value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Class<?> clazz = obj.getClass();
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		field.setInt(obj, value);
	}
	
	public static void setFloat(Object obj, String name, float value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Class<?> clazz = obj.getClass();
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		field.setFloat(obj, value);
	}
	
	public static void setBoolean(Object obj, String name, boolean value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		Field field;
		while(true)
		{
			try
			{
				field = clazz.getDeclaredField(name);
				break;
			}
			catch(NoSuchFieldException error)
			{
				if(clazz == Object.class)
				{
					throw(error);
				}
				clazz = clazz.getSuperclass();
			}
		}
		field.setAccessible(true);
		field.setBoolean(obj, value);
	}
	
	public static Object getObj(Object obj, String name) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		Field field;
		while(true)
		{
			try
			{
				field = clazz.getDeclaredField(name);
				break;
			}
			catch(NoSuchFieldException error)
			{
				if(clazz == Object.class)
				{
					throw(error);
				}
				clazz = clazz.getSuperclass();
			}
		}
		field.setAccessible(true);
		return field.get(obj);
	}
	
	@SuppressWarnings("unchecked")
   public static <T> T callMethod(Object obj, String name, Object ... args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Class<?> clazz = obj.getClass();
		Method method;
		while(true)
		{
			try
			{
				method = clazz.getDeclaredMethod(name);
				break;
			}
			catch(NoSuchMethodException error)
			{
				if(clazz == Object.class)
				{
					throw(error);
				}
				clazz = clazz.getSuperclass();
			}
		}
		
		method.setAccessible(true);
		return (T) method.invoke(obj, args);
	}
}
