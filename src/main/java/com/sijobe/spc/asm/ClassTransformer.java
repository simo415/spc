package com.sijobe.spc.asm;

import java.util.Map;
import java.util.HashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ClassTransformer extends ClassVisitor
{
	protected String name;
	protected Map<String, MethodTransformer> methodTransformers; 
	
	ClassTransformer(String name)
	{
		super(Opcodes.ASM4, new ClassWriter(Opcodes.ASM4));
		this.name = name;
		this.methodTransformers = new HashMap<String, MethodTransformer>();
	}
	
	String getApplicableClass()
	{
		return this.name;
	}
	
	ClassWriter getWriter()
	{
		return (ClassWriter) this.cv;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		MethodVisitor writer = super.visitMethod(access, name, desc, signature, exceptions);
		String id = this.getApplicableClass()+":"+name+":"+desc;
		
		if(this.methodTransformers.containsKey(id))
		{
			MethodTransformer transformer = this.methodTransformers.get(id);
			transformer.injectMethodWriter(writer);
			return transformer;
		}
		else
		{
			return writer;
		}
	}
	
	void registerMethodTransformer(MethodTransformer mt) throws IllegalArgumentException
	{
		String name = mt.getApplicableMethod();
		String clazz = name.split(":", 2)[0];
		if(!clazz.equals(this.getApplicableClass()))
		{
			throw(new IllegalArgumentException("MethodTransformer not of correct class"));
		}
		this.methodTransformers.put(mt.getApplicableMethod(), mt);
	}
	
	void regsiterMethodTransformers(MethodTransformer[] mt) throws IllegalArgumentException
	{
		for(MethodTransformer i : mt)
		{
			this.registerMethodTransformer(i);
		}
	}
}
