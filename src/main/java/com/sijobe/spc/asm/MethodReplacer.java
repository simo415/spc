package com.sijobe.spc.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.MethodVisitor;

class MethodReplacer extends MethodHooker
{
	protected MethodVisitor writer;
	
	MethodReplacer(String id, Method method) throws IllegalArgumentException
	{
		super(id, method);
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Hook
	{
		String value();
	}

	@Override
	void injectMethodWriter(MethodVisitor mv)
	{
		this.writer = mv;
	}

	@Override
	protected MethodVisitor getWriter()
	{
		return this.writer;
	}
}
