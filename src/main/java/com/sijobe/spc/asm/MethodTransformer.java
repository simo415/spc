package com.sijobe.spc.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

abstract class MethodTransformer extends MethodVisitor
{
	String id;
	
	MethodTransformer(String id)
	{
		super(Opcodes.ASM4);
		this.id = id;
	}
	
	String getApplicableMethod()
	{
		return this.id;
	}
	
	abstract void injectMethodWriter(MethodVisitor mv);
	
	static MethodTransformer[] generateFromFunctions(Class<?> clazz)
	{
		List<MethodTransformer> modifiers = new LinkedList<MethodTransformer>();
		for(Method method : clazz.getDeclaredMethods())
		{
			if(Modifier.isStatic(method.getModifiers()))
			{
				if(method.isAnnotationPresent(MethodReplacer.Hook.class))
				{
					String annotation = method.getAnnotation(MethodReplacer.Hook.class).value();
					System.out.println("found replacement for "+annotation);
					String cl = annotation.split(":", 2)[0];
					modifiers.add(new MethodReplacer(annotation, method));
				}
				else if(method.isAnnotationPresent(MethodPrefixer.Hook.class))
				{
					String annotation = method.getAnnotation(MethodPrefixer.Hook.class).value();
					System.out.println("found prefix for "+annotation);
					String cl = annotation.split(":", 2)[0];
					modifiers.add(new MethodPrefixer(annotation, method));
				}
			}
		}
		
		MethodTransformer[] r = new MethodTransformer[modifiers.size()];
		return modifiers.toArray(r);
	}
}
