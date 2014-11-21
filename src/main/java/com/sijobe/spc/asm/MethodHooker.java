package com.sijobe.spc.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.MethodVisitor;

abstract class MethodHooker extends MethodTransformer
{	
	protected Method method;
	
	MethodHooker(String id, Method method) throws IllegalArgumentException
	{
		super(id);
		if(!Modifier.isStatic(method.getModifiers()))
		{
			throw(new IllegalArgumentException("MethodTransformer not of correct class"));
		}
		this.method = method;
	}
	
	protected abstract MethodVisitor getWriter();
	
	@Override
	public void visitCode()
	{
		try {
			this.method.invoke(null, this.getWriter());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
