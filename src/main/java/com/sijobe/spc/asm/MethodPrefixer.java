package com.sijobe.spc.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;

class MethodPrefixer extends MethodHooker
{
	MethodPrefixer(String id, Method method) throws IllegalArgumentException
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
		this.mv = mv;
	}

	@Override
	protected MethodVisitor getWriter()
	{
		return this.mv;
	}
}
